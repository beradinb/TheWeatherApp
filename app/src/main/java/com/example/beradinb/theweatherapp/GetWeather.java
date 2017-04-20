package com.example.beradinb.theweatherapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetWeather {

    private String webURL = "http://api.openweathermap.org/data/2.5/weather?units=metric&APPID=";

    public JSONObject getWeatherData(String location, String apikey) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            webURL += apikey + "&q="  + location;
            Log.e("data", webURL);
            con = (HttpURLConnection) ( new URL(webURL)).openConnection();
            con.setRequestMethod("GET");
            con.connect();
            Log.e("data1", webURL);
            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            String str = buffer.toString();

            JSONObject json = new JSONObject(buffer.toString());
            Log.e("data", json.toString());
            return json;
        }
        catch(Throwable t) {
            t.printStackTrace();
            Log.e("data", "DTT");
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }

    public Bitmap getWeatherIcon(String icon) {

        String iconURL = "http://openweathermap.org/img/w/";

        try {
            iconURL += icon + ".png";
            URL iconU = new URL(iconURL);
            Log.e("icon",iconURL);
            Bitmap iconBMP = BitmapFactory.decodeStream(iconU.openConnection().getInputStream());
            Log.e("icon",iconBMP.toString());
            return iconBMP;

        }catch (Exception ex) {

        }
        return null;
    }
}
