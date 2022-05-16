package com.example.cst338_sp22_project2.adminControls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.MainActivity;
import com.example.cst338_sp22_project2.R;
import com.example.cst338_sp22_project2.databinding.ActivityAdminControlsBinding;
import com.example.cst338_sp22_project2.databinding.ActivityMainBinding;

public class AdminControls extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.cst338_sp22_project2.userIdKey";

    private writeItDAO mWriteItDAO;

    private Button mBackButton;
    private Button mBanUser;
    private Button mBanUserPost;

    private TextView mAdminControlsTitle;

    private int mUserId;

    private User mUser;

    private ActivityAdminControlsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_controls);

        getDatabase();

        binding = ActivityAdminControlsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mBackButton = binding.AdminControlBackButton;
        mBanUser = binding.BanUserButton;
        mBanUserPost = binding.RemoveUserPost;

        mAdminControlsTitle = binding.AdminControlTitle;

        getUser();
        setAdminControlTitle();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
        mBanUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = banUser.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
        mBanUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RemoveUserPost.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
    }

    private void setAdminControlTitle() {
        mAdminControlsTitle.setText("Welcome " + mUser.getUserName()+"\nAdminControls!");
    }

    private void getUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        mUser = mWriteItDAO.getUserByUserId(mUserId);
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AdminControls.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }

}