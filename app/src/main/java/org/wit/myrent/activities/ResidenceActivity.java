package org.wit.myrent.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
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

import static org.wit.android.helpers.ContactHelpers.getContact;
import static org.wit.android.helpers.ContactHelpers.getEmail;
import static org.wit.android.helpers.ContactHelpers.sendEmail;
import static org.wit.android.helpers.IntentHelpers.navigateUp;
import static org.wit.android.helpers.IntentHelpers.selectContact;
import android.content.Intent;

public class ResidenceActivity extends AppCompatActivity implements TextWatcher, OnCheckedChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText geolocation;
    private Residence residence;
    private CheckBox rented;
    private Button dateButton;
    private Portfolio portfolio;
    private static final int REQUEST_CONTACT = 1;       //An ID we will use for the implicit Intent
    private Button   tenantButton;             //Button to trigger the intent
    private String emailAddress = "";
    private Button   reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);
        //for navigation purposes
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        tenantButton = (Button)   findViewById(R.id.tenant);        //initialisation of tenantButton
        tenantButton.setOnClickListener(this);      //event handler set up for tenantButton

        reportButton = (Button)   findViewById(R.id.residence_reportButton);        //initialisation of reportButton
        reportButton.setOnClickListener(this);      //enabling the event handler
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


    /**
     * Event handler
     * @param view
     */
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
            case R.id.tenant :
                selectContact(this, REQUEST_CONTACT);       //selectContact method within IntentHelpers class
                break;
            case R.id.residence_reportButton :
                sendEmail(this, emailAddress,
                        getString(R.string.residence_report_subject), residence.getResidenceReport(this));
                break;
        }
    }

    //trigger a save when the user leaves the ResidenceActivity
    @Override
    public void onPause()
    {
        super.onPause();
        portfolio.saveResidences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:  navigateUp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //To deal with selectContent method triggering a 'startActivityForResult'
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CONTACT:
                String name = getContact(this, data);
                emailAddress = getEmail(this, data);
                tenantButton.setText(name + " : " + emailAddress);
                residence.tenant = name;
                break;
        }
    }
}
