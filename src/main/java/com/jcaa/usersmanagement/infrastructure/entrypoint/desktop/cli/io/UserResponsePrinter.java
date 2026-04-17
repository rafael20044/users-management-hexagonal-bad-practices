package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-10s : %s%n";

  private final ConsoleIO console;

  public void print(final UserResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID", response.getId());
    console.printf(ROW_FORMAT, "Name", response.getName());
    console.printf(ROW_FORMAT, "Email", response.getEmail());
    console.printf(ROW_FORMAT, "Role", response.getRole());
    console.printf(ROW_FORMAT, "Status", getStatusLabel(response.getStatus()));
    console.println(SEPARATOR);
  }

  public void printList(final List<UserResponse> users) {
    if (users.isEmpty()) {
      console.println("  No users found.");
      return;
    }

    console.printf("%n  Total: %d user(s)%n", users.size());
    users.forEach(this::print);
  }

  public void printSummary(final List<UserResponse> users) {
    if (users.isEmpty()) {
      console.println("  No users found.");
      return;
    }

    for (UserResponse user : users) {
      console.printf("  %s (%s)%n", user.getName(), getStatusLabel(user.getStatus()));
    }
  }

  private static String getStatusLabel(final String status) {
    Map<String, String> statusLabels = new HashMap<>();
    statusLabels.put("ACTIVE", "Activo");
    statusLabels.put("INACTIVE", "Inactivo");
    statusLabels.put("PENDING", "Pendiente de activacion");
    statusLabels.put("BLOCKED", "Bloqueado");
    statusLabels.put("DELETED", "Eliminado");
    return statusLabels.getOrDefault(status, "Estado desconocido");
  }
}