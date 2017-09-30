package org.wit.myrent.models;
import java.util.ArrayList;

import android.util.Log;

public class Portfolio
{
    public ArrayList<Residence> residences;

    public Portfolio() {
        residences = new ArrayList<Residence>();
        this.generateTestData();
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

    private void generateTestData() {
        for (int i = 0; i < 100; i += 1) {
            Residence r = new Residence();
            r.geolocation = (52.253456 + i) % 90 + ", " + (-7.187162 - i) % 180 + "";
            if (i % 2 == 0) {
                r.rented = true;
            }
            else {
                r.rented = false;
            }
            residences.add(r);
        }
    }
}