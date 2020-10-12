package com.thaina.filmesdahora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thaina.filmesdahora.httpHandler.HttpHandler;
import com.codesgood.views.JustifiedTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MovieDetail extends AppCompatActivity {
    private Handler handler = new Handler();

    private Intent intent;

    private String TAG = Movies.class.getSimpleName();
    private String imdbID = "";

    private HashMap<String, String> movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setTitle("Movie Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.intent = getIntent();
        this.imdbID = (String) this.intent.getSerializableExtra("imdbID");
        this.movie = new HashMap<>();

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MovieDetail.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String myKey = "b58d9546";
            String jsonStr = sh.makeServiceCall(myKey, imdbID);

            Log.e(TAG, "Response from url: "+jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    movie.put("Title", jsonObj.getString("Title"));
                    movie.put("Year", jsonObj.getString("Year"));
                    movie.put("Rated", jsonObj.getString("Rated"));
                    movie.put("Released", jsonObj.getString("Released"));
                    movie.put("Runtime", jsonObj.getString("Runtime"));
                    movie.put("Genre", jsonObj.getString("Genre"));
                    movie.put("Director", jsonObj.getString("Director"));
                    movie.put("Writer", jsonObj.getString("Writer"));
                    movie.put("Actors", jsonObj.getString("Actors"));
                    movie.put("Plot", jsonObj.getString("Plot"));
                    movie.put("Language", jsonObj.getString("Language"));
                    movie.put("Country", jsonObj.getString("Country"));
                    movie.put("Awards", jsonObj.getString("Awards"));
                    movie.put("Poster", jsonObj.getString("Poster"));
                    movie.put("Ratings", jsonObj.getString("Ratings"));
                    movie.put("Metascore", jsonObj.getString("Metascore"));
                    movie.put("imdbRating", jsonObj.getString("imdbRating"));
                    movie.put("imdbVotes", jsonObj.getString("imdbVotes"));
                    movie.put("imdbID", jsonObj.getString("imdbID"));
                    movie.put("Type", jsonObj.getString("Type"));
                    movie.put("DVD", jsonObj.getString("DVD"));
                    movie.put("BoxOffice", jsonObj.getString("BoxOffice"));
                    movie.put("Production", jsonObj.getString("Production"));
                    movie.put("Website", jsonObj.getString("Website"));

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
            TextView tvTitleDetail = (TextView) findViewById(R.id.tvTitleDetail);
            TextView tvYearDetail = (TextView) findViewById(R.id.tvYearDetail);
            TextView tvTime = (TextView) findViewById(R.id.tvTime);
            TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
            TextView tvActors = (TextView) findViewById(R.id.tvActors);
            TextView tvDirector = (TextView) findViewById(R.id.tvDirector);
            TextView tvCountry = (TextView) findViewById(R.id.tvCountry);
            TextView tvLanguage = (TextView) findViewById(R.id.tvLanguage);
            //TextView tvPlot = (TextView) findViewById(R.id.tvPlot);
            JustifiedTextView tvPlot = (JustifiedTextView) findViewById(R.id.tvPlot);

            loadImage();
            tvTitleDetail.setText(movie.get("Title"));
            tvYearDetail.setText("Ano: "+movie.get("Year"));
            tvTime.setText("Tempo de filme: "+movie.get("Runtime"));
            tvGenre.setText("Gênero: "+movie.get("Genre"));
            tvActors.setText("Atores: "+movie.get("Actors"));
            tvDirector.setText("Diretor: "+movie.get("Director"));
            tvCountry.setText("País: "+movie.get("Country"));
            tvLanguage.setText("Idioma: "+movie.get("Language"));
            //tvPlot.setText("Descrição: "+movie.get("Plot"));
            tvPlot.setText(Html.fromHtml("Descrição: "+movie.get("Plot")));

        }

        private void loadImage() {
            new Thread() {
                public void run() {
                    Bitmap img = null;
                    try {
                        URL url = new URL(movie.get("Poster"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        InputStream in = conn.getInputStream();
                        img = BitmapFactory.decodeStream(in);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final Bitmap imgAux = img;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView iv = new ImageView(getBaseContext());
                            iv.setImageBitmap(imgAux);

                            LinearLayout ll = (LinearLayout) findViewById(R.id.llPoster);
                            ll.addView(iv);
                        }
                    });
                }
            }.start();
        }
    }
}