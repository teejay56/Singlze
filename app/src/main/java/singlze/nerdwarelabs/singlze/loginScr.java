package singlze.nerdwarelabs.singlze;

import android.content.Context;
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

public class loginScr extends AppCompatActivity {

    EditText passwordTxt;
    TextView loginHeader,loginSubHeader,passwordheader;
    Button cLoginBtn,cancelLoginNumber,forgotPassword;
    String status;
    RelativeLayout loginLoadingScr;
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        cLoginBtn = (Button)findViewById(R.id.cLoginBtn);
        forgotPassword = (Button)findViewById(R.id.forgotPassword);
        loginHeader = (TextView)findViewById(R.id.loginHeader);
        loginLoadingScr = (RelativeLayout)findViewById(R.id.loginloadingScr);
        passwordheader = (TextView)findViewById(R.id.passwordheader);
        loginSubHeader = (TextView)findViewById(R.id.loginSubHeader);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        cancelLoginNumber = (Button)findViewById(R.id.cancelLoginNumber);
        loginSubHeader.setText("Please enter your password for account linked with "+getIntent().getExtras().getString("CountryCode")+getIntent().getExtras().getString("Number")+", to login with your 'Singlze' account.");
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        loginHeader.setTypeface(tp);
        loginSubHeader.setTypeface(tp);
        passwordTxt.setTypeface(tp);
        cLoginBtn.setTypeface(tp);
        passwordheader.setTypeface(tp);
        cancelLoginNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.passwordFieldHolder));
                    return;
                }

               new loginAPICall().execute();

            }
        });

        cancelLoginNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginScr.this,verifyNumberScr.class);
                i.putExtra("ISRECOVERY","YES");
                i.putExtra("CountryCode",getIntent().getExtras().getString("CountryCode"));
                i.putExtra("Number",getIntent().getExtras().getString("Number"));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }


    class loginAPICall extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/login.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            loginLoadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    Intent i  = new Intent(loginScr.this,HomeScr.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(loginScr.this);
                    builder1.setMessage("Wrong login details, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else if(status.equalsIgnoreCase("2")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(loginScr.this);
                    builder1.setMessage("Wrong password, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(loginScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(loginScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("number",getIntent().getExtras().getString("CountryCode")+getIntent().getExtras().getString("Number")));
                    nameValuePairs.add(new BasicNameValuePair("pass",passwordTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("login_type","NUMBER"));
                    nameValuePairs.add(new BasicNameValuePair("device_type","ANDROID"));
                    nameValuePairs.add(new BasicNameValuePair("last_login",createddate));

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
                        try {
                            JSONObject data = jo.getJSONObject("result");
                            editor.putString("user_id", data.getString("user_id").toString());
                            editor.putString("user_name", data.getString("user_name").toString());
                            editor.putString("user_number", data.getString("user_number").toString());
                            editor.putString("is_verified", data.getString("is_verified").toString());
                            editor.putString("u_status", data.getString("u_status").toString());
                            editor.putString("u_pic2", data.getString("u_pic2").toString());
                            editor.putString("u_pic3", data.getString("u_pic3").toString());
                            editor.putString("u_pic4", data.getString("u_pic4").toString());
                            editor.putString("u_pic5", data.getString("u_pic5").toString());
                            editor.putString("u_tags", data.getString("u_tags").toString());
                            if(data.getString("u_about_me").length()==0){
                                editor.putString("u_about_me", "NA");
                            }else{
                                editor.putString("u_about_me", data.getString("u_about_me").toString());
                            }
                            editor.putString("u_singlze_id", data.getString("u_singlze_id").toString());
                            editor.putString("u_hometown", data.getString("u_hometown").toString());
                            editor.putString("created_on", data.getString("created_on").toString());
                            editor.putString("last_login", data.getString("last_login").toString());
                            editor.putString("device_type", data.getString("device_type").toString());
                            editor.putString("user_pic", data.getString("user_pic").toString());
                            editor.putString("user_dob", data.getString("user_dob").toString());
                            editor.putString("user_gender", data.getString("user_gender").toString());
                            editor.putString("login_type", data.getString("login_type").toString());
                            editor.putString("agency_id", data.getString("agency_id").toString());
                            editor.putString("is_streamer", data.getString("is_streamer").toString());
                            editor.putString("raw_data",data.toString());
                            editor.commit();
                        }catch (Exception r){}
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
