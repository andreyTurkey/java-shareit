package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Service
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    private final Set<String> deletedEmails = new HashSet<>();

    private Long count = 0L;

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User newUser) {
        if (emails.contains(newUser.getEmail())) {
            throw new DuplicateEmailException("Email exists.");
        }
        emails.add(newUser.getEmail());
        count++;
        newUser.setId(count);
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
            } else if (changedUser.getEmail().equals(user.getEmail())) {
                users.put(changedUser.getId(), user);
            } else {
                throw new DuplicateEmailException("Email exist.");
            }
        }
        users.put(changedUser.getId(), user);
        return users.get(changedUser.getId());
    }

    private void updateEmail(User changedUser) {
        deletedEmails.add(users.get(changedUser.getId()).getEmail());
        emails.remove(users.get(changedUser.getId()).getEmail());
        emails.add(changedUser.getEmail());
    }

    @Override
    public void deleteUserById(Long id) {
        if (users.containsKey(id)) {
            emails.remove(users.get(id).getEmail());
            users.remove(id);
        } else isUserExist(users.get(id));
    }

    @Override
    public void isUserExist(User user) {
        if (!users.containsKey(user.getId())) throw new EntityNotFoundException("User is not exist");
    }

    @Override
    public void isUserExistById(Long id) {
        if (!users.containsKey(id)) throw new EntityNotFoundException("User is not exist");
    }
}
