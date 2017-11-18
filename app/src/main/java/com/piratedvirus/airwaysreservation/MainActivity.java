package com.piratedvirus.airwaysreservation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView insertDate;
    int year,month,day;
    SweetAlertDialog pDialog;
    static final int DIALOG_ID = 0;
    public static final String PHP_SEARCH_FLIGHT = "https://scouncilgeca.com/flight/findflights.php";
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView insertDate = (TextView)findViewById(R.id.userDate);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/lato.ttf", true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }



////        // Spinner element
//        Spinner spinner = (Spinner) findViewById(R.id.selectClass);
//
//        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
//
//        // Spinner Drop down elements
//        List<String> categories = new ArrayList<String>();
//        categories.add("Business Class");
//        categories.add("Economy Class");
//
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);

        final Calendar cal = Calendar.getInstance();
        cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        final TextView hiddenVal = (TextView) findViewById(R.id.hiddenVal);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        hiddenVal.setText(item);

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void openPicker(View v){
        showDialog(DIALOG_ID);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==DIALOG_ID)
            return new DatePickerDialog(this, PickerListner, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener PickerListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


            year = i;
            month =i1;
            day = i2;
            String mon=MONTHS[i1];

            Toast.makeText(getApplicationContext(),year + "/" + month + "/" + day, Toast.LENGTH_SHORT).show();
            TextView insertDate = (TextView)findViewById(R.id.userDate);
            insertDate.setText(day + " " + mon + " " + year);


        }


    };


    public void findFlight(View v) {

        final TextView startCity = (TextView) findViewById(R.id.StartCity);
        final TextView endCity = (TextView) findViewById(R.id.EndCity);
        final TextView userDate = (TextView) findViewById(R.id.userDate);
        final Spinner planeClass = (Spinner) findViewById(R.id.selectClass);
        final TextView AdultNum = (TextView) findViewById(R.id.AdultNum);
        final TextView ChildrenNum = (TextView) findViewById(R.id.ChildrenNum);
        final TextView hiddenVal = (TextView) findViewById(R.id.hiddenVal);

        Intent i = new Intent(getApplicationContext(), showFlights.class);
        i.putExtra("startCity", startCity.getText().toString());
        i.putExtra("endCity", endCity.getText().toString());
        i.putExtra("departDate", userDate.getText().toString());
        i.putExtra("planeClass", hiddenVal.getText().toString());
        i.putExtra("adultNum", AdultNum.getText().toString());
        i.putExtra("ChildrenNum", ChildrenNum.getText().toString());
        startActivity(i);
    }

}
