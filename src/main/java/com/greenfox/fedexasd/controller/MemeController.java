package com.greenfox.fedexasd.controller;

import com.greenfox.fedexasd.exception.MemeDoesNotExistException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.model.CreateMemeRequestDTO;
import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.service.MemeService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemeController {

  private final MemeService memeService;

  @Autowired
  public MemeController(MemeService memeService) {
    this.memeService = memeService;
  }

  @GetMapping("/memes")
  public ResponseEntity<Object> getAllMemes() {
    return new ResponseEntity<>(memeService.getAllMemes(), HttpStatus.OK);
  }

  @GetMapping("/meme/{id}")
  public ResponseEntity<Object> getMemeById(@PathVariable Long id) {
    try {
      return new ResponseEntity<>(memeService.getMemeById(id), HttpStatus.OK);
    } catch (MemeDoesNotExistException e) {
      return new ResponseEntity<>(e.message(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/meme/delete/{id}")
  public ResponseEntity<Object> deleteMemeById(@PathVariable Long id) {
    try {
      memeService.deleteMemeById(id);
      return new ResponseEntity<>("Meme with id: "+id+ " successfully deleted.",HttpStatus.OK);

    } catch (MemeDoesNotExistException e) {
      return new ResponseEntity<>(e.message(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/meme")
  public ResponseEntity<Object> createMeme(@RequestBody CreateMemeRequestDTO createMemeRequestDTO,
                                           Principal principal)
      throws UserDoesNotExistException {
    String username = principal.getName();
    Meme meme = memeService.createMeme(createMemeRequestDTO, username);
    return new ResponseEntity<>(memeService.memeToMemeResponseDTO(meme), HttpStatus.OK);
  }
}
