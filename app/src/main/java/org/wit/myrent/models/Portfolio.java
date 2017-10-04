package org.wit.myrent.models;
import java.util.ArrayList;

import android.util.Log;

import static org.wit.android.helpers.LogHelpers.info;

public class Portfolio
{
    public ArrayList<Residence> residences;
    // introduce the serializer as a member of the Portfolio class
    private PortfolioSerializer   serializer;

    //revise the constructor to take a serializer when it is being initialised
    public Portfolio(PortfolioSerializer serializer)
    {
        this.serializer = serializer;
        try
        {
            residences = serializer.loadResidences();
        }
        catch (Exception e)
        {
            info(this, "Error loading residences: " + e.getMessage());
            residences = new ArrayList<Residence>();
        }
    }

    //method to add a residence to the list
    public void addResidence(Residence residence) {
        residences.add(residence);
    }

    public Residence getResidence(Long id) {
        Log.i(this.getClass().getSimpleName(), "Long parameter id: " + id);

        for (Residence res : residences) {
            if (id.equals(res.id)) {
                return res;
            }
        }
        return null;
    }

    //introduce a new method to save all the residences to disk
    public boolean saveResidences()
    {
        try
        {
            serializer.saveResidences(residences);
            info(this, "Residences saved to file");
            return true;
        }
        catch (Exception e)
        {
            info(this, "Error saving residences: " + e.getMessage());
            return false;
        }
    }
}