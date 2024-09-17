package com.example.examenfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView userName, userEmail, userAddress, userAge, userPhone, userCell, userNationality;
    private ImageView userImage, flagImage;
    private MapView mapView;
    private GoogleMap googleMap;
    private double latitude, longitude;
    private String country, city;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Inicializar las vistas
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userAddress = findViewById(R.id.userAddress);
        userAge = findViewById(R.id.userAge);
        userPhone = findViewById(R.id.userPhone);
        userCell = findViewById(R.id.userCell);
        userNationality = findViewById(R.id.userNationality);
        userImage = findViewById(R.id.userImage);
        flagImage = findViewById(R.id.flagImage);
        mapView = findViewById(R.id.mapView);

        // Inicializar la cola de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Obtener datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String address = intent.getStringExtra("address");
            String imageUrl = intent.getStringExtra("imageUrl");
            country = intent.getStringExtra("country");
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
            city = intent.getStringExtra("city");
            int age = intent.getIntExtra("age", 0);
            String phone = intent.getStringExtra("phone");
            String cell = intent.getStringExtra("cell");
            String nationality = intent.getStringExtra("nationality");

            // Mostrar datos del usuario
            userName.setText(name);
            userEmail.setText(email);
            userAddress.setText(address);
            userAge.setText("Age: " + age);
            userPhone.setText("Phone: " + phone);
            userCell.setText("Cell: " + cell);

            // Cargar la imagen del usuario
            Glide.with(this).load(imageUrl).into(userImage);

            // Cargar la bandera y mostrar la nacionalidad
            fetchCountryFlagAndNationality(country, nationality);
        }

        // Configurar el MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    // Método para obtener la bandera del país y mostrar la nacionalidad con el nombre y la abreviatura
    private void fetchCountryFlagAndNationality(String country, String nationality) {
        String url = "https://restcountries.com/v3.1/name/" + country;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> {
            try {
                JSONObject countryObject = response.getJSONObject(0);
                String flagUrl = countryObject.getJSONObject("flags").getString("png");
                String commonName = countryObject.getJSONObject("name").getString("common");
                String countryCode = countryObject.getString("cca2");

                // Mostrar la nacionalidad (ejemplo: France (FR))
                userNationality.setText("Nationality: " + commonName + " (" + countryCode + ")");

                // Cargar la bandera usando Glide
                Glide.with(UserDetailActivity.this).load(flagUrl).into(flagImage);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UserDetailActivity.this, "Error al procesar la bandera", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(UserDetailActivity.this, "Error al obtener la bandera", Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng userLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(userLocation).title(city));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

        // Al hacer clic en el marcador, mostrar el nombre de la ciudad
        googleMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(this, "City: " + city, Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
