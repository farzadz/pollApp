package com.farzadz.poll.controller;

import static com.farzadz.poll.controller.PollEndpoints.POLL_USERS_PATH;
import static com.farzadz.poll.controller.PollEndpoints.POLL_USER_PATH;

import com.farzadz.poll.domain.dto.PollUserDTO;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

  private final PollUserDetailsService userService;

  private final MapperFacade mapper;

  @RequestMapping(value = POLL_USERS_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PollUserDTO> getAllUsers() {
    return userService.getAllUsers().stream().map(user -> mapper.map(user, PollUserDTO.class))
        .collect(Collectors.toList());
  }

  @RequestMapping(value = POLL_USER_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public PollUserDTO getUser(@PathVariable String username) {
    return mapper.map(userService.getUserByUsername(username), PollUserDTO.class);
  }

  @RequestMapping(value = POLL_USERS_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public PollUserDTO createUser(@RequestBody PollUserDTO userDTO) {
    return mapper.map(userService.createUser(mapper.map(userDTO, PollUser.class)), PollUserDTO.class);
  }

  @RequestMapping(value = POLL_USER_PATH, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public PollUserDTO editUser(@PathVariable String username, @RequestBody PollUserDTO userDTO) {
    return mapper.map(userService.updateUser(username, mapper.map(userDTO, PollUser.class)), PollUserDTO.class);
  }

  @RequestMapping(value = POLL_USER_PATH, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteUser(@PathVariable String username) {
    userService.deleteUser(username);
  }

}
