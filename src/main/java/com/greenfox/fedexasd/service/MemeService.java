package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.CommentNotExistException;
import com.greenfox.fedexasd.exception.InvalidGenreException;
import com.greenfox.fedexasd.exception.MemeDoesNotExistException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.model.Comment;
import com.greenfox.fedexasd.model.CommentRequestDTO;
import com.greenfox.fedexasd.model.CommentSuccessResponseDTO;
import com.greenfox.fedexasd.model.CreateMemeRequestDTO;
import com.greenfox.fedexasd.model.CreateMemeResponseDTO;
import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.model.MemeDTO;
import com.greenfox.fedexasd.model.MemeGenreDTO;
import com.greenfox.fedexasd.model.MemeMinDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.CommentRepository;
import com.greenfox.fedexasd.repository.MemeRepository;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemeService {

  private final MemeRepository memeRepository;
  private final UserService userService;
  private final CommentRepository commentRepository;

  @Autowired
  public MemeService(MemeRepository memeRepository, UserService userService,
                     CommentRepository commentRepository) {
    this.memeRepository = memeRepository;
    this.userService = userService;
    this.commentRepository = commentRepository;
  }

  public List<MemeDTO> getAllMemes() {
    List<Meme> memes = memeRepository.findAll();
    return memes.stream().map(
        meme -> new MemeDTO(meme.getId(),meme.getCaption(), meme.getImage(), meme.getFunny(), meme.getSad(),
            meme.getErotic(), meme.getScary(), meme.getCreatedAt(), meme.getUser().getUsername()))
        .collect(
            Collectors.toList());
  }

  public MemeDTO getMemeById(Long id) throws MemeDoesNotExistException {
    Meme meme = memeRepository.findById(id).orElseThrow(MemeDoesNotExistException::new);
    ModelMapper modelMapper = new ModelMapper();
    List<CommentSuccessResponseDTO> comments = meme.getCommentList()
        .stream()
        .map(m -> modelMapper.map(m, CommentSuccessResponseDTO.class))
        .collect(Collectors.toList());
    return new MemeDTO(meme.getId(),meme.getCaption(), meme.getImage(), meme.getFunny(), meme.getSad(),
        meme.getErotic(), meme.getScary(), meme.getCreatedAt(), meme.getUser().getUsername(), comments);
  }

  public Meme createMeme(CreateMemeRequestDTO createMemeRequestDTO, String username, MultipartFile file)
      throws UserDoesNotExistException, InvalidGenreException {
    User user = userService.getUserByUsername(username);
    validateGenre(createMemeRequestDTO.getGenre());
    Meme meme = new Meme();
    try {
      meme.setImage("data:image/png;base64," + Base64.getEncoder().encodeToString(file.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    meme.setCaption(createMemeRequestDTO.getCaption());
    meme.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    meme.setUser(user);
    meme.setGenre(createMemeRequestDTO.getGenre());
    return memeRepository.save(meme);

  }

  public CreateMemeResponseDTO memeToMemeResponseDTO(Meme meme) {
    ModelMapper mapper = new ModelMapper();
    return mapper.map(meme, CreateMemeResponseDTO.class);
  }

  public void deleteMemeById(Long id) throws MemeDoesNotExistException {
    if (memeRepository.findById(id).isPresent()) {
      Meme meme = memeRepository.findById(id).get();
      meme.setUser(null);
      memeRepository.deleteById(id);
    } else {
      throw new MemeDoesNotExistException();
    }
  }

  public Meme addComment(Long memeId, CommentRequestDTO commentRequestDTO)
      throws MemeDoesNotExistException, UserDoesNotExistException {
    Meme meme = memeRepository.findById(memeId).orElseThrow(MemeDoesNotExistException::new);
    User userByUsername = userService.getUserByUsername(commentRequestDTO.getUserId());

    meme.getCommentList()
        .add(new Comment(commentRequestDTO.getMessage(), userByUsername,
            new Timestamp(System.currentTimeMillis()),
            meme));
    return memeRepository.save(meme);
  }

  public void deleteCommentById(Long id) throws CommentNotExistException {
    if (commentRepository.findById(id).isPresent()) {
      commentRepository.deleteById(id);
    } else {
      throw new CommentNotExistException();
    }
  }

  public List<MemeMinDTO> getByHitCountInOrderDesc() {
    List<Meme> orderByHitCount = memeRepository.findAll().stream()
        .sorted(Comparator.comparing(Meme::getHitCount).reversed())
        .collect(Collectors.toList());

    return orderByHitCount.stream()
        .map(o -> new MemeMinDTO(o.getId(), o.getCaption(), o.getImage(), o.getHitCount()))
        .collect(Collectors.toList());
  }

  public void validateGenre(String genre) throws InvalidGenreException {
    if (genre == null || genre.equals("")) {
      throw new InvalidGenreException();
    }
    genre = genre.toLowerCase();
    if ((!genre.equals("funny")) && (!genre.equals("scary")) && (!genre.equals("erotic")) &&
        (!genre.equals("sad"))) {
      throw new InvalidGenreException();
    }
  }

  public List<MemeGenreDTO> memesByGenre(String genre) {
    List<Meme> memesBySpecificGenre = memeRepository.findAllByGenre(genre).stream()
        .sorted(Comparator.comparing(Meme::getHitCount).reversed())
        .collect(Collectors.toList());
    ModelMapper mapper = new ModelMapper();
    return memesBySpecificGenre.stream().map(meme -> mapper.map(meme, MemeGenreDTO.class))
        .collect(Collectors.toList());
  }
}
