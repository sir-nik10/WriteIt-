package com.example.cst338_sp22_project2;

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
import android.widget.Toast;

import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String USER_ID_KEY ="com.example.cst338_sp22_project2.userIdKey" ;

    private static EditText mUsernameField;
    private static EditText mPasswordField;

    private String mUsername;
    private String mPassword;

    private Button loginButton;

    private writeItDAO mWriteItDAO;

    ActivityLoginBinding binding;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDatabase();

        mUsernameField =  findViewById(R.id.EditTextUsername);
        mPasswordField = findViewById(R.id.EditTextPassword);
        loginButton = binding.ButtonLogin;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }else{
                        if(checkUserBanned()){
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertBuilder.setMessage("Your account has been banned \n Please contact support.");

                            alertBuilder.setPositiveButton("I understand",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            //nothing
                                        }
                                    });
                            alertBuilder.create().show();
                        }else{
                            Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private boolean checkUserBanned() {
        return mUser.isBanned();
    }

    private boolean validatePassword() {
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private Boolean checkForUserInDatabase(){
        mUser = mWriteItDAO.getUserByUsername(mUsername);
        //System.out.println(mUser);
        if(mUser == null){
            Toast.makeText(this, "No user\n" + mUsername + "\n found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
            .allowMainThreadQueries()
            .build().mWriteItDAO();
}
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}