package com.example.cst338_sp22_project2.Posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.MainActivity;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityMainBinding;
import com.example.cst338_sp22_project2.databinding.ActivityModifyPostBinding;

import java.util.List;

public class ModifyPost extends AppCompatActivity {

    private static final String USERNAME_KEY = "com.example.cst338_sp22_project2.usernameKey";

    private writeItDAO mWriteItDAO;

    private ActivityModifyPostBinding binding;

    private Button mSearchPostIdButton;
    private Button mModifyPostButton;
    private Button mBackButton;

    private EditText mPostIdEditText;
    private EditText mPostPlayAreaEditText;

    private Post mPost;
    private User mUser;

    private int postId;
    private int mUserId;

    private String postContent;
    private String currentUsername;

    private List<Post> mCurrentUserPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_post);

        getDatabase();

        binding = ActivityModifyPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPostIdEditText = binding.getPostIdEditText;
        mPostPlayAreaEditText = binding.ModifyPostEditText;

        currentUsername = getIntent().getStringExtra(USERNAME_KEY);

        mUser = mWriteItDAO.getUserByUsername(currentUsername);
        mUserId = mUser.getUserId();

        mSearchPostIdButton = binding.modifyPostSearchButton;
        mSearchPostIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValueFromEditText();
                mPost = mWriteItDAO.getPostByPostId(postId);
                if (mPost != null) {
                    if (checkPostBelongsToUser()) {
                        postContent = mPost.getPostContent();
                        mPostPlayAreaEditText.setText(postContent);
                    } else {
                        Toast.makeText(ModifyPost.this, "Post does not belong to this user!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("mPost WAS NULL");
                }
            }
        });
        mModifyPostButton = binding.ConfirmModifyButton;
        mModifyPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPost = setNewPostContent();
                mWriteItDAO.update(mPost);
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
        mBackButton = binding.ModifyPostBackButton;
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
    }

    private Post setNewPostContent() {
        this.postContent = mPostPlayAreaEditText.getText().toString();
        for(Post p: mWriteItDAO.getAllPosts()){
            if(p.getPostId() == postId){
                p.setPostContent(postContent);
                return p;
            }
        }
        System.out.println("SOMETHING WENT WRONG IN SETNEWPOSTCONTENT");;
        return null;
    }

    private boolean checkPostBelongsToUser() {
        mCurrentUserPosts = mWriteItDAO.getAllPostsFromUsername(currentUsername);
        for(Post p: mCurrentUserPosts){
            if(p.getPostId() == postId){
                return true;
            }
        }
        return false;
    }

    private void getValueFromEditText() {
        postId = Integer.parseInt(String.valueOf(mPostIdEditText.getText()));
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }

    public static Intent intentFactory(Context context, String username){
        Intent intent = new Intent(context, ModifyPost.class);
        intent.putExtra(USERNAME_KEY,username);
        return intent;
    }
}