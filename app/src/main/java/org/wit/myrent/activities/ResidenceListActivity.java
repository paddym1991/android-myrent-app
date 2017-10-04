//In order to update the list with the residence objects contained in the portfolio, we need an Adapter.
//        An Adapter is a special class we can append to the end of the existing ResidenceListActivity class (make sure it is outside the closing brace of ResidenceListActivity).

package org.wit.myrent.activities;

import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

//Introducing ResidenceAdapter requires the following additional import statements
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import static org.wit.android.helpers.IntentHelpers.startActivityWithData;
import static org.wit.android.helpers.IntentHelpers.startActivityWithDataForResult;

import android.view.MenuItem;

import java.util.ArrayList;

public class ResidenceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView listView;
    private Portfolio portfolio;
    //In order to incorporate it ResidenceAdapter into ResidenceListActivity, first introduce a new field
    private ResidenceAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_residence_list);

        listView = (ListView) findViewById(R.id.residenceList);

        MyRentApp app = (MyRentApp) getApplication();
        portfolio = app.portfolio;

        //in order to trigger ResidenceAdapter
        adapter = new ResidenceAdapter(this, portfolio.residences);
        listView.setAdapter(adapter);

        //install the ResidenceListActivity as a Listener
        listView.setOnItemClickListener(this);
    }

    //interface method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Residence residence = adapter.getItem(position);
        startActivityWithData(this, ResidenceActivity.class, "RESIDENCE_ID", residence.id);
    }

    //Ensure changes made in ResidenceActivity are reflected in the list
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    //we bind the newly-created menu to the activity within which we wish the menu to appear
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.residencelist, menu);
        return true;
    }

    //override onOptionsItemSelected to respond to selecting the menu item to create a new residence instance
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_residence: Residence residence = new Residence();
                portfolio.addResidence(residence);
                startActivityWithDataForResult(this, ResidenceActivity.class, "RESIDENCE_ID", residence.id, 0);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
}


class ResidenceAdapter extends ArrayAdapter<Residence>
{
    private Context context;

    public ResidenceAdapter(Context context, ArrayList<Residence> residences)
    {
        super(context, 0, residences);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_item_residence, null);
        }
        Residence res = getItem(position);

        TextView geolocation = (TextView) convertView.findViewById(R.id.residence_list_item_geolocation);
        geolocation.setText(res.geolocation);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.residence_list_item_dateTextView);
        dateTextView.setText(res.getDateString());

        CheckBox rentedCheckBox = (CheckBox) convertView.findViewById(R.id.residence_list_item_isrented);
        rentedCheckBox.setChecked(res.rented);

        return convertView;
    }
}