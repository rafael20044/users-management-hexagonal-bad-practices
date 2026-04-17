package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;

  @Override
  public UserModel execute(final LoginCommand command) {

    final UserEmail email = new UserEmail(command.email());

    final UserModel user = getUserByEmail(email);
    validUser(user, command.password());

    return user;
  }

  private UserModel getUserByEmail(final UserEmail email) {
    return getUserByEmailPort.getByEmail(email).orElse(null);
  }

  private void validUser(final UserModel user, final String plainPassword) {
    if (Objects.isNull(user)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }

    if (!user.getPassword().verifyPlain(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }

    if (user.getStatus() != UserStatus.ACTIVE) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }

}
