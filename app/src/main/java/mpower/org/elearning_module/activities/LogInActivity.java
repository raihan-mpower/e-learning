package mpower.org.elearning_module.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import mpower.org.elearning_module.MainActivity;
import mpower.org.elearning_module.R;
import mpower.org.elearning_module.utils.AppConstants;
import mpower.org.elearning_module.utils.NetUtils;
import mpower.org.elearning_module.utils.UserCollection;
import mpower.org.elearning_module.utils.UserDataCollection;
import mpower.org.elearning_module.utils.UserType;

public class LogInActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    private String username = "", password = "";

    private UserCollection user;

    private RadioGroup userTypeGroup;
    private String userType=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // FIX: Initial back button from homescreen causes problem logging in
        UserCollection.getInstance().logOff("");


        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        IntentFilter authenticationDoneFilter = new IntentFilter(UserCollection.BROADCAST_ACTION_AUTHENTICATION_DONE);
        authenticationDoneFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(authenticationDoneReceiver, authenticationDoneFilter);

        IntentFilter authenticationNeededFilter = new IntentFilter(UserCollection.BROADCAST_ACTION_AUTHENTICATION_NEEDED);
        authenticationNeededFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(authenticationNeededReceiver, authenticationNeededFilter);

        user = UserCollection.getInstance();

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        usernameEditText.setFilters(new InputFilter[] { getReturnFilter(), getWhitespaceFilter() });
        passwordEditText.setFilters(new InputFilter[] { getReturnFilter(), getWhitespaceFilter() });
        userTypeGroup=findViewById(R.id.user_group);
        loginButton = findViewById(R.id.login_button);
    }

    public void checkLogIn(View view) {
        checkLogin();
    }






    private BroadcastReceiver authenticationDoneReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateData(null);
        }
    };

    private BroadcastReceiver authenticationNeededReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String logoutMessage = intent.getStringExtra("message");
            updateData(logoutMessage);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(authenticationDoneReceiver);
        unregisterReceiver(authenticationNeededReceiver);
    }






    private void updateData(String logoutMessage) {

        if (user.isLoggedin()) {
            processValidLogin();
        } else {
            processInvalidLogin(logoutMessage);
        }
    }

    private void checkLogin() {
        username = usernameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        if (!(username.length() > 0)) {
            Toast.makeText(this, "Please enter User ID", Toast.LENGTH_LONG).show();
            return;
        }

        if (!(password.length() > 0)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }



        try {
          //  user.checkLogin(username, password, this);
            user.getUserData().setUsername(username);
            processValidLogin();
        } catch (Exception e) {
            showAlertDialog("Login failed!", e.getMessage());
            e.printStackTrace();
        }

    }

    private void processInvalidLogin(String logoutMessage) {
        if (logoutMessage == null || "".equals(logoutMessage)) {
            logoutMessage = UserCollection.LOGOUT_MESSAGE_UNKNOWN;
        }
        showAlertDialog("Login failed!", "Possible causes:" + "\n\n" + logoutMessage);
        passwordEditText.setText("");
    }

    private void processValidLogin() {
        startNextActivity();
    }

    private void startNextActivity() {

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(AppConstants.USER_TYPE,UserType.DOT);
        startActivity(i);
        finish();
    }

    private UserType getUserType() {
        switch (userType){
            case "DOT":
                return UserType.DOT;
            case "Other":
                return UserType.OTHER;
            default:
              return  UserType.DOT;
        }
    }

    private void showAlertDialog(String title, String message) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle(title);
        adb.setMessage(message);

        adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        adb.show();
    }

    private InputFilter getReturnFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.getType((source.charAt(i))) == Character.CONTROL) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    private InputFilter getWhitespaceFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    public void checkLoginOnline() {
        new LoginTask().execute();
    }

    public void gotoSignUp(View view) {
        Intent intent=new Intent(this,UserTypeSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoForgotPassword(View view) {
    }

    /**
     * LoginTask - AsyncTask for logging in to server
     *
     * @author Mehdi Hasan <mhasan@Mpower-health.com>
     *
     */
    private class LoginTask extends AsyncTask<Void, Void, Void> {

        private String loginUrl;
        private int timeOut;
        private int loginStatus = 0;
        @SuppressWarnings("unused")
        private Exception loginE = null;
        private String loginResponse = "";
        private UserDataCollection onlineLd = null;

        private ProgressDialog pbarDialog;

        private void initPrefs() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
            loginUrl = prefs.getString(AppConstants.KEY_SERVER_URL, getString(R.string.default_server_url));
            if(!loginUrl.endsWith("/")) loginUrl += "/";
            loginUrl += username;
            loginUrl += AppConstants.URL_PART_LOGIN;
            loginUrl += "?";
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            if(UserCollection.getInstance().getUserData().getUsername() != null)
                params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            String paramString = URLEncodedUtils.format(params, "utf-8");
            loginUrl+= paramString;
            Log.d(this.getClass().getSimpleName(), "Login URL = " + loginUrl);
            //TODO for testing Sabbir http://192.168.19.87:8001/accounts/login/
            timeOut = AppConstants.CONNECTION_TIMEOUT;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pbarDialog = new ProgressDialog(LogInActivity.this);
            pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pbarDialog.setTitle(getString(R.string.please_wait));
            pbarDialog.setMessage("Logging in...");
            pbarDialog.setCancelable(false);
            pbarDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if ("".equals(username) || "".equals(password)) {
                return null;
            }

            NetUtils.clearAllCredentials();
            String enCryptedPassword=NetUtils.getSHA512(password);
            //Log.d("Password",enCryptedPassword);
            NetUtils.addCredentials(username,enCryptedPassword);

            initPrefs();
            login();

            return null;
        }

        private void login() {
            HttpResponse response;
            try {
                response = NetUtils.stringResponseGet(loginUrl, NetUtils.getHttpContext(), NetUtils.createHttpClient(timeOut));
                loginStatus = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();

                Log.d("Login Status", "code = " + loginStatus);
                if ((entity != null) && (loginStatus == 200)) {
                    onlineLd = new UserDataCollection();

                }

            } catch (Exception e) {
                loginE = e;
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {

            if (pbarDialog != null && pbarDialog.isShowing()) {
                pbarDialog.dismiss();
            }

            if ((loginStatus == 200) && (onlineLd != null)) {

                onlineLd.setUsername(username);
                onlineLd.setPassword(password);

                UserCollection.getInstance().setLoginResult(true, onlineLd, null);
            } else {
                if (loginStatus != 401) {
                    // There was an error checking login online, but we are not
                    // explicitly denied, let's proceed with offline login if
                    // possible

                    try {
                        boolean offlineUserDataAvailable = UserCollection.getInstance().offlineUserDataAvailable();

                        if (offlineUserDataAvailable) {
                            if (UserCollection.getInstance().checkOfflineLogin(username, password)) {
                                UserDataCollection offlineLd = UserCollection.getInstance().extractOfflineLoginData();
                                UserCollection.getInstance().setLoginResult(true, offlineLd, null);
                            } else {
                                // Offline login username/password mismatch
                                UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_ID_MISSMATCH);
                            }
                        } else {
                            // Offline login data not available
                            UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_NETWORK_SERVER);
                        }
                    } catch (Exception e) {
                        UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_INTERNAL_ERROR);
                    }

                } else {
                    // Login failed for sure, server returned 401
                    UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_ID_MISSMATCH);
                }
            }
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void,Void,Void>{
        private ProgressDialog progressDialog;
        private String loginUrl;
        private int timeOut;
        int loginStatus;
        private UserDataCollection onlineUserData;
        private void initPrefs() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LogInActivity.this);
            loginUrl = prefs.getString(AppConstants.KEY_SERVER_URL, getString(R.string.default_server_url));
            if(!loginUrl.endsWith("/")) loginUrl += "/";
            loginUrl += username;
            loginUrl += AppConstants.URL_PART_LOGIN;
            loginUrl += "?";
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            if(UserCollection.getInstance().getUserData().getUsername() != null)
                params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            String paramString = URLEncodedUtils.format(params, "utf-8");
            loginUrl+= paramString;
            Log.d(this.getClass().getSimpleName(), "Login URL = " + loginUrl);
            //TODO for testing Sabbir http://192.168.19.87:8001/accounts/login/
            timeOut = AppConstants.CONNECTION_TIMEOUT;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LogInActivity.this);
            progressDialog.setTitle("Logging In");
            progressDialog.setMessage("Loading....Please Wait");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            initPrefs();
            try {
                login();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void login() throws Exception {
            URL url=new URL(loginUrl);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            loginStatus=httpURLConnection.getResponseCode();

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (loginStatus==200){
                onlineUserData=new UserDataCollection();
                onlineUserData.setUsername(username);
                onlineUserData.setPassword(password);
                UserCollection.getInstance().setLoginResult(true, onlineUserData, null);
            }else {
                if (loginStatus != 401) {
                    // There was an error checking login online, but we are not
                    // explicitly denied, let's proceed with offline login if
                    // possible

                    try {
                        boolean offlineUserDataAvailable = UserCollection.getInstance().offlineUserDataAvailable();

                        if (offlineUserDataAvailable) {
                            if (UserCollection.getInstance().checkOfflineLogin(username, password)) {
                                UserDataCollection offlineLd = UserCollection.getInstance().extractOfflineLoginData();
                                UserCollection.getInstance().setLoginResult(true, offlineLd, null);
                            } else {
                                // Offline login username/password mismatch
                                UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_ID_MISSMATCH);
                            }
                        } else {
                            // Offline login data not available
                            UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_NETWORK_SERVER);
                        }
                    } catch (Exception e) {
                        UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_INTERNAL_ERROR);
                    }

                } else {
                    // Login failed for sure, server returned 401
                    UserCollection.getInstance().setLoginResult(false, null, UserCollection.LOGOUT_MESSAGE_ID_MISSMATCH);
                }
            }
        }
    }
}