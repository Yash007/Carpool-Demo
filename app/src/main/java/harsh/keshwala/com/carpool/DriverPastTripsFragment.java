package harsh.keshwala.com.carpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DriverPastTripsFragment extends android.support.v4.app.Fragment {

    String TAG = "PastTrip";
    private ProgressDialog pDialog;
    private ListView lv;

    ArrayList<HashMap<String, String>> tripPastList;
    Activity context;

    public DriverPastTripsFragment()    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view =  inflater.inflate(R.layout.fragment_driver_past_trips,container,false);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        tripPastList = new ArrayList<>();
        lv = (ListView) context.findViewById(R.id.tripPastList);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

        String dId = sharedPreferences.getString("dId","");
        new GetPastTripList(dId).execute();

    }



    public class GetPastTripList extends AsyncTask<Void, Void, Void> {

        String dId;
        public GetPastTripList(String dId)  {
            this.dId = dId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Config.URL+"/UserClass.php?driverPastTrips=true&dId="+dId);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("trips");

                    tripPastList.clear();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("tId");
                        String source = c.getString("tSource");
                        String destination = c.getString("tDestination");
                        String date = c.getString("tDate");
                        String time = c.getString("tTime");
                        String expense = c.getString("tExpense");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();


                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("address", source + " to " + destination);
                        contact.put("dateTime", date + " at " + time);
                        contact.put("expense", "$ " + expense);

                        // adding contact to contact list
                        tripPastList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    context, tripPastList,
                    R.layout.trip_list, new String[]{"id","address", "dateTime", "expense"},
                    new int[]{R.id.tId, R.id.tAddress, R.id.tDateTime, R.id.tExpense});

            lv.setAdapter(adapter);
        }

    }
}
