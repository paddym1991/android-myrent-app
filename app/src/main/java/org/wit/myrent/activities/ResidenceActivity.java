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
import android.widget.EditText;

import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

public class ResidenceActivity extends AppCompatActivity implements TextWatcher, OnCheckedChangeListener {

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
        // disable dateButton when activity is created
        dateButton.setEnabled(false);

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
}
