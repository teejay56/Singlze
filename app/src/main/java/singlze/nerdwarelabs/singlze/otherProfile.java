package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class otherProfile extends AppCompatActivity {
    ViewPager imgPager;
    TextView imageCurrCountLbl,uNameLbl,uSinglzeIdLbl,uStatusLbl,goLiveLbl,diamondCountLbl,chatLbl,followersCounterLbl,followerStaticLbl;
    TextView followingCounterLbl,followingStaticLbl,likesCounterLbl,likesStaticLbl,recentFollowersLbl,buyDiamondsLbl,tagsValLbl,aboutMeHeaderLbl;
    TextView abouteMeTxtLbl;
    Button proOptionsBtn,proLogoutBtn,proSettingsBtn,proHelpDeskBtn,proEditBtn,proBackBtn;
    SharedPreferences sharedpreferences;
    RelativeLayout optionsPopup;
    Button goliveBtnpro,followingProBtn,followersProBtn,proBuyDiamondsBtn;
    RelativeLayout profileloadingScr;
    String status = "";
    CircleImageView ucircularImg;
    TextView countryHometown;
    TextView hometownLbl,genderageLbl;
    ImagePagerAdapter imgPagerAdap;
    int countImages = 0;
    JSONArray followersArr,followingArr;
    public static final String UDATAPREFERENCES = "UDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        ucircularImg = (CircleImageView)findViewById(R.id.ucircularImg);
        genderageLbl = (TextView)findViewById(R.id.genderageLbl);
        goLiveLbl = (TextView)findViewById(R.id.goLiveLbl);
        followingProBtn = (Button)findViewById(R.id.followingProBtn);
        followersProBtn = (Button)findViewById(R.id.followersProBtn);
        hometownLbl = (TextView)findViewById(R.id.hometownLbl);
        countryHometown = (TextView)findViewById(R.id.countryHometown);
        goliveBtnpro = (Button)findViewById(R.id.goliveBtnpro);
        proBuyDiamondsBtn = (Button)findViewById(R.id.proBuyDiamondsBtn);
        profileloadingScr = (RelativeLayout)findViewById(R.id.profileloadingScr);
        optionsPopup = (RelativeLayout)findViewById(R.id.optionsPopup);
        imageCurrCountLbl = (TextView)findViewById(R.id.imageCurrCountLbl);
        followersCounterLbl = (TextView)findViewById(R.id.followersCounterLbl);
        recentFollowersLbl = (TextView)findViewById(R.id.recentFollowersLbl);
        followerStaticLbl = (TextView)findViewById(R.id.followerStaticLbl);
        likesStaticLbl = (TextView)findViewById(R.id.likesStaticLbl);
        proLogoutBtn = (Button)findViewById(R.id.proLogoutBtn);
        proEditBtn = (Button)findViewById(R.id.proEditBtn);
        proBackBtn = (Button)findViewById(R.id.proBackBtn);
        proHelpDeskBtn = (Button)findViewById(R.id.proHelpDeskBtn);
        proSettingsBtn = (Button)findViewById(R.id.proSettingsBtn);
        proOptionsBtn = (Button)findViewById(R.id.proOptionsBtn);
        tagsValLbl = (TextView)findViewById(R.id.tagsValLbl);
        likesCounterLbl = (TextView)findViewById(R.id.likesCounterLbl);
        aboutMeHeaderLbl = (TextView)findViewById(R.id.aboutMeHeaderLbl);
        buyDiamondsLbl = (TextView)findViewById(R.id.buyDiamondsLbl);
        abouteMeTxtLbl = (TextView)findViewById(R.id.abouteMeTxtLbl);
        uNameLbl = (TextView)findViewById(R.id.uNameLbl);
        followingStaticLbl = (TextView)findViewById(R.id.followingStaticLbl);
        followingCounterLbl = (TextView)findViewById(R.id.followingCounterLbl);
        chatLbl = (TextView)findViewById(R.id.chatLbl);
        uStatusLbl = (TextView)findViewById(R.id.uStatusLbl);
        uSinglzeIdLbl = (TextView)findViewById(R.id.uSinglzeIdLbl);
        diamondCountLbl = (TextView)findViewById(R.id.diamondCountLbl);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        uNameLbl.setTypeface(tp);
        abouteMeTxtLbl.setTypeface(tp);
        tagsValLbl.setTypeface(tp);
        aboutMeHeaderLbl.setTypeface(tp);
        genderageLbl.setTypeface(tp);
        followingStaticLbl.setTypeface(tp);
        buyDiamondsLbl.setTypeface(tp);
        recentFollowersLbl.setTypeface(tp);
        followingCounterLbl.setTypeface(tp);
        followerStaticLbl.setTypeface(tp);
        followersCounterLbl.setTypeface(tp);
        goLiveLbl.setTypeface(tp);
        chatLbl.setTypeface(tp);
        uStatusLbl.setTypeface(tp);
        diamondCountLbl.setTypeface(tp);
        hometownLbl.setTypeface(tp);
        countryHometown.setTypeface(tp);
        uSinglzeIdLbl.setTypeface(tp);
        imageCurrCountLbl.setTypeface(tp);



        proEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsPopup.setVisibility(View.GONE);
                Intent i = new Intent(otherProfile.this,EditProfileScr.class);
                i.putExtra("ISEDITING","YES");
                startActivity(i);
            }
        });
        proBuyDiamondsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(otherProfile.this,buyCreditScr.class);
                startActivity(i);
            }
        });

        followingProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followingArr.length()==0){
                    return;
                }
                Intent i = new Intent(otherProfile.this,followesScr.class);
                i.putExtra("FOLLOWERS",followersArr.toString());
                i.putExtra("FOLLOWING",followingArr.toString());
                startActivity(i);
            }
        });

        followersProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followersArr.length()==0){
                    return;
                }
                Intent i = new Intent(otherProfile.this,followesScr.class);
                i.putExtra("FOLLOWERS",followersArr.toString());
                i.putExtra("FOLLOWING",followingArr.toString());
                startActivity(i);
            }
        });

        proBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        proOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionsPopup.getVisibility()==View.GONE) {
                    optionsPopup.setVisibility(View.VISIBLE);
                }else{
                    optionsPopup.setVisibility(View.GONE);
                }
            }
        });

        goliveBtnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(otherProfile.this,streamingScr.class);
                startActivity(i);
            }
        });

        proLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Intent i = new Intent(otherProfile.this, accountScr.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        proSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optionsPopup.setVisibility(View.GONE);
                Intent i = new Intent(otherProfile.this, settingsScr.class);
                startActivity(i);
            }
        });
        imgPager = (ViewPager)findViewById(R.id.imagePager);

        imgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if(position==0){
                    imageCurrCountLbl.setText("Image 1 of "+countImages);
                }else if(position==1){
                    imageCurrCountLbl.setText("Image 2 of "+countImages);
                }else if(position==2){
                    imageCurrCountLbl.setText("Image 3 of "+countImages);
                }else if(position==3){
                    imageCurrCountLbl.setText("Image 4 of "+countImages);
                }else if(position==4){
                    imageCurrCountLbl.setText("Image 5 of "+countImages);
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        String propic = sharedpreferences.getString("user_pic","");
        if(propic.equalsIgnoreCase("NA")==false){
            propic = "https://singlze.com/"+propic;
            Picasso.with(this)
                    .load(propic)
                    .placeholder(R.drawable.defpic)
                    .into(ucircularImg);

        }
        try {
        Date date1 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dob = sharedpreferences.getString("user_dob","");
        Date date2 = null;
            date2 = dateFormat.parse(dob);
            int userAge = (int) ((date1.getTime() - date2.getTime()) / 86400000 / 365);
            genderageLbl.setText(sharedpreferences.getString("user_gender","")+" - "+userAge+" years old");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        abouteMeTxtLbl.setText(sharedpreferences.getString("u_about_me",""));
        if(abouteMeTxtLbl.getText().toString().equalsIgnoreCase("NA")){
            abouteMeTxtLbl.setText("Tell us something about yourself...");
        }
        tagsValLbl.setText(sharedpreferences.getString("u_tags",""));
        if(tagsValLbl.getText().toString().equalsIgnoreCase("NA")){
            tagsValLbl.setText("No Tags Added");
        }
        uStatusLbl.setText(sharedpreferences.getString("u_status",""));
        uSinglzeIdLbl.setText(sharedpreferences.getString("u_singlze_id",""));
        countryHometown.setText(getUserCountry(this));
        if(countryHometown.getText().toString().length()==0) {
            countryHometown.setText("India");
        }
        hometownLbl.setText(sharedpreferences.getString("u_hometown",""));
        uNameLbl.setText(sharedpreferences.getString("user_name",""));

        if(sharedpreferences.getString("user_pic","").equalsIgnoreCase("NA")==false){
            countImages = countImages+1;
        }
        if(sharedpreferences.getString("u_pic2","").equalsIgnoreCase("NA")==false){
            countImages=  countImages+1;
        }
        if(sharedpreferences.getString("u_pic3","").equalsIgnoreCase("NA")==false){
            countImages = countImages+1;
        }if(sharedpreferences.getString("u_pic4","").equalsIgnoreCase("NA")==false){
            countImages = countImages+1;
        }if(sharedpreferences.getString("u_pic5","").equalsIgnoreCase("NA")==false){
            countImages = countImages+1;
        }
        try {
            followersArr = new JSONArray(sharedpreferences.getString("followers", ""));
            followingArr = new JSONArray(sharedpreferences.getString("following", ""));

            followingCounterLbl.setText(followingArr.length()+"");
            followersCounterLbl.setText(followersArr.length()+"");
            JSONObject jobj = new JSONObject(sharedpreferences.getString("raw_data",""));
            imageCurrCountLbl.setText("Image 1 of "+countImages);
            imgPagerAdap = new ImagePagerAdapter(otherProfile.this,countImages,jobj);
            imgPager.setAdapter(imgPagerAdap);
        }catch (Exception r){}
        status = "";
        new getFollowersData().execute();

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
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(otherProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    followersArr = new JSONArray(sharedpreferences.getString("followers", ""));
                    followingArr = new JSONArray(sharedpreferences.getString("following", ""));

                    followingCounterLbl.setText(followingArr.length()+"");
                    followersCounterLbl.setText(followersArr.length()+"");
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(otherProfile.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(otherProfile.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(otherProfile.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    //  JSONObject data  = jo.getJSONObject("data");


                    // System.out.println("hello data get:::"+data+":::");

                    /*id=data.getString("user_id");
                    System.out.println("iD get:::"+id);*/
                    if (status.equalsIgnoreCase("1")) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("followers", jo.getJSONArray("followers").toString());
                        editor.putString("following", jo.getJSONArray("following").toString());
                        editor.commit();
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
