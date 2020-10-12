package com.thaina.filmesdahora.httpHandler;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String makeServiceCall(String myKey, String search, String year) {
        String response = null;
        URL url = null;

        try {
            search = search.replace(" ","%20");
            //http://www.omdbapi.com/?apikey=b58d9546&plot=short&type=movie&page=1&s=%20&y=2021
            if (!search.isEmpty() && !year.isEmpty())
                url = new URL("http://www.omdbapi.com/?apikey=" + myKey + "&plot=full&type=movie&s=" + search + "&y=" + year);
            else if (search.isEmpty() && !year.isEmpty())
                url = new URL("http://www.omdbapi.com/?apikey=" + myKey + "&plot=full&type=movie&s=%20" + "&y=" + year);
            else if (!search.isEmpty() && year.isEmpty())
                url = new URL("http://www.omdbapi.com/?apikey=" + myKey + "&plot=full&type=movie&s=" + search + "&y=%20");
            else if (search.isEmpty() && year.isEmpty())
                url = new URL("http://www.omdbapi.com/?apikey=" + myKey + "&plot=full&type=movie&s=%20" + "&y=%20");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: "+e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: "+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: "+e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: "+e.getMessage());
        }

        return response;
    }

    public String makeServiceCall(String myKey, String imdbID) {
        String response = null;
        URL url = null;

        try {
            //http://www.omdbapi.com/?apikey=b58d9546&plot=short&type=movie&page=1&s=%20&y=2021
            url = new URL("http://www.omdbapi.com/?apikey=" + myKey + "&plot=full&type=movie&i=" + imdbID);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: "+e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: "+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: "+e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: "+e.getMessage());
        }

        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder('\n');

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
