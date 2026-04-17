package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  private static final String MESSAGE_EMPTY_NAME = "The user name must not be empty.";
  private static final String MESSAGE_SHORT_NAME = "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    return new InvalidUserNameException(MESSAGE_EMPTY_NAME);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserNameException(
        String.format(MESSAGE_SHORT_NAME, minimumLength));
  }
}
