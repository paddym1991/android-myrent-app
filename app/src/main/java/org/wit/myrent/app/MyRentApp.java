package org.wit.myrent.app;

import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.PortfolioSerializer;

import android.app.Application;
import static org.wit.android.helpers.LogHelpers.info;

public class MyRentApp extends Application
{
    public Portfolio portfolio;
    //introduce a field to hold the file name we will use to store the portfolio
    private static final String FILENAME = "portfolio.json";

    @Override
    public void onCreate()
    {
        super.onCreate();
        //portfolio creation
        PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);
        portfolio = new Portfolio(serializer);

        info(this, "MyRent app is launched");
    }
}