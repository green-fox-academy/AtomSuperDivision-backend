package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.CommentNotExistException;
import com.greenfox.fedexasd.exception.MemeDoesNotExistException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.model.Comment;
import com.greenfox.fedexasd.model.CommentRequestDTO;
import com.greenfox.fedexasd.model.CommentSuccessResponseDTO;
import com.greenfox.fedexasd.model.CreateMemeRequestDTO;
import com.greenfox.fedexasd.model.CreateMemeResponseDTO;
import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.model.MemeDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.CommentRepository;
import com.greenfox.fedexasd.repository.MemeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
        meme -> new MemeDTO(meme.getCaption(), meme.getUrl(), meme.getFunny(), meme.getSad(),
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

    return new MemeDTO(meme.getCaption(), meme.getUrl(), meme.getFunny(), meme.getSad(),
        meme.getErotic(), meme.getScary(), meme.getCreatedAt(), meme.getUser().getUsername(), comments);
  }

  public Meme createMeme(CreateMemeRequestDTO createMemeRequestDTO, String username)
      throws UserDoesNotExistException {
    User user = userService.getUserByUsername(username);
    Meme meme = new Meme();
    meme.setCaption(createMemeRequestDTO.getCaption());
    meme.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    meme.setUrl(createMemeRequestDTO.getUrl());
    meme.setUser(user);
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
        .add(new Comment(commentRequestDTO.getMessage(), userByUsername, new Timestamp(System.currentTimeMillis()),
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
}
