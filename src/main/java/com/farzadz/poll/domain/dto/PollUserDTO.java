package com.farzadz.poll.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollUserDTO {

  private String username;

  private String password;

  private List<String> roles;

}
