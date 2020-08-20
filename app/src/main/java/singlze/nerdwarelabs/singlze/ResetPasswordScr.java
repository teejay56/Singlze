package singlze.nerdwarelabs.singlze;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResetPasswordScr extends AppCompatActivity {
    Button cancelresetbtn,resetUpdatePassBtn;
    TextView resetHeader,resetSubHeader,resetpasswordheader,resetConfirmheader;
    EditText resetpasswordTxt,resetconfirmTxt;
    String status;
    RelativeLayout resetloadingScr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        resetloadingScr = (RelativeLayout)findViewById(R.id.resetloadingScr);
        cancelresetbtn = (Button)findViewById(R.id.cancelresetbtn);
        resetHeader = (TextView)findViewById(R.id.resetHeader);
        resetSubHeader = (TextView)findViewById(R.id.resetSubHeader);
        resetpasswordheader = (TextView)findViewById(R.id.resetpasswordheader);
        resetConfirmheader = (TextView)findViewById(R.id.resetConfirmheader);
        resetpasswordTxt = (EditText)findViewById(R.id.resetpasswordTxt);
        resetconfirmTxt = (EditText)findViewById(R.id.resetconfirmTxt);
        resetUpdatePassBtn = (Button)findViewById(R.id.resetUpdatePassBtn);

        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        resetHeader.setTypeface(tp);
        resetSubHeader.setTypeface(tp);
        resetpasswordheader.setTypeface(tp);
        resetConfirmheader.setTypeface(tp);
        resetUpdatePassBtn.setTypeface(tp);
        resetconfirmTxt.setTypeface(tp);
        resetpasswordTxt.setTypeface(tp);
        cancelresetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPasswordScr.this,numberLoginScr.class);
                startActivity(i);
                finish();
            }
        });

        resetUpdatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resetpasswordTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.resetpasswordFieldHolder));
                    return;
                }else if(resetconfirmTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.resetpasswordConfirmHolder));
                    return;
                }
                new updatePasswordAPI().execute();
            }
        });

    }

    class updatePasswordAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/update_password.php");
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            resetloadingScr.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            resetloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    new AlertDialog.Builder(ResetPasswordScr.this)
                            .setTitle("Password Updated")
                            .setMessage("Please re-login to your 'Singlze' account with your new password.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i = new Intent(ResetPasswordScr.this,numberLoginScr.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();



                }else if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(ResetPasswordScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("number",getIntent().getExtras().getString("CountryCode")+getIntent().getExtras().getString("Number")));
                    nameValuePairs.add(new BasicNameValuePair("user_pass",resetpasswordTxt.getText().toString()));

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
