package com.luvtas.australia_covid_19_hot_spots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.ui.IconGenerator;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView map_title, map_info, map_start_date, map_end_date, map_location, map_address,update,vic_local,vic_overseas_case,tas_local,tas_overseas_case,nsw_local,nsw_overseas_case;
    private TextView qld_local,qld_overseas_case,act_local,act_overseas_case,sa_local,sa_overseas_case,wa_local,wa_overseas_case,nt_local,nt_overseas_case,total_vaccine,start_date;
    private String MapLat,MapLng,MapTitle,MapInfo,MapPhoto,MapStartTime,MapEndTime,MapSmallPhoto,MapAddress,MapUserEmail,MAP_ID;
    private String txt_vic_local,txt_vic_overseas_case,txt_tas_local,txt_tas_overseas_case,txt_nsw_local,txt_nsw_overseas_case,txt_qld_local,txt_qld_overseas_case;
    private String txt_act_local,txt_act_overseas_case,txt_sa_local,txt_sa_overseas_case,txt_wa_local,txt_wa_overseas_case,txt_nt_local,txt_nt_overseas_case,txt_total_vaccine,txt_start_date;
    private String txt_vic_rule,txt_tas_rule,txt_nsw_rule,txt_qld_rule,txt_act_rule,txt_sa_rule,txt_wa_rule,txt_nt_rule;
    private LinearLayout travel_rules;
    int MapType;
    public String rule;
    private Marker marker;
    private ImageView map_img,vic_rule,tas_rule,nsw_rule,qld_rule,act_rule,sa_rule,wa_rule,nt_rule;
    private static final String URL_MAPSHOW = "https://";
    private ArrayList<MapShow> mapShowArrayList = new ArrayList<>();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 8001;
    private Boolean mLocationPermissionsGranted = false;
    private static final float DEFAULT_ZOOM = 16f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private AdView mAdView;
    private int requestCount = 1;
    PlacesClient placesClient;
    private RequestQueue requestQueue;
    private Button tips;
    public SupportMapFragment mapFragment;
    private AnimationDrawable anim;

    public MainActivity() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(anim != null && !anim.isRunning()){
            anim.stop();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(anim != null && !anim.isRunning()){
            anim.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tips = findViewById(R.id.tips);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelRuleModel travelRuleModel = new TravelRuleModel();
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tips, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                update = bottomSheetView.findViewById(R.id.update);
                travel_rules = bottomSheetView.findViewById(R.id.travel_rules);
                vic_local = bottomSheetView.findViewById(R.id.vic_local);
                vic_overseas_case = bottomSheetView.findViewById(R.id.vic_overseas_case);
                tas_local = bottomSheetView.findViewById(R.id.tas_local);
                tas_overseas_case = bottomSheetView.findViewById(R.id.tas_overseas_case);
                nsw_local = bottomSheetView.findViewById(R.id.nsw_local);
                nsw_overseas_case = bottomSheetView.findViewById(R.id.nsw_overseas_case);
                qld_local = bottomSheetView.findViewById(R.id.qld_local);
                qld_overseas_case = bottomSheetView.findViewById(R.id.qld_overseas_case);
                act_local = bottomSheetView.findViewById(R.id.act_local);
                act_overseas_case = bottomSheetView.findViewById(R.id.act_overseas_case);
                sa_local = bottomSheetView.findViewById(R.id.sa_local);
                sa_overseas_case = bottomSheetView.findViewById(R.id.sa_overseas_case);
                wa_local = bottomSheetView.findViewById(R.id.wa_local);
                wa_overseas_case = bottomSheetView.findViewById(R.id.wa_overseas_case);
                nt_local = bottomSheetView.findViewById(R.id.nt_local);
                nt_overseas_case = bottomSheetView.findViewById(R.id.nt_overseas_case);
                total_vaccine = bottomSheetView.findViewById(R.id.total_vaccine);
                start_date = bottomSheetView.findViewById(R.id.update);

                vic_local.setText("Local case: " +txt_vic_local);
                vic_overseas_case.setText("Overseas case: " +txt_vic_overseas_case);
                tas_local.setText("Local case: " +txt_tas_local);
                tas_overseas_case.setText("Overseas case: " +txt_tas_overseas_case);
                nsw_local.setText("Local case: " +txt_nsw_local);
                nsw_overseas_case.setText("Overseas case: " +txt_nsw_overseas_case);
                qld_local.setText("Local case: " +txt_qld_local);
                qld_overseas_case.setText("Overseas case: " +txt_qld_overseas_case);
                act_local.setText("Local case: " +txt_act_local);
                act_overseas_case.setText("Overseas case: " +txt_act_overseas_case);
                sa_local.setText("Local case: " +txt_sa_local);
                sa_overseas_case.setText("Overseas case: " +txt_sa_overseas_case);
                wa_local.setText("Local case: " +txt_wa_local);
                wa_overseas_case.setText("Overseas case: " +txt_wa_overseas_case);
                nt_local.setText("Local case: " +txt_nt_local);
                nt_overseas_case.setText("Overseas case: " +txt_nt_overseas_case);
                total_vaccine.setText(txt_total_vaccine);
                start_date.setText("Last update: \n" + txt_start_date);

                travel_rules.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, TravelRulesActivity.class);
                        intent.putExtra("vic", txt_vic_rule);
                        intent.putExtra("tas", txt_tas_rule);
                        intent.putExtra("nsw", txt_nsw_rule);
                        intent.putExtra("qld", txt_qld_rule);
                        intent.putExtra("act", txt_act_rule);
                        intent.putExtra("sa", txt_sa_rule);
                        intent.putExtra("wa", txt_wa_rule);
                        intent.putExtra("nt", txt_nt_rule);
                        startActivity(intent);
                    }
                });


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        anim = (AnimationDrawable) tips.getBackground();
        anim.setEnterFadeDuration(2300);
        anim.setExitFadeDuration(2300);
        getLocationPermission();
        getDeviceLocation();
        requestQueue = Volley.newRequestQueue(this);
        loadTravelRule();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        String apikey ="";
        if(!Places.isInitialized()){
            Places.initialize(this, apikey);
        }
        mapFragment = new SupportMapFragment();
        placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private JsonArrayRequest getDataFromServer(int requestCount)  {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Load...");

        final String DATA_URL = "https://";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
        return jsonArrayRequest;
    }
    private void loadTravelRule() {
        requestQueue.add(getDataFromServer(requestCount));
        requestCount++;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            TravelRuleModel travelRuleModel = new TravelRuleModel();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                String TAG_VIC_Rule = "VIC_Rule";
                String TAG_VIC_Local_case = "VIC_Local_case";
                String TAG_VIC_Overseas_case = "VIC_Overseas_case";
                String TAG_TAS_Rule = "TAS_Rule";
                String TAG_TAS_Local_case = "TAS_Local_case";
                String TAG_TAS_Overseas_case = "TAS_Overseas_case";
                String TAG_NSW_Rule = "NSW_Rule";
                String TAG_NSW_Local_case = "NSW_Local_case";
                String TAG_NSW_Overseas_case = "NSW_Overseas_case";
                String TAG_QLD_Rule = "QLD_Rule";
                String TAG_QLD_Local_case = "QLD_Local_case";
                String TAG_QLD_Overseas_case = "QLD_Overseas_case";
                String TAG_ACT_Rule = "ACT_Rule";
                String TAG_ACT_Local_case = "ACT_Local_case";
                String TAG_ACT_Overseas_case = "ACT_Overseas_case";
                String TAG_SA_Rule = "SA_Rule";
                String TAG_SA_Local_case = "SA_Local_case";
                String TAG_SA_Overseas_case = "SA_Overseas_case";
                String TAG_WA_Rule = "WA_Rule";
                String TAG_WA_Local_case = "WA_Local_case";
                String TAG_WA_Overseas_case = "WA_Overseas_case";
                String TAG_NT_Rule = "NT_Rule";
                String TAG_NT_Local_case = "NT_Local_case";
                String TAG_NT_Overseas_case = "NT_Overseas_case";
                String TAG_TotalVaccine = "TotalVaccine";
                String TAG_StartDate = "StartDate";

                travelRuleModel.setVIC_Rule(json.getString(TAG_VIC_Rule));
                travelRuleModel.setVIC_Local_case(json.getString(TAG_VIC_Local_case));
                travelRuleModel.setVIC_Overseas_case(json.getString(TAG_VIC_Overseas_case));
                travelRuleModel.setTAS_Rule(json.getString(TAG_TAS_Rule));
                travelRuleModel.setTAS_Local_case(json.getString(TAG_TAS_Local_case));
                travelRuleModel.setTAS_Overseas_case(json.getString(TAG_TAS_Overseas_case));
                travelRuleModel.setNSW_Rule(json.getString(TAG_NSW_Rule));
                travelRuleModel.setNSW_Local_case(json.getString(TAG_NSW_Local_case));
                travelRuleModel.setNSW_Overseas_case(json.getString(TAG_NSW_Overseas_case));
                travelRuleModel.setQLD_Rule(json.getString(TAG_QLD_Rule));
                travelRuleModel.setQLD_Local_case(json.getString(TAG_QLD_Local_case));
                travelRuleModel.setQLD_Overseas_case(json.getString(TAG_QLD_Overseas_case));
                travelRuleModel.setACT_Rule(json.getString(TAG_ACT_Rule));
                travelRuleModel.setACT_Local_case(json.getString(TAG_ACT_Local_case));
                travelRuleModel.setACT_Overseas_case(json.getString(TAG_ACT_Overseas_case));
                travelRuleModel.setSA_Rule(json.getString(TAG_SA_Rule));
                travelRuleModel.setSA_Local_case(json.getString(TAG_SA_Local_case));
                travelRuleModel.setSA_Overseas_case(json.getString(TAG_SA_Overseas_case));
                travelRuleModel.setWA_Rule(json.getString(TAG_WA_Rule));
                travelRuleModel.setWA_Local_case(json.getString(TAG_WA_Local_case));
                travelRuleModel.setWA_Overseas_case(json.getString(TAG_WA_Overseas_case));
                travelRuleModel.setNT_Rule(json.getString(TAG_NT_Rule));
                travelRuleModel.setNT_Local_case(json.getString(TAG_NT_Local_case));
                travelRuleModel.setNT_Overseas_case(json.getString(TAG_NT_Overseas_case));
                travelRuleModel.setTotalVaccine(json.getString(TAG_TotalVaccine));
                travelRuleModel.setStartDate(json.getString(TAG_StartDate));

                txt_vic_rule = json.getString(TAG_VIC_Rule);
                txt_vic_local = json.getString(TAG_VIC_Local_case);
                txt_vic_overseas_case = json.getString(TAG_VIC_Overseas_case);
                txt_tas_rule = json.getString(TAG_TAS_Rule);
                txt_tas_local = json.getString(TAG_TAS_Local_case);
                txt_tas_overseas_case = json.getString(TAG_TAS_Overseas_case);
                txt_nsw_rule = json.getString(TAG_NSW_Rule);
                txt_nsw_local = json.getString(TAG_NSW_Local_case);
                txt_nsw_overseas_case = json.getString(TAG_NSW_Overseas_case);
                txt_qld_rule = json.getString(TAG_QLD_Rule);
                txt_qld_local = json.getString(TAG_QLD_Local_case);
                txt_qld_overseas_case = json.getString(TAG_QLD_Overseas_case);
                txt_act_rule = json.getString(TAG_ACT_Rule);
                txt_act_local = json.getString(TAG_ACT_Local_case);
                txt_act_overseas_case = json.getString(TAG_ACT_Overseas_case);
                txt_sa_rule = json.getString(TAG_SA_Rule);
                txt_sa_local = json.getString(TAG_SA_Local_case);
                txt_sa_overseas_case = json.getString(TAG_SA_Overseas_case);
                txt_wa_rule = json.getString(TAG_WA_Rule);
                txt_wa_local = json.getString(TAG_WA_Local_case);
                txt_wa_overseas_case = json.getString(TAG_WA_Overseas_case);
                txt_nt_rule = json.getString(TAG_NT_Rule);
                txt_nt_local = json.getString(TAG_NT_Local_case);
                txt_nt_overseas_case = json.getString(TAG_NT_Overseas_case);
                txt_total_vaccine = json.getString(TAG_TotalVaccine);
                txt_start_date = json.getString(TAG_StartDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mLocationPermissionsGranted){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true); //回到定位
            initMap();
        }
        getDeviceLocation();
        loadMaps();
    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        try{
                            if(task.isSuccessful()){
                                Location currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My Location");
                            }else{
                                Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                        }
                    }
                });
            }
        }catch (SecurityException e){
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this,FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this ,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    private void loadMaps() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Load...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_MAPSHOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String showmaplocation) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(showmaplocation);
                            JSONArray jsonArray = jsonObject.getJSONArray("map");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject mapShow = jsonArray.getJSONObject(i);
                                mapShowArrayList.add(new MapShow(
                                        mapShow.getString("MapID"),
                                        mapShow.getString("UserEmail"),
                                        mapShow.getString("MapLat"),
                                        mapShow.getString("MapLng"),
                                        mapShow.getString("MapTitle"),
                                        mapShow.getString("MapInfo"),
                                        mapShow.getString("MapPhoto"),
                                        mapShow.getString("MapPhotoSmall"),
                                        mapShow.getString("MapAddress"),
                                        mapShow.getInt("MapType"),
                                        mapShow.getString("StartTime"),
                                        mapShow.getString("EndTime")
                                ));

                                MapUserEmail = mapShowArrayList.get(i).getUserEmail();
                                MapLat  = mapShowArrayList.get(i).getLat();
                                MapLng  = mapShowArrayList.get(i).getLng();
                                MapTitle = mapShowArrayList.get(i).getTitle();
                                MapInfo = mapShowArrayList.get(i).getInfo();
                                MapPhoto = mapShowArrayList.get(i).getiConPicture();
                                MapSmallPhoto = mapShowArrayList.get(i).getSmallPhoto();
                                MapAddress = mapShowArrayList.get(i).getAddress();
                                MAP_ID = mapShowArrayList.get(i).getMapID();
                                MapType = mapShowArrayList.get(i).getMtype();
                                MapStartTime = mapShowArrayList.get(i).getMapStartTime();
                                MapEndTime = mapShowArrayList.get(i).getMapEndTime();

                                double latitude = Double.parseDouble(mapShowArrayList.get(i).getLat());
                                double longitude = Double.parseDouble(mapShowArrayList.get(i).getLng());
                                LatLng nowlocat = new LatLng(latitude, longitude);

                                boolean imageCreated = false;
                                Bitmap bmp = null;
                                imageCreated = true;
                                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                                bmp = Bitmap.createBitmap(200, 120, conf);
                                Canvas canvas1 = new Canvas(bmp);
                                Paint color = new Paint();
                                Paint wordcolor = new Paint();
                                color.setTypeface(Typeface.SANS_SERIF); //設定字體
                                color.setTextAlign(Paint.Align.LEFT); //文字對其方式
                                wordcolor.setTextSize(30);
                                int width = (int)wordcolor.measureText(mapShowArrayList.get(i).getTitle());
                                color.setColor(Color.WHITE);
                                wordcolor.setColor(Color.BLACK);

                                BitmapFactory.Options opt = new BitmapFactory.Options();
                                opt.inMutable = true;
                                canvas1.drawRoundRect(new RectF(30,-40,width+50,50),10,10, color);
                                canvas1.drawText(mapShowArrayList.get(i).getTitle(), 40, 40, wordcolor);
                                CustomObject customObject = new CustomObject(MAP_ID,MapLat,MapLng,MapTitle, MapInfo, MapPhoto,MapSmallPhoto,MapAddress,MapType,MapStartTime,MapEndTime,MapUserEmail);
                                try {
                                    Bitmap bmImg = Ion.with(getApplicationContext()).load(mapShowArrayList.get(i).getSmallPhoto()).asBitmap().get();
                                    marker = mMap.addMarker(new MarkerOptions().position(nowlocat).icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                                    marker.setTag(customObject);
                                    marker.showInfoWindow();

                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }

                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        CustomObject data = (CustomObject) marker.getTag();
                                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                                MainActivity.this, R.style.BottomSheetDialogTheme);
                                        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottomsheet_map_detail, (LinearLayout) MainActivity.this.findViewById(R.id.bottomSheetContainer));
                                        map_img = (ImageView) bottomSheetView.findViewById(R.id.map_img);
                                        map_title = (TextView) bottomSheetView.findViewById(R.id.map_title);
                                        map_info = (TextView) bottomSheetView.findViewById(R.id.map_info);
                                        map_start_date = (TextView) bottomSheetView.findViewById(R.id.map_start_date);
                                        map_end_date = (TextView) bottomSheetView.findViewById(R.id.map_end_date);
                                        map_location = (TextView) bottomSheetView.findViewById(R.id.map_location);
                                        map_address = (TextView) bottomSheetView.findViewById(R.id.map_address);

                                        Picasso.get().load(data.getiConPicture()).into(map_img);
                                        map_title.setText(data.getTitle());
                                        map_info.setText(data.getInfo());
                                        map_address.setText("Address: "+data.getAddress());
                                        StringBuilder Latlng = new StringBuilder();
                                        map_location.setText(Latlng.append("Location : ").append(data.getLat()).append(" , ").append(data.getLng()));
                                        map_start_date.setText(new StringBuilder("Start Date : ").append(data.getMapStartTime()));
                                        map_end_date.setText(new StringBuilder("End Date : ").append(data.getMapEndTime()));
                                        bottomSheetDialog.setContentView(bottomSheetView);
                                        bottomSheetDialog.show();
                                        return false;
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error Reading Detail. " + error.toString() , Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
