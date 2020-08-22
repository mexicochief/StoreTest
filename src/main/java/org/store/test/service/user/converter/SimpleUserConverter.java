package org.store.test.service.user.converter;

import org.store.test.dto.UserDto;
import org.store.test.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleUserConverter implements UserConverter {

    @Override
    public User convert(UserDto userDto) {
        return new User(userDto.getId(), userDto.getFirstName(), userDto.getLastName());
    }

    @Override
    public UserDto convert(User oUser) {
        return new UserDto(oUser.getId(), oUser.getFirstName(), oUser.getLastName());
    }

    @Override
    public List<UserDto> convert(List<User> users) {
        return users.stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }
}
