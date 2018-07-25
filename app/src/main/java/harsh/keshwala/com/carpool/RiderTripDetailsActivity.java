package harsh.keshwala.com.carpool;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class RiderTripDetailsActivity extends AppCompatActivity {

    private String tId,rId;
    private SharedPreferences sharedPreferences;
    private TextView source, destination, date, time, expense, driverName, driverPhone, driverEmail, carModel, carNumber, carYear;
    private TextView riderName, riderEmail, riderNumber, dId;
    private ProgressDialog pDialog;
    private Button sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_trip_details);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME,MODE_PRIVATE);
        rId = sharedPreferences.getString("rId","");

        tId = getIntent().getExtras().getString("tId","");


        source = (TextView) findViewById(R.id.tdSource);
        destination  = (TextView) findViewById(R.id.tdDestination);
        date  = (TextView) findViewById(R.id.tdDate);
        time  = (TextView) findViewById(R.id.tdTime);
        expense  = (TextView) findViewById(R.id.tdExpense);
        driverName  = (TextView) findViewById(R.id.tdDriverName);
        driverEmail  = (TextView) findViewById(R.id.tdDriverEmail);
        driverPhone  = (TextView) findViewById(R.id.tdDriverPhone);
        carModel  = (TextView) findViewById(R.id.tdCarModel);
        carNumber  = (TextView) findViewById(R.id.tdCarNumber);
        carYear  = (TextView) findViewById(R.id.tdCarYear);
        riderName  = (TextView) findViewById(R.id.tdRiderName);
        riderEmail  = (TextView) findViewById(R.id.tdRiderEmail);
        riderNumber  = (TextView) findViewById(R.id.tdRiderPhone);
        dId = (TextView) findViewById(R.id.tdDriverId);

        sendRequest = (Button) findViewById(R.id.sendRequestButton);

        new TripDetails(tId).execute();


        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendRequest(dId.getText().toString(),rId,tId).execute();
            }
        });
    }

    //login with Credentials
    public class TripDetails extends AsyncTask<String, Void, String> {

        String tId;
        public TripDetails(String tId) {
            this.tId = tId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RiderTripDetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(Config.URL+"UserClass.php?driverTripDetails=true&tId="+tId);
                Log.d("YYYYYYY",url.toString());
                JSONObject postDataParams = new JSONObject();
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        Log.e("+++++", "line: "+line);
                        sb.append(line);
                        //break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e) {
                Log.e("~~~", e.toString());
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("TAG-------",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                source.setText(jsonObject.getString("tSource"));
                destination.setText(jsonObject.getString("tDestination"));
                date.setText(jsonObject.getString("tDate"));
                time.setText(jsonObject.getString("tTime"));
                expense.setText(jsonObject.getString("tExpense") + " CAD");
                String riderStatus = jsonObject.getString("riderStatus");
                String temp = "AVAILABLE";


                JSONObject driver = jsonObject.getJSONObject("driver");

                driverName.setText(driver.getString("dFirstName") + " " + driver.getString("dLastName"));
                driverEmail.setText(driver.getString("dEmail"));
                driverPhone.setText(driver.getString("dPhone"));
                dId.setText(driver.getString("dId"));

                JSONObject car = jsonObject.getJSONObject("car");

                carNumber.setText(car.getString("cVehicleNumber"));
                carModel.setText(car.getString("cModelName"));
                carYear.setText(car.getString("cModelYear"));

                if(riderStatus.equals("AVAILABLE"))    {

                    JSONObject rider = jsonObject.getJSONObject("rider");
                    riderName.setText(rider.getString("rFirstName") + " " + rider.getString("rLastName"));
                    riderEmail.setText(rider.getString("rEmail"));
                    riderNumber.setText(rider.getString("rPhone"));
                }
                else    {
                    riderName.setText(riderStatus);
                    riderEmail.setText(riderStatus);
                    riderNumber.setText(riderStatus);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            Log.d("TAG",result.toString());
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    //login with Credentials
    public class SendRequest extends AsyncTask<String, Void, String> {
        String dId, rId, tId;
        public SendRequest(String dId, String rId, String tId)   {
            this.dId = dId;
            this.rId = rId;
            this.tId = tId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RiderTripDetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... arg0) {
            try {
                String data = "dId="+ dId + "&rId=" + rId + "&tId=" + tId;

                URL url = new URL(Config.URL+"UserClass.php?requestTrip=true&"+data);
                Log.d("YYYYYYY",url.toString());
                JSONObject postDataParams = new JSONObject();
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        Log.e("+++++", "line: "+line);
                        sb.append(line);
                        //break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e) {
                Log.e("~~~", e.toString());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("TAG-------",result);
            String status = null;
            String message = null;
            try {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");
                //JSONObject profile = jsonObject.
                Log.d("LOGIN_RESULT",message);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
            if(status.equals("Ok") == true) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            else    {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        }
    }
}