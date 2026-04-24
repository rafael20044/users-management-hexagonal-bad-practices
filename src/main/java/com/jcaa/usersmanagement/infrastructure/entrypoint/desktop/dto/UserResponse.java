package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

import lombok.Value;

@Value
public class UserResponse {

  private String id;
  private String name;
  private String email;
  private String role;
  private String status;

  public UserResponse(
      final String id,
      final String name,
      final String email,
      final String role,
      final String status) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.status = status;
  }
}
