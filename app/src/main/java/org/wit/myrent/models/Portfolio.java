package org.wit.myrent.models;
import java.util.ArrayList;

import android.util.Log;

public class Portfolio
{
    public ArrayList<Residence> residences;

    public Portfolio() {
        residences = new ArrayList<Residence>();
    }

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
}