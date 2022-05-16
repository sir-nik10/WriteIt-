package com.example.cst338_sp22_project2.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cst338_sp22_project2.DAO.Post;
import com.example.cst338_sp22_project2.DAO.User;
import com.example.cst338_sp22_project2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Post> mPosts;

    public RecyclerAdapter(List<Post> mPosts){
        this.mPosts = mPosts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTextView;
        private TextView postTextView;
        private TextView mPostId;

        public MyViewHolder(final View view){
            super(view);
            usernameTextView = view.findViewById(R.id.ItemCellUsername);
            postTextView = view.findViewById(R.id.ItemCellPost);
            mPostId = view.findViewById(R.id.textViewPostId);

        }

    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name = mPosts.get(position).getUsername();
        holder.usernameTextView.setText(name);
        int postId = mPosts.get(position).getPostId();
        holder.mPostId.setText("PostId: "+postId);
        String content = mPosts.get(position).getPostContent();
        holder.postTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
