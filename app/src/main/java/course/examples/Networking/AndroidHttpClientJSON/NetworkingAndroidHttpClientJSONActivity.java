package course.examples.Networking.AndroidHttpClientJSON;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;

public class NetworkingAndroidHttpClientJSONActivity extends ListActivity {

	//JSON keys
	private static final String LONGITUDE_TAG = "lng";
	private static final String LATITUDE_TAG = "lat";
	private static final String MAGNITUDE_TAG = "magnitude";
	private static final String EARTHQUAKE_TAG = "earthquakes";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new HttpGetTask().execute();
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

		private static final String BASE_URL = "api.geonames.org";
		private static final String JSON_SEG = "earthquakesJSON";
		private static final String NORTH_P = "north";
		private static final String SOUTH_P = "south";
		private static final String EAST_P = "east";
		private static final String WEST_P = "west";
		private static final String USER_P = "username";

		@Override
		protected List<String> doInBackground(Void... params) {
			URL queryURL;
			JSONObject result;

			//Load upcoming movies
			queryURL = NetworkUtils.buildURL(BASE_URL,
											 new String[]{JSON_SEG},
											 new Pair(NORTH_P, "44.1"),
											 new Pair(SOUTH_P, "-9.9"),
											 new Pair(EAST_P, "-22"),
											 new Pair(WEST_P, "55.2"),
											 new Pair(USER_P, "aporter"));
			result = NetworkUtils.getJSONResponse(queryURL);
			if(result != null)
				return jsonToList(result);

			return null;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			setListAdapter(new ArrayAdapter<String>(
					NetworkingAndroidHttpClientJSONActivity.this,
					R.layout.list_item, result));
		}
	}

	public List<String> jsonToList(JSONObject responseObject) {
			List<String> result = new ArrayList<String>();

			try {
				// Extract value of "earthquakes" key -- a List
				JSONArray earthquakes = responseObject
						.getJSONArray(EARTHQUAKE_TAG);

				// Iterate over earthquakes list
				for (int idx = 0; idx < earthquakes.length(); idx++) {

					// Get single earthquake data - a Map
					JSONObject earthquake = (JSONObject) earthquakes.get(idx);

					// Summarize earthquake data as a string and add it to
					// result
					result.add(MAGNITUDE_TAG + ":"
							+ earthquake.get(MAGNITUDE_TAG) + ","
							+ LATITUDE_TAG + ":"
							+ earthquake.getString(LATITUDE_TAG) + ","
							+ LONGITUDE_TAG + ":"
							+ earthquake.get(LONGITUDE_TAG));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
	}
}
