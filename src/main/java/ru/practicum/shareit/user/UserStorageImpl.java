package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Qualifier("UserStorage")
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final List<String> emails = new ArrayList<>();

    private final List<String> deletedEmails = new ArrayList<>();

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User createUser(User newUser) {
        if (emails.contains(newUser.getEmail())) {
            throw new DuplicateEmailException("Email exists.");
        }
        emails.add(newUser.getEmail());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User changedUser) {
        updateEmail(changedUser);
        User user = users.get(changedUser.getId());
        if (changedUser.getName() != null) user.setName(changedUser.getName());
        if (changedUser.getEmail() != null) {
            if (!deletedEmails.contains(changedUser.getEmail())) {
                user.setEmail(changedUser.getEmail());
            } else {
                throw new DuplicateEmailException("Email exist.");
            }
        }
        users.put(changedUser.getId(), user);
        return users.get(changedUser.getId());
    }

        private void updateEmail (User changedUser){
            deletedEmails.add(users.get(changedUser.getId()).getEmail());
            emails.remove(users.get(changedUser.getId()).getEmail());
            emails.add(changedUser.getEmail());
        }

        @Override
        public void deleteUserById (Integer id){

        }

        @Override
        public void isUserExist (User user){
            if (!users.containsKey(user.getId())) throw new UserNotFoundException("User is not exist");
        }
    }
