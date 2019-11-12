package course.examples.Networking.AndroidHttpClientJSON;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* Trabajo de fin de grado - inFilms
 * Centro: Escuela politécnica - Universidad de Extremadura
 * Alumno: Manuel Sánchez Mostazo */

public class NetworkUtils {

    public static URL buildURL(String baseURL, String[] segments) {
        return buildURL(baseURL, segments, null, null);
    }

    @SafeVarargs
    public static URL buildURL(String baseURL, String[] segments, Pair<String, String>... params) {

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http");
        builder.authority(baseURL);

        for(String string : segments){
            builder.appendPath(string);
        }

        for (Pair<String, String> param : params) {
            if(param!= null)
                builder.appendQueryParameter(param.first, param.second);
        }

        try {
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e(NetworkUtils.class.getName(), "URL bad formed " + e.getMessage());
            return null;
        }
    }

    public static String getResponse(URL url) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader;

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        Log.i(NetworkUtils.class.getName(), "URL: " + url.toString());

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            if(urlConnection.getResponseCode() != 200)
                Log.e(NetworkUtils.class.getName(), "Connection Error: Error retrieving connection message.");
        } catch (Exception e) {
            Log.e(NetworkUtils.class.getName(), "NetworkError: " + e.getMessage());
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return stringBuilder.toString();
    }

    public static JSONObject getJSONResponse (URL url) {
        String result = getResponse(url);

        if(!result.equals("null")) {
            try {
                return new JSONObject(result);
            } catch (JSONException e) {
                Log.e(NetworkUtils.class.getName(), "Error creating JSON object from retrieved data: " + e.getMessage());
            }
        }

        return null;
    }

}
