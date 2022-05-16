package com.example.cst338_sp22_project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.DB.AppDataBase;
import com.example.cst338_sp22_project2.DB.writeItDAO;
import com.example.cst338_sp22_project2.Posts.DeleteActivity;
import com.example.cst338_sp22_project2.Posts.MakePostActivity;
import com.example.cst338_sp22_project2.Posts.ModifyPost;
import com.example.cst338_sp22_project2.Recycler.RecyclerAdapter;
import com.example.cst338_sp22_project2.adminControls.AdminControls;
import com.example.cst338_sp22_project2.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.cst338_sp22_project2.userIdKey";
    private static final String USERNAME_KEY = "com.example.cst338_sp22_project2.usernameKey";
    private static final String PREFERENCE_KEY = "com.example.cst338_sp22_project2.PREFERENCE_KEY";

    private TextView mTitleTextView;
    private TextView mUsernameTextView;
    private TextView mIsAdminTextView;

    private Button mPostButton;
    private Button mModifyButton;
    private Button mDeleteButton;
    private Button mAdminButton;

    private writeItDAO mWriteItDAO;
    private ActivityMainBinding binding;

    private int mUserId=-1;

    private SharedPreferences mPreferences;

    private User mUser;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getDatabase();
        checkForUser();
        setPreferences();
        invalidateOptionsMenu();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRecyclerView = binding.mainFeed;

        setAdapter();

        mUser = mWriteItDAO.getUserByUserId(mUserId);


        mPostButton = binding.makeAPost;
        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MakePostActivity.intentFactory(getApplicationContext(),mUser.getUserName());
                startActivity(intent);
            }
        });
        mModifyButton = binding.editPost;
        mModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ModifyPost.intentFactory(getApplicationContext(),mUser.getUserName());
                startActivity(intent);
            }
        });
        mDeleteButton =binding.deletePost;
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DeleteActivity.intentFactory(getApplicationContext(),mUser.getUserName());
                startActivity(intent);
            }
        });
        mAdminButton = binding.AdminControls;
        checkForAdmin();
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminControls.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });

    }

    private void checkForAdmin() {
        if(mUser != null){
            if(mUser.isAdmin()){
                mAdminButton.setVisibility(View.VISIBLE);
            }else{
                System.out.println("ADMIN WAS FALSE");
            }
        }else{
            System.out.println("mUSER ADMIN CHECK WAS NULL");
        }
    }

    private void setAdapter() {
        List<Post> posts = mWriteItDAO.getAllPosts();
        RecyclerAdapter adapter = new RecyclerAdapter(posts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.logoutMenu);
            System.out.println("USERNAME == " +mUser.getUserName());
            item.setTitle(mUser.getUserName());
        }else{
            System.out.println("mUSER WAS NULL");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        logoutUser();
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        Intent intent = LandingPage.intentFactory(getApplicationContext());
                        startActivity(intent);                    }
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

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY,-1);
    }
    private void clearUserFromPref(){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREFERENCE_KEY,-1);
        editor.apply();
    }

    private void setPreferences() {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PREFERENCE_KEY,mUserId);
        editor.apply();
    }

    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE);
    }

    private void checkForUser() {
        //do we have user in intent?
        mUserId= getIntent().getIntExtra(USER_ID_KEY,-1);
        System.out.println("CHECK USER INTENT == " +mUserId);
      //do we have a user in the preferences?"

        if(mUserId != -1) {
            return;
        }
        if(mPreferences == null){
            getPrefs();
        }
            mUserId = mPreferences.getInt(PREFERENCE_KEY, -1);
            System.out.println("CHECK USER PREFERENCE== " +mUserId);
        if(mUserId !=  -1){
            return;
        }
        //do we have users at all?
        List<User> users = mWriteItDAO.getAllUsers();
        System.out.println("users.size() == "+users.size());
        if(users.size() <= 0 ){
            //DEFAULT PREDEFINED USERS
            User defaultUser = new User("testuser","test123",false);
            User altUser = new User("adminuser","admin123",true);
            mWriteItDAO.insert(defaultUser, altUser);
        }
        Intent intent = LandingPage.intentFactory(this);
        startActivity(intent);
    }

    private void getDatabase(){
        mWriteItDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().mWriteItDAO();
    }


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }

}