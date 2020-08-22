package org.store.test.service.user;

import org.store.test.dto.UserDto;
import org.store.test.exception.EntityNotFoundException;
import org.store.test.model.User;
import org.store.test.repository.UserDbManager;
import org.store.test.service.user.converter.SimpleUserConverter;
import org.store.test.service.user.converter.UserConverter;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDbManager userDbManager;
    private final UserConverter userConverter;

    public UserService(UserDbManager userDbManager, UserConverter userConverter) {
        this.userDbManager = userDbManager;
        this.userConverter = userConverter;
    }

    public UserDto put(UserDto userDto) {
        final Optional<User> oUser = userDbManager.put(userConverter.convert(userDto));
        final User user = oUser.orElseThrow(() -> new EntityNotFoundException("Order"));
        return userConverter.convert(user);
    }

    public List<UserDto> get() {
        return userConverter.convert(userDbManager.get());
    }

    public UserDto getById(long id) {
        final Optional<User> oUser = userDbManager.getById(id);
        final User user = oUser.orElseThrow(() -> new EntityNotFoundException("Order"));
        return userConverter.convert(user);
    }
}
