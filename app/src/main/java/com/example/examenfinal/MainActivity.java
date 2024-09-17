package com.example.examenfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    private void fetchUsers() {
        String url = "https://randomuser.me/api/?results=20";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject userObject = results.getJSONObject(i);
                        JSONObject nameObject = userObject.getJSONObject("name");
                        String fullName = nameObject.getString("first") + " " + nameObject.getString("last");

                        // Ubicación: país, ciudad y dirección
                        JSONObject locationObject = userObject.getJSONObject("location");
                        String country = locationObject.getString("country");
                        String city = locationObject.getString("city");
                        JSONObject streetObject = locationObject.getJSONObject("street");
                        String address = streetObject.getString("number") + " " + streetObject.getString("name") + ", " + city + ", " + country;

                        // Coordenadas
                        JSONObject coordinatesObject = locationObject.getJSONObject("coordinates");
                        double latitude = Double.parseDouble(coordinatesObject.getString("latitude"));
                        double longitude = Double.parseDouble(coordinatesObject.getString("longitude"));

                        // Email
                        String email = userObject.getString("email");

                        // Imagen
                        String imageUrl = userObject.getJSONObject("picture").getString("large");

                        // Edad
                        int age = userObject.getJSONObject("dob").getInt("age");

                        // Teléfonos
                        String phone = userObject.getString("phone");
                        String cell = userObject.getString("cell");

                        // Nacionalidad
                        String nationality = userObject.getString("nat");

                        // Agregar usuario a la lista
                        userList.add(new User(fullName, country, city, email, imageUrl, address, age, phone, cell, nationality, latitude, longitude));
                    }

                    userAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "JSON Parsing error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", "Error: " + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    // Manejar el clic en un usuario
    private void onUserClick(User user) {
        Intent intent = new Intent(this, UserDetailActivity.class);
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
