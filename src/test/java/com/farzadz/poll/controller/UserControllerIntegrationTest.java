package com.farzadz.poll.controller;

import static com.farzadz.poll.TestUtils.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userDTO.getUsername())
        .with(httpBasic(userDTO.getUsername(), userDTO.getPassword()))).andExpect(status().isOk());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userDTO.getUsername()).with(httpBasic("user", "password")))
        .andExpect(status().isForbidden());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userDTO.getUsername()).with(httpBasic("admin", "password")))
        .andExpect(status().isOk());
  }

  @Test
  public void createUser__UserShouldBeEditableByHimselfAndAdmin() throws Exception {
    String userName = "sampleUser" + random.nextLong();
    PollUserDTO userDTO = new PollUserDTO(userName, "password");
    mockMvc.perform(
        post(PollEndpoints.POLL_USERS_PATH).contentType(MediaType.APPLICATION_JSON).content(asJsonString(userDTO)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    PollUserDTO updatedUser = new PollUserDTO(userName, "newPassword");

    mockMvc.perform(put(PollEndpoints.POLL_USER_PATH, userName).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(updatedUser)).with(httpBasic(userName, userDTO.getPassword()))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic(userName, updatedUser.getPassword())))
        .andExpect(status().isOk());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic("admin", "password")))
        .andExpect(status().isOk());

    PollUserDTO adminUpdatedUser = new PollUserDTO(userName, "adminCreatedPass");

    mockMvc.perform(put(PollEndpoints.POLL_USER_PATH, userName).contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(adminUpdatedUser)).with(httpBasic("admin", "password"))).andExpect(status().isOk());

    mockMvc.perform(get(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic(userName, updatedUser.getPassword())))
        .andExpect(status().isUnauthorized());

    mockMvc
        .perform(get(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic(userName, adminUpdatedUser.getPassword())))
        .andExpect(status().isOk());

    mockMvc.perform(
        delete(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic(userName, adminUpdatedUser.getPassword())))
        .andExpect(status().isOk());

    //Fixme status code is not informative enough
    mockMvc
        .perform(get(PollEndpoints.POLL_USER_PATH, userName).with(httpBasic(userName, adminUpdatedUser.getPassword())))
        .andExpect(status().isUnauthorized());
  }
}
