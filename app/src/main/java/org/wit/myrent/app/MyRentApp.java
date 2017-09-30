package org.wit.myrent.app;

import org.wit.myrent.models.Portfolio;
import android.app.Application;

public class MyRentApp extends Application
{
    public Portfolio portfolio;

    @Override
    public void onCreate()
    {
        super.onCreate();
        portfolio = new Portfolio();
    }
}