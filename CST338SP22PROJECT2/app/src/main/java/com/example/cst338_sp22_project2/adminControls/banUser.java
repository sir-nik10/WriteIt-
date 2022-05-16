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
import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.MainActivity;
import com.example.cst338_sp22_project2.Posts.DeleteActivity;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityAdminControlsBinding;
import com.example.cst338_sp22_project2.databinding.ActivityBanUserBinding;

import java.util.List;

public class banUser extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.cst338_sp22_project2.userIdKey";

    private writeItDAO mWriteItDAO;

    private ActivityBanUserBinding binding;

    private Button mBackButton;
    private Button mSearchButton;
    private Button mConfirmButton;

    private TextView mUsernameTextView;

    private EditText mUsernameSearchEditText;

    private String mSearchUsername;

    private User mUser;
    private List<User> mUserList;
    private List<Post> mPostList;

    private int adminUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_user);

        getDatabase();

        binding = ActivityBanUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUsernameSearchEditText = binding.banUserEditText;

        mUsernameTextView = binding.banUserTextView;

        mSearchButton = binding.banUserSearchButton;
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValueFromEditText();
                if(checkForUser()){
                    mUsernameTextView.setText(mSearchUsername);
                }
            }
        });
        mConfirmButton = binding.ConfirmBanUser;
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(banUser.this);
                alertBuilder.setMessage("Are you sure you want to ban user?");
                alertBuilder.setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(checkIfEmpty()){
                            if(checkIfBanned()){
                                Toast.makeText(banUser.this, "User already Banned!", Toast.LENGTH_SHORT).show();
                            }else{
                                mUser.setIsBanned(true);
                                mWriteItDAO.update(mUser);
                                setUserPostsToBanned();
                                adminUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
                                Intent intent = AdminControls.intentFactory(getApplicationContext(),adminUserId);
                                startActivity(intent);
                            }
                        }
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
        mBackButton = binding.banUserBackButton;
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminControls.intentFactory(getApplicationContext(),adminUserId);
                startActivity(intent);
            }
        });
    }

    private void setUserPostsToBanned() {
        mPostList = mWriteItDAO.getAllPostsFromUsername(mUser.getUserName());
        for(Post p: mPostList){
            p.setPostContent("[User has been banned]");
            mWriteItDAO.update(p);
        }
    }

    private boolean checkIfBanned() {
        return mUser.isBanned();
    }

    private boolean checkIfEmpty() {
        if(mSearchUsername.equals("")){
            Toast.makeText(this, "User field cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private boolean checkForUser() {
        mUserList = mWriteItDAO.getAllUsers();
        for(User u: mUserList){
            if(u.getUserName().equals(mSearchUsername)){
                mUser = u;
                return true;
            }
        }
        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void getValueFromEditText() {
        mSearchUsername = mUsernameSearchEditText.getText().toString();
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, banUser.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }

}