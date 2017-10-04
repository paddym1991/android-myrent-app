package org.wit.myrent.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;

import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;

public class ResidenceActivity extends AppCompatActivity implements TextWatcher, OnCheckedChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText geolocation;
    private Residence residence;
    private CheckBox rented;
    private Button dateButton;
    private Portfolio portfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        geolocation = (EditText) findViewById(R.id.geolocation);
        residence = new Residence();
        // Register a TextWatcher in the EditText geolocation object and CheckBox rented object
        geolocation.addTextChangedListener(this);

        dateButton  = (Button) findViewById(R.id.registration_date);

        rented      = (CheckBox) findViewById(R.id.isrented);
        rented.setOnCheckedChangeListener(this);

        //initialising portfolio field
        MyRentApp app = (MyRentApp)getApplication();
        portfolio = app.portfolio;

        //recover the id passed to us by via the intent
        Long resId = (Long)getIntent().getExtras().getSerializable("RESIDENCE_ID");
        //and get the residence object from the portfolio
        residence = portfolio.getResidence(resId);

        //call updateControls method if sure we found a valid reference
        if(residence != null) {
            updateControls(residence);
        }

        dateButton.setOnClickListener(this);
    }

    //Send the residence data to the view widgets.
    public void updateControls(Residence residence) {
        geolocation.setText(residence.geolocation);
        rented.setChecked(residence.rented);
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        residence.setGeolocation(editable.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence c, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence c, int start, int count, int after)
    {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Log.i(this.getClass().getSimpleName(), "rented Checked");
        residence.rented = isChecked;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        residence.date = date.getTime();
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.registration_date :
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog (this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
        }
    }
}
