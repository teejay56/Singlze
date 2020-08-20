package singlze.nerdwarelabs.singlze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchScr extends AppCompatActivity {
    TextView searchHeader,resultsCountLbl;
    Button searchBackBtn;
    EditText searchTxtField;
    ListView searchResultsTbl;
    searchListAdapter listAdapter;
    String status="";
    JSONArray searchResults;
    TextView noSearchResults;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        noSearchResults = (TextView)findViewById(R.id.noSearchResults);
        searchResultsTbl = (ListView)findViewById(R.id.searchResultsTbl);
        searchResults = new JSONArray();
        listAdapter = new searchListAdapter();
        searchResultsTbl.setAdapter(listAdapter);
        searchHeader = (TextView)findViewById(R.id.searchHeader);
        searchBackBtn = (Button)findViewById(R.id.searchBackBtn);
        searchTxtField = (EditText)findViewById(R.id.searchTxtField);
        resultsCountLbl = (TextView)findViewById(R.id.resultsCountLbl);

        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        Typeface tb = Typeface.createFromAsset(getAssets(),"arialbd.ttf");
        resultsCountLbl.setTypeface(tb);
        searchHeader.setTypeface(tp);
        searchTxtField.setTypeface(tp);
        searchBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchTxtField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(searchTxtField.getText().toString().length()>0){
                        hideKeyboard(searchScr.this);
                       status = "";
                       new searchWithCriteria().execute();
                    }
                    handled = true;
                }
                return handled;
            }
        });


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    class searchWithCriteria extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/search_users.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    if(searchResults.length()>1) {
                        resultsCountLbl.setText(searchResults.length() + " Results");
                    }else{
                        resultsCountLbl.setText(searchResults.length() + " Result");
                    }
                    noSearchResults.setVisibility(View.GONE);
                    resultsCountLbl.setVisibility(View.VISIBLE);
                    searchResultsTbl.setVisibility(View.VISIBLE);
                    listAdapter.notifyDataSetChanged();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    noSearchResults.setVisibility(View.VISIBLE);
                    resultsCountLbl.setVisibility(View.GONE);
                    searchResultsTbl.setVisibility(View.GONE);
                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(searchScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("criteria",searchTxtField.getText().toString()));

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
                       searchResults = jo.getJSONArray("result");
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                   listAdapter.notifyDataSetChanged();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(searchScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
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
                    listAdapter.notifyDataSetChanged();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(searchScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(searchScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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



    class searchListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return searchResults.length();
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

            View v = i.inflate(R.layout.search_cell, parent,false);

            TextView searchResUName = (TextView)v.findViewById(R.id.searchResultUName);
            TextView searchResStatus = (TextView)v.findViewById(R.id.searchUserStatus);
            TextView searchCountryName = (TextView)v.findViewById(R.id.searchCountryName);
            TextView searchLikesCount = (TextView)v.findViewById(R.id.searchLikesCount);
            TextView searchdiamondCount = (TextView)v.findViewById(R.id.searchdiamondCount);
            final Button searchfollowUserBtn = (Button)v.findViewById(R.id.searchfollowUserBtn);

            CircleImageView searchCellPic = (CircleImageView)v.findViewById(R.id.searchCellPic);
            Typeface tHR = Typeface.createFromAsset(getAssets(),"arial.ttf");
            searchLikesCount.setTypeface(tHR);
            searchdiamondCount.setTypeface(tHR);
            searchCountryName.setTypeface(tHR);
            searchResUName.setTypeface(tHR);
            searchResStatus.setTypeface(tHR);

            try{
                searchResUName.setText(searchResults.getJSONObject(position).getString("user_name"));
                searchResStatus.setText(searchResults.getJSONObject(position).getString("u_status"));
                searchCountryName.setText(searchResults.getJSONObject(position).getString("u_hometown"));


                if(searchResults.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))){
                    searchfollowUserBtn.setVisibility(View.GONE);
                }else{
                    searchfollowUserBtn.setVisibility(View.VISIBLE);
                }

                boolean isexist = isfollowing(searchResults.getJSONObject(position).getString("user_id"));
                if(isexist==true){
                    searchfollowUserBtn.setText("1");
                    searchfollowUserBtn.setBackgroundResource(R.drawable.unfback);
                }else{
                    searchfollowUserBtn.setText("0");
                    searchfollowUserBtn.setBackgroundResource(R.drawable.followicon);
                }

                searchfollowUserBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (searchfollowUserBtn.getText().toString().equalsIgnoreCase("0")) {
                                String followState = "YES";
                                searchfollowUserBtn.setText("1");
                                searchfollowUserBtn.setBackgroundResource(R.drawable.unfback);
                                String idfollowing = searchResults.getJSONObject(position).getString("user_id");
                                status = "";
                                new followUserAPI().execute(followState, idfollowing);
                            } else {
                                String followState = "NO";
                                searchfollowUserBtn.setText("0");
                                searchfollowUserBtn.setBackgroundResource(R.drawable.followicon);
                                String idfollowing = searchResults.getJSONObject(position).getString("user_id");
                                status = "";
                                new followUserAPI().execute(followState, idfollowing);
                            }
                        }catch (Exception r){}
                    }
                });

                String propic = searchResults.getJSONObject(position).getString("user_pic");
                if(propic.equalsIgnoreCase("NA")==false){
                    propic = "https://singlze.com/"+propic;
                    Picasso.with(searchScr.this)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(searchCellPic);
                }

            }catch (Exception r){}

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if(searchResults.getJSONObject(position).getString("user_id").equalsIgnoreCase(sharedpreferences.getString("user_id",""))){
                            Intent intent = new Intent(searchScr.this,otherProfile.class);
                            startActivity(intent);
                        }else {
                            Intent i = new Intent(searchScr.this, differUserProfile.class);
                            i.putExtra("DATA", searchResults.getJSONObject(position).toString());
                            startActivity(i);
                        }
                    }catch (Exception r){}
                }
            });
            return v;
        }
    }


}
