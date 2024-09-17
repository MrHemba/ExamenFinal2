package com.example.examenfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView userImage;
    private ImageView flagImage;  // Imagen para la bandera
    private TextView userDetails, userEmail, userAddress, userAge, userPhone, userCell, userNationality;
    private MapView mapView;
    private GoogleMap gMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RequestQueue requestQueue;
    private double latitude;
    private double longitude;
    private String city;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Inicializar las vistas
        userImage = findViewById(R.id.userImage);
        flagImage = findViewById(R.id.flagImage);  // Para la bandera
        userDetails = findViewById(R.id.userDetails);
        userEmail = findViewById(R.id.userEmail);
        userAddress = findViewById(R.id.userAddress);
        userAge = findViewById(R.id.userAge);
        userPhone = findViewById(R.id.userPhone);
        userCell = findViewById(R.id.userCell);
        userNationality = findViewById(R.id.userNationality);
        mapView = findViewById(R.id.mapView);

        requestQueue = Volley.newRequestQueue(this);

        // Obtener datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String imageUrl = intent.getStringExtra("imageUrl");
            String address = intent.getStringExtra("address");
            int age = intent.getIntExtra("age", 0);
            String phone = intent.getStringExtra("phone");
            String cell = intent.getStringExtra("cell");
            String nationality = intent.getStringExtra("nationality");
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
            city = intent.getStringExtra("city");
            country = intent.getStringExtra("country");

            // Cargar la imagen del usuario con Glide
            Glide.with(this).load(imageUrl).into(userImage);

            // Mostrar los detalles del usuario
            userDetails.setText("Name: " + name);
            userEmail.setText("Email: " + email);
            userAddress.setText("Address: " + address);
            userAge.setText("Age: " + age);
            userPhone.setText("Phone: " + phone);
            userCell.setText("Cell: " + cell);
            userNationality.setText("Nationality: " + nationality);

            // Llamar a la función para cargar la bandera del país
            fetchCountryFlag(country);
        }

        // Configurar el MapView
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    // Método para obtener la bandera del país usando la API de restcountries.com
    private void fetchCountryFlag(String countryName) {

        // Construir la URL sin codificación
        String url = "https://restcountries.com/v3.1/name/" + country;

        // Imprimir la URL en el Logcat para depuración
        Log.d("fetchCountryFlag", "URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Obtener el primer objeto del array de países
                    JSONObject countryObject = response.getJSONObject(0);

                    // Acceder al campo "flags" para obtener la URL de la bandera
                    JSONObject flagsObject = countryObject.getJSONObject("flags");
                    String flagUrl = flagsObject.getString("png");

                    // Imprimir la URL de la bandera para verificarla
                    Log.d("fetchCountryFlag", "Flag URL: " + flagUrl);

                    // Cargar la imagen usando Glide
                    Glide.with(UserDetailActivity.this)
                            .load(flagUrl)
                            .into(flagImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UserDetailActivity", "Error fetching flag: " + error.getMessage());
                Toast.makeText(UserDetailActivity.this, "Error al obtener la bandera", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng userLocation = new LatLng(latitude, longitude);

        // Agregar un marcador y centrar el mapa en la ubicación del usuario
        Marker marker = gMap.addMarker(new MarkerOptions().position(userLocation).title(city));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

        // Mostrar el nombre de la ciudad en un Toast cuando se haga clic en el marcador
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(UserDetailActivity.this, "City: " + city, Toast.LENGTH_SHORT).show();
                return false; // Devolver false para que el comportamiento predeterminado del marcador ocurra también
            }
        });
    }

    // Manejar el ciclo de vida del MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
