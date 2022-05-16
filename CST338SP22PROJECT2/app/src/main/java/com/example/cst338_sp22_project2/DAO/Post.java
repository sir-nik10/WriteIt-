package com.example.cst338_sp22_project2.DAO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cst338_sp22_project2.DB.AppDataBase;

@Entity(tableName = AppDataBase.POST_TABLE)
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int mPostId;

    private String mUsername;
    private String mPostContent;

    public Post(String username, String postContent) {
        this.mUsername = username;
        this.mPostContent = postContent;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPostContent() {
        return mPostContent;
    }

    public void setPostContent(String postContent) {
        this.mPostContent = postContent;
    }

    public int getPostId() {
        return mPostId;
    }

    public void setPostId(int postId) {
        mPostId = postId;
    }
}
