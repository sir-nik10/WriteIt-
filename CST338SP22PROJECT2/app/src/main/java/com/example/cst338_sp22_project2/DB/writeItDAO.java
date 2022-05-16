package com.example.cst338_sp22_project2.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;

import java.util.List;

@Dao
public interface writeItDAO {
    @Insert
    void insert(User... users);
    @Update
    void update(User... users);
    @Delete
    void delete(User... users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username ")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Post... posts);
    @Update
    void update(Post... posts);
    @Delete
    void delete(Post... posts);

    @Query("SELECT * FROM " + AppDataBase.POST_TABLE)
    List<Post> getAllPosts();

    @Query("SELECT * FROM " + AppDataBase.POST_TABLE + " WHERE mUsername = :username ")
    List<Post> getAllPostsFromUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.POST_TABLE + " WHERE mUserName = :username ")
    Post getPostByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.POST_TABLE + " WHERE mPostId = :postId")
    Post getPostByPostId(int postId);


}
