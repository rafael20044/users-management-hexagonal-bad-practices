package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;
  private final Validator validator;

  @Override
  public UserModel execute(final LoginCommand command) {
    validateCommand(command);

    final UserEmail email = new UserEmail(command.email());

    final UserModel user = getUserByEmail(email);
    validUser(user, command.password());

    return user;
  }

  // Clean Code - Regla 8: viola CQS — consulta Y tiene efectos de modificación implícitos.
  // Clean Code - Regla 1: hace demasiadas cosas: busca usuario, verifica contraseña y valida estado.
  // Clean Code - Regla 2 (funciones cortas): este método creció hasta convertirse en una mini-clase.
  //   Hace fetch → null-check → password-verify → status-check → return; son 4 responsabilidades.
  //   Si exige demasiado análisis para entenderse, debe dividirse.
  // Clean Code - Regla 14 (Ley de Deméter): se navega a internals del objeto:
  //   user → getPassword() → verifyPlain() en lugar de delegar con user.passwordMatches(plain).

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


  private void validateCommand(final LoginCommand command) {
    final Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
