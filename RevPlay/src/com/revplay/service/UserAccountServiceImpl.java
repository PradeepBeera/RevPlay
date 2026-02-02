package com.revplay.service;

import com.revplay.dao.IUserAccountDao;
import com.revplay.dao.UserAccountDaoImpl;
import com.revplay.model.UserAccount;

import java.util.List;

public class UserAccountServiceImpl implements IUserAccountService {

    private IUserAccountDao userDao = new UserAccountDaoImpl();

    public void setUserDao(IUserAccountDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean addUserAccount(UserAccount userAccount) {
        com.revplay.util.LoggerUtil.logInfo("Attempting to register user: " + userAccount.getEmail());
        if (getUserByEmail(userAccount.getEmail()) != null) {
            com.revplay.util.LoggerUtil
                    .logWarning("Registration failed: User already exists - " + userAccount.getEmail());
            return false;
        }
        boolean isCreated = userDao.addUserAccount(userAccount);
        if (isCreated) {
            com.revplay.util.LoggerUtil.logInfo("User registered successfully: " + userAccount.getEmail());
        } else {
            com.revplay.util.LoggerUtil.logWarning("User registration failed for: " + userAccount.getEmail());
        }
        return isCreated;
    }

    @Override
    public boolean updateUserAccount(UserAccount userAccount) {
        return userDao.updateUserAccount(userAccount);
    }

    @Override
    public boolean deleteUserAccount(int userId) {
        return userDao.deleteUserAccount(userId);
    }

    @Override
    public UserAccount getUserAccount(int userId) {
        return userDao.getUserAccount(userId);
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return userDao.getAllUserAccounts();
    }

    @Override
    public UserAccount getUserByEmail(String email) {
        return userDao.getUserAccountByEmail(email);
    }
}
