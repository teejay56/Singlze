package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;
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

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScr extends AppCompatActivity {
    BottomNavigationView navigation;
    FrameLayout fragment_container;
    RelativeLayout gridHolder;
    GridView liveCamGrid;
    GridAdapter gridAdap;
    TextView liveStreamOptLbl;
    RelativeLayout liveStreamOptHolder,searchOptHolder;
    Button searchBtn,filterBtn;
    ImageView filtericon,searchicon;
    String country;
    String status = "";
    JSONArray streamersArray;
    TextView noStreamersLbl;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragment = new HomeFragment();
                     new getStreamsAPI().execute();
                    liveStreamOptHolder.setVisibility(View.VISIBLE);
                    searchOptHolder.setVisibility(View.VISIBLE);
                    gridHolder.setVisibility(View.VISIBLE);
                    fragment_container.setVisibility(View.GONE);
                    break;
                case R.id.navigation_profile:
                    Intent intent = new Intent(HomeScr.this,otherProfile.class);
                    startActivity(intent);
//                    liveStreamOptHolder.setVisibility(View.GONE);
//                    searchOptHolder.setVisibility(View.GONE);
//                    gridHolder.setVisibility(View.GONE);
//                    fragment_container.setVisibility(View.VISIBLE);
//                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_live_sher:
                    Intent i = new Intent(HomeScr.this,streamingScr.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_notifications:
                    liveStreamOptHolder.setVisibility(View.GONE);
                    searchOptHolder.setVisibility(View.GONE);
                    gridHolder.setVisibility(View.GONE);
                    fragment_container.setVisibility(View.VISIBLE);
                    fragment = new NotificationFragment();
                    break;
                case R.id.navigation_sett:
                    Intent intent1 = new Intent(HomeScr.this,settingsScr.class);
                    startActivity(intent1);

                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        country = sharedpreferences.getString("u_hometown","");
//        country = "in";

        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        searchOptHolder = (RelativeLayout)findViewById(R.id.searchOptHolder);
        liveStreamOptHolder = (RelativeLayout)findViewById(R.id.liveStreamOptHolder);
        liveStreamOptLbl = (TextView)findViewById(R.id.liveStreamOptLbl);
        fragment_container = (FrameLayout)findViewById(R.id.fragment_container);
        gridHolder = (RelativeLayout) findViewById(R.id.gridHolder);
        liveCamGrid = (GridView)findViewById(R.id.liveCamGrid);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchicon = (ImageView)findViewById(R.id.searchicon);
        filtericon = (ImageView)findViewById(R.id.filtericon);
        noStreamersLbl = (TextView)findViewById(R.id.noStreamersLbl);
        filterBtn = (Button)findViewById(R.id.filterBtn);
        streamersArray = new JSONArray();
        gridAdap = new GridAdapter();
        liveCamGrid.setAdapter(gridAdap);
        noStreamersLbl.setVisibility(View.GONE);
        gridHolder.setVisibility(View.GONE);
        fragment_container.setVisibility(View.GONE);
        liveStreamOptLbl.setTypeface(tp);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        navigation.setItemIconSize(60);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new getStreamsAPI().execute();
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScr.this,filtersScr.class);
                startActivity(i);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScr.this,searchScr.class);
                startActivity(i);
            }
        });


        BottomNavigationMenuView menuView = (BottomNavigationMenuView)
                navigation.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            if(i==2) {
                final View iconView =
                        menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);

                final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();

                final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
// set your height here
                layoutParams.height = (int)
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, displayMetrics);

// set your width here
                layoutParams.width = (int)
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, displayMetrics);

                iconView.setLayoutParams(layoutParams);

            }
        }


        loadFragment(new HomeFragment());
    }

    @Override
    public void onResume(){
        super.onResume();
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
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

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(HomeScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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


    class getStreamsAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/get_streams.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    if(streamersArray.length()<2){
                        searchicon.setImageResource(R.drawable.bsearchicon);
                        filtericon.setImageResource(R.drawable.bfiltericon);
                    }else{
                        searchicon.setImageResource(R.drawable.searchicon);
                        filtericon.setImageResource(R.drawable.filtericon);
                    }
                    noStreamersLbl.setVisibility(View.GONE);
                    gridHolder.setVisibility(View.VISIBLE);
                    gridAdap.notifyDataSetChanged();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    searchicon.setImageResource(R.drawable.bsearchicon);
                    filtericon.setImageResource(R.drawable.bfiltericon);
                    noStreamersLbl.setVisibility(View.VISIBLE);
                    gridHolder.setVisibility(View.GONE);
                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(HomeScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("country",country));

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
                        streamersArray = jo.getJSONArray("result");
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



    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub


            return streamersArray.length();
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

            LayoutInflater i = getLayoutInflater();

            View v = i.inflate(R.layout.livecamcell, parent,false);
            ImageView overlaylayer = (ImageView)v.findViewById(R.id.overlaylayer);

            ImageView livepic=(ImageView)v.findViewById(R.id.livePic);
            TextView streamname = (TextView)v.findViewById(R.id.streamName);
            TextView streamerName = (TextView)v.findViewById(R.id.streamerName);
            CircleImageView streamerPic = (CircleImageView)v.findViewById(R.id.streamerPic);
            Button viewStreamerProfile = (Button)v.findViewById(R.id.viewStreamerProfile);
            TextView totalLikesLbl = (TextView)v.findViewById(R.id.totalLikesLbl);
            Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");

            viewStreamerProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(streamersArray.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))){
                            Intent i = new Intent(HomeScr.this, otherProfile.class);
                            startActivity(i);
                        }else {
                            Intent i = new Intent(HomeScr.this, differUserProfile.class);
                            i.putExtra("DATA", streamersArray.getJSONObject(position).toString());
                            startActivity(i);
                        }
                    }catch (Exception r){}
                }
            });

            try {
                streamerName.setText(streamersArray.getJSONObject(position).getString("u_singlze_id"));
                streamname.setText(streamersArray.getJSONObject(position).getString("stream_title"));
                String propic = streamersArray.getJSONObject(position).getString("user_pic");
                if(propic.equalsIgnoreCase("NA")==false){
                    propic = "https://singlze.com/"+propic;
                    Picasso.with(HomeScr.this)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(streamerPic);
                }

                String thumbpic = streamersArray.getJSONObject(position).getString("stream_thumb");
                if(propic.equalsIgnoreCase("NA")==false){
                    propic = "https://singlze.com/"+thumbpic;
                    Picasso.with(HomeScr.this)
                            .load(propic)
                            .placeholder(R.drawable.defpicedit)
                            .into(livepic);
                }
            }catch (Exception r){}
            streamerName.setTypeface(tp);
            streamname.setTypeface(tp);
            totalLikesLbl.setTypeface(tp);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) livepic.getLayoutParams();
//            overlaylayer.setLayoutParams(params);
//            v.setLayoutParams(params);
            RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams) v.getLayoutParams();

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(HomeScr.this, viewersScreen.class);
                        i.putExtra("STREAM", streamersArray.getJSONObject(position).toString());
                        startActivity(i);
                    }catch (Exception r){}
                }
            });
            return v;
        }

    }


}
