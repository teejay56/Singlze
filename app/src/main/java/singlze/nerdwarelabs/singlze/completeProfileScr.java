package singlze.nerdwarelabs.singlze;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.opencsv.CSVReader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Calendar.*;

public class completeProfileScr extends AppCompatActivity {
    TextView completeProHeader,completeProsubheader,nameheader,genderheader,dobheader,infoLbl,maleLbl,femaleLbl,basicdetailsLbl,cpasswordHeader;
    EditText fullNameTxt,dobTxt,cpasswordTxt;
    String thumburl = "";
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    Button confirmCompletionBtn,maleGenderBtn,femaleGenderBtn,selDOBbtn;
    ImageView maleRadio,femaleRadio;
    DatePicker dateSelectorDP;
    Button selectedDOBBtn,selectPictureBtn;
    RelativeLayout calendarHolder,completeloadingScr;
    TextView ctermsLbl;
    Button ctermsBtn;
    String filepath;
    TextView singlzeIDHeader,singlzeIDTxt;

    CircleImageView cprofile_image;
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    int MY_PERMISSIONS_REQUEST_READEXTERNAL=1;
    private static int CAMERA_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;
    String status,dobString;
    JSONArray jobjCountries;
    JSONObject snsObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            jobjCountries = new JSONArray(loadJSONFromAsset());
            String fetched = "";
        }catch (Exception r){
            String error = r.getLocalizedMessage();
        }

        status = "";
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        cprofile_image = (CircleImageView)findViewById(R.id.cprofile_image);
        completeloadingScr = (RelativeLayout)findViewById(R.id.completeloadingScr);
        singlzeIDTxt = (TextView)findViewById(R.id.singlzeIDTxt);
        cpasswordHeader = (TextView)findViewById(R.id.cpasswordHeader);
        cpasswordTxt = (EditText)findViewById(R.id.cpasswordTxt);
        singlzeIDHeader = (TextView)findViewById(R.id.singlzeIDHeader);
        selectPictureBtn = (Button)findViewById(R.id.selectPictureBtn);

        ctermsBtn = (Button)findViewById(R.id.ctermsBtn);
        ctermsLbl = (TextView)findViewById(R.id.ctermsLbl);
        calendarHolder = (RelativeLayout)findViewById(R.id.calendarHolder);
        dateSelectorDP = (DatePicker)findViewById(R.id.dateSelectorDP);
        selectedDOBBtn = (Button)findViewById(R.id.selectedDOBBtn);
        maleGenderBtn = (Button)findViewById(R.id.maleGenderBtn);
        femaleGenderBtn = (Button)findViewById(R.id.femaleGenderBtn);
        maleRadio = (ImageView)findViewById(R.id.maleRadio);
        femaleRadio = (ImageView)findViewById(R.id.femaleRadio);
        femaleLbl = (TextView)findViewById(R.id.femaleLbl);
        maleLbl = (TextView)findViewById(R.id.maleLbl);
        basicdetailsLbl = (TextView)findViewById(R.id.basicdetailsLbl);
        completeProHeader = (TextView)findViewById(R.id.completeProHeader);
        completeProsubheader = (TextView)findViewById(R.id.completeProsubheader);
        nameheader = (TextView)findViewById(R.id.nameheader);
        genderheader = (TextView)findViewById(R.id.genderheader);
        dobheader = (TextView)findViewById(R.id.dobheader);
        infoLbl = (TextView)findViewById(R.id.infoLbl);
        fullNameTxt = (EditText)findViewById(R.id.fullNameTxt);
        selDOBbtn = (Button)findViewById(R.id.selDOBbtn);
        dobTxt = (EditText)findViewById(R.id.dobTxt);
        confirmCompletionBtn = (Button)findViewById(R.id.confirmCompletionBtn);
        setupUI();
    }

    public Date validMaxDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println("Current Date " + dateFormat.format(date));

        // Convert Date to Calendar
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // Perform addition/subtraction
        c.add(Calendar.YEAR, -18);

        // Convert calendar back to Date
        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne;

    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }



    public void setupUI(){
        Typeface arialt = Typeface.createFromAsset(getAssets(),"arial.ttf");


        selectedDOBBtn.setTypeface(arialt);
        completeProHeader.setTypeface(arialt);
        completeProsubheader.setTypeface(arialt);
        nameheader.setTypeface(arialt);
        genderheader.setTypeface(arialt);
        ctermsLbl.setTypeface(arialt);
        dobheader.setTypeface(arialt);
        fullNameTxt.setTypeface(arialt);
        singlzeIDHeader.setTypeface(arialt);
        dobTxt.setTypeface(arialt);
        singlzeIDTxt.setTypeface(arialt);
        cpasswordTxt.setTypeface(arialt);
        cpasswordHeader.setTypeface(arialt);
        infoLbl.setTypeface(arialt);
        confirmCompletionBtn.setTypeface(arialt);
        femaleLbl.setTypeface(arialt);
        basicdetailsLbl.setTypeface(arialt);
        maleLbl.setTypeface(arialt);
        dateSelectorDP.setMaxDate(validMaxDate().getTime());
        ctermsLbl.setText("Privacy Policy | Terms & Conditions");
        if(getIntent().getStringExtra("ISSOCIAL").equalsIgnoreCase("YES")){
            try {
                snsObject = new JSONObject(getIntent().getStringExtra("DATA"));
                fullNameTxt.setText(snsObject.getString("NAME"));
                String propic = snsObject.getString("IMAGE");
                if(propic.equalsIgnoreCase("NA")==false){

                    Picasso.with(completeProfileScr.this)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(cprofile_image);
                }
                cpasswordHeader.setVisibility(View.GONE);
                RelativeLayout cpassholder = (RelativeLayout)findViewById(R.id.cpasswordHolder);
                cpassholder.setVisibility(View.GONE);
            }catch (Exception r){}
        }

        ctermsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        selectedDOBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = dateSelectorDP.getDayOfMonth()+"/"+dateSelectorDP.getMonth()+"/"+dateSelectorDP.getYear();
                dobString = dateSelectorDP.getMonth()+"/"+dateSelectorDP.getDayOfMonth()+"/"+dateSelectorDP.getYear();
                SimpleDateFormat dformatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateSel = dformatter.parse(dob);
                    dobTxt.setText(""+getDiffYears(dateSel,new Date())+" years old");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarHolder.setVisibility(View.GONE);

            }
        });
        maleGenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarHolder.requestFocus();
                maleRadio.setBackgroundResource(R.drawable.radioon);
                femaleRadio.setBackgroundResource(R.drawable.radiooff);
                maleLbl.setTextColor(getResources().getColor(R.color.primaryColor));
                femaleLbl.setTextColor(Color.parseColor("#000000"));
                maleGenderBtn.setText("1");
                femaleGenderBtn.setText("0");
            }
        });

        femaleGenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarHolder.requestFocus();
                femaleRadio.setBackgroundResource(R.drawable.radioon);
                maleRadio.setBackgroundResource(R.drawable.radiooff);
                femaleLbl.setTextColor(getResources().getColor(R.color.primaryColor));
                maleLbl.setTextColor(Color.parseColor("#000000"));
                femaleGenderBtn.setText("1");
                maleGenderBtn.setText("0");
            }
        });

        selDOBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarHolder.requestFocus();
                calendarHolder.setVisibility(View.VISIBLE);
            }
        });

        confirmCompletionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMsg = "";
                if(fullNameTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.nameHolder));
                   errorMsg = "Please tell us your full name first.";
                    return;
                }else if(maleGenderBtn.getText().toString().equalsIgnoreCase("0") && femaleGenderBtn.getText().toString().equalsIgnoreCase("0")){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.genderHolder));
                    errorMsg = "Please tell us your gender.";
                    return;
                }else if(dobTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.dobHolder));
                    errorMsg = "Please tell us your age.";
                    return;
                }else if(singlzeIDTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.singlzeIDHolder));
                    errorMsg = "Please set your singlze ID.";
                    return;
                }else if (singlzeIDTxt.getText().toString().contains(" ")) {
                    singlzeIDTxt.setError("No Spaces Allowed");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("White space are not allowed in ID.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }
                if(getIntent().getStringExtra("ISSOCIAL").equalsIgnoreCase("YES") && getIntent().getStringExtra("ISFB").equalsIgnoreCase("YES")){

                }else {
                    if (cpasswordTxt.getText().toString().length() == 0) {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .repeat(0)
                                .playOn(findViewById(R.id.cpasswordHolder));
                        errorMsg = "Please tell us your age.";
                        return;
                    }
                }
                status = "";
                new uploadPicture().execute();
            }
        });



        selectPictureBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(completeProfileScr.this)
                        .setTitle("Upload Image")
                        .setItems(R.array.SelectImage,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (i == 0) {
                                            try {
                                                int result = ContextCompat.checkSelfPermission(completeProfileScr.this, android.Manifest.permission.CAMERA);
                                                if (result == PackageManager.PERMISSION_GRANTED){


                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
// ******** code for crop image
                                                    startActivityForResult(intent, CAMERA_REQUEST);

                                                } else {
                                                    ActivityCompat.requestPermissions(completeProfileScr.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                                }
                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        if (i == 1) {
                                            int result = ContextCompat.checkSelfPermission(completeProfileScr.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                                            if (result == PackageManager.PERMISSION_GRANTED) {

                                                Intent i1 = new Intent(Intent.ACTION_PICK,
                                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                try {

                                                    startActivityForResult(i1, RESULT_LOAD_IMAGE);

                                                } catch (ActivityNotFoundException e) {
                                                    Toast.makeText(getApplicationContext(), "Crop not supported", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                ActivityCompat.requestPermissions(completeProfileScr.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READEXTERNAL);
                                            }
                                        }
                                    }
                                })
                        .show();

            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("CountryCodes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    class signupAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/signup.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            completeloadingScr.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            completeloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    Intent i = new Intent(completeProfileScr.this,HomeScr.class);
                    startActivity(i);
                    finish();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(completeProfileScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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

                    SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy");
                    String createddate = spf.format(new Date());
                    String country = getUserCountry(completeProfileScr.this);


                    if(TextUtils.isEmpty(country)){
                        country = "India";
                    }else{
                        country  = getFullCountryName(country);
                    }
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("singlze_id",singlzeIDTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("hometown",country));
                    if(getIntent().getStringExtra("ISSOCIAL").equalsIgnoreCase("YES")){
                        nameValuePairs.add(new BasicNameValuePair("number", snsObject.getString("ID")));
                    }else {
                        nameValuePairs.add(new BasicNameValuePair("number", getIntent().getExtras().getString("number")));
                    }
                    nameValuePairs.add(new BasicNameValuePair("pass",cpasswordTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("name",fullNameTxt.getText().toString()));
                    if(femaleGenderBtn.getText().toString().equalsIgnoreCase("1")) {
                        nameValuePairs.add(new BasicNameValuePair("gender", "FEMALE"));
                    }else{
                        nameValuePairs.add(new BasicNameValuePair("gender", "MALE"));
                    }
                    nameValuePairs.add(new BasicNameValuePair("dob",dobString));
                    nameValuePairs.add(new BasicNameValuePair("is_verified","YES"));
                    if(getIntent().getStringExtra("ISSOCIAL").equalsIgnoreCase("YES")){
                        if(getIntent().getStringExtra("ISFB").equalsIgnoreCase("YES")) {
                            nameValuePairs.add(new BasicNameValuePair("login_type", "FACEBOOK"));
                        }else{
                            nameValuePairs.add(new BasicNameValuePair("login_type", "GOOGLE"));
                        }
                    }else {
                        nameValuePairs.add(new BasicNameValuePair("login_type", "NUMBER"));
                    }
                    nameValuePairs.add(new BasicNameValuePair("user_pic",thumburl));
                    nameValuePairs.add(new BasicNameValuePair("device_type","ANDROID"));
                    nameValuePairs.add(new BasicNameValuePair("createddate",createddate));
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

    public String getFullCountryName(String countryCode){
        String fullName = "";
        for(int i = 0;i<jobjCountries.length();i++){
            try {
                String code = jobjCountries.getJSONObject(i).getString("code");
                if(code.equalsIgnoreCase(countryCode)){
                    fullName = jobjCountries.getJSONObject(i).getString("name");
                    break;
                }
            }catch (Exception r){}
        }
        return fullName;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
//            Gallery
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                //Do your work here
                String str = "Granted";
                Intent i1 = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {

                    startActivityForResult(i1, RESULT_LOAD_IMAGE);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Crop not supported", Toast.LENGTH_SHORT).show();
                }
//Perform operations here only which requires permission
            }else{
                String str = "Not Granted";
            }
        }else{
            //Camera
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                //Do your work here
                String str = "Granted";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
// ******** code for crop image
                startActivityForResult(intent, CAMERA_REQUEST);
//Perform operations here only which requires permission
            }else{
                String str = "Not Granted";
            }
        }
    }

    class uploadPicture extends AsyncTask<String, String, String> {


        //ProgressDialog dialog = new ProgressDialog(validate.this);
        @Override
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub

            return getResponse1("https://singlze.com/API/uploadmedia.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            completeloadingScr.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            completeloadingScr.setVisibility(View.GONE);
            try {
                if(status.equalsIgnoreCase("1")){
                    status = "0";
                    new signupAPI().execute();
                }else if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(completeProfileScr.this);
                    builder1.setMessage("Oops! Please check your connection and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
            catch(Exception e){
//                Toast.makeText(LoginScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                //  categories.clear();
                org.apache.http.entity.mime.MultipartEntity mpEntity = new org.apache.http.entity.mime.MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Bitmap bitmap = ((BitmapDrawable)cprofile_image.getDrawable()).getBitmap();
                    File file = getFileForBitmap(bitmap,getIntent().getExtras().getString("number")+".jpg");
                    mpEntity.addPart("image",new FileBody(file));
                httppost.setEntity(mpEntity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                Log.e("RESPONSE",responseString);

                JSONObject jo = new JSONObject(responseString);
                status = jo.getString("status");
                if(status.equalsIgnoreCase("1")) {
                    thumburl = jo.getString("link");
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

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(8);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    private File getFileForBitmap(Bitmap bmp,String filename){
        try {
            File f = new File(this.getCacheDir(), filename);
            f.createNewFile();

//Convert bitmap to byte array
            Bitmap bitmap = bmp;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        }catch (Exception r){
            return null;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            cprofile_image.setImageBitmap(photo);
        }else if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                cprofile_image.setImageBitmap(selectedImage);
            }catch (Exception r){}
        }
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
