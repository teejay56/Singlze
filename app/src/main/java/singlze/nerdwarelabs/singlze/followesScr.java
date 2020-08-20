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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class followesScr extends AppCompatActivity {
    TextView followersViewTxt,followingViewTxt;
    Button followingViewBtn,followersViewBtn,followersBackBtn;
    LinearLayout followersLiner,followingLiner;
    EditText followerSearchTxt;
    ListView followersTbl;
    followersListAdapter flistAdapter;
    JSONArray showFollowersArr,showFollowingArr;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    String status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followes_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        flistAdapter = new followersListAdapter();
        try {
            showFollowersArr = new JSONArray(getIntent().getStringExtra("FOLLOWERS"));
            showFollowingArr = new JSONArray(getIntent().getStringExtra("FOLLOWING"));
        }catch (Exception r){
            String error = r.getLocalizedMessage();
        }
        followersBackBtn = (Button)findViewById(R.id.followersBackBtn);
        followersViewTxt = (TextView)findViewById(R.id.followersViewTxt);
        followingViewTxt = (TextView)findViewById(R.id.followingViewTxt);
        followingViewBtn = (Button)findViewById(R.id.followingViewBtn);
        followersTbl = (ListView)findViewById(R.id.followersTbl);

        followerSearchTxt = (EditText)findViewById(R.id.followerSearchTxt);
        followersViewBtn = (Button)findViewById(R.id.followersViewBtn);
        followersLiner = (LinearLayout)findViewById(R.id.followersLiner);
        followingLiner = (LinearLayout)findViewById(R.id.followingLiner);

        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        followersViewTxt.setTypeface(tp);
        followingViewTxt.setTypeface(tp);
        followerSearchTxt.setTypeface(tp);
        followersTbl.setAdapter(flistAdapter);
        followersBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        followingViewTxt.setText(showFollowingArr.length()+" Following");
        followersViewTxt.setText(showFollowersArr.length()+" Followers");
        followersViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersLiner.setVisibility(View.VISIBLE);
                followingLiner.setVisibility(View.GONE);
            }
        });

        followingViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersLiner.setVisibility(View.GONE);
                followingLiner.setVisibility(View.VISIBLE);
            }
        });

    }

    class followersListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(followersLiner.getVisibility()==View.VISIBLE) {
                return showFollowersArr.length();
            }else{
                return showFollowingArr.length();
            }
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            System.out.println("HI ENTER::::");

            LayoutInflater i = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = i.inflate(R.layout.followers_cell, parent,false);

            TextView followerName = (TextView)v.findViewById(R.id.followerName);
            TextView followerSinglzeID = (TextView)v.findViewById(R.id.followerSinglzeID);
            final TextView followBtnTxt = (TextView)v.findViewById(R.id.followBtnTxt);
            final Button cellFollowBtn = (Button)v.findViewById(R.id.cellFollowBtn);
            CircleImageView followerCellPic = (CircleImageView)v.findViewById(R.id.followerCellPic);
            final ImageView followbtnback = (ImageView)v.findViewById(R.id.followbtnback);
            Typeface tHR = Typeface.createFromAsset(getAssets(),"arial.ttf");
            followBtnTxt.setTypeface(tHR);
            followerName.setTypeface(tHR);
            followerSinglzeID.setTypeface(tHR);
            cellFollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String followState = "";
                        if (followBtnTxt.getText().toString().equalsIgnoreCase("follow")) {

                            String idfollowing = "";
                            if (followingLiner.getVisibility() == View.VISIBLE) {
                                idfollowing = showFollowingArr.getJSONObject(position).getString("user_id");
                            } else {
                                idfollowing = showFollowersArr.getJSONObject(position).getString("user_id");
                            }
                            followState = "YES";
                            followbtnback.setImageResource(R.drawable.unfollowbtn);
                            followBtnTxt.setText("Unfollow");
                            status = "";
                            new followUserAPI().execute(followState,idfollowing);
                        } else {
                            String idfollowing = "";
                            if (followingLiner.getVisibility() == View.VISIBLE) {
                                idfollowing = showFollowingArr.getJSONObject(position).getString("user_id");
                            } else {
                                idfollowing = showFollowersArr.getJSONObject(position).getString("user_id");
                            }
                            followState = "NO";
                            followbtnback.setImageResource(R.drawable.followbtngreen);
                            followBtnTxt.setText("Follow");
                            status = "";
                            new followUserAPI().execute(followState,idfollowing);
                        }
                    }catch (Exception r){}
                }
            });
            if(followersLiner.getVisibility()==View.VISIBLE){
                try {
                    followerName.setText(showFollowersArr.getJSONObject(position).getString("user_name"));
                    followerSinglzeID.setText(showFollowersArr.getJSONObject(position).getString("u_singlze_id"));
                    String propic = showFollowersArr.getJSONObject(position).getString("user_pic");
                    if (propic.equalsIgnoreCase("NA") == false) {
                        propic = "https://singlze.com/" + propic;
                        Picasso.with(followesScr.this)
                                .load(propic)
                                .placeholder(R.drawable.defpic)
                                .into(followerCellPic);

                    }
                    boolean isexist = isfollowing(showFollowersArr.getJSONObject(position).getString("user_id"));
                    if(isexist==true){
                        followbtnback.setImageResource(R.drawable.unfollowbtn);
                        followBtnTxt.setText("Unfollow");
                    }else{
                        followbtnback.setImageResource(R.drawable.followbtngreen);
                        followBtnTxt.setText("Follow");
                    }
                    if(showFollowersArr.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))) {
                        followbtnback.setVisibility(View.GONE);
                        followBtnTxt.setVisibility(View.GONE);
                        cellFollowBtn.setVisibility(View.GONE);
                    }else{
                        followbtnback.setVisibility(View.VISIBLE);
                        followBtnTxt.setVisibility(View.VISIBLE);
                        cellFollowBtn.setVisibility(View.VISIBLE);
                    }
                }catch (Exception r){}
            }else{
                try {
                    followerName.setText(showFollowingArr.getJSONObject(position).getString("user_name"));
                    followerSinglzeID.setText(showFollowingArr.getJSONObject(position).getString("u_singlze_id"));
                    String propic = showFollowingArr.getJSONObject(position).getString("user_pic");
                    if (propic.equalsIgnoreCase("NA") == false) {
                        propic = "https://singlze.com/" + propic;
                        Picasso.with(followesScr.this)
                                .load(propic)
                                .placeholder(R.drawable.defpic)
                                .into(followerCellPic);

                    }
                    boolean isexist = isfollowing(showFollowingArr.getJSONObject(position).getString("user_id"));
                    if(isexist==true){
                        followbtnback.setImageResource(R.drawable.unfollowbtn);
                        followBtnTxt.setText("Unfollow");

                    }else{
                        followbtnback.setImageResource(R.drawable.followbtngreen);
                        followBtnTxt.setText("Follow");
                    }
                    if(showFollowingArr.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))) {
                        followbtnback.setVisibility(View.GONE);
                        followBtnTxt.setVisibility(View.GONE);
                        cellFollowBtn.setVisibility(View.GONE);
                    }else{
                        followbtnback.setVisibility(View.VISIBLE);
                        followBtnTxt.setVisibility(View.VISIBLE);
                        cellFollowBtn.setVisibility(View.VISIBLE);
                    }
                }catch (Exception r){}
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (followersLiner.getVisibility() == View.VISIBLE) {
                            if(showFollowersArr.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))){
                                Intent intent = new Intent(followesScr.this,otherProfile.class);
                                startActivity(intent);
                            }else {
                                Intent i = new Intent(followesScr.this, differUserProfile.class);
                                i.putExtra("DATA", showFollowersArr.getJSONObject(position).toString());
                                startActivity(i);
                            }
                        }else{
                            if(showFollowingArr.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))){
                                Intent intent = new Intent(followesScr.this,otherProfile.class);
                                startActivity(intent);
                            }else {
                                Intent i = new Intent(followesScr.this, differUserProfile.class);
                                i.putExtra("DATA", showFollowingArr.getJSONObject(position).toString());
                                startActivity(i);
                            }
                        }
                        }catch(Exception r){ }

                }
            });
            return v;
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
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    flistAdapter.notifyDataSetChanged();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(followesScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    status = "";
                    new getFollowersData().execute();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    flistAdapter.notifyDataSetChanged();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(followesScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(followesScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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

}
