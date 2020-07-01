package com.farzadz.poll.domain.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PollUserDTO {

  @NonNull
  private String username;

  @NonNull
  private String password;

  private List<String> roles;

}
