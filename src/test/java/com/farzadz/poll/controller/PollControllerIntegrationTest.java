package com.farzadz.poll.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.TestUtils;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
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
  public void createAndGetAllQuestions__ShouldReturnCreatedQuestion() throws Exception {
    TestUtils.loginUser();
    String sampleQuestionText = "How was your day?";
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setText(sampleQuestionText);

    String postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_QUESTIONS_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(questionDTO))).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    QuestionDTO returnedQuestion = mapper.readValue(postResponse, QuestionDTO.class);
    assertNotNull(returnedQuestion);
    assertNotNull(returnedQuestion.getId());
    assertNotNull(returnedQuestion.getCreatedAt());
    assertEquals(sampleQuestionText, returnedQuestion.getText());

    String responseString = mockMvc.perform(get(PollEndpoints.POLL_QUESTIONS_PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    List<QuestionDTO> questionDTOS = Arrays.asList(mapper.readValue(responseString, QuestionDTO[].class));
    assertTrue(questionDTOS.stream().anyMatch(q -> q.getText().equals(sampleQuestionText)));

  }

  @Test
  public void createAndQuestionWithAnswerOption__ShouldReturnCreatedQuestionAndOptions() throws Exception {
    TestUtils.loginUser();
    String sampleQuestionText = "How was your day?";
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setText(sampleQuestionText);

    String postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_QUESTIONS_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(questionDTO))).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    QuestionDTO returnedQuestion = mapper.readValue(postResponse, QuestionDTO.class);
    assertNotNull(returnedQuestion);
    assertNotNull(returnedQuestion.getId());
    assertNotNull(returnedQuestion.getCreatedAt());
    assertEquals(sampleQuestionText, returnedQuestion.getText());

    //check access to created question by user
    mockMvc.perform(get(PollEndpoints.POLL_QUESTION_PATH, returnedQuestion.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    String responseString = mockMvc.perform(get(PollEndpoints.POLL_QUESTIONS_PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    List<QuestionDTO> questionDTOS = Arrays.asList(mapper.readValue(responseString, QuestionDTO[].class));
    assertTrue(questionDTOS.stream().anyMatch(q -> q.getText().equals(sampleQuestionText)));

  }

  @Test
  public void requestQuestionThatDostNotExist_NormalUser_ShouldReturnForbidden() throws Exception {
    TestUtils.loginUser();
    mockMvc.perform(get(PollEndpoints.POLL_QUESTION_PATH, 12345).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

  }

  //Fixme check specific exception type
  @Test(expected = Exception.class)
  public void requestQuestionThatDostNotExist_AdminUser_ShouldReturnForbidden() throws Exception {
    TestUtils.loginAdmin();
    mockMvc.perform(get(PollEndpoints.POLL_QUESTION_PATH, 12345).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
  }

  private String asJsonString(Object val) throws JsonProcessingException {
    return mapper.writeValueAsString(val);

  }
}
