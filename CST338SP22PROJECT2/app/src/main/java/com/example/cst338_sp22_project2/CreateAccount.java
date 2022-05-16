package com.example.cst338_sp22_project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.databinding.ActivityCreateAccountBinding;

import java.util.List;

public class CreateAccount extends AppCompatActivity {

    private ActivityCreateAccountBinding binding;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private String mUsername;
    private String mPassword;

    private Button mCreateAccountButton;

    private writeItDAO mWriteItDAO;

    private List<User> mUsers;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mUsernameEditText = binding.createAccountUsernameEditText;
        mPasswordEditText = binding.createAccountPasswordEditText;

        mCreateAccountButton = binding.createAccountButton;

        getDatabase();
        getUsersFromDatabase();

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForUserExists()){
                    insertUserInDatabase();
                    mUser = mWriteItDAO.getUserByUsername(mUsername);
                    Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                    startActivity(intent);
                }
            }
        });

    }

    private void insertUserInDatabase() {
        mPassword = mPasswordEditText.getText().toString();
        User user = new User(mUsername, mPassword, false);
        mWriteItDAO.insert(user);
    }

    private void getUsersFromDatabase() {
        mUsers = mWriteItDAO.getAllUsers();
    }

    private Boolean checkForUserExists() {
        mUsername = mUsernameEditText.getText().toString();
        if(mUsername.equals("")){
            Toast.makeText(this, "Username cannot be blank" , Toast.LENGTH_LONG).show();
            return false;
        }
        for (User u:mUsers){
            if(u.getUserName().equals(mUsername)){
                Toast.makeText(this, "Username " + mUsername+"\nAlready Exists!" , Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, CreateAccount.class);
        return intent;
    }

}