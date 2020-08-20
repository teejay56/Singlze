package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bosphere.fadingedgelayout.FadingEdgeLayout;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.view.R5VideoView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewersScreen extends AppCompatActivity {
    Button vstopStreamingBtn,vsendMsgIcon,giftBtn,reportBtn,likeBtn;
    TextView streamerNameLbl;
    ImageView vlikeicon;
    EditText vcommentTxt;
    ListView vcommentsListV;
    CommentsListAdapter listAdapter;
    RelativeLayout giftsHolder;
    RelativeLayout vcommentTypeHolder;
    FadingEdgeLayout fading_edge_layout;
    Button cancelGiftSelBtn,followStreamerBtn;
    JSONObject streamData;
    GridView giftsGrid;
    GridAdapter gridAdapter;
    R5VideoView viewerVideoView;
    R5Stream stream;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    boolean isSubscribing = false;
    public R5Configuration configuration;
    CircleImageView streamer_image;
    String status = "";
    String followState = "0";
    JSONArray giftsArr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewers_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        giftsArr = new JSONArray();
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        try {
            streamData = new JSONObject(getIntent().getStringExtra("STREAM"));
        }catch (Exception r){
            vstopStreamingBtn.performClick();
            return;
        }
        viewerVideoView = (R5VideoView)findViewById(R.id.viewerVideoView);

        streamer_image = (CircleImageView)findViewById(R.id.streamer_image);
        try {
            boolean isexist = isfollowing(streamData.getString("user_id"));
            if(isexist==true){
                followStreamerBtn.setBackgroundResource(R.drawable.unfollowsmall);
                followStreamerBtn.setText("Unfollow");
            }else{
                followStreamerBtn.setBackgroundResource(R.drawable.followbtn);
                followStreamerBtn.setText("Follow");
            }
        }catch (Exception r){}
        vstopStreamingBtn = (Button)findViewById(R.id.vstopStreamingBtn);
        vsendMsgIcon = (Button)findViewById(R.id.vsendMsgIcon);
        giftBtn = (Button)findViewById(R.id.giftBtn);
        reportBtn = (Button)findViewById(R.id.reportBtn);
        likeBtn = (Button)findViewById(R.id.likeBtn);
        vlikeicon = (ImageView)findViewById(R.id.vlikeicon);
        vcommentTxt = (EditText)findViewById(R.id.vcommentTxt);
        vcommentsListV = (ListView)findViewById(R.id.vcommentsListV);
        streamerNameLbl = (TextView)findViewById(R.id.streamerNameLbl);
        giftsHolder = (RelativeLayout)findViewById(R.id.giftsHolder);
        cancelGiftSelBtn = (Button)findViewById(R.id.cancelGiftSelBtn);
        giftsGrid = (GridView)findViewById(R.id.giftsGrid);
        gridAdapter = new GridAdapter();
        giftsGrid.setAdapter(gridAdapter);

        listAdapter = new CommentsListAdapter();
        vcommentsListV.setAdapter(listAdapter);
        vcommentTypeHolder = (RelativeLayout)findViewById(R.id.vcommentTypeHolder);
        fading_edge_layout = (FadingEdgeLayout) findViewById(R.id.fading_edge_layout);
        followStreamerBtn = (Button)findViewById(R.id.followStreamerBtn);
        Typeface tarial = Typeface.createFromAsset(getAssets(),"arial.ttf");
        streamerNameLbl.setTypeface(tarial);
        followStreamerBtn.setTypeface(tarial);
        try {
            streamerNameLbl.setText(streamData.getString("u_singlze_id"));
            String propic = streamData.getString("user_pic");
            if (propic.equalsIgnoreCase("NA") == false) {
                propic = "https://singlze.com/" + propic;
                Picasso.with(viewersScreen.this)
                        .load(propic)
                        .placeholder(R.drawable.defpic)
                        .into(streamer_image);
            }
        }catch (Exception r){}
        vstopStreamingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        followStreamerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followStreamerBtn.getText().toString().equalsIgnoreCase("Follow")){
                    followState = "YES";
                    followStreamerBtn.setBackgroundResource(R.drawable.unfollowsmall);
                    followStreamerBtn.setText("Unfollow");
                }else{
                    followState = "NO";
                    followStreamerBtn.setBackgroundResource(R.drawable.followbtn);
                    followStreamerBtn.setText("Follow");
                }
                status = "";
                new followUserAPI().execute();
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeBtn.getText().toString().equalsIgnoreCase("0")){
                    vlikeicon.setBackgroundResource(R.drawable.alreadyliked);
                    likeBtn.setText("1");
                }else{
                    vlikeicon.setBackgroundResource(R.drawable.unliked);
                    likeBtn.setText("0");
                }
            }
        });

        giftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftsHolder.setVisibility(View.VISIBLE);
                vcommentTypeHolder.setVisibility(View.GONE);
                fading_edge_layout.setVisibility(View.GONE);
            }
        });

        cancelGiftSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftsHolder.setVisibility(View.GONE);
                vcommentTypeHolder.setVisibility(View.VISIBLE);
                fading_edge_layout.setVisibility(View.VISIBLE);
            }
        });

        new getGiftsAPI().execute();
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


    class getGiftsAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/getgifts.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success

                    gridAdapter.notifyDataSetChanged();

                }else if (status.equalsIgnoreCase("0")) {
                    //Failed

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(viewersScreen.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                        giftsArr = jo.getJSONArray("result");
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

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
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
                    if(followState.equalsIgnoreCase("YES")){
                        followStreamerBtn.setBackgroundResource(R.drawable.followbtn);
                        followStreamerBtn.setText("Follow");
                    }else{
                        followStreamerBtn.setBackgroundResource(R.drawable.unfollowsmall);
                        followStreamerBtn.setText("Unfollow");
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(viewersScreen.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(viewersScreen.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("following",streamData.getString("user_id")));
                    nameValuePairs.add(new BasicNameValuePair("state",followState));

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



    public void start() {

        try {
            stream = new R5Stream(new R5Connection(configuration));
            viewerVideoView.attachStream(stream);
            stream.play("red5prostream10");

        }catch (Exception r){
            vstopStreamingBtn.performClick();
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    public void stop() {
        if(stream != null) {
            stream.stop();
        }
    }


    private void onSubscribeToggle() {
        start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isSubscribing==false) {
            try {
                configuration = new R5Configuration(R5StreamProtocol.RTSP, "localhost", 8554,"live", 1.0f);
                configuration.setLicenseKey("E6CM-6GRH-RSYU-QBWP");
                configuration.setBundleID(getPackageName());
            }catch (Exception r){
                vstopStreamingBtn.performClick();
                return;
            }
            onSubscribeToggle();
        }
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub


            return giftsArr.length();
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

            View v = i.inflate(R.layout.gifts_cell, parent,false);
            ImageView gift_img = (ImageView)v.findViewById(R.id.gift_img);
            TextView gift_cost_Lbl = (TextView)v.findViewById(R.id.gift_cost_Lbl);

            Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
            gift_cost_Lbl.setTypeface(tp);
            try {
                gift_cost_Lbl.setText(giftsArr.getJSONObject(position).getString("cost_in_diamonds"));
                String propic = giftsArr.getJSONObject(position).getString("gift_icon");
                if(propic.equalsIgnoreCase("NA")==false){
                    propic = "https://singlze.com/"+propic;
                    Picasso.with(viewersScreen.this)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(gift_img);
                }
            }catch (Exception r){}

//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) livepic.getLayoutParams();
//            overlaylayer.setLayoutParams(params);
//            v.setLayoutParams(params);
            RelativeLayout.LayoutParams lparams = (RelativeLayout.LayoutParams) v.getLayoutParams();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    giftsHolder.setVisibility(View.GONE);
                    vcommentTypeHolder.setVisibility(View.VISIBLE);
                    fading_edge_layout.setVisibility(View.VISIBLE);
                }
            });
            return v;
        }

    }


    class CommentsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 10;
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

            View v = i.inflate(R.layout.commentcell, parent,false);
            TextView commentLbl = (TextView)v.findViewById(R.id.commentLbl);
            Typeface tarial = Typeface.createFromAsset(getAssets(),"arial.ttf");
            commentLbl.setTypeface(tarial);
            String name = "";
            String msg = "";
            if(position==0){
                name = "Shiana:";
                msg = " Hi, i am the first to comment";
            }else if(position==1){
                name = "Samantha:";
                msg = " How to prepare the material?";
            }else if(position==2){
                name = "Michael:";
                msg = " How long this whole process takes?";
            }else if(position==3){
                name = "Rajat:";
                msg = " Nice video!";
            }else if(position==4){
                name = "Inderpreet:";
                msg = " Awesome!";
            }else if(position==5){
                name = "Colin:";
                msg = " Where are you from?";
            }else if(position==6){
                name = "Travis:";
                msg = " Super!";
            }else if(position==7){
                name = "Maayan:";
                msg = " I have no experience in this.";
            }else if(position==8){
                name = "Megha:";
                msg = " How much it costs?";
            }else if(position==9){
                name = "Anna:";
                msg = " Great video, i like all your streams. Keep it up!";
            }


            String nameColored = "<font color='#f6be00'>"+name+"</font>";
            String msgColored = "<font color='#ffffff'>"+msg+"</font>";

            commentLbl.setText(Html.fromHtml(nameColored + msgColored));
            return v;
        }

    }


}
