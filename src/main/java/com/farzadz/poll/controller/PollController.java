package com.farzadz.poll.controller;

import static com.farzadz.poll.controller.PollEndpoints.POLL_ANSWER_OPTIONS_PATH;
import static com.farzadz.poll.controller.PollEndpoints.POLL_ANSWER_OPTION_PATH;
import static com.farzadz.poll.controller.PollEndpoints.POLL_QUESTIONS_PATH;
import static com.farzadz.poll.controller.PollEndpoints.POLL_QUESTION_PATH;

import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.farzadz.poll.security.SecurityAnnotations.QuestionWrite;
import com.farzadz.poll.security.user.PollUser;
import com.farzadz.poll.security.user.PollUserDetailsService;
import com.farzadz.poll.service.PollService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PollController {

  private final PollService pollService;

  private final MapperFacade mapper;

  private final PollUserDetailsService userDetailsService;

  @RequestMapping(value = POLL_QUESTIONS_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<QuestionDTO> getAllQuestions() {
    return pollService.getAllQuestions().stream().map(question -> mapper.map(question, QuestionDTO.class))
        .collect(Collectors.toList());
  }

  @RequestMapping(value = POLL_QUESTION_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public QuestionDTO getQuestion(@PathVariable Long questionId) {
    return mapper.map(pollService.getQuestion(questionId), QuestionDTO.class);
  }

  @PreAuthorize("isAuthenticated()")
  @RequestMapping(value = POLL_QUESTIONS_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public QuestionDTO createQuestion(@RequestBody QuestionDTO questionDTO, Principal principal) {
    PollUser user = userDetailsService.loadUserByUsername((principal.getName()));
    return mapper.map(pollService.createQuestion(mapper.map(questionDTO, Question.class), user), QuestionDTO.class);
  }

  @QuestionWrite
  @RequestMapping(value = POLL_QUESTION_PATH, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public QuestionDTO updateQuestion(@PathVariable final Long questionId, @RequestBody QuestionDTO questionDTO) {
    return mapper
        .map(pollService.updateQuestion(questionId, mapper.map(questionDTO, Question.class)), QuestionDTO.class);
  }

  @QuestionWrite
  @RequestMapping(value = POLL_QUESTION_PATH, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteQuestion(@PathVariable final Long questionId) {
    pollService.deleteQuestion(questionId);
  }

  @RequestMapping(value = POLL_ANSWER_OPTIONS_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<AnswerOptionDTO> getAllAnswerOptions(@PathVariable final Long questionId) {
    return pollService.getAnswerOptionsForQuestion(questionId).stream()
        .map(question -> mapper.map(question, AnswerOptionDTO.class)).collect(Collectors.toList());
  }

  @RequestMapping(value = POLL_ANSWER_OPTION_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public AnswerOptionDTO getAnswerOption(@PathVariable Long questionId, @PathVariable Long answerOptionId) {
    return mapper.map(pollService.getAnswerOptionForQuestion(questionId, answerOptionId), AnswerOptionDTO.class);
  }

  @QuestionWrite
  @RequestMapping(value = POLL_ANSWER_OPTIONS_PATH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public AnswerOptionDTO createAnswerOption(@PathVariable final Long questionId,
      @RequestBody AnswerOptionDTO answerOptionDTO, Principal principal) {
    return mapper.map(pollService.createAnswerOption(questionId, mapper.map(answerOptionDTO, AnswerOption.class)),
        AnswerOptionDTO.class);
  }

  @QuestionWrite
  @RequestMapping(value = POLL_ANSWER_OPTION_PATH, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public AnswerOptionDTO updateAnswerOption(@PathVariable final Long questionId,
      @PathVariable final Long answerOptionId, @RequestBody AnswerOptionDTO answerOptionDTO) {
    return mapper.map(
        pollService.updateAnswerOption(questionId, answerOptionId, mapper.map(answerOptionDTO, AnswerOption.class)),
        AnswerOptionDTO.class);
  }

  @QuestionWrite
  @RequestMapping(value = POLL_ANSWER_OPTION_PATH, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteAnswerOption(@PathVariable final Long questionId, @PathVariable final Long answerOptionId) {
    pollService.deleteAnswerOption(questionId, answerOptionId);
  }
}
