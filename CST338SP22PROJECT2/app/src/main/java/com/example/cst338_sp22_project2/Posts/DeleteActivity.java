package com.example.cst338_sp22_project2.Posts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.cst338_sp22_project2.LandingPage;
import com.example.cst338_sp22_project2.MainActivity;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityDeleteBinding;

import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "com.example.cst338_sp22_project2.usernameKey";

    private writeItDAO mWriteItDAO;

    private TextView postToBeDeleted;

    private EditText postIdEditText;

    private Button mDeleteButton;
    private Button mSearchButton;

    private List<Post> mPostsList;
    private Post mPost;
    private User mUser;

    private ActivityDeleteBinding binding;

    private int currentPostId;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        binding = ActivityDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDatabase();

        postToBeDeleted = binding.DeletePostTextView;
        postIdEditText = binding.getPostIdEditTextDelete;

        mSearchButton = binding.DeletePostSearchButton;
        mDeleteButton = binding.ConfirmDeleteButton;

        mUsername = getIntent().getStringExtra(USERNAME_KEY);

        mUser = mWriteItDAO.getUserByUsername(mUsername);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromEditText();
                if(checkPostsBelongToUser()){
                   setPostTextView();
                }
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DeleteActivity.this);
                alertBuilder.setMessage("Are you sure you want to delete post?");
                alertBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mWriteItDAO.delete(mPost);
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                    }
                });
                alertBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing
                            }
                        });
                alertBuilder.create().show();
            }
        });
    }

    private void setPostTextView() {
        postToBeDeleted.setText(mPost.getPostContent());
    }

    private boolean checkPostsBelongToUser() {
        mPostsList = mWriteItDAO.getAllPostsFromUsername(mUsername);
        for(Post p: mPostsList){
            if(p.getPostId() == currentPostId){
                mPost = p;
                return true;
            }
        }
        Toast.makeText(this, "Could not find post!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void getValuesFromEditText() {
        currentPostId = Integer.parseInt(postIdEditText.getText().toString());
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }

    public static Intent intentFactory(Context context, String username){
        Intent intent = new Intent(context, DeleteActivity.class);
        intent.putExtra(USERNAME_KEY,username);
        return intent;
    }
}