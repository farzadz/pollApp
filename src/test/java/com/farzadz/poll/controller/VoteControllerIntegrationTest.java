package com.farzadz.poll.controller;

import static com.farzadz.poll.TestUtils.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.domain.dto.AnswerOptionDTO;
import com.farzadz.poll.domain.dto.QuestionDTO;
import com.farzadz.poll.domain.dto.VoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PollApplication.class)
@TestPropertySource(locations = "classpath:test.yaml")
class VoteControllerIntegrationTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void createAndDeleteVote_UsersIsAuthenticated_ShouldSuccessWithVoteDTODetails() throws Exception {

    String sampleQuestionText = "How was your day?";
    QuestionDTO questionDTO = new QuestionDTO();
    questionDTO.setText(sampleQuestionText);

    String postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_QUESTIONS_PATH).with(user("user").roles("USER")).contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(questionDTO))).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    QuestionDTO returnedQuestion = mapper.readValue(postResponse, QuestionDTO.class);

    AnswerOptionDTO answerOptionDTO = new AnswerOptionDTO();
    answerOptionDTO.setQuestionId(returnedQuestion.getId());
    answerOptionDTO.setText("answerOption");

    postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_ANSWER_OPTIONS_PATH, returnedQuestion.getId()).with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON).content(asJsonString(answerOptionDTO))).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    AnswerOptionDTO returnedAnswerOption = mapper.readValue(postResponse, AnswerOptionDTO.class);

    postResponse = mockMvc.perform(
        post(PollEndpoints.POLL_VOTE_PATH, returnedAnswerOption.getId()).with(user("user").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse()
        .getContentAsString();

    VoteDTO returnedVote = mapper.readValue(postResponse, VoteDTO.class);

    assertEquals("user", returnedVote.getUsername());
    assertEquals(returnedAnswerOption.getId(), returnedVote.getAnswerOptionId());

    //retract vote
    mockMvc.perform(delete(PollEndpoints.POLL_VOTE_PATH, returnedAnswerOption.getId()).with(user("user").roles("USER")))
        .andExpect(status().isOk());

    //vote should not exist anymore
    mockMvc.perform(get(PollEndpoints.POLL_VOTE_PATH, returnedAnswerOption.getId()).with(user("user").roles("USER")))
        .andExpect(status().isBadRequest());
  }

  //TODO add checks for retracting vote with different user
}