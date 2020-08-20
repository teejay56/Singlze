package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class differUserProfile extends AppCompatActivity {
    ViewPager differimgPager;
    TextView differimageCurrCountLbl,differuNameLbl,differuSinglzeIdLbl,differuStatusLbl,differgoLiveLbl,differdiamondCountLbl,differchatLbl,differfollowersCounterLbl,differfollowerStaticLbl;
    TextView differfollowingCounterLbl,differfollowingStaticLbl,differlikesCounterLbl,differlikesStaticLbl,differrecentFollowersLbl,differbuyDiamondsLbl,differtagsValLbl,differaboutMeHeaderLbl;
    TextView differabouteMeTxtLbl;
    Button differproOptionsBtn,differproBackBtn;
    Button differgoliveBtnpro,differfollowingProBtn,differfollowersProBtn,differproBuyDiamondsBtn;
    RelativeLayout differprofileloadingScr;
    String status = "";
    CircleImageView differucircularImg;
    TextView differcountryHometown;
    TextView differhometownLbl,differgenderageLbl;
    ImagePagerAdapter differimgPagerAdap;
    ImageView followbigImg;
    int countImages = 0;
    JSONArray differfollowersArr,differfollowingArr;
    JSONObject differProObject;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differ_user_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        followbigImg = (ImageView)findViewById(R.id.followbigImg);
        differucircularImg = (CircleImageView)findViewById(R.id.differucircularImg);
        differgenderageLbl = (TextView)findViewById(R.id.differgenderageLbl);
        differgoLiveLbl = (TextView)findViewById(R.id.differgoLiveLbl);
        differfollowingProBtn = (Button)findViewById(R.id.differfollowingProBtn);
        differfollowersProBtn = (Button)findViewById(R.id.differfollowersProBtn);
        differhometownLbl = (TextView)findViewById(R.id.differhometownLbl);
        differcountryHometown = (TextView)findViewById(R.id.differcountryHometown);
        differgoliveBtnpro = (Button)findViewById(R.id.differgoliveBtnpro);
        differproBuyDiamondsBtn = (Button)findViewById(R.id.differproBuyDiamondsBtn);
        differprofileloadingScr = (RelativeLayout)findViewById(R.id.differprofileloadingScr);

        differimageCurrCountLbl = (TextView)findViewById(R.id.differimageCurrCountLbl);
        differfollowersCounterLbl = (TextView)findViewById(R.id.differfollowersCounterLbl);
        differrecentFollowersLbl = (TextView)findViewById(R.id.differrecentFollowersLbl);
        differfollowerStaticLbl = (TextView)findViewById(R.id.differfollowerStaticLbl);
        differlikesStaticLbl = (TextView)findViewById(R.id.differlikesStaticLbl);


        differproBackBtn = (Button)findViewById(R.id.differproBackBtn);


        differproOptionsBtn = (Button)findViewById(R.id.differproOptionsBtn);
        differtagsValLbl = (TextView)findViewById(R.id.differtagsValLbl);
        differlikesCounterLbl = (TextView)findViewById(R.id.differlikesCounterLbl);
        differaboutMeHeaderLbl = (TextView)findViewById(R.id.differaboutMeHeaderLbl);
        differbuyDiamondsLbl = (TextView)findViewById(R.id.differbuyDiamondsLbl);
        differabouteMeTxtLbl = (TextView)findViewById(R.id.differabouteMeTxtLbl);
        differuNameLbl = (TextView)findViewById(R.id.differuNameLbl);
        differfollowingStaticLbl = (TextView)findViewById(R.id.differfollowingStaticLbl);
        differfollowingCounterLbl = (TextView)findViewById(R.id.differfollowingCounterLbl);
        differchatLbl = (TextView)findViewById(R.id.differchatLbl);
        differuStatusLbl = (TextView)findViewById(R.id.differuStatusLbl);
        differuSinglzeIdLbl = (TextView)findViewById(R.id.differuSinglzeIdLbl);
        differdiamondCountLbl = (TextView)findViewById(R.id.differdiamondCountLbl);

        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");

        differuNameLbl.setTypeface(tp);
        differabouteMeTxtLbl.setTypeface(tp);
        differtagsValLbl.setTypeface(tp);
        differaboutMeHeaderLbl.setTypeface(tp);
        differgenderageLbl.setTypeface(tp);
        differfollowingStaticLbl.setTypeface(tp);
        differbuyDiamondsLbl.setTypeface(tp);
        differrecentFollowersLbl.setTypeface(tp);
        differfollowingCounterLbl.setTypeface(tp);
        differfollowerStaticLbl.setTypeface(tp);
        differfollowersCounterLbl.setTypeface(tp);
        differgoLiveLbl.setTypeface(tp);
        differchatLbl.setTypeface(tp);
        differuStatusLbl.setTypeface(tp);
        differdiamondCountLbl.setTypeface(tp);
        differhometownLbl.setTypeface(tp);
        differcountryHometown.setTypeface(tp);
        differuSinglzeIdLbl.setTypeface(tp);
        differimageCurrCountLbl.setTypeface(tp);

        differfollowingProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(differfollowersArr.length()==0){
                    return;
                }
                Intent i = new Intent(differUserProfile.this,followesScr.class);
                i.putExtra("FOLLOWERS",differfollowersArr.toString());
                i.putExtra("FOLLOWING",differfollowersArr.toString());
                startActivity(i);
            }
        });

        differfollowersProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(differfollowersArr.length()==0){
                    return;
                }
                Intent i = new Intent(differUserProfile.this,followesScr.class);
                i.putExtra("FOLLOWERS",differfollowersArr.toString());
                i.putExtra("FOLLOWING",differfollowersArr.toString());
                startActivity(i);
            }
        });

        differproBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        differgoliveBtnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Following API Call

            }
        });
        differimgPager = (ViewPager)findViewById(R.id.differimagePager);
        differimgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position==0){
                    differimageCurrCountLbl.setText("Image 1 of "+countImages);
                }else if(position==1){
                    differimageCurrCountLbl.setText("Image 2 of "+countImages);
                }else if(position==2){
                    differimageCurrCountLbl.setText("Image 3 of "+countImages);
                }else if(position==3){
                    differimageCurrCountLbl.setText("Image 4 of "+countImages);
                }else if(position==4){
                    differimageCurrCountLbl.setText("Image 5 of "+countImages);
                }
            }
        });

        try {
            differProObject = new JSONObject(getIntent().getStringExtra("DATA"));
            boolean isexist = isfollowing(differProObject.getString("user_id"));
            if(isexist==true){
                followbigImg.setImageResource(R.drawable.unfollowbig);
                differgoLiveLbl.setText("Unfollow");
            }else{
                followbigImg.setImageResource(R.drawable.greenbackbtn);
                differgoLiveLbl.setText("Follow");
            }
        }catch (Exception r){
            differproBackBtn.performClick();
            return;
        }

        differgoliveBtnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = "";
                if(differgoLiveLbl.getText().toString().equalsIgnoreCase("Follow")){
                    followbigImg.setImageResource(R.drawable.unfollowbig);
                    differgoLiveLbl.setText("Unfollow");
                    state = "YES";
                }else{
                    followbigImg.setImageResource(R.drawable.greenbackbtn);
                    differgoLiveLbl.setText("Follow");
                    state = "NO";
                }
                try {
                    status = "";
                    new followUserAPI().execute(state, differProObject.getString("user_id"));
                }catch (Exception r){}
            }
        });

        status = "";
        new getFollowersData().execute();
    }

    public boolean isfollowing(String u_id){
        boolean isexist = false;
        try{
            JSONArray ffarr = new JSONArray(sharedpreferences.getString("following",""));
            for(int i = 0;i<ffarr.length();i++){
                String ffid = ffarr.getJSONObject(i).getString("user_id");
                if(ffid.equalsIgnoreCase(u_id)){
                    isexist = true;
                    break;
                }
            }
            return isexist;
        }catch (Exception r){
            return false;
        }
    }


    class followUserAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String state = "";
        String followingid = "";
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            state = params[0];
            followingid = params[1];
            return getResponse1("https://singlze.com/API/follow_u.php");


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

            try {
                if(status.length()==0){
                    if(state.equalsIgnoreCase("NO")){
                        followbigImg.setImageResource(R.drawable.unfollowbig);
                        differgoLiveLbl.setText("Unfollow");

                    }else{
                        followbigImg.setImageResource(R.drawable.greenbackbtn);
                        differgoLiveLbl.setText("Follow");

                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success

                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(differUserProfile.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("followed_by",sharedpreferences.getString("user_id","")));
                    nameValuePairs.add(new BasicNameValuePair("following", followingid));
                    nameValuePairs.add(new BasicNameValuePair("state",state));

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


    class getFollowersData extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/followers_data.php");
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            differprofileloadingScr.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            differprofileloadingScr.setVisibility(View.GONE);

            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    differfollowingCounterLbl.setText(differfollowingArr.length()+"");
                    differfollowersCounterLbl.setText(differfollowersArr.length()+"");
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(differUserProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(differUserProfile.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("user_id",differProObject.getString("user_id")));

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
                        differfollowersArr = jo.getJSONArray("followers");
                        differfollowingArr = jo.getJSONArray("following");
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


    @Override
    public void onResume(){
        super.onResume();
        try {
            String propic = differProObject.getString("user_pic");
            if (propic.equalsIgnoreCase("NA") == false) {
                propic = "https://singlze.com/" + propic;
                Picasso.with(this)
                        .load(propic)
                        .placeholder(R.drawable.defpic)
                        .into(differucircularImg);

            }
            try {
                Date date1 = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String dob = differProObject.getString("user_dob");
                Date date2 = null;
                date2 = dateFormat.parse(dob);
                int userAge = (int) ((date1.getTime() - date2.getTime()) / 86400000 / 365);
                differgenderageLbl.setText(differProObject.getString("user_gender") + " - " + userAge + " years old");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            differabouteMeTxtLbl.setText(differProObject.getString("u_about_me"));
            if (differabouteMeTxtLbl.getText().toString().equalsIgnoreCase("NA")) {
                differabouteMeTxtLbl.setText("Tell us something about yourself...");
            }
            differtagsValLbl.setText(differProObject.getString("u_tags"));
            if (differtagsValLbl.getText().toString().equalsIgnoreCase("NA")) {
                differtagsValLbl.setText("No Tags Added");
            }
            differuStatusLbl.setText(differProObject.getString("u_status"));
            differuSinglzeIdLbl.setText(differProObject.getString("u_singlze_id"));
            differcountryHometown.setText(getUserCountry(this));
            if (differcountryHometown.getText().toString().length() == 0) {
                differcountryHometown.setText("India");
            }
            differhometownLbl.setText(differProObject.getString("u_hometown"));
            differuNameLbl.setText(differProObject.getString("user_name"));

            if (differProObject.getString("user_pic").equalsIgnoreCase("NA") == false) {
                countImages = countImages + 1;
            }
            if (differProObject.getString("u_pic2").equalsIgnoreCase("NA") == false) {
                countImages = countImages + 1;
            }
            if (differProObject.getString("u_pic3").equalsIgnoreCase("NA") == false) {
                countImages = countImages + 1;
            }
            if (differProObject.getString("u_pic4").equalsIgnoreCase("NA") == false) {
                countImages = countImages + 1;
            }
            if (differProObject.getString("u_pic5").equalsIgnoreCase("NA") == false) {
                countImages = countImages + 1;
            }
//            try {
//                differfollowersArr = new JSONArray(differProObject.getString("followers", ""));
//                followingArr = new JSONArray(sharedpreferences.getString("following", ""));
//
//                followingCounterLbl.setText(followingArr.length() + "");
//                followersCounterLbl.setText(followersArr.length() + "");
//            } catch (Exception r) {
//            }
            differimageCurrCountLbl.setText("Image 1 of "+countImages);
            differimgPagerAdap = new ImagePagerAdapter(differUserProfile.this, countImages,differProObject);
            differimgPager.setAdapter(differimgPagerAdap);
        }catch (Exception r){}
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


}
