package com.example.earthquake;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String[] items;
    private RequestQueue requestQueue;
    public static CustomAdapter ca;
    public static ArrayList<String> quakes = new ArrayList<>();
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        ListView listView = findViewById(R.id.list);
        requestQueue = Volley.newRequestQueue(this);
        
        parseJson();

        ca = new CustomAdapter();
        listView.setAdapter(ca);
        
        items = new String[quakes.size()];
        for (int i = 0; i < quakes.size(); i++) {
            items[i] = quakes.get(i);
        }

    }

    public void parseJson() {

        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?starttime=2021-10-08&format=geojson&minmagnitude=4.5";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                        JSONArray jsonArray = response.optJSONArray("features");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(i);

                                JSONObject property = jsonObject.optJSONObject("properties");

                                assert property != null;
                                String place = property.optString("place");
                                quakes.add(place);
                            }
                        }
                }, Throwable::printStackTrace);

        requestQueue.add(request);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
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
            TextView mag = findViewById(R.id.mag);
            mag.setText(items[i]);

            return myView;
        }
    }
}
