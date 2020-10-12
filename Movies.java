package com.thaina.filmesdahora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.thaina.filmesdahora.httpHandler.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Movies extends AppCompatActivity {
    private String TAG = Movies.class.getSimpleName();
    private ListView lv;

    private ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        getSupportActionBar().setTitle("Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.movieList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        if (Backup.searchBackup != null) {
            TextView tvSearch = (TextView) findViewById(R.id.txt_Search);
            tvSearch.setText(Backup.searchBackup);
            this.search();
        }

        onClickDetail();
    }

    private void search() {
        movieList.clear();
        new GetContacts().execute();
    }

    public void onClickSearch(View view) {
        this.search();
    }

    private void onClickDetail() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvSearch = (TextView) findViewById(R.id.txt_Search);
                Backup.searchBackup = tvSearch.getText().toString();
                HashMap info = movieList.get(i);
                String imdbID = info.get("imdbID").toString();

                Intent intent = new Intent(Movies.this, MovieDetail.class);
                intent.putExtra("imdbID", imdbID);
                startActivity(intent);
            }
        });
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
         private EditText editTextSearch = findViewById(R.id.txt_Search);
         //private EditText editTextYear = findViewById(R.id.txt_ano);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Movies.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String myKey = "b58d9546";
            String search = editTextSearch.getText().toString();
            String search_year = "";
            String jsonStr = sh.makeServiceCall(myKey, search, search_year);

            Log.e(TAG, "Response from url: "+jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray result_movies = jsonObj.getJSONArray("Search");

                    for (int i = 0; i < result_movies.length(); i++) {
                        JSONObject c = result_movies.getJSONObject(i);

                        String imdbID = c.getString("imdbID");
                        String title = c.getString("Title");
                        String year = c.getString("Year");
                        String type = c.getString("Type");
                        String poster = c.getString("Poster");

                        HashMap<String, String> movie = new HashMap<>();
                        movie.put("imdbID", imdbID);
                        movie.put("Title", title);
                        movie.put("Year", year);
                        movie.put("Type", type);
                        movie.put("Poster", poster);

                        movieList.add(movie);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Movies.this, movieList,
                    R.layout.list_item, new String[]{"Title", "Year"}, new int[]{R.id.tvTitleDetail, R.id.tvYear});
            lv.setAdapter(adapter);
        }
    }
}