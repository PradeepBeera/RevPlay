package com.revplay.service;

import com.revplay.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserAccountService using mock DAO.
 */
public class UserAccountServiceTest {

    private MockUserAccountDao mockUserDao;
    private UserAccountServiceImpl userService;

    @BeforeEach
    public void setup() {
        mockUserDao = new MockUserAccountDao();
        userService = new UserAccountServiceImpl();
        userService.setUserDao(mockUserDao);
    }

    @Test
    public void testRegisterUser_Success() {
        UserAccount user = new UserAccount();
        user.setEmail("test@example.com");
        user.setPasswordHash("password123");
        user.setFullName("Test User");

        boolean result = userService.addUserAccount(user);

        assertTrue(result, "User should be registered successfully");
        assertNotNull(mockUserDao.getUserAccountByEmail("test@example.com"));
        assertEquals(1, user.getUserId());
    }

    @Test
    public void testRegisterUser_DuplicateEmail() {
        UserAccount user1 = new UserAccount();
        user1.setEmail("duplicate@example.com");
        user1.setPasswordHash("pass1");
        userService.addUserAccount(user1);

        UserAccount user2 = new UserAccount();
        user2.setEmail("duplicate@example.com");
        user2.setPasswordHash("pass2");

        boolean result = userService.addUserAccount(user2);

        assertFalse(result, "Should not allow duplicate email registration");
    }

    @Test
    public void testGetUserByEmail_Found() {
        UserAccount user = new UserAccount();
        user.setEmail("find@example.com");
        user.setFullName("Find Me");
        mockUserDao.addUserAccount(user);

        UserAccount found = userService.getUserByEmail("find@example.com");

        assertNotNull(found);
        assertEquals("Find Me", found.getFullName());
    }

    @Test
    public void testGetUserByEmail_NotFound() {
        UserAccount user = userService.getUserByEmail("nonexistent@example.com");

        assertNull(user, "Should return null for non-existent user");
    }

    @Test
    public void testUpdateUser_Success() {
        UserAccount user = new UserAccount();
        user.setEmail("update@example.com");
        user.setFullName("Original Name");
        mockUserDao.addUserAccount(user);

        user.setFullName("Updated Name");
        boolean result = userService.updateUserAccount(user);

        assertTrue(result);
        assertEquals("Updated Name", mockUserDao.getUserAccount(user.getUserId()).getFullName());
    }
}
