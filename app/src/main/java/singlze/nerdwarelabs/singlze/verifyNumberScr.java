package singlze.nerdwarelabs.singlze;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class verifyNumberScr extends AppCompatActivity {
    TextView verifynumberHeader,verifynumbersubheader,vseparator,verifyingLbl;
    Button changeNumberBtn,resendCodeBtn,confirmVerifyBtn,cancelVerifyNumber;
    EditText otpcode;
    RelativeLayout loadingScr;
    String numberVal,status,requestId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        numberVal = getIntent().getExtras().getString("CountryCode")+getIntent().getExtras().getString("Number");
        loadingScr = (RelativeLayout)findViewById(R.id.loadingScr);
        verifyingLbl = (TextView)findViewById(R.id.verifyingLbl);
        verifynumberHeader = (TextView)findViewById(R.id.verifynumberHeader);
        verifynumbersubheader = (TextView)findViewById(R.id.verifynumbersubheader);
        changeNumberBtn = (Button)findViewById(R.id.changeNumberBtn);
        cancelVerifyNumber = (Button)findViewById(R.id.cancelVerifyNumber);

        resendCodeBtn = (Button)findViewById(R.id.resendCodeBtn);
        confirmVerifyBtn = (Button)findViewById(R.id.confirmVerifyBtn);
        otpcode = (EditText)findViewById(R.id.otpcode);

        otpcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(otpcode.getText().toString().length()==4){
                   String codeEntered = otpcode.getText().toString();
                       status = "0";
                       new verifynexmoOTP().execute();
               }
            }
        });
        setupUI();
    }


    class callNexmoForSMS extends AsyncTask<String, String, String> {


        //ProgressDialog dialog = new ProgressDialog(validate.this);
        @Override
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub

            return getResponse1("https://api.nexmo.com/verify/json");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loadingScr.setVisibility(View.VISIBLE);
            verifyingLbl.setText("Sending OTP...");
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            loadingScr.setVisibility(View.GONE);


            try {
                if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(verifyNumberScr.this);
                    builder1.setMessage("Something went wrong, check your number and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Oops! We can't find the account associated with this Email/Username. Please check your Email/Username.
                    //Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }else{
                    verifynumbersubheader.setText("We've sent you an OTP on your number "+numberVal+", please enter your OTP...");
                    loadingScr.setVisibility(View.GONE);
                    verifyingLbl.setText("Verifying number...");
                }
            }
            catch(Exception e){

            }
        }

        private void showMsg1(String string) {
            // TODO Auto-generated method stub
//
        }



        private String getResponse1(String url) {
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);

                try {
                    String number = numberVal.replaceAll("[+]","");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("api_secret","hjZ7az3pSSo62hFZ"));
                    nameValuePairs.add(new BasicNameValuePair("api_key", "149e4c66"));
                    nameValuePairs.add(new BasicNameValuePair("number", number));
                    nameValuePairs.add(new BasicNameValuePair("brand", "Singlze"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("STRING GET:" + responseString);
                    //Toast.makeText(Login.this, "You are succefuly login", Toast.LENGTH_SHORT).show();

                    String jsonString = responseString.toString();
                    JSONObject jo = new JSONObject(jsonString);
                    String nstatus = (String) jo.get("status");
                    System.out.println("success:::" + nstatus);
                    if(nstatus.equalsIgnoreCase("0")){
                        status = "1";
                        requestId = jo.getString("request_id");
                    }
                    //  JSONObject data  = jo.getJSONObject("data");


                    // System.out.println("hello data get:::"+data+":::");

                    /*id=data.getString("user_id");
                    System.out.println("iD get:::"+id);*/




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


    class verifynexmoOTP extends AsyncTask<String, String, String> {


        //ProgressDialog dialog = new ProgressDialog(validate.this);
        @Override
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub

            return getResponse1("https://api.nexmo.com/verify/check/json");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            verifyingLbl.setText("Verifying OTP...");
            loadingScr.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            loadingScr.setVisibility(View.GONE);
            try {
                if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(verifyNumberScr.this);
                    builder1.setMessage("Please check OTP again and enter correct OTP...");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Oops! We can't find the account associated with this Email/Username. Please check your Email/Username.
                    //Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();

                }else{
                   if(getIntent().getExtras().getString("ISRECOVERY").equalsIgnoreCase("NO")) {
                       Intent i = new Intent(verifyNumberScr.this, completeProfileScr.class);
                       i.putExtra("number", numberVal);
                       i.putExtra("ISSOCIAL","NO");
                       i.putExtra("ISEDITING","NO");
                       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(i);
                   }else{
                       Intent i = new Intent(verifyNumberScr.this, ResetPasswordScr.class);
                       i.putExtra("number", numberVal);
                       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(i);
                   }
                }
            }
            catch(Exception e){

            }
        }

        private void showMsg1(String string) {
            // TODO Auto-generated method stub
//
        }



        private String getResponse1(String url) {
            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);

                try {
                    String codeEntered = otpcode.getText().toString();
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("api_secret","hjZ7az3pSSo62hFZ"));
                    nameValuePairs.add(new BasicNameValuePair("api_key", "149e4c66"));
                    nameValuePairs.add(new BasicNameValuePair("code", codeEntered));
                    nameValuePairs.add(new BasicNameValuePair("request_id", requestId));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("STRING GET:" + responseString);
                    //Toast.makeText(Login.this, "You are succefuly login", Toast.LENGTH_SHORT).show();

                    String jsonString = responseString.toString();
                    JSONObject jo = new JSONObject(jsonString);
                    String nstatus = (String) jo.get("status");
                    System.out.println("success:::" + nstatus);
                    if(nstatus.equalsIgnoreCase("0")){
                        status = "1";
                        requestId = jo.getString("request_id");
                    }
                    //  JSONObject data  = jo.getJSONObject("data");


                    // System.out.println("hello data get:::"+data+":::");

                    /*id=data.getString("user_id");
                    System.out.println("iD get:::"+id);*/




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



    public void setupUI(){
        Typeface arialt = Typeface.createFromAsset(getAssets(),"arial.ttf");
        verifynumberHeader.setTypeface(arialt);

        verifynumbersubheader.setText("Sending OTP to your number "+numberVal+"...");
        otpcode.setTypeface(arialt);
        verifynumbersubheader.setTypeface(arialt);
        changeNumberBtn.setTypeface(arialt);

        resendCodeBtn.setTypeface(arialt);
        confirmVerifyBtn.setTypeface(arialt);
        verifyingLbl.setTypeface(arialt);
        changeNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelVerifyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new callNexmoForSMS().execute();
            }
        });
        new callNexmoForSMS().execute();
    }
}
