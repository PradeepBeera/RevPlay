package com.revplay.dao;

import com.revplay.model.UserAccount;

import java.util.List;

public interface IUserAccountDao {
    public boolean addUserAccount(UserAccount userAccount);

    public boolean updateUserAccount(UserAccount userAccount);

    public boolean deleteUserAccount(int userId);

    public UserAccount getUserAccount(int userId);

    public List<UserAccount> getAllUserAccounts();

    public UserAccount getUserAccountByEmail(String email);

}
