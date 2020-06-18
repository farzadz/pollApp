package com.farzadz.poll.controller;

public class PollEndpoints {

  public static final String BASE_API_PATH = "/api";

  public static final String POLLS_PATH = BASE_API_PATH + "/polls";

  public static final String POLL_QUESTIONS_PATH = POLLS_PATH + "/questions";

  public static final String POLL_QUESTION_PATH = POLL_QUESTIONS_PATH + "/{questionId}";

  public static final String POLL_ANSWER_OPTIONS_PATH = POLL_QUESTION_PATH + "/answerOptions";

  public static final String POLL_ANSWER_OPTION_PATH = POLL_ANSWER_OPTIONS_PATH + "/{answerOptionId}";

  public static final String POLL_USERS_PATH = BASE_API_PATH + "/users";

  public static final String POLL_USER_PATH = POLL_USERS_PATH + "/{userId}";

}
