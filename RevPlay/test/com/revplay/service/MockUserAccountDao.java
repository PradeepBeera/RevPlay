package com.revplay.service;

import com.revplay.dao.IUserAccountDao;
import com.revplay.model.UserAccount;
import java.util.*;

/**
 * Mock implementation of IUserAccountDao for unit testing.
 * Uses in-memory storage instead of database.
 */
public class MockUserAccountDao implements IUserAccountDao {
    private Map<Integer, UserAccount> users = new HashMap<>();
    private Map<String, UserAccount> usersByEmail = new HashMap<>();
    private int nextId = 1;

    @Override
    public boolean addUserAccount(UserAccount user) {
        if (user.getEmail() != null && usersByEmail.containsKey(user.getEmail())) {
            return false; // Duplicate email
        }
        user.setUserId(nextId++);
        users.put(user.getUserId(), user);
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
        return true;
    }

    @Override
    public boolean updateUserAccount(UserAccount user) {
        if (!users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
        return true;
    }

    @Override
    public boolean deleteUserAccount(int userId) {
        UserAccount removed = users.remove(userId);
        if (removed != null && removed.getEmail() != null) {
            usersByEmail.remove(removed.getEmail());
        }
        return removed != null;
    }

    @Override
    public UserAccount getUserAccount(int userId) {
        return users.get(userId);
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserAccount getUserAccountByEmail(String email) {
        return usersByEmail.get(email);
    }

    public void clear() {
        users.clear();
        usersByEmail.clear();
        nextId = 1;
    }
}
