package com.revplay.dao;

import com.revplay.model.UserAccount;
import com.revplay.util.JDBCUtil;

import java.sql.*;

import java.util.List;

public class UserAccountDaoImpl implements IUserAccountDao {

    @Override
    public boolean addUserAccount(UserAccount user) {
        String sql = "INSERT INTO USER_ACCOUNT (full_name, email, password_hash, phone, status, created_at, security_question, security_answer_hash, password_hint) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            ps.setString(7, user.getSecurityQuestion());
            ps.setString(8, user.getSecurityAnswerHash());
            ps.setString(9, user.getPasswordHint());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addUserAccount: " + e.getMessage(), e);
            // Check for duplicate key
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate")) {
                return false; // Preserve existing behavior for duplicates
            }
            throw new com.revplay.exception.RevPlayException("Error adding user account", e);
        }
    }

    @Override
    public boolean updateUserAccount(UserAccount user) {
        String sql = "UPDATE USER_ACCOUNT SET full_name = ?, password_hash = ?, phone = ?, status = ?, security_question = ?, security_answer_hash = ?, password_hint = ? WHERE user_id = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getStatus());
            ps.setString(5, user.getSecurityQuestion());
            ps.setString(6, user.getSecurityAnswerHash());
            ps.setString(7, user.getPasswordHint());
            ps.setInt(8, user.getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in updateUserAccount: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error updating user account", e);
        }
    }

    @Override
    public boolean deleteUserAccount(int userId) {
        return false;
    }

    @Override
    public UserAccount getUserAccount(int userId) {
        String sql = "SELECT * FROM USER_ACCOUNT WHERE user_id = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToUser(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getUserAccount: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching user", e);
        }
        return null;
    }

    @Override
    public List<UserAccount> getAllUserAccounts() {
        return List.of();
    }

    @Override
    public UserAccount getUserAccountByEmail(String email) {
        String sql = "SELECT * FROM USER_ACCOUNT WHERE email = ?";
        try (Connection con = JDBCUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToUser(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getUserAccountByEmail: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching user by email", e);
        }
        return null;
    }

    private UserAccount mapResultSetToUser(ResultSet rs) throws SQLException {
        UserAccount user = new UserAccount();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setSecurityQuestion(rs.getString("security_question"));
        user.setSecurityAnswerHash(rs.getString("security_answer_hash"));
        user.setPasswordHint(rs.getString("password_hint"));
        return user;
    }
}
