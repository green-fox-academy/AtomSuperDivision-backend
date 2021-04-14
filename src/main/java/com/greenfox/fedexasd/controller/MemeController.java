package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.exception.CommentNotExistException;
import com.greenfox.fedexasd.exception.InvalidGenreException;
import com.greenfox.fedexasd.exception.MemeDoesNotExistException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.model.CommentRequestDTO;
import com.greenfox.fedexasd.model.CommentSuccessResponseDTO;
import com.greenfox.fedexasd.model.CreateMemeRequestDTO;
import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.model.MemeDTO;
import com.greenfox.fedexasd.model.MemeGenreDTO;
import com.greenfox.fedexasd.model.MemeMinDTO;
import com.greenfox.fedexasd.service.MemeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MemeController {

  private final MemeService memeService;

  @Autowired
  public MemeController(MemeService memeService) {
    this.memeService = memeService;
  }

  @GetMapping("/meme")
  public ResponseEntity<Object> getAllMemes() {
    return new ResponseEntity<>(memeService.getAllMemes(), HttpStatus.OK);
  }

  @GetMapping("/meme/{id}")
  public ResponseEntity<Object> getMemeById(@PathVariable Long id) throws MemeDoesNotExistException {
    return new ResponseEntity<>(memeService.getMemeById(id), HttpStatus.OK);
  }

  @DeleteMapping("/meme/delete/{id}")
  public ResponseEntity<Object> deleteMemeById(@PathVariable Long id) throws MemeDoesNotExistException {
    memeService.deleteMemeById(id);
    return new ResponseEntity<>("Meme with id: " + id + " successfully deleted.", HttpStatus.OK);
  }

  @PostMapping("/meme")
  public ResponseEntity<Object> createMeme(@RequestParam("file") MultipartFile file, @ModelAttribute
      CreateMemeRequestDTO createMemeRequestDTO,
                                           Principal principal)
      throws UserDoesNotExistException, InvalidGenreException {
    String username = principal.getName();
    Meme meme = memeService.createMeme(createMemeRequestDTO, username, file);
    return new ResponseEntity<>(memeService.memeToMemeResponseDTO(meme), HttpStatus.OK);
  }

  @PostMapping("meme/{id}")
  public ResponseEntity<MemeDTO> addComment(@PathVariable Long id,
                                            @RequestBody CommentRequestDTO commentRequestDTO)
      throws MemeDoesNotExistException, UserDoesNotExistException {

    Meme meme = memeService.addComment(id, commentRequestDTO);

    List<CommentSuccessResponseDTO> comments = meme.getCommentList().stream()
        .map(c -> new CommentSuccessResponseDTO(c.getMessage(), c.getUser().getUsername(), c.getCreatedAt()))
        .collect(Collectors.toList());

    return ResponseEntity
        .ok(new MemeDTO(meme.getCaption(), meme.getImage(), meme.getFunny(), meme.getSad(), meme.getErotic(),
            meme.getScary(),
            meme.getCreatedAt(), meme.getUser().getUsername(), comments));
  }

  @DeleteMapping("meme/comment/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws CommentNotExistException {
    memeService.deleteCommentById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("memes/trending")
  public ResponseEntity<List<MemeMinDTO>> getAllMemesByPopularity() {
    return ResponseEntity.ok(memeService.getByHitCountInOrderDesc());
  }

  @GetMapping("memes")
  public ResponseEntity<List<MemeGenreDTO>> memesByGenre(@RequestParam String genre){
    return ResponseEntity.ok(memeService.memesByGenre(genre));
  }
}
