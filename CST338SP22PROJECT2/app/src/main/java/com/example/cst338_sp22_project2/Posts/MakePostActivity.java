package com.example.cst338_sp22_project2.Posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.MainActivity;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityMakePostBinding;

public class MakePostActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "com.example.cst338_sp22_project2.usernameKey";

    private writeItDAO mWriteItDAO;

    private TextView mPostingUser;

    private EditText mMainPost;

    private Button mPostButton;

    private String postContent;
    private String mUsername;

    private ActivityMakePostBinding binding;

    private User mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        binding = ActivityMakePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDatabase();

        mMainPost = binding.mainPost;
        mPostingUser = binding.postingUsername;
        mPostButton = binding.postingButton;

        mUsername = getIntent().getStringExtra(USERNAME_KEY);
        getUserFromDatabase();

        setValuesFromView();

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromView();
                if(postContent.equals("")){
                    Toast.makeText(MakePostActivity.this, "Cannot Post Nothing!", Toast.LENGTH_LONG).show();
                }else{
                    Post post = new Post(mUsername,postContent);
                    mWriteItDAO.insert(post);
                    Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                    startActivity(intent);
                }
            }
        });
    }

    private void setValuesFromView() {
        mPostingUser.setText("Posting as: "+mUsername);

    }
    private void getValuesFromView() {
        postContent = mMainPost.getText().toString();
    }

    private void getUserFromDatabase(){
        mUser = mWriteItDAO.getUserByUsername(mUsername);
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }
    public static Intent intentFactory(Context context, String username){
        Intent intent = new Intent(context,MakePostActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        return intent;
    }
}