package com.example.cst338_sp22_project2.adminControls;

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
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityBanUserBinding;
import com.example.cst338_sp22_project2.databinding.ActivityRemoveUserPostBinding;

import java.util.List;

public class RemoveUserPost extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.cst338_sp22_project2.userIdKey";

    private writeItDAO mWriteItDAO;

    private ActivityRemoveUserPostBinding binding;

    private Button mBackButton;
    private Button mSearchButton;
    private Button mConfirmButton;

    private EditText mUsername;
    private EditText mPostId;

    private TextView mPlayArea;

    private Post mCurrentPost;
    private List<Post> mPostsList;

    private String mCurrentUsername;
    private int mCurrentPostId = -1;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user_post);

        getDatabase();

        binding = ActivityRemoveUserPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBackButton = binding.removeUserPostBackButton;
        mSearchButton = binding.SearchUserButton;
        mConfirmButton = binding.RemoveUserPostConfirmButton;

        mUsername = binding.RemoveUserPostFindUser;
        mPostId = binding.RemoveUserPostFindPost;

        mPlayArea = binding.RemoveUserPostPlayArea;



        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromEditText();
                if(mCurrentUsername.equals("") || mCurrentPostId == -1){
                    Toast.makeText(RemoveUserPost.this, "Username or PostId is blank", Toast.LENGTH_SHORT).show();
                }else{
                   findUserAndPost();
                    }
                }
        });
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentUsername.equals("") || mCurrentPostId == -1){
                    Toast.makeText(RemoveUserPost.this, "Username or PostId is blank", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RemoveUserPost.this);
                    alertBuilder.setMessage("Are you sure you want to ban users post?");
                    alertBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            mCurrentPost.setPostContent("[Post removed for inappropriate content]");
                            mWriteItDAO.update(mCurrentPost);
                            userId =getIntent().getIntExtra(USER_ID_KEY,-1);
                            Intent intent = AdminControls.intentFactory(getApplicationContext(),userId);
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
            }
        });
    }

    private void findUserAndPost() {
       mCurrentPost=  mWriteItDAO.getPostByPostId(mCurrentPostId);
       mPlayArea.setText(mCurrentPost.getPostContent());
    }

    private void getValuesFromEditText() {
        mCurrentUsername = mUsername.getText().toString();
        if(!mPostId.getText().toString().equals("")){
            mCurrentPostId = Integer.parseInt(mPostId.getText().toString());
        }
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, RemoveUserPost.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }
}