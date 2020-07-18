package com.farzadz.poll.controller;

import static com.farzadz.poll.controller.PollEndpoints.POLL_QUESTION_STATS_PATH;
import static com.farzadz.poll.controller.PollEndpoints.POLL_QUESTION_USERS_STATS_PATH;

import com.farzadz.poll.security.SecurityAnnotations.QuestionWrite;
import com.farzadz.poll.service.VoteService;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

  private final VoteService voteService;

  private final MapperFacade mapper;

  @QuestionWrite
  @RequestMapping(value = POLL_QUESTION_STATS_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<Long, Long> getQuestionCountStats(@PathVariable Long questionId) {
    return voteService.questionStats(questionId).entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
  }

  @QuestionWrite
  @RequestMapping(value = POLL_QUESTION_USERS_STATS_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<Long, Set<String>> getQuestionUserAnswers(@PathVariable Long questionId) {
    return voteService.userQuestionAnswers(questionId).entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().getId(), Entry::getValue));
  }
}
