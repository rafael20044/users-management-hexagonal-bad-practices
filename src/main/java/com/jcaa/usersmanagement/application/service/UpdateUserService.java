package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public final class UpdateUserService implements UpdateUserUseCase {

    private final UpdateUserPort updateUserPort;
    private final GetUserByIdPort getUserByIdPort;
    private final GetUserByEmailPort getUserByEmailPort;
    private final EmailNotificationService emailNotificationService;

    @Override
    public UserModel execute(final UpdateUserCommand command) {
        final UserId userId = new UserId(command.id());
        final UserEmail newEmail = new UserEmail(command.email());
        final UserModel current = findExistingUserOrFail(userId);
        
        checkEmailAvailability(newEmail, userId);

        final UserModel userToUpdate = UserApplicationMapper.fromUpdateCommandToModel(
            command, 
            current.getPassword()
        );
        final UserModel updatedUser = updateUserPort.update(userToUpdate);

        emailNotificationService.notifyUserUpdated(updatedUser);

        return updatedUser;
    }

    private void checkEmailAvailability(UserEmail email, UserId currentOwnerId) {
        getUserByEmailPort.getByEmail(email)
            .ifPresent(existingUser -> {
                if (isAnotherUser(existingUser, currentOwnerId)) {
                    throw UserAlreadyExistsException.becauseEmailAlreadyExists(email.value());
                }
            });
    }

    private boolean isAnotherUser(UserModel existingUser, UserId currentOwnerId) {
        return !existingUser.getId().equals(currentOwnerId);
    }

    private UserModel findExistingUserOrFail(final UserId userId) {
        return getUserByIdPort.getById(userId)
            .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
    }
}