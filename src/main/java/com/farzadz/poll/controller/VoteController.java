package com.farzadz.poll.controller;

import static com.farzadz.poll.controller.PollEndpoints.POLL_VOTE_PATH;

import com.farzadz.poll.domain.dto.VoteDTO;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import com.farzadz.poll.service.VoteService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VoteController {

  private final VoteService voteService;

  private final PollUserDetailsService userDetailsService;

  private final MapperFacade mapper;

  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = POLL_VOTE_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDTO vote(@PathVariable Long answerOptionId, Principal principal) {
    PollUser user = userDetailsService.loadUserByUsername((principal.getName()));
    return mapper.map(voteService.vote(answerOptionId, user), VoteDTO.class);
  }

  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = POLL_VOTE_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public VoteDTO getVote(@PathVariable Long answerOptionId, Principal principal) {
    PollUser user = userDetailsService.loadUserByUsername((principal.getName()));
    return mapper.map(voteService.getVote(answerOptionId, user), VoteDTO.class);
  }

  @RequestMapping(value = POLL_VOTE_PATH, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void retractVote(@PathVariable Long answerOptionId, Principal principal) {
    PollUser user = userDetailsService.loadUserByUsername((principal.getName()));
    voteService.retractVote(user, answerOptionId);
  }

}
