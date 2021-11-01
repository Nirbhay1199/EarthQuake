package com.example.earthquake;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.media.tv.TvView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    public static CustomAdapter ca;
    public static ArrayList<String> quakes = new ArrayList<>();
    public static ArrayList<String> magnitude = new ArrayList<>();
    public static ArrayList<String> time5= new ArrayList<>();
    ListView listView;
    double magnitude2;
    String eTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        requestQueue = Volley.newRequestQueue(this);
        
        parseJson();


    }


    public void parseJson() {

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startTime = dateFormat.format(d);


        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?starttime="+startTime+"&format=geojson&minmagnitude=4.0&limit=20";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                        JSONArray jsonArray = response.optJSONArray("features");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);

                                JSONObject property = jsonObject.optJSONObject("properties");

                                String place = property.optString("place");
                                magnitude2 = property.optDouble("mag");
                                eTime = property.optString("time").replace("-", "");

                                Calendar calendar = Calendar.getInstance();
                                long timeInMills = Long.parseLong(eTime);
                                calendar.setTimeInMillis(timeInMills);
                                @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("h:mm a");
                                dateFormat.format(calendar.getTime());
                                String newTime = sdf.format(calendar.getTime());

                                quakes.add(place);
                                magnitude.add(Double.toString(magnitude2));
                                time5.add(newTime);
                            }
                        }
                        displayQuake();
                }, Throwable::printStackTrace);

        requestQueue.add(request);
    }

    void displayQuake(){
        ca = new CustomAdapter();
        listView.setAdapter(ca);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return magnitude.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View myView = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView mag = myView.findViewById(R.id.mag);
            mag.setText(magnitude.get(i));

            TextView place = myView.findViewById(R.id.place);
            place.setSingleLine();
            place.setSelected(true);
            place.setMarqueeRepeatLimit(1);
            place.setText(quakes.get(i));
            if (quakes.get(i).equals("null")){
                place.setText(R.string.nullValue);
                place.setTextColor(Color.parseColor("#FF0000"));
            }

            TextView time = myView.findViewById(R.id.time);
            time.setText(time5.get(i));

            return myView;
        }
    }
}
