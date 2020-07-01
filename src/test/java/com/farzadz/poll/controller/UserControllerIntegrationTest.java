package com.farzadz.poll.controller;

import static com.farzadz.poll.TestUtils.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.farzadz.poll.PollApplication;
import com.farzadz.poll.domain.dto.PollUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
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
public class UserControllerIntegrationTest {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  private Random random = new Random();

  /**
   * user:password and admin:password already exist in db with their corresponding roles.
   */
  @Test
  public void createUser_UserDoesNotExist_UserShouldSeeTheirStatusStatusNotVisibleForNonAdmins() throws Exception {
    PollUserDTO userDTO = new PollUserDTO("sampleUser" + random.nextLong(), "password");
    String response = mockMvc.perform(
        post(PollEndpoints.POLL_USERS_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(userDTO)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    PollUserDTO returnedUser = mapper.readValue(response, PollUserDTO.class);
    assertEquals(returnedUser.getUsername(), userDTO.getUsername());
    assertNotNull(returnedUser.getRoles());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, returnedUser.getUsername())
        .with(httpBasic(userDTO.getUsername(), userDTO.getPassword())).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userDTO))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, returnedUser.getUsername()).with(httpBasic("user", "password"))
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userDTO))).andExpect(status().isForbidden())
        .andReturn().getResponse().getContentAsString();

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, returnedUser.getUsername()).with(httpBasic("admin", "password"))
        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userDTO))).andExpect(status().isOk()).andReturn()
        .getResponse().getContentAsString();
  }
}
