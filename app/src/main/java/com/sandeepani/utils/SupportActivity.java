package com.sandeepani.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepani.interfaces.IOnSwichChildListener;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SupportActivity extends Activity implements View.OnClickListener,IOnSwichChildListener {

    private TopBar1 topBar;
    private AppController appController = null;
    TextView date1;
    private static final int PICK_FROM_GALLERY=101;
    int columnIndex;
    private String email,subject;
    String childName;

    Button attach,send;
    Uri URI=null;
    EditText msg,path;
    // String imagepath;
    private static final int SELECT_PICTURE = 1;
    private String imagepath=null;

//    private ImageView attachment;
//    private ImageView sendmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        setOnClickListener();
        setTopBar();

    date1=(TextView)findViewById(R.id.date);
    Calendar c = Calendar.getInstance();
    System.out.println("Current time => "+c.getTime());

    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy ");
    String formattedDate = df.format(c.getTime());
    date1.setText(formattedDate);
    }

    private void setOnClickListener() {


        send=(Button)findViewById(R.id.send);
        attach = (Button) findViewById(R.id.attach);
        msg = (EditText) findViewById(R.id.message);
        path = (EditText) findViewById(R.id.path);
        attach.setOnClickListener(this);
        send.setOnClickListener(this);


    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setOnClickListener(this);
        topBar.titleTV.setText(getString(R.string.help));
        // topBar.logoutIV.setOnClickListener(this);
    }
    @Override
    public void onSwitchChild(int selectedChildPosition) {
        childName = Constants.getChildNameAfterSelecting(selectedChildPosition, appController.getParentsData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();


            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagepath = cursor.getString(columnIndex);
            Log.e("Attachment Path:", imagepath);
            URI = Uri.parse("file://" + imagepath);
            cursor.close();
            path.setText("Uploading file path:" +imagepath);


        }
    }
    //    @Override
//    protected void onResume() {
//    	super.onResume();
//    private String getRealPathFromURI(Uri contentUri)
//    {
//        String path = null;
//        String[] proj = { MediaStore.MediaColumns.DATA };
//
//        if("content".equalsIgnoreCase(contentUri.getScheme ()))
//        {
//            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//            if (cursor.moveToFirst()) {
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                path = cursor.getString(column_index);
//                URI=Uri.parse(path);
//            }
//            cursor.close();
//            return path;
//        }
//        else if("file".equalsIgnoreCase(contentUri.getScheme()))
//        {
//            return contentUri.getPath();
//        }
//        return null;
//    }




    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow_iv:
                onBackPressed();
            default:
                if (v == attach)
                {
                    imagepath=null;
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);

                }

                if (v == send) {
                    try {
                        email = "bobby@valuechecktech.com";
                        String mailFrom = StorageManager.readString(this, "username", "");
                        subject ="Saandeepani"+ "("+mailFrom +")";
                        // msg = message.getText().toString();

                        String body=msg.getText().toString();
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{email});

                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);

                        if (URI != null)
                        {
                            emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                        }

                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

                        this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));



                    } catch (Throwable t)
                    {
                        Toast.makeText(this, "Request failed try again: " + t.toString(), Toast.LENGTH_LONG).show();

                    }

                }

        }
    }}

