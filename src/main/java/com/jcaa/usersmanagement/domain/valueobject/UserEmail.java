package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String value) {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

  public UserEmail {
    final String normalizedValue =
        Objects.requireNonNull(value, "UserEmail cannot be null").trim().toLowerCase();
    validate(normalizedValue);
    value = normalizedValue;
  }

  private static void validate(final String normalizedValue) {

    if (normalizedValue.isEmpty()) {
      throw InvalidUserEmailException.becauseValueIsEmpty();
    }

    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidUserEmailException.becauseFormatIsInvalid(normalizedValue);
    }
    
  }

  @Override
  public String toString() {
    return value;
  }
}
