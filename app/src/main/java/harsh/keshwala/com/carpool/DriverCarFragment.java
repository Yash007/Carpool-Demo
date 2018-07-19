package harsh.keshwala.com.carpool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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


public class DriverCarFragment extends Fragment {

    String TAG = "CarList";
    private ProgressDialog pDialog;
    private ListView lv;

    ArrayList<HashMap<String, String>> carList;

    FloatingActionButton addCar;
    Activity context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view =  inflater.inflate(R.layout.fragment_driver_car,container,false);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        addCar = (FloatingActionButton) context.findViewById(R.id.addCarFloatingActionButton);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(context,DriverAddCarActivity.class));
            }
        });

        carList = new ArrayList<>();
        lv = (ListView) context.findViewById(R.id.carList);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);

        String dId = sharedPreferences.getString("dId","");

        new GetDriverCarList(dId).execute();

    }

    public class GetDriverCarList extends AsyncTask<Void, Void, Void> {

        String dId;
        public GetDriverCarList(String dId)  {
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
            String jsonStr = sh.makeServiceCall(Config.URL+"/UserClass.php?driverCarList=true&dId="+dId);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("cars");

                    carList.clear();
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String cId = c.getString("cId");
                        String cModelName = c.getString("cModelName");
                        String cVehicleNumber = c.getString("cVehicleNumber");
                        String cModelYear = c.getString("cModelYear");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();


                        // adding each child node to HashMap key => value
                        contact.put("cId", cId);
                        contact.put("cModelName", cModelName );
                        contact.put("cVehicleNumber", cVehicleNumber);
                        contact.put("cModelYear", cModelYear);

                        // adding contact to contact list
                        carList.add(contact);
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
                    context, carList,
                    R.layout.car_list, new String[]{"cId","cModelName", "cVehicleNumber", "cModelYear"},
                    new int[]{R.id.cId, R.id.cModelName, R.id.cVehicleNumber, R.id.cModelYear});

            lv.setAdapter(adapter);
        }

    }
}
