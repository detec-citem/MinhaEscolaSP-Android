package br.gov.sp.educacao.minhaescola.view;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.task.GoogleMapsAsynsTask;

public class MapaActivity
        extends AppCompatActivity
        implements OnMapReadyCallback {

    public static final String ENDERECO_KEY = "br.gov.sp.educacao.balcaodenegocios.activity.MapaActivity.ENDERECO";

    private static final String LATITUDE_KEY = "br.gov.sp.educacao.balcaodenegocios.activity.MapaActivity.LATITUDE";
    private static final String LONGITUDE_KEY = "br.gov.sp.educacao.balcaodenegocios.activity.MapaActivity.LONGITUDE";

    private boolean retornouEndereco;

    private double latitude;
    private double longitude;

    private String endereco;

    private GoogleMap googleMap;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Confirme a localização");

        progressBar = findViewById(R.id.progressBar);

        if (savedInstanceState != null) {

            retornouEndereco = true;

            progressBar.setVisibility(View.GONE);

            latitude = savedInstanceState.getDouble(LATITUDE_KEY);
            longitude = savedInstanceState.getDouble(LONGITUDE_KEY);
            endereco = savedInstanceState.getString(ENDERECO_KEY);
        }
        else {

            endereco = getIntent().getStringExtra(ENDERECO_KEY);

            new GoogleMapsAsynsTask(this).execute(endereco);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_mapa);

        mapFragment.getMapAsync(this);
    }

    public void voltar(View view) {

        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putDouble(LATITUDE_KEY, latitude);
        outState.putDouble(LONGITUDE_KEY, longitude);
        outState.putString(ENDERECO_KEY, endereco);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        if (retornouEndereco) {

            configurarGoogleMaps();
        }
    }

    public void repostaEndereco(Address address) {

        retornouEndereco = true;

        progressBar.setVisibility(View.GONE);

        if (address != null) {

            latitude = address.getLatitude();

            longitude = address.getLongitude();

            if (googleMap != null) {

                configurarGoogleMaps();
            }
        }
    }

    private void configurarGoogleMaps() {

        LatLng coordinate = new LatLng(latitude, longitude);

        googleMap.addMarker(

                new MarkerOptions()
                        .position(coordinate)
                        .title(endereco).draggable(true)
                        .title("Seu Endereço")
        );

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
            }
        });

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17.5f));
    }

    public void salvarCordenadas(View v){

        Intent intent = new Intent();

        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        setResult(RESULT_OK, intent);

        finish();
    }
}
