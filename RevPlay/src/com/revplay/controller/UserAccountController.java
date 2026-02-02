package com.revplay.controller;

import com.revplay.model.UserAccount;
import com.revplay.service.IUserAccountService;
import com.revplay.service.UserAccountServiceImpl;

import java.util.List;

public class UserAccountController {

    private static IUserAccountService userAccountService=new UserAccountServiceImpl();
    public boolean addUserAccount(UserAccount userAccount) {

        return userAccountService.addUserAccount(userAccount);
    }


    public boolean updateUserAccount(UserAccount userAccount) {
        return false;
    }

    public boolean deleteUserAccount(int userId) {
        return false;
    }


    public UserAccount getUserAccount(int userId) {
        return null;
    }


    public List<UserAccount> getAllUserAccounts() {
        return List.of();
}
}