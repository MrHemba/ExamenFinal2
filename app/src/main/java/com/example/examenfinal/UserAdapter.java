package com.example.examenfinal;

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
    private OnUserClickListener listener;

    // Constructor para pasar la lista y el listener de clic
    public UserAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout user_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Obtener el usuario de la lista
        User user = userList.get(position);

        // Configurar los datos del usuario
        holder.userName.setText(user.getName());
        holder.userCountry.setText(user.getCountry());
        holder.userEmail.setText(user.getEmail());

        // Cargar la imagen del usuario usando Glide
        Glide.with(holder.itemView.getContext())
                .load(user.getImageUrl())
                .into(holder.userImage);

        // Manejar el clic en el elemento
        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder que enlaza los elementos de user_item.xml
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userCountry, userEmail;
        ImageView userImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userCountry = itemView.findViewById(R.id.userCountry);
            userEmail = itemView.findViewById(R.id.userEmail);
            userImage = itemView.findViewById(R.id.userImage);
        }
    }

    // Interfaz para manejar los clics
    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
