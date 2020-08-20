package singlze.nerdwarelabs.singlze;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class profileSettings extends AppCompatActivity {
    TextView editProfileHeader,changePasswordTxt,delAccText,pchangePassHeaderLbl,pchangePassSubheader,pdelAccHeaderLbl,pdelAccLbl1;
    EditText unameEdit,dobEditTxt,pcurrentPassTxt,pnewPasswordTxt,pconfirmPassTxt;
    Button dobEditBtn,changePassBtn,deleteAccBtn,cancelEditProfileBtn,pcancelChangePassBtn,pupdatepasswordBtn,pcancelDeleteAccount,deleteConfirmBtn,dontdeleteConfirmBtn;
    RelativeLayout pChangePasswordHolder,pDeleteAccountHolder,proSettloadingScr;
    SharedPreferences sharedpreferences;
    CircleImageView ppprofile_image;
    TextView pro_sett_verifyingLbl;
    String status ;
    String thumburl,prodobString;
    Button updatePicBtn;
    RelativeLayout procalendarHolder;
    Button proSaveBtn;
    DatePicker prodateSelectorDP;
    Button proselectedDOBBtn;
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    int MY_PERMISSIONS_REQUEST_READEXTERNAL=0;
    private static int CAMERA_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;
    public static final String UDATAPREFERENCES = "UDATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        proSaveBtn = (Button)findViewById(R.id.proSaveBtn);
        proselectedDOBBtn = (Button)findViewById(R.id.proselectedDOBBtn);
        prodateSelectorDP = (DatePicker)findViewById(R.id.prodateSelectorDP);
        procalendarHolder = (RelativeLayout)findViewById(R.id.procalendarHolder);
        ppprofile_image = (CircleImageView)findViewById(R.id.ppprofile_image);
        pro_sett_verifyingLbl = (TextView)findViewById(R.id.pro_sett_verifyingLbl);
        proSettloadingScr = (RelativeLayout)findViewById(R.id.proSettloadingScr);
        pDeleteAccountHolder = (RelativeLayout)findViewById(R.id.pDeleteAccountHolder);
        pconfirmPassTxt = (EditText)findViewById(R.id.pconfirmPassTxt);
        pnewPasswordTxt = (EditText)findViewById(R.id.pnewPasswordTxt);
        pcurrentPassTxt = (EditText)findViewById(R.id.pcurrentPassTxt);
        cancelEditProfileBtn = (Button)findViewById(R.id.cancelEditProfileBtn);
        pcancelDeleteAccount = (Button)findViewById(R.id.pcancelDeleteAccount);
        pdelAccHeaderLbl = (TextView)findViewById(R.id.pdelAccHeaderLbl);
        deleteConfirmBtn = (Button)findViewById(R.id.deleteConfirmBtn);
        pdelAccLbl1 = (TextView)findViewById(R.id.pdelAccLbl1);
        dontdeleteConfirmBtn = (Button)findViewById(R.id.dontdeleteConfirmBtn);
        editProfileHeader = (TextView)findViewById(R.id.editProfileHeader);
        dobEditTxt = (EditText) findViewById(R.id.dobEditTxt);
        pupdatepasswordBtn = (Button)findViewById(R.id.pupdatepasswordBtn);
        changePasswordTxt = (TextView)findViewById(R.id.changePasswordTxt);
        delAccText = (TextView)findViewById(R.id.delAccText);
        unameEdit = (EditText)findViewById(R.id.unameEdit);
        dobEditBtn = (Button)findViewById(R.id.dobEditBtn);
        pchangePassHeaderLbl = (TextView)findViewById(R.id.pchangePassHeaderLbl);
        pchangePassSubheader = (TextView)findViewById(R.id.pchangePassSubheader);
        changePassBtn = (Button)findViewById(R.id.changePassBtn);
        deleteAccBtn = (Button)findViewById(R.id.deleteAccBtn);
        pcancelChangePassBtn = (Button)findViewById(R.id.pcancelChangePassBtn);
        pChangePasswordHolder = (RelativeLayout)findViewById(R.id.pChangePasswordHolder);
        updatePicBtn = (Button)findViewById(R.id.updatePicBtn);
        setupUI();
    }

    public void setupUI(){
        Typeface arialt = Typeface.createFromAsset(getAssets(),"arial.ttf");
        Typeface arialb = Typeface.createFromAsset(getAssets(),"arialbd.ttf");
        editProfileHeader.setTypeface(arialt);
        pchangePassHeaderLbl.setTypeface(arialb);
        pchangePassSubheader.setTypeface(arialt);
        proSaveBtn.setTypeface(arialt);
        pnewPasswordTxt.setTypeface(arialt);
        pcurrentPassTxt.setTypeface(arialt);
        pconfirmPassTxt.setTypeface(arialt);
        pro_sett_verifyingLbl.setTypeface(arialt);
        pdelAccHeaderLbl.setTypeface(arialb);
        pdelAccLbl1.setTypeface(arialt);
        deleteConfirmBtn.setTypeface(arialt);
        dontdeleteConfirmBtn.setTypeface(arialt);
        dobEditTxt.setTypeface(arialt);
        changePasswordTxt.setTypeface(arialt);
        delAccText.setTypeface(arialt);
        unameEdit.setTypeface(arialt);
        pupdatepasswordBtn.setTypeface(arialt);
        prodateSelectorDP.setMaxDate(validMaxDate().getTime());
        pupdatepasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pcurrentPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.pcurrentPassHolder));
                    return;
                }else if(pnewPasswordTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.pnewpasswordHolder));
                    return;
                }else if(pconfirmPassTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.pconfirmPassHolder));
                    return;
                }else if(pconfirmPassTxt.getText().toString().equalsIgnoreCase(pnewPasswordTxt.getText().toString())==false){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Password mismatch, please re-confirm your new password.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }
                proSettloadingScr.setVisibility(View.VISIBLE);
                new changePassword().execute();
            }
        });


        proSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unameEdit.getText().toString().length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Please give us your name, what shall we call you?");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                   return;
                }
                proSettloadingScr.setVisibility(View.VISIBLE);
                new updateInfo().execute();

            }
        });

        dobEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procalendarHolder.requestFocus();
                procalendarHolder.setVisibility(View.VISIBLE);
            }
        });

        proselectedDOBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = prodateSelectorDP.getDayOfMonth()+"/"+prodateSelectorDP.getMonth()+"/"+prodateSelectorDP.getYear();
                prodobString = prodateSelectorDP.getMonth()+"/"+prodateSelectorDP.getDayOfMonth()+"/"+prodateSelectorDP.getYear();
                SimpleDateFormat dformatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateSel = dformatter.parse(dob);
                    dobEditTxt.setText(""+getDiffYears(dateSel,new Date())+" years old");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                procalendarHolder.setVisibility(View.GONE);

            }
        });

        updatePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(profileSettings.this)
                        .setTitle("Upload Video")
                        .setItems(R.array.SelectImage,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        if (i == 0) {
                                            try {
                                                int result = ContextCompat.checkSelfPermission(profileSettings.this, android.Manifest.permission.CAMERA);
                                                if (result == PackageManager.PERMISSION_GRANTED){


                                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
// ******** code for crop image
                                                    startActivityForResult(intent, CAMERA_REQUEST);

                                                } else {
                                                    ActivityCompat.requestPermissions(profileSettings.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                                }
                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        if (i == 1) {
                                            int result = ContextCompat.checkSelfPermission(profileSettings.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
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
                                                ActivityCompat.requestPermissions(profileSettings.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READEXTERNAL);
                                            }
                                        }
                                    }
                                })
                        .show();
            }
        });

        unameEdit.setText(sharedpreferences.getString("user_name",""));
        SimpleDateFormat dformatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dateSel = dformatter.parse(sharedpreferences.getString("user_dob",""));
            dobEditTxt.setText(""+getDiffYears(dateSel,new Date())+" years old");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String propic = sharedpreferences.getString("user_pic","");
        if(propic.equalsIgnoreCase("NA")==false){
            propic = "https://singlze.com/"+propic;
            Picasso.with(profileSettings.this)
                    .load(propic)
                    .placeholder(R.drawable.defpic)
                    .into(ppprofile_image);

        }

        pcancelDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDeleteAccountHolder.setVisibility(View.GONE);
            }
        });

        dontdeleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDeleteAccountHolder.setVisibility(View.GONE);
            }
        });

        cancelEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDeleteAccountHolder.setVisibility(View.VISIBLE);
            }
        });

        pcancelChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pChangePasswordHolder.setVisibility(View.GONE);
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pChangePasswordHolder.setVisibility(View.VISIBLE);
            }
        });

        deleteConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteAcc().execute();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ppprofile_image.setImageBitmap(photo);
            new uploadPicture().execute();
        }else if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ppprofile_image.setImageBitmap(selectedImage);
                new uploadPicture().execute();
            }catch (Exception r){}
        }
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
            proSettloadingScr.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            proSettloadingScr.setVisibility(View.GONE);
            try {
                if(status.equalsIgnoreCase("1")){
                    status = "0";
                    new updatePicOnServer().execute();
                }else if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
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
                Bitmap bitmap = ((BitmapDrawable)ppprofile_image.getDrawable()).getBitmap();
                File file = getFileForBitmap(bitmap,"propic"+".jpg");
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


    class updateInfo extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/update_info.php");


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
            proSettloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password

                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(profileSettings.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("user_name",unameEdit.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("user_dob",prodobString));
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
                            editor.putString("user_name", unameEdit.getText().toString());
                            editor.putString("user_dob", prodobString);
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


    class changePassword extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/change_pass.php");


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
            proSettloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    new AlertDialog.Builder(profileSettings.this)
                            .setTitle("Password Updated")
                            .setMessage("Please re-login to your 'Singlze' account with your new password.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    Intent i = new Intent(profileSettings.this,accountScr.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            })
                            .show();


                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(profileSettings.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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
                    nameValuePairs.add(new BasicNameValuePair("user_pass",pcurrentPassTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("n_pass",pnewPasswordTxt.getText().toString()));

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



    class deleteAcc extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);
        String camp_id;
        String reason;
        String link;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/del_acc.php");


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
            proSettloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Ask for password
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = new Intent(profileSettings.this,accountScr.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else if (status.equalsIgnoreCase("0")) {
                    //New Sign up
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
//                    Toast.makeText(numberLoginScr.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }

            }
            catch(Exception e){
                Toast.makeText(profileSettings.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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



    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
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

    class updatePicOnServer extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/update_pic.php");


        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            proSettloadingScr.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            proSettloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    String propic = sharedpreferences.getString("user_pic","");
                    if(propic.equalsIgnoreCase("NA")==false){
                        propic = "https://singlze.com/"+propic;
                        Picasso.with(profileSettings.this)
                                .load(propic)
                                .placeholder(R.drawable.defpic)
                                .into(ppprofile_image);

                    }


                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(profileSettings.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    String propic = sharedpreferences.getString("user_pic","");
                    if(propic.equalsIgnoreCase("NA")==false){
                        propic = "https://singlze.com/"+propic;
                        Picasso.with(profileSettings.this)
                                .load(propic)
                                .placeholder(R.drawable.defpic)
                                .into(ppprofile_image);

                    }

                }

            }
            catch(Exception e){
                Toast.makeText(profileSettings.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();
                String propic = sharedpreferences.getString("user_pic","");
                if(propic.equalsIgnoreCase("NA")==false){
                    propic = "https://singlze.com/"+propic;
                    Picasso.with(profileSettings.this)
                            .load(propic)
                            .placeholder(R.drawable.defpic)
                            .into(ppprofile_image);

                }


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
                    nameValuePairs.add(new BasicNameValuePair("user_pic",thumburl));
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
                        try {
                            editor.putString("user_pic", jo.getString("result").toString());
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


}
