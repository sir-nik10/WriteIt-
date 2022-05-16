package com.example.cst338_sp22_project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.databinding.ActivityLandingPageBinding;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.cst338_sp22_project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.example.cst338_sp22_project2.pref_Key";

    private TextView mTitle;

    private Button mCreateAccButton;
    private Button mLoginButton;

    private writeItDAO mWriteItDAO;

    private int mUserId = -1;

    private User mUser;

    private SharedPreferences mPreferences;


    private ActivityLandingPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCreateAccButton = findViewById(R.id.LandingPageCreateAccountButton);
        mCreateAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CreateAccount.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        mLoginButton = binding.loginButton;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void addUserToPreference(int userId){
        if(mPreferences == null){
            getPrefs();
        }

    }
    private void checkForUser() {
        //do we have user in intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        //do we have a user in the preferences?"
        if (mUserId != -1) {
            return;
        }
        if (mPreferences == null) {
            getPrefs();
        }
        if (mUserId != -1) {
            return;
        }
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY,-1);
    }
    private void clearUserFromPref(){
        addUserToPreference(-1);
    }
    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE);
    }


    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context,LandingPage.class);
        return intent;
    }

}