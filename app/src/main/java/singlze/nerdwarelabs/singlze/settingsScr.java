package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class settingsScr extends AppCompatActivity {
    Button settingBackBtn,settDeleteAccBtn;
    TextView settingHeader,profileSettingsHeader,settChangePassLbl,settDelAccLbl,appSettingsLbl,settPrivacyPolicy,settFeedback,settAboutLbl,settRateUsLbl;
    TextView versionLbl;
    RelativeLayout nChangePasswordHolder;
    Button cancelChangePassBtn,updatepasswordBtn,changePassStartBtn;
    EditText currentPassTxt,newPasswordTxt,confirmPassTxt;
    String status = "";
    RelativeLayout settLoadingScr;
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        versionLbl = (TextView)findViewById(R.id.versionLbl);
        currentPassTxt = (EditText)findViewById(R.id.currentPassTxt);
        newPasswordTxt = (EditText)findViewById(R.id.newPasswordTxt);
        confirmPassTxt = (EditText)findViewById(R.id.confirmPassTxt);
        settDeleteAccBtn = (Button)findViewById(R.id.settDeleteAccBtn);
        settLoadingScr = (RelativeLayout)findViewById(R.id.settloadingScr);
        settingHeader = (TextView)findViewById(R.id.settingHeader);
        updatepasswordBtn = (Button)findViewById(R.id.updatepasswordBtn);
        settingBackBtn = (Button)findViewById(R.id.settingBackBtn);
        cancelChangePassBtn = (Button)findViewById(R.id.cancelChangePassBtn);
        nChangePasswordHolder = (RelativeLayout)findViewById(R.id.nChangePasswordHolder);
        profileSettingsHeader = (TextView)findViewById(R.id.profileSettingsHeader);
        settChangePassLbl = (TextView)findViewById(R.id.settChangePassLbl);
        settDelAccLbl = (TextView)findViewById(R.id.settDelAccLbl);
        appSettingsLbl = (TextView)findViewById(R.id.appSettingsLbl);
        settPrivacyPolicy = (TextView)findViewById(R.id.settPrivacyPolicy);
        settFeedback = (TextView)findViewById(R.id.settFeedback);
        settAboutLbl = (TextView)findViewById(R.id.settAboutLbl);
        settRateUsLbl = (TextView)findViewById(R.id.settRateUsLbl);
        changePassStartBtn = (Button)findViewById(R.id.changePassStartBtn);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        appSettingsLbl.setTypeface(tp);
        versionLbl.setTypeface(tp);
        settRateUsLbl.setTypeface(tp);
        settAboutLbl.setTypeface(tp);
        settFeedback.setTypeface(tp);
        settPrivacyPolicy.setTypeface(tp);
        settingHeader.setTypeface(tp);
        settDelAccLbl.setTypeface(tp);
        settChangePassLbl.setTypeface(tp);
        profileSettingsHeader.setTypeface(tp);

        settDeleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(settingsScr.this)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure, you want to delete your `Singlze` account. You won't be able to recover your diamonds or other perks associated with this account.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                new deleteAccAPI().execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        updatepasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.currentPassHolder));
                    return;
                }else if(newPasswordTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.newpasswordHolder));
                    return;
                }else if(confirmPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.confirmPassHolder));
                    return;
                }else if(confirmPassTxt.getText().toString().equalsIgnoreCase(newPasswordTxt.getText().toString())==false){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.newpasswordHolder));
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.confirmPassHolder));
                    return;
                }
                new changePassAPI().execute();

            }
        });

        changePassStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nChangePasswordHolder.setVisibility(View.VISIBLE);
            }
        });

        settingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nChangePasswordHolder.setVisibility(View.GONE);
            }
        });

    }

    class deleteAccAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/del_acc.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            settLoadingScr.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            settLoadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password

                    Intent i  = new Intent(settingsScr.this,accountScr.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(settingsScr.this);
                    builder1.setMessage("Wrong login details, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(settingsScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(settingsScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

            }
        }

        private void showMsg1(String string) {
            // TODO Auto-generated method stub
//
        }

        private String getResponse1(String url) {
            try
            {
                status = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                //  categories.clear();
                try {
                    SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
                    String createddate = spf.format(new Date());
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("user_id",sharedpreferences.getString("user_id","")));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("STRING GET:" + responseString);
                    //Toast.makeText(Login.this, "You are succefuly login", Toast.LENGTH_SHORT).show();

                    String jsonString = responseString.toString();
                    JSONObject jo = new JSONObject(jsonString);
                    status = (String) jo.get("status");
                    System.out.println("success:::" + status);

                    if (status.equalsIgnoreCase("1")) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.apply();
                    }else{

                    }
                }
                catch (JSONException e) {
                    Log.e("hii..tt.", e.getMessage());
                    e.printStackTrace();
                }
            }
            catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }
    }



    class changePassAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/change_pass.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            settLoadingScr.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            settLoadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    new AlertDialog.Builder(settingsScr.this)
                            .setTitle("Password Updated")
                            .setMessage("Your password is updated. Please re-login with your new password.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    Intent i  = new Intent(settingsScr.this,accountScr.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(settingsScr.this);
                    builder1.setMessage("Wrong login details, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(settingsScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(settingsScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

            }
        }

        private void showMsg1(String string) {
            // TODO Auto-generated method stub
//
        }

        private String getResponse1(String url) {
            try
            {
                status = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                //  categories.clear();
                try {
                    SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
                    String createddate = spf.format(new Date());
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("user_id",sharedpreferences.getString("user_id","")));
                    nameValuePairs.add(new BasicNameValuePair("n_pass",newPasswordTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("user_pass",currentPassTxt.getText().toString()));



                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("STRING GET:" + responseString);
                    //Toast.makeText(Login.this, "You are succefuly login", Toast.LENGTH_SHORT).show();

                    String jsonString = responseString.toString();
                    JSONObject jo = new JSONObject(jsonString);
                    status = (String) jo.get("status");
                    System.out.println("success:::" + status);

                    if (status.equalsIgnoreCase("1")) {

                    }else{

                    }
                }
                catch (JSONException e) {
                    Log.e("hii..tt.", e.getMessage());
                    e.printStackTrace();
                }
            }
            catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }
    }


}
