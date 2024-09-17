package com.example.examenfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de usuarios y el adapter
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this::onUserClick);
        recyclerView.setAdapter(userAdapter);

        // Inicializar la cola de peticiones de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Llamar a la función para obtener los usuarios
        fetchUsers();
    }

    // Método para obtener los usuarios de la API
    private void fetchUsers() {
        String url = "https://randomuser.me/api/?results=20";

        JsonObjectRequest request = new JsonObjectRequest(url, null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject userObject = results.getJSONObject(i);

                    // Obtener los datos del usuario
                    JSONObject nameObject = userObject.getJSONObject("name");
                    String fullName = nameObject.getString("first") + " " + nameObject.getString("last");

                    JSONObject locationObject = userObject.getJSONObject("location");
                    String country = locationObject.getString("country");
                    String city = locationObject.getString("city");
                    JSONObject streetObject = locationObject.getJSONObject("street");
                    String address = streetObject.getString("name") + ", " + city + ", " + country;

                    JSONObject coordinatesObject = locationObject.getJSONObject("coordinates");
                    double latitude = coordinatesObject.getDouble("latitude");
                    double longitude = coordinatesObject.getDouble("longitude");

                    String email = userObject.getString("email");
                    String imageUrl = userObject.getJSONObject("picture").getString("large");

                    // Obtener edad, teléfono, celular, y nacionalidad
                    int age = userObject.getJSONObject("dob").getInt("age");
                    String phone = userObject.getString("phone");
                    String cell = userObject.getString("cell");
                    String nationality = userObject.getString("nat");

                    // Agregar el usuario a la lista con todos los campos
                    userList.add(new User(fullName, country, city, email, imageUrl, address, latitude, longitude, age, phone, cell, nationality));
                }

                // Notificar al adaptador de que los datos han cambiado
                userAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            // Manejar el error aquí
        });

        requestQueue.add(request);
    }


    // Manejar el clic en un usuario
    private void onUserClick(User user) {
        Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
        intent.putExtra("name", user.getName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("address", user.getAddress());
        intent.putExtra("country", user.getCountry());
        intent.putExtra("city", user.getCity());
        intent.putExtra("latitude", user.getLatitude());
        intent.putExtra("longitude", user.getLongitude());
        intent.putExtra("imageUrl", user.getImageUrl());
        intent.putExtra("age", user.getAge());
        intent.putExtra("phone", user.getPhone());
        intent.putExtra("cell", user.getCell());
        intent.putExtra("nationality", user.getNationality());
        startActivity(intent);
    }

}
