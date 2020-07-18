package com.farzadz.poll.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.TestUtils;
import com.farzadz.poll.dataentry.entity.AnswerOption;
import com.farzadz.poll.dataentry.entity.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
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
public class StatsControllerIntegrationTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MapperFacade dtoMapper;

  @Autowired
  private TestUtils testUtils;

  @Test
  public void statsUpdate_QuestionAndOptionsExist_ShouldUpdateStats() throws Exception {
    Question questionInDB = testUtils.createQuestionWithAnswerOptionForUser("user");
    List<AnswerOption> answerOptions = questionInDB.getAnswerOptions();
    TypeReference<HashMap<Long, Long>> typeRef = new TypeReference<>() {
    };
    String getQuestionStatsResponse = mockMvc
        .perform(get(PollEndpoints.POLL_QUESTION_STATS_PATH, questionInDB.getId()).with(user("user").roles("USER")))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    HashMap<Long, Long> answerOptionStats = mapper.readValue(getQuestionStatsResponse, typeRef);
    assertEquals(0L, answerOptionStats.get(answerOptions.get(0).getId()).longValue());
    assertEquals(0L, answerOptionStats.get(answerOptions.get(1).getId()).longValue());

    mockMvc.perform(post(PollEndpoints.POLL_VOTE_PATH, answerOptions.get(0).getId()).with(user("user").roles("USER"))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    mockMvc.perform(post(PollEndpoints.POLL_VOTE_PATH, answerOptions.get(1).getId()).with(user("user").roles("USER"))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

    getQuestionStatsResponse = mockMvc
        .perform(get(PollEndpoints.POLL_QUESTION_STATS_PATH, questionInDB.getId()).with(user("user").roles("USER")))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    answerOptionStats = mapper.readValue(getQuestionStatsResponse, typeRef);
    assertEquals(1L, answerOptionStats.get(answerOptions.get(0).getId()).longValue());
    assertEquals(1L, answerOptionStats.get(answerOptions.get(1).getId()).longValue());
  }

  @Test
  public void statsUpdate_QuestionAndOptionsExist_ShouldUpdateUserStats() throws Exception {
    Question questionInDB = testUtils.createQuestionWithAnswerOptionForUser("user");
    List<AnswerOption> answerOptions = questionInDB.getAnswerOptions();
    TypeReference<HashMap<Long, Set<String>>> typeRef = new TypeReference<>() {
    };
    String getQuestionStatsResponse = mockMvc.perform(
        get(PollEndpoints.POLL_QUESTION_USERS_STATS_PATH, questionInDB.getId()).with(user("user").roles("USER")))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    HashMap<Long, Set<String>> userAnswerOptionStats = mapper.readValue(getQuestionStatsResponse, typeRef);
    assertTrue(userAnswerOptionStats.isEmpty());

    mockMvc.perform(post(PollEndpoints.POLL_VOTE_PATH, answerOptions.get(0).getId()).with(user("user").roles("USER"))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    mockMvc.perform(post(PollEndpoints.POLL_VOTE_PATH, answerOptions.get(1).getId()).with(user("user").roles("USER"))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

    getQuestionStatsResponse = mockMvc.perform(
        get(PollEndpoints.POLL_QUESTION_USERS_STATS_PATH, questionInDB.getId()).with(user("user").roles("USER")))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    userAnswerOptionStats = mapper.readValue(getQuestionStatsResponse, typeRef);
    assertTrue(userAnswerOptionStats.get(answerOptions.get(0).getId()).contains("user"));
    assertTrue(userAnswerOptionStats.get(answerOptions.get(1).getId()).contains("user"));
  }
}
