package com.example.examenfinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userCountry.setText(user.getCountry());
        holder.userEmail.setText(user.getEmail());

        // Cargar imagen con Glide
        Glide.with(context).load(user.getImageUrl()).into(holder.userImage);

        // Manejar clics en cada usuario
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("imageUrl", user.getImageUrl());
            intent.putExtra("address", user.getAddress());
            intent.putExtra("age", user.getAge());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("cell", user.getCell());
            intent.putExtra("nationality", user.getNationality());
            intent.putExtra("latitude", user.getLatitude());
            intent.putExtra("longitude", user.getLongitude());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName, userCountry, userEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            userCountry = itemView.findViewById(R.id.userCountry);
            userEmail = itemView.findViewById(R.id.userEmail);
        }
    }
}
