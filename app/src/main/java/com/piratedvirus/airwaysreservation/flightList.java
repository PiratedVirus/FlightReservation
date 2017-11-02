package com.piratedvirus.airwaysreservation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.anwarshahriar.calligrapher.Calligrapher;

public class flightList extends ArrayAdapter<String> {

    SweetAlertDialog pDialog;
    int cart_sum =0 ;

    private final Activity context;
    private final ArrayList startCity_arr ;
    private final ArrayList endCity_arr;
    private final ArrayList departDate_arr;
    private final ArrayList fair_arr;
    private final ArrayList uniqueID_arr;
    private final ArrayList compName;
    private final ArrayList compLogo;


    public static final String PHP_BOOK_FLIGHT = "https://scouncilgeca.com/flight/bookflight.php";





    public flightList(Activity context,
                      ArrayList startCity_arr, ArrayList endCity_arr, ArrayList departDate_arr, ArrayList fair_arr, ArrayList uniqueID_arr, ArrayList compName,ArrayList compLogo) {
        super(context, R.layout.flight_list, startCity_arr);

        this.context = context;
        this.startCity_arr = startCity_arr;
        this.endCity_arr = endCity_arr;
        this.departDate_arr = departDate_arr;
        this.fair_arr = fair_arr;
        this.uniqueID_arr = uniqueID_arr;
        this.compName = compName;
        this.compLogo = compLogo;

        Log.e("PV","bochya"+startCity_arr);
    }





    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Log.e("PV","xyz");
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.flight_list_new, null, true);
        rowView.setMinimumHeight(80);
        rowView.setBackgroundColor(Color.WHITE);

        Calligrapher calligrapher = new Calligrapher(getContext());
        calligrapher.setFont((Activity) getContext(), "fonts/lato.ttf", true);

        cart_sum = 0;

        TextView comName = (TextView) rowView.findViewById(R.id.comName);
        TextView fair = (TextView) rowView.findViewById(R.id.fair);
        TextView hiddenLogo = (TextView) rowView.findViewById(R.id.hiddenLogo);
        TextView hiddenUniqueID = (TextView) rowView.findViewById(R.id.hiddenUniqueID);
        ImageView compLogo_xml = (ImageView)rowView.findViewById(R.id.compLogo_xml);


        Typeface mont = Typeface.createFromAsset(getContext().getAssets(),  "fonts/lato.ttf");
        fair.setTypeface(mont);
        comName.setTypeface(mont);



        rowView.setBackgroundColor(Color.WHITE);
        fair.setText((CharSequence) fair_arr.get(position));
        comName.setText((CharSequence)compName.get(position));
        hiddenLogo.setText((CharSequence)compLogo.get(position));
        hiddenUniqueID.setText((CharSequence)uniqueID_arr.get(position));

        String imgUrl = hiddenLogo.getText().toString();
        final String commonID = hiddenUniqueID.getText().toString();
        final String fairPrice = fair.getText().toString();



        Picasso.with(context).load(imgUrl).into(compLogo_xml);





        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Do you really want to book this Flight?");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(context,  "Registration Confirmed. Visit Tickets tab for more Details.", Toast.LENGTH_SHORT).show();
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                                    PHP_BOOK_FLIGHT,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                        }
                                                    },
                                                    new Response.ErrorListener(){
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                                                        }
                                                    }){
                                                public static final String TAG = "PV";

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {

                                                    Map<String,String> params = new HashMap<>();
                                                    String temp = commonID;
                                                    params.put("uniqueID", commonID);
                                                    params.put("fair",fairPrice);

                                                    return params;
                                                }


                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                            requestQueue.add(stringRequest);
                                        }




                                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();





                return true;
            }
        });


        return rowView;
    }

}