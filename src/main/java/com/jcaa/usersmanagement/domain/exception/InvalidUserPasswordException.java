package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserPasswordException extends DomainException {

  private static final String MESSAGE_IS_EMPTY = "The user password must not be empty.";

  private InvalidUserPasswordException(final String message) {
    super(message);
  }

  public static InvalidUserPasswordException becauseValueIsEmpty() {
    return new InvalidUserPasswordException(MESSAGE_IS_EMPTY);
  }

  public static InvalidUserPasswordException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserPasswordException(
        String.format("The user password must have at least %d characters.", minimumLength));
  }
}
