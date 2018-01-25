package dog.snow.androidrecruittest.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.activities.BaseActivity;
import dog.snow.androidrecruittest.interfaces.JSONCommunicationManager;
import dog.snow.androidrecruittest.utils.Constants;


public class NetworkCalls extends AsyncTask<String, Integer, String> {

    JSONCommunicationManager communicationManager;
    Context context;
    String response;
    String webAddress = Constants.baseUrl;
    Boolean error = false;
    private boolean isInternetConnected = true;
    private HttpURLConnection urlConnection;

    public NetworkCalls(JSONCommunicationManager communicationManager, Context context) {
        this.communicationManager = communicationManager;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (!((BaseActivity) context).haveInternet()) {
            ((BaseActivity) context).showAlertDialog(context.getString(R.string.title_alert),
                    context.getString(R.string.title_internet_problem),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((BaseActivity) context).dismissDialog();
                        }
                    });
            isInternetConnected = false;
            return;
        } else {
            super.onPreExecute();
            communicationManager.onPreRequest();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if (isInternetConnected) {
            URL url;
            StringBuilder result = new StringBuilder();

            try {

                url = new URL(webAddress.concat(params[0]));
                Log.d("URL", url.toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_time_out);
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            } finally {
                urlConnection.disconnect();
            }


            response = result.toString();
            Log.i("response", response);

            if (response.contains("failure"))
                error = true;
            else if (response.isEmpty()) {
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            }


            return response;
        } else {
            return null;
        }
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            if (error) {
                communicationManager.onError(response);
            } else {
                communicationManager.onResponse(response, communicationManager);
            }
        }
    }
}
