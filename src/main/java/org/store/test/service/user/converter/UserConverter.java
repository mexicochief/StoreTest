package org.store.test.service.user.converter;

import org.store.test.dto.UserDto;
import org.store.test.model.User;

import java.util.List;

public interface UserConverter {
    User convert(UserDto userDto);

    UserDto convert(User oUser);

    List<UserDto> convert(List<User> users);
}
