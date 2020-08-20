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
import android.widget.EditText;
import android.widget.ImageView;
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
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileScr extends AppCompatActivity {
    int selImageUpload;
    Button editProBackBtn,cUpdateProBtn,mainProPictureBtn;
    TextView editProfileHeader,editProSinglzeID;
    TextView editProCountryLbl,editProGenderLbl,editProDOBLbl,edit_verifyingLbl;
    CircleImageView mainProPicture;
    Button selLocationBtn;
    EditText editNameTxt,editStatusTxt,editLocationTxt,editProTagsTxt,editProAboutMeTxt;
    RelativeLayout editproloadingScr;
    public static final String UDATAPREFERENCES = "UDATA";
    SharedPreferences sharedpreferences;
    ImageView editPic2,editPic3,editPic4,editPic5;
    Button editPic2Btn,editPic3Btn,editPic4Btn,editPic5Btn;
    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    int MY_PERMISSIONS_REQUEST_READEXTERNAL=1;
    private static int CAMERA_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;
    String status="";
    String imgLink1,imgLink2,imgLink3,imgLink4,imgLink5;
    Bitmap probmp, pic2bmp,pic3bmp,pic4bmp,pic5bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_scr);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        selImageUpload = -1;
        sharedpreferences = getSharedPreferences(UDATAPREFERENCES, Context.MODE_PRIVATE);
        selLocationBtn = (Button)findViewById(R.id.selLocationBtn);
        mainProPictureBtn = (Button)findViewById(R.id.mainProPictureBtn);
        editproloadingScr = (RelativeLayout)findViewById(R.id.editproloadingScr);
        edit_verifyingLbl = (TextView)findViewById(R.id.edit_verifyingLbl);
        editPic2Btn = (Button)findViewById(R.id.editPic2Btn);
        editPic3Btn = (Button)findViewById(R.id.editPic3Btn);
        editPic4Btn = (Button)findViewById(R.id.editPic4Btn);
        editPic5Btn = (Button)findViewById(R.id.editPic5Btn);
        editPic2 = (ImageView)findViewById(R.id.editPic2);
        editPic3 = (ImageView)findViewById(R.id.editPic3);
        editPic4 = (ImageView)findViewById(R.id.editPic4);
        editPic5 = (ImageView)findViewById(R.id.editPic5);
        cUpdateProBtn = (Button)findViewById(R.id.cUpdateProBtn);
        editProAboutMeTxt = (EditText)findViewById(R.id.editProAboutMeTxt);
        editProTagsTxt = (EditText)findViewById(R.id.editProTagsTxt);
        editProfileHeader = (TextView)findViewById(R.id.editProfileHeader);
        Typeface tp = Typeface.createFromAsset(getAssets(),"arial.ttf");
        editProfileHeader.setTypeface(tp);
        editProCountryLbl = (TextView)findViewById(R.id.editProCountryLbl);
        editProBackBtn = (Button)findViewById(R.id.editProBackBtn);
        editProSinglzeID = (TextView)findViewById(R.id.editProSinglzeID);
        editProDOBLbl = (TextView)findViewById(R.id.editProDOBLbl);
        editProGenderLbl = (TextView)findViewById(R.id.editProGenderLbl);
        editNameTxt = (EditText)findViewById(R.id.editNameTxt);
        editStatusTxt = (EditText)findViewById(R.id.editStatusTxt);
        editLocationTxt = (EditText)findViewById(R.id.editLocationTxt);
        mainProPicture = (CircleImageView)findViewById(R.id.mainProPicture);
        editProSinglzeID.setTypeface(tp);
        cUpdateProBtn.setTypeface(tp);
        edit_verifyingLbl.setTypeface(tp);
        editProAboutMeTxt.setTypeface(tp);
        editProTagsTxt.setTypeface(tp);
        editProGenderLbl.setTypeface(tp);
        editProCountryLbl.setTypeface(tp);
        editLocationTxt.setTypeface(tp);
        editStatusTxt.setTypeface(tp);
        editNameTxt.setTypeface(tp);

        editProTagsTxt.setText(sharedpreferences.getString("u_tags",""));
        if(editProTagsTxt.getText().toString().equalsIgnoreCase("NA")){
            editProTagsTxt.setText("");
        }
        editNameTxt.setText(sharedpreferences.getString("user_name",""));
        editStatusTxt.setText(sharedpreferences.getString("u_status",""));
        editLocationTxt.setText(sharedpreferences.getString("u_hometown",""));
        editProSinglzeID.setText(sharedpreferences.getString("u_singlze_id",""));
        editProCountryLbl.setText(sharedpreferences.getString("u_hometown",""));
        editProGenderLbl.setText(sharedpreferences.getString("user_gender",""));
        editProDOBLbl.setText(sharedpreferences.getString("user_dob",""));
        imgLink1 = sharedpreferences.getString("user_pic","");
        imgLink2 = sharedpreferences.getString("u_pic2","");
        imgLink3 = sharedpreferences.getString("u_pic3","");
        imgLink4 = sharedpreferences.getString("u_pic4","");
        imgLink5 = sharedpreferences.getString("u_pic5","");
        String about =  sharedpreferences.getString("u_about_me","");
        if(about.equalsIgnoreCase("NA")==false){
            editProAboutMeTxt.setText(about);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dated = formatter.parse(editProDOBLbl.getText().toString());
            formatter = new SimpleDateFormat("dd MMM, yyyy");
            editProDOBLbl.setText(formatter.format(dated));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        settingupView();
    }


    public void settingupView(){
        String propic = sharedpreferences.getString("user_pic","");
        if(propic.equalsIgnoreCase("NA")==false){
            propic = "https://singlze.com/"+propic;
            Picasso.with(this)
                    .load(propic)
                    .placeholder(R.drawable.defpic)
                    .into(mainProPicture);

        }

        String propic2 = sharedpreferences.getString("u_pic2","");
        if(propic2.equalsIgnoreCase("NA")==false){
            propic2 = "https://singlze.com/"+propic2;
            Picasso.with(this)
                    .load(propic2)
                    .placeholder(R.drawable.defpicedit)
                    .into(editPic2);

        }

        String propic3 = sharedpreferences.getString("u_pic3","");
        if(propic3.equalsIgnoreCase("NA")==false){
            propic3 = "https://singlze.com/"+propic3;
            Picasso.with(this)
                    .load(propic3)
                    .placeholder(R.drawable.defpicedit)
                    .into(editPic3);
        }

        String propic4 = sharedpreferences.getString("u_pic4","");
        if(propic4.equalsIgnoreCase("NA")==false){
            propic4 = "https://singlze.com/"+propic4;
            Picasso.with(this)
                    .load(propic4)
                    .placeholder(R.drawable.defpicedit)
                    .into(editPic4);
        }

        String propic5 = sharedpreferences.getString("u_pic5","");
        if(propic5.equalsIgnoreCase("NA")==false){
            propic5 = "https://singlze.com/"+propic5;
            Picasso.with(this)
                    .load(propic5)
                    .placeholder(R.drawable.defpicedit)
                    .into(editPic5);
        }

        selLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileScr.this,selectCountryScr.class);
                i.putExtra("ISNAME","YES");
                startActivityForResult(i,999);
            }
        });

        editProBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mainProPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selImageUpload = 1;
                showImageSelection();
            }
        });

        editPic2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selImageUpload = 2;
                showImageSelection();
            }
        });

        editPic3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selImageUpload = 3;
                showImageSelection();
            }
        });

        editPic4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selImageUpload = 4;
                showImageSelection();
            }
        });

        editPic5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selImageUpload = 5;
                showImageSelection();
            }
        });

        cUpdateProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNameTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.editNameHolder));
                    return;
                }else if(editLocationTxt.getText().toString().length()==0){
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.editLocationHolder));

                    return;
                }
                if(probmp!=null) {
                    status = "";
                    selImageUpload =1;
                    new uploadPicture().execute();
                }else if(pic2bmp!=null){
                    status = "";
                    selImageUpload =2;
                    new uploadPicture().execute();
                }else if(pic3bmp!=null){
                    status = "";
                    selImageUpload =3;
                    new uploadPicture().execute();
                }else if(pic4bmp!=null){
                    status = "";
                    selImageUpload =4;
                    new uploadPicture().execute();
                }else if(pic5bmp!=null){
                    status = "";
                    selImageUpload =5;
                    new uploadPicture().execute();
                }else{
                    status = "";
                    edit_verifyingLbl.setText("Saving Profile...");
                    new updateProfile().execute();
                }
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
            editproloadingScr.setVisibility(View.VISIBLE);
            edit_verifyingLbl.setText("Uploading Image(s)...");
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if(status.equalsIgnoreCase("1")){
                    status = "0";
                    if(selImageUpload==1){
                        if(pic2bmp!=null) {
                            selImageUpload = 2;
                            new uploadPicture().execute();
                        }else if(pic3bmp!=null){
                            selImageUpload = 3;
                            new uploadPicture().execute();
                        }else if(pic4bmp!=null){
                            selImageUpload = 4;
                            new uploadPicture().execute();
                        }else if(pic5bmp!=null){
                            selImageUpload = 5;
                            new uploadPicture().execute();
                        }else{
                            edit_verifyingLbl.setText("Saving Profile...");
                            new updateProfile().execute();
                        }
                    }else if(selImageUpload==2){
                        if(pic3bmp!=null) {
                            selImageUpload = 3;
                            new uploadPicture().execute();
                        }else if(pic4bmp!=null){
                            selImageUpload = 4;
                            new uploadPicture().execute();
                        }else if(pic5bmp!=null){
                            selImageUpload = 5;
                            new uploadPicture().execute();
                        }else{
                            edit_verifyingLbl.setText("Saving Profile...");
                            new updateProfile().execute();
                        }
                    }else if(selImageUpload==3){
                        if(pic4bmp!=null) {
                            selImageUpload = 4;
                            new uploadPicture().execute();
                        }else if(pic5bmp!=null){
                            selImageUpload = 5;
                            new uploadPicture().execute();
                        }else{
                            edit_verifyingLbl.setText("Saving Profile...");
                            new updateProfile().execute();
                        }
                    }else if(selImageUpload==4){
                        if(pic5bmp!=null) {
                            selImageUpload = 5;
                            new uploadPicture().execute();
                        }else{
                            edit_verifyingLbl.setText("Saving Profile...");
                            new updateProfile().execute();
                        }
                    }else if(selImageUpload==5){
                        edit_verifyingLbl.setText("Saving Profile...");
                        new updateProfile().execute();
                    }
                }else if (status.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileScr.this);
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
                Bitmap bitmap = null;
                if(selImageUpload==1) {
                    bitmap = probmp;
                }else if(selImageUpload==2){
                    bitmap = pic2bmp;
                }else if(selImageUpload==3){
                    bitmap = pic3bmp;
                }else if(selImageUpload==4){
                    bitmap = pic4bmp;
                }else if(selImageUpload==5){
                    bitmap = pic5bmp;
                }
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
                    if(selImageUpload==1) {
                        imgLink1 = jo.getString("link");
                    }else if(selImageUpload==2) {
                        imgLink2 = jo.getString("link");
                    }else if(selImageUpload==3) {
                        imgLink3 = jo.getString("link");
                    }else if(selImageUpload==4) {
                        imgLink4 = jo.getString("link");
                    }else if(selImageUpload==5) {
                        imgLink5 = jo.getString("link");
                    }
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



    public void showImageSelection(){
        new AlertDialog.Builder(EditProfileScr.this)
                .setTitle("Upload Image")
                .setItems(R.array.SelectImage,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                if (i == 0) {
                                    try {
                                        int result = ContextCompat.checkSelfPermission(EditProfileScr.this, android.Manifest.permission.CAMERA);
                                        if (result == PackageManager.PERMISSION_GRANTED){


                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
// ******** code for crop image
                                            startActivityForResult(intent, CAMERA_REQUEST);

                                        } else {
                                            ActivityCompat.requestPermissions(EditProfileScr.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                        }
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                if (i == 1) {
                                    int result = ContextCompat.checkSelfPermission(EditProfileScr.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
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
                                        ActivityCompat.requestPermissions(EditProfileScr.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READEXTERNAL);
                                    }
                                }
                            }
                        })
                .show();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode==RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if(selImageUpload==1){
                probmp = photo;
                mainProPicture.setImageBitmap(photo);
            }else if(selImageUpload==2){
                pic2bmp = photo;
                editPic2.setImageBitmap(photo);
            }else if(selImageUpload==3){
                pic3bmp = photo;
                editPic3.setImageBitmap(photo);
            }else if(selImageUpload==4){
                pic4bmp = photo;
                editPic4.setImageBitmap(photo);
            }else if(selImageUpload==5){
                pic5bmp = photo;
                editPic5.setImageBitmap(photo);
            }
        }else if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                if(selImageUpload==1){
                    probmp = selectedImage;
                    mainProPicture.setImageBitmap(selectedImage);
                }else if(selImageUpload==2){
                    pic2bmp = selectedImage;
                    editPic2.setImageBitmap(selectedImage);
                }else if(selImageUpload==3){
                    pic3bmp = selectedImage;
                    editPic3.setImageBitmap(selectedImage);
                }else if(selImageUpload==4){
                    pic4bmp = selectedImage;
                    editPic4.setImageBitmap(selectedImage);
                }else if(selImageUpload==5){
                    pic5bmp = selectedImage;
                    editPic5.setImageBitmap(selectedImage);
                }
            }catch (Exception r){}
        }else if(requestCode==999){
            String checker = data.getData().toString();
            if(checker.equalsIgnoreCase("CANCEL")==false) {
                editLocationTxt.setText(data.getData().toString());
            }
        }
    }

    class updateProfile extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(validate.this);

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return getResponse1("https://singlze.com/API/updateProfile.php");


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
            editproloadingScr.setVisibility(View.GONE);
            try {
                if(status.length()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return;
                }

                if(status.equalsIgnoreCase("1")){
                    //Success
                    editProBackBtn.performClick();
                }else if (status.equalsIgnoreCase("0")) {
                    //Failed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileScr.this);
                    builder1.setMessage("Something went wrong, please try again. If problem persists, contact us.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else if(status.equalsIgnoreCase("5")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileScr.this);
                    builder1.setMessage("Oops! Select other Singlze ID, this one is already occupied.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditProfileScr.this);
                    builder1.setMessage("Seems like a connection issue, please check and try again.");
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
            catch(Exception e){
                Toast.makeText(EditProfileScr.this, "Oops! Seems like a connection issue, please check and try again.", Toast.LENGTH_SHORT).show();

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

                    String about = editProAboutMeTxt.getText().toString();
                    if(about.length()==0){
                        about = "NA";
                    }
                    String tags = editProTagsTxt.getText().toString();
                    if(tags.length()==0){
                        tags = "NA";
                    }

                    String statePro = editStatusTxt.getText().toString();
                    if(statePro.length()==0){
                        statePro = "NA";
                    }
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("appkey","85E2EH48WU20SRJDPE33"));
                    nameValuePairs.add(new BasicNameValuePair("appapp","SINGLZE"));
                    nameValuePairs.add(new BasicNameValuePair("user_loc",editLocationTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("user_pic",imgLink1));
                    nameValuePairs.add(new BasicNameValuePair("u_pic2",imgLink2));
                    nameValuePairs.add(new BasicNameValuePair("u_pic3",imgLink3));
                    nameValuePairs.add(new BasicNameValuePair("u_pic4",imgLink4));
                    nameValuePairs.add(new BasicNameValuePair("u_pic5",imgLink5));
                    nameValuePairs.add(new BasicNameValuePair("user_name",editNameTxt.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("user_status",statePro));
                    nameValuePairs.add(new BasicNameValuePair("user_tags",tags));
                    nameValuePairs.add(new BasicNameValuePair("user_about",about));
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



}
