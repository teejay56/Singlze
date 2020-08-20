package singlze.nerdwarelabs.singlze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

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

import java.util.ArrayList;
import java.util.List;

public class accountScr extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    TextView apunchline,separator,loginSignupHeader;
    Button aprivacyBtn,atermsBtn,numberloginBtn,fbbBtn,googleBtn;
    LoginButton fbBtn;
    SignInButton sign_in_button;
    CallbackManager callbackManager;
    SharedPreferences sharedpreferences;
    public static final String UDATAPREFERENCES = "UDATA";
    RelativeLayout snsloadingScr;
    TextView loginwphoneTxt,loginwfbTxt,loginwgoogleTxt;
    String status="";
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        fbBtn = (LoginButton)findViewById(R.id.fbBtn);
        fbbBtn = (Button)findViewById(R.id.fbbBtn);
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);


        googleBtn = (Button)findViewById(R.id.googleBtn);
        snsloadingScr = (RelativeLayout)findViewById(R.id.snsloadingScr);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        callbackManager = CallbackManager.Factory.create();
        fbBtn.setReadPermissions("email");
        fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                  loginResult.getAccessToken();
                  getFbInfo();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        apunchline = (TextView)findViewById(R.id.apunchline);
        separator = (TextView)findViewById(R.id.separator);
        aprivacyBtn = (Button)findViewById(R.id.aprivacyBtn);
        atermsBtn = (Button)findViewById(R.id.atermsBtn);
        numberloginBtn = (Button)findViewById(R.id.numberloginBtn);
        loginSignupHeader = (TextView)findViewById(R.id.loginSignupHeader);
        loginwphoneTxt = (TextView)findViewById(R.id.loginwphoneTxt);
        loginwfbTxt = (TextView)findViewById(R.id.loginwfbTxt);
        loginwgoogleTxt = (TextView)findViewById(R.id.loginwgoogleTxt);
        customizeGooglePlusButton(sign_in_button);
        settingUpView();
    }


    public static void customizeGooglePlusButton(SignInButton signInButton) {
        for (int i = 0; i < signInButton.getChildCount(); i++)
        {
            View v = signInButton.getChildAt(i);


                if(v instanceof TextView) {
                    TextView tv = (TextView) v;
                    tv.setTextColor(Color.TRANSPARENT);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                }else if( v instanceof ImageView){
                    ImageView imgV = (ImageView)v;
                    imgV.setVisibility(View.GONE);
                }
                //here you can customize what you want


        }
    }

    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String profilePicUrl="NA";
                            if(object.has("picture")){
                                profilePicUrl= object.getJSONObject("picture").getJSONObject("data").getString("url");

                            }
                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

                            String email;
                            if (object.has("email")) {
                                email = object.getString("email");
                            }

                            snsloadingScr.setVisibility(View.VISIBLE);
                            LoginManager.getInstance().logOut();
                            JSONObject obj = new JSONObject();
                            obj.put("NAME",first_name+" "+last_name);
                            obj.put("IMAGE",profilePicUrl);
                            obj.put("ID",id);
                            new checkLogin().execute(id,"YES",obj.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,picture.type(large)"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        snsloadingScr.setVisibility(View.GONE);

    }



    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            try {
                GoogleSignInAccount acct = result.getSignInAccount();
                JSONObject obj = new JSONObject();
                obj.put("NAME", acct.getDisplayName().toString());
                try {
                    obj.put("IMAGE", acct.getPhotoUrl().toString());
                }catch (Exception r){
                    obj.put("IMAGE", "NA");
                }
                obj.put("ID", acct.getId().toString());
                snsloadingScr.setVisibility(View.VISIBLE);
                new checkLogin().execute(acct.getId().toString(), "NO", obj.toString());
            }catch (Exception r){

                String error = r.getLocalizedMessage();

            }
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    class checkLogin extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String idfb;
        String isfb;
        String dataStr;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            idfb = params[0];
            isfb = params[1];
            dataStr = params[2];
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
            snsloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    Intent i = new Intent(accountScr.this,HomeScr.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    Intent i = new Intent(accountScr.this, completeProfileScr.class);
                    i.putExtra("ISSOCIAL","YES");
                    i.putExtra("ISFB",isfb);
                    i.putExtra("ISEDITING","NO");
                    i.putExtra("DATA",dataStr);
                    startActivity(i);
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(accountScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(accountScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("number",idfb));
                    if(isfb.equalsIgnoreCase("YES")) {
                        nameValuePairs.add(new BasicNameValuePair("login_type", "FACEBOOK"));
                    }else{
                        nameValuePairs.add(new BasicNameValuePair("login_type", "GOOGLE"));
                    }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void settingUpView(){
        Typeface canot = Typeface.createFromAsset(getAssets(),"Cano.otf");
        Typeface arialt = Typeface.createFromAsset(getAssets(),"arial.ttf");
        loginSignupHeader.setTypeface(canot);
        apunchline.setTypeface(canot);
        separator.setTypeface(arialt);
        aprivacyBtn.setTypeface(arialt);
        atermsBtn.setTypeface(arialt);
        loginwgoogleTxt.setTypeface(arialt);
        loginwfbTxt.setTypeface(arialt);
        loginwphoneTxt.setTypeface(arialt);
        numberloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountScr.this,numberLoginScr.class);
                startActivity(i);
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_in_button.performClick();
            }
        });

        fbbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbBtn.performClick();
            }
        });
    }




}
