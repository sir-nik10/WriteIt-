package com.example.cst338_sp22_project2.DAO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cst338_sp22_project2.DB.AppDataBase;


@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;

    private boolean mIsAdmin;
    private boolean mIsBanned;

    public User(String userName, String password, Boolean isAdmin) {
        mUserName = userName;
        mPassword = password;
        mIsAdmin = isAdmin;
        mIsBanned = false;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }

    public boolean isBanned() {
        return mIsBanned;
    }

    public void setIsBanned(boolean banned) {
        mIsBanned = banned;
    }
}
