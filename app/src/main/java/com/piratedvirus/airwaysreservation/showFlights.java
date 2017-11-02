package com.piratedvirus.airwaysreservation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class showFlights extends AppCompatActivity {

    SweetAlertDialog pDialog;
    JSONArray cart_user_list;
    JSONObject jso;
    ListView cart;
    flightList ad;
    public static final String PHP_SEARCH_FLIGHT = "https://scouncilgeca.com/flight/findflights.php";
    ArrayList<String> startCity_arr = new ArrayList<String>();
    ArrayList<String> endCity_arr = new ArrayList<String>();
    ArrayList<String> departDate_arr = new ArrayList<String>();
    ArrayList<String> fair_arr = new ArrayList<String>();
    ArrayList<String> uniqueID_arr = new ArrayList<String>();
    ArrayList<String> compName = new ArrayList<String>();
    ArrayList<String> compLogo = new ArrayList<String>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flights);
        final TextView startCityShort = (TextView) findViewById(R.id.startCityShort);
        final TextView fullCityName = (TextView) findViewById(R.id.fullCityName);
        final TextView endCityShort = (TextView) findViewById(R.id.endCityShort);
        final TextView endCityName = (TextView) findViewById(R.id.endCityName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        String sCity = getIntent().getStringExtra("startCity");
        String eCity = getIntent().getStringExtra("endCity");
        String ssCity =sCity.substring(0, Math.min(sCity.length(), 3));
        String seCity = eCity.substring(0,Math.min(sCity.length(), 3));
        startCityShort.setText(ssCity);
        fullCityName.setText(sCity);
        endCityShort.setText(seCity);
        endCityName.setText(eCity);

        getFlights();
    }

    public void getFlights(){

        final String startCity = getIntent().getStringExtra("startCity");
        final String endCity = getIntent().getStringExtra("endCity");
        final String departDate = getIntent().getStringExtra("departDate");
        final String planeClass = getIntent().getStringExtra("planeClass");
        final String adultNum = getIntent().getStringExtra("adultNum");
        final String ChildrenNum = getIntent().getStringExtra("ChildrenNum");

        Log.e("PV", "onCreate: " + startCity + endCity + departDate + planeClass);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                PHP_SEARCH_FLIGHT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("showFlights", "onResponse: ghusla volley req madhye");
                        json(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(showFlights.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {
            public static final String TAG = "PV";

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();

                params.put("startCity", getIntent().getStringExtra("startCity"));
                params.put("endCity", getIntent().getStringExtra("endCity"));
                params.put("departDate", getIntent().getStringExtra("departDate"));
                Log.e("PVT", "Location hagla = "+getIntent().getStringExtra("startCity"));
                return params;
            }


        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {

            }
        });
    }

    public void json(String response) {
//        pDialog.dismissWithAnimation();

        int adultNum = Integer.parseInt(getIntent().getStringExtra("adultNum").toString());
        int ChildrenNum = Integer.parseInt(getIntent().getStringExtra("ChildrenNum").toString());



        Log.e("PVC", "json: ghusla json madhye" );
        try {
            jso = new JSONObject(response);
            cart_user_list = jso.getJSONArray("result");
            Log.e("response", "json: " + response );
            for (int i = 0; i < cart_user_list.length(); i++) {
                Log.e("PV", "json: inside d for" );
                JSONObject c = cart_user_list.getJSONObject(i);

                int fair_int = Integer.parseInt(c.getString("fair").toString());

                int totalFair = adultNum*fair_int + (ChildrenNum*fair_int)/2;


                if(getIntent().getStringExtra("planeClass").equals("Business Class")){
                    totalFair = totalFair*2;
                }
                String stringFair = String.valueOf(totalFair);


                startCity_arr.add(c.getString("startCity"));
                endCity_arr.add(c.getString("endCity"));
                departDate_arr.add(c.getString("departDate"));
                fair_arr.add(stringFair);
                uniqueID_arr.add(c.getString("uniqueID"));
                compName.add(c.getString("compName"));
                compLogo.add(c.getString("compLogo"));



            }


            ad = new flightList(showFlights.this, startCity_arr, endCity_arr, departDate_arr, fair_arr, uniqueID_arr,compName,compLogo);
            cart = (ListView) findViewById(R.id.cart_list_show);
            cart.setAdapter(ad);





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void viewTicket(View v){
        Intent i = new Intent(showFlights.this,Ticket.class);
        startActivity(i);
    }

}