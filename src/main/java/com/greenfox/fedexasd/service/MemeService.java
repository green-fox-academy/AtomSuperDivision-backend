package com.greenfox.fedexasd.service;

import com.greenfox.fedexasd.exception.MemeDoesNotExistException;
import com.greenfox.fedexasd.exception.UserDoesNotExistException;
import com.greenfox.fedexasd.model.CreateMemeRequestDTO;
import com.greenfox.fedexasd.model.CreateMemeResponseDTO;
import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.model.MemeDTO;
import com.greenfox.fedexasd.model.User;
import com.greenfox.fedexasd.repository.MemeRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemeService {

  private final MemeRepository memeRepository;
  private final UserService userService;

  @Autowired
  public MemeService(MemeRepository memeRepository, UserService userService) {
    this.memeRepository = memeRepository;
    this.userService = userService;
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
    return new MemeDTO(meme.getCaption(), meme.getUrl(), meme.getFunny(), meme.getSad(),
        meme.getErotic(), meme.getScary(), meme.getCreatedAt(), meme.getUser().getUsername());

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
}
