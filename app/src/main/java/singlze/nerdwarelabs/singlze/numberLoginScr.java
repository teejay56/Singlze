package singlze.nerdwarelabs.singlze;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class numberLoginScr extends AppCompatActivity {
    TextView loginnumberHeader,loginnumberSubheader,cellnumberheader,countryCodeBtn,numberTxt,confirmLoginBtn,num_verifyingLbl;
    Button cancelLoginNumber;
    RelativeLayout numLoadingScr;
    String status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_login_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        numLoadingScr = (RelativeLayout)findViewById(R.id.numloadingScr);
        num_verifyingLbl = (TextView)findViewById(R.id.num_verifyingLbl);
        confirmLoginBtn = (Button)findViewById(R.id.confirmLoginBtn);
        numberTxt = (TextView)findViewById(R.id.numberTxt);
        countryCodeBtn = (TextView)findViewById(R.id.countryCodeBtn);
        loginnumberSubheader = (TextView)findViewById(R.id.loginnumbersubheader);
        loginnumberHeader = (TextView)findViewById(R.id.loginnumberHeader);
        cellnumberheader = (TextView)findViewById(R.id.cellnumberheader);
        cancelLoginNumber = (Button)findViewById(R.id.cancelLoginNumber);
        setupUi();

        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        countryCodeBtn.setText(getCountryDialCode());
        if(countryCodeBtn.getText().toString().length()==0){
            countryCodeBtn.setText("+91");
        }else{
            countryCodeBtn.setText("+"+countryCodeBtn.getText().toString());
        }
        countryCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(numberLoginScr.this,selectCountryScr.class);
                i.putExtra("ISNAME","NO");
                startActivityForResult(i,999);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==999){
            String checker = data.getData().toString();
            if(checker.equalsIgnoreCase("CANCEL")==false) {
                countryCodeBtn.setText(data.getData().toString());
            }
        }
    }


    public String getCountryDialCode(){
        String contryId = null;
        String contryDialCode = "";
        TelephonyManager telephonyMngr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode=this.getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrContryCode.length; i++){
            String[] arrDial = arrContryCode[i].split(",");
            if(arrDial[1].trim().equals(contryId.trim())){
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }

    public void setupUi(){
        Typeface arialt = Typeface.createFromAsset(getAssets(),"arial.ttf");
        loginnumberHeader.setTypeface(arialt);
        loginnumberSubheader.setTypeface(arialt);
        cellnumberheader.setTypeface(arialt);
        countryCodeBtn.setTypeface(arialt);
        numberTxt.setTypeface(arialt);
        confirmLoginBtn.setTypeface(arialt);
        num_verifyingLbl.setTypeface(arialt);
        cancelLoginNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberTxt.getText().toString().length()>0){

                    numLoadingScr.setVisibility(View.VISIBLE);
                    new checkLogin().execute();
                }else{
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.txtFieldHolder));
                }

            }
        });

    }

    class checkLogin extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/check_login.php");


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
            numLoadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                  //Ask for password
                    Intent i = new Intent(numberLoginScr.this,loginScr.class);
                    i.putExtra("CountryCode",countryCodeBtn.getText().toString());
                    i.putExtra("Number",numberTxt.getText().toString());
                    startActivity(i);
                }else if (status.equalsIgnoreCase("0")) {
                  //New Sign up
                    Intent i = new Intent(numberLoginScr.this,verifyNumberScr.class);
                    i.putExtra("ISRECOVERY","NO");
                    i.putExtra("CountryCode",countryCodeBtn.getText().toString());
                    i.putExtra("Number",numberTxt.getText().toString());
                    startActivity(i);
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(numberLoginScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(numberLoginScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("number",countryCodeBtn.getText().toString()+numberTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("login_type","NUMBER"));

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
                    //  JSONObject data  = jo.getJSONObject("data");


                    // System.out.println("hello data get:::"+data+":::");

                    /*id=data.getString("user_id");
                    System.out.println("iD get:::"+id);*/
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
