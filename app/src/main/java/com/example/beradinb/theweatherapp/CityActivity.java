package com.example.beradinb.theweatherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CityActivity.this, FavActivity.class));
            }
        });
        Intent getIntent = getIntent();
        String cityLoc = getIntent.getExtras().getString("city");
        String cityApi = this.getString(R.string.api_code);
        TextView ccity = (TextView)findViewById(R.id.cCity);
        ccity.setText(cityLoc);
        new weatherSearch().execute(cityLoc, cityApi);
    }

    public class weatherSearch extends AsyncTask <String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            GetWeather cityWeather = new GetWeather();
            JSONObject cityJSON = cityWeather.getWeatherData(params[0], params[1]);
            return cityJSON;
        }

        protected void onPostExecute(JSONObject cityDataJSON) {

            try {
                JSONArray jWeatherArray = cityDataJSON.getJSONArray("weather");
                JSONObject jWeather = jWeatherArray.getJSONObject(0);
                String weatherArray = jWeather.getString("main");
                String icon = jWeather.getString("icon");
                Log.e("main", jWeather.getString("main"));
                TextView desc = (TextView)findViewById(R.id.weatherDesc);
                desc.setText(weatherArray);
                JSONObject jMain = cityDataJSON.getJSONObject("main");
                String mainTemp = jMain.getString(("temp"));
                Log.e("temp", jMain.getString("temp"));
                TextView idTemp = (TextView)findViewById(R.id.idTemp);
                idTemp.setText(mainTemp + " \u2103");

                new iconSearch().execute(icon);

            } catch (JSONException e) {

            }
        }
    }

    public class iconSearch extends AsyncTask <String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            GetWeather iconWeather = new GetWeather();
            Bitmap cityICON = iconWeather.getWeatherIcon(params[0]);

            return cityICON;
        }

        protected void onPostExecute(Bitmap cityIcon) {

            try {
                ImageView iconW = (ImageView)findViewById(R.id.iconView);
                iconW.setImageBitmap(cityIcon);

            } catch (Exception e) {

            }
        }
    }

}
