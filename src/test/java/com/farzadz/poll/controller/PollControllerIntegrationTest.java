package com.farzadz.poll.controller;

import static com.farzadz.poll.TestUtils.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Uses TestUtils instead of mockUser annotations
 */

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PollApplication.class)
@TestPropertySource(locations = "classpath:test.yaml")
public class PollControllerIntegrationTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void createAndGetAllQuestions_UserExists_ShouldReturnCreatedQuestion() throws Exception {
    String sampleQuestionText = "How was your day?";
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setText(sampleQuestionText);

    String postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_QUESTIONS_PATH).with(user("user").roles("USER")).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(questionDTO))).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    QuestionDTO returnedQuestion = mapper.readValue(postResponse, QuestionDTO.class);
    assertNotNull(returnedQuestion);
    assertNotNull(returnedQuestion.getId());
    assertNotNull(returnedQuestion.getCreatedAt());
    assertEquals(sampleQuestionText, returnedQuestion.getText());

    String responseString = mockMvc
        .perform(get(PollEndpoints.POLL_QUESTIONS_PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    List<QuestionDTO> questionDTOS = Arrays.asList(mapper.readValue(responseString, QuestionDTO[].class));
    assertTrue(questionDTOS.stream().anyMatch(q -> q.getText().equals(sampleQuestionText)));

  }

  @Test
  public void createAndQuestionWithAnswerOption_UserExists_ShouldReturnCreatedQuestionAndOptions() throws Exception {
    String sampleQuestionText = "How was your day?";
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setText(sampleQuestionText);

    String postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_QUESTIONS_PATH).with(user("user").roles("USER")).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(questionDTO))).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    String responseString = mockMvc.perform(get(PollEndpoints.POLL_QUESTIONS_PATH)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    List<QuestionDTO> questionDTOS = Arrays.asList(mapper.readValue(responseString, QuestionDTO[].class));
    assertTrue(questionDTOS.stream().anyMatch(q -> q.getText().equals(sampleQuestionText)));

    QuestionDTO returnedQuestion = mapper.readValue(postResponse, QuestionDTO.class);

    //check access to created question by user
    mockMvc.perform(
        get(PollEndpoints.POLL_QUESTION_PATH, returnedQuestion.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    AnswerOptionDTO answerOptionDTO = new AnswerOptionDTO();
    answerOptionDTO.setQuestionId(returnedQuestion.getId());
    answerOptionDTO.setText("answerOption");

    postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_ANSWER_OPTIONS_PATH, returnedQuestion.getId()).with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON).content(asJsonString(answerOptionDTO))).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    AnswerOptionDTO returnedAnswerOption = mapper.readValue(postResponse, AnswerOptionDTO.class);
    assertEquals(answerOptionDTO.getText(), returnedAnswerOption.getText());
    assertEquals(answerOptionDTO.getQuestionId(), returnedAnswerOption.getQuestionId());

    //check user access to answerOption
    String getResponse = mockMvc
        .perform(get(PollEndpoints.POLL_ANSWER_OPTION_PATH, returnedQuestion.getId(), returnedAnswerOption.getId()))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    returnedAnswerOption = mapper.readValue(getResponse, AnswerOptionDTO.class);
    assertEquals(answerOptionDTO.getText(), returnedAnswerOption.getText());
    assertEquals(answerOptionDTO.getQuestionId(), returnedAnswerOption.getQuestionId());

  }

  @Test
  public void requestQuestionThatDostNotExist_NormalUser_ShouldReturnNotFound() throws Exception {
    mockMvc.perform(get(PollEndpoints.POLL_QUESTION_PATH, 12345).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

  }

  @Test
  public void requestQuestionThatDostNotExist_AdminUser_ShouldReturnNotFound() throws Exception {
    mockMvc.perform(get(PollEndpoints.POLL_QUESTION_PATH, 12345).with(user("user").roles("ADMIN"))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

}

