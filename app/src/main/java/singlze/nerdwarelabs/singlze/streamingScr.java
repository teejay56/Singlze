package singlze.nerdwarelabs.singlze;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class streamingScr extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceView cameraSurfaceView;
    SurfaceHolder cameraSurfaceHolder;
    CommentsListAdapter listAdapter;
    android.hardware.Camera camera;
    RelativeLayout nStreamInfoHolder,overlaysHolder;
    Button cancelStreamBtn,goliveBtn,termsBtn,stopStreamingBtn;
    TextView startStreamLbl,startStreamSubheader,streamTagsTxt,streamTitleTxt,termsLbl,coinsEarnedLbl,followersGainedLbl;
    ImageView checkedimg;
    ListView commentsListV;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    protected android.hardware.Camera cameraop;
    protected boolean isPublishing = false;
    protected R5Stream stream;
    String status = "";
    JSONObject streamData;
    String uniqueId;
    public R5Configuration configuration;
    RelativeLayout surfaceViewH;
    Bitmap bitmapthumb;
    String imgLinkThumb;
    List<android.hardware.Camera.Size> mSupportedPreviewSizes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmm");
         uniqueId=sharedpreferences.getString("user_id","")+formatter.format(new Date());
        configuration = new R5Configuration(R5StreamProtocol.RTSP, "localhost",  8554, "live", 1.0f);
        configuration.setLicenseKey("E6CM-6GRH-RSYU-QBWP");
        configuration.setBundleID(getPackageName());
        surfaceViewH = (RelativeLayout)findViewById(R.id.surfaceViewH);
        commentsListV = (ListView)findViewById(R.id.commentsListV);
        listAdapter = new CommentsListAdapter();
        commentsListV.setAdapter(listAdapter);
        followersGainedLbl = (TextView)findViewById(R.id.followersGainedLbl);
        nStreamInfoHolder = (RelativeLayout)findViewById(R.id.nStreamInfoHolder);
        cancelStreamBtn = (Button)findViewById(R.id.cancelStreamBtn);
        startStreamLbl = (TextView)findViewById(R.id.startStreamLbl);
        startStreamSubheader = (TextView)findViewById(R.id.startStreamSubheader);
        streamTitleTxt = (TextView)findViewById(R.id.streamTitleTxt);
        streamTagsTxt = (TextView)findViewById(R.id.streamTagsTxt);
        goliveBtn = (Button)findViewById(R.id.goliveBtn);
        termsBtn = (Button)findViewById(R.id.termsBtn);
        termsLbl = (TextView)findViewById(R.id.termsLbl);
        checkedimg = (ImageView)findViewById(R.id.checkedimg);
        coinsEarnedLbl = (TextView)findViewById(R.id.coinsEarnedLbl);
        overlaysHolder = (RelativeLayout)findViewById(R.id.overlaysHolder);
        stopStreamingBtn = (Button)findViewById(R.id.stopStreamingBtn);
        setupUI();


    }

    private void preview(){

        cameraop = android.hardware.Camera.open(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        cameraSurfaceView = (SurfaceView)findViewById(R.id.cameraSurfaceView);
        cameraSurfaceView.getHolder().addCallback(this);
    }

    private void captureThumb(){

        cameraop.takePicture(null, null, new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                bitmapthumb = BitmapFactory.decodeByteArray(data, 0, data.length);
                onPublishToggle();
               new uploadThumb().execute();
            }
        });
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


    class endStreamAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/e_stream.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    stop();
                    finish();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(streamingScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("stream_id",streamData.getString("stream_id")));


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


    class updateStreamThumb extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/update_stream_thumb.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(streamingScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("stream_id",streamData.getString("stream_id")));
                    nameValuePairs.add(new BasicNameValuePair("stream_thumb",imgLinkThumb));

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


    class uploadThumb extends AsyncTask<String, String, String> {


        //ProgressDialog dialog = new ProgressDialog(validate.this);
        @Override
        protected String doInBackground(String... params) {

            // TODO Auto-generated method stub

            return getResponse1("https://singlze.com/API/upload_thumb.php");


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
                if(status.equalsIgnoreCase("1")){
                    status = "0";
                    new updateStreamThumb().execute();
                }else if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
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
                Bitmap bitmap = bitmapthumb;

                File file = getFileForBitmap(bitmap,"filled"+".jpg");
                mpEntity.addPart("image",new FileBody(file));
                httppost.setEntity(mpEntity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                Log.e("RESPONSE",responseString);

                JSONObject jo = new JSONObject(responseString);
                status = jo.getString("status");
                if(status.equalsIgnoreCase("1")) {
                    imgLinkThumb = jo.getString("link");
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


    private void setupUI(){
        Typeface tarial = Typeface.createFromAsset(getAssets(),"arial.ttf");
        Typeface barial = Typeface.createFromAsset(getAssets(),"arialbd.ttf");
        startStreamLbl.setTypeface(barial);
        startStreamSubheader.setTypeface(tarial);
        streamTagsTxt.setTypeface(tarial);
        streamTitleTxt.setTypeface(tarial);
        termsLbl.setTypeface(tarial);
        goliveBtn.setTypeface(tarial);
        coinsEarnedLbl.setTypeface(tarial);
        followersGainedLbl.setTypeface(tarial);
        cancelStreamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(termsBtn.getText().toString().equalsIgnoreCase("0")){
                       checkedimg.setBackgroundResource(R.drawable.check);
                       termsBtn.setText("1");
                   }else{
                       checkedimg.setBackgroundResource(R.drawable.uncheck);
                       termsBtn.setText("0");
                   }
            }
        });

        goliveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(streamTitleTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.streamTitleHolder));
                    return;
                }else if(streamTagsTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.streamTagsHolder));
                    return;
                }else if(termsBtn.getText().toString().equalsIgnoreCase("0")){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.termsHolder));
                    return;
                }
                overlaysHolder.setVisibility(View.VISIBLE);
                nStreamInfoHolder.setVisibility(View.GONE);
                status = "";
                new createStreamAPI().execute();


            }
        });

        stopStreamingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new endStreamAPI().execute();
            }
        });
        commentsListV.setEnabled(false);
        commentsListV.setSelection(listAdapter.getCount() - 1);

    }

    class createStreamAPI extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/create_stream.php");


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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    captureThumb();

                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(streamingScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(streamingScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("stream_by",sharedpreferences.getString("user_id","")));
                    nameValuePairs.add(new BasicNameValuePair("stream_tags",streamTagsTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("stream_title",streamTitleTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("stream_lat","0"));
                    nameValuePairs.add(new BasicNameValuePair("stream_lng","0"));
                    nameValuePairs.add(new BasicNameValuePair("stream_thumb",sharedpreferences.getString("user_pic","")));
                    nameValuePairs.add(new BasicNameValuePair("stream_country",sharedpreferences.getString("u_hometown","")));
                    nameValuePairs.add(new BasicNameValuePair("stream_url",uniqueId));

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
                        streamData = jo.getJSONObject("result");
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
    public void onPause() {
        super.onPause();
        if(isPublishing) {
            onPublishToggle();
        }
    }

    public void start() {
        cameraop.stopPreview();

        stream = new R5Stream(new R5Connection(configuration));
        stream.setView(cameraSurfaceView);

        R5Camera r5Camera = new R5Camera(cameraop, cameraSurfaceView.getWidth(), cameraSurfaceView.getHeight());
        R5Microphone r5Microphone = new R5Microphone();

        stream.attachCamera(r5Camera);
        stream.attachMic(r5Microphone);

        stream.publish("red5prostream10", R5Stream.RecordType.Live);
        cameraop.startPreview();
    }

    public void stop() {
        if(stream != null) {
            stream.stop();
//            cameraop.startPreview();
        }
    }

    private void onPublishToggle() {

        if(isPublishing) {
            stop();
        }
        else {
            start();
        }
        isPublishing = !isPublishing;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(ContextCompat.checkSelfPermission(streamingScr.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(streamingScr.this,
                    new String[]{Manifest.permission.CAMERA},
                    1235);
        }

        if (ContextCompat.checkSelfPermission(streamingScr.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(streamingScr.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1234);
        }else{
           preview();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Log.d("TAG", "permission denied by user");
                }
                return;
            }
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

    private android.hardware.Camera.Size getOptimalPreviewSize(List<android.hardware.Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        android.hardware.Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (android.hardware.Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (android.hardware.Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            cameraop.setPreviewDisplay(holder);
            cameraop.setDisplayOrientation(90);
            cameraop.startPreview();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
