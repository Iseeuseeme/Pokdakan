package app.pokdakan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class SplashActivity extends AppCompatActivity {

    MyTask myTask;
    View rootView;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username, password, hak_akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);


        progressBar = (ProgressBar) findViewById(R.id.splash_loadingBar);
        rootView = progressBar;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myTask = new MyTask();
                myTask.execute();
            }
        }, 1500);
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // Check for Internet Connection from the static method of Helper class
            if (isNetworkAvailable(SplashActivity.this)) {

                // Call the method of StartAppRequests class to process App Startup Requests
                //startAppRequests.StartRequests();

                return "1";
            } else {
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("0")) {

                progressBar.setVisibility(View.GONE);

                // No Internet Connection
                Snackbar.make(rootView, "Tidak Ada Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Coba Lagi", new View.OnClickListener() {

                            // Handle the Retry Button Click
                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);

                                // Restart MyTask after 3 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        myTask = new MyTask();
                                        myTask.execute();
                                    }
                                }, 1500);
                            }
                        })
                        .show();

            }
            else {
                if(session){

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

    }

    public static boolean isNetworkAvailable(Activity activity) {

        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Network[] networks = connectivity.getAllNetworks();
            NetworkInfo networkInfo;

            for (Network mNetwork : networks) {

                networkInfo = connectivity.getNetworkInfo(mNetwork);

                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }

        }
        else {
            if (connectivity != null) {

                NetworkInfo[] info = connectivity.getAllNetworkInfo();

                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
