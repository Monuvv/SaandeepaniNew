package com.sandeepani.view.Parent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.model.ParentModel;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar1;
import com.sandeepani.view.CommonToApp.BaseFragmentActivity;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;
import com.sandeepani.webserviceparser.TeacherListForChildParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ParentWriteMailToTeacher2 extends BaseFragmentActivity implements RequestCompletion, View.OnClickListener {
    public static final String TAG = ParentWriteMailToTeacher2.class.getSimpleName();
    private TopBar1 topBar;
    //private SwitchChildView switchChild;
    private ParentModel parentModel = null;
    private AppController appController = null;
    private int selectedChildPosition = 0;
    private Dialog dialog = null;
    private ImageView backButton,url_bt1;
    private EditText subject,message;
    private AutoCompleteTextView to;
    private ImageView sendMail;
    String responseType;
    TextView date;
    EditText url_ed1;
    private ArrayAdapter<String> arrayAdapter;
    public static List<String> ll ;
    AutoCompleteTextView textView;
    HashMap<String,String> maildMap=new HashMap<String,String>();
    Boolean NoreplyMail=true;
    public String pm;
    public Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_write_mail_to_teacher2);
        onClickListeners();
        setTopBar();
        //  switchChildBar();
        UpdateUI();
        if(NoreplyMail) {
            getTeacherList();
        }
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy ");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();


        // Now we display formattedDate value in TextView

        date.setText(formattedDate);
        message.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView V, int i, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    message.setSelection(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
                    return true;
                } else {
                    return false;
                }


            }
        });

    }

    String mailToId;
    public void UpdateUI(){
        Intent intent = getIntent();
        if(intent.hasExtra("mailto")) {
            extras = intent.getExtras();
            if (extras != null) {
                NoreplyMail=false;
                textView.setText(extras.getString("mailto"));
                message.setText(extras.getString("msg"));
                textView.setFocusable(false);
                mailToId=extras.getString("mailToId");
                maildMap.put(extras.getString("mailto"),mailToId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // switchChild.childNameTV.setText(Constants.SWITCH_CHILD_FLAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;

            case R.id.switch_child:
                if (parentModel.getChildList() != null) {
                    dialog = CommonUtils.getSwitchChildDialog(this, parentModel.getChildList(), selectedChildPosition);
                } else {
                    Toast.makeText(this, "No Child data found..", Toast.LENGTH_LONG).show();
                }

                break;
            //  case R.id.url_mail_btn:
//                Constants.showProgress(this);
//                postEailToServer();
            //    Intent intent=new Intent();
            //  intent .setType("*/*");
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(intent,"complete action using"),1);
            //break;

            case R.id.send_mail_btn:
                Constants.showProgress(this);
                postEailToServer();
                break;
//            case R.id.logoutIV:
//                Toast.makeText(this, "Clicked Logout", Toast.LENGTH_LONG).show();
//                Constants.logOut(this);
//
//                Intent i = new Intent(this, LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//
//                break;

            default:
                //Enter code in the event that that no cases match
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath();

            Uri selectedImageUri = data.getData();
            String imagepath =getRealPathFromURI(selectedImageUri);
            //bitmap=BitmapFactory.decodeFile(imagepath);
            //imageview.setImageBitmap(bitmap);
            //  url_ed1.setText("Uploading file path:" +imagepath);

        }
    }


    private String getRealPathFromURI(Uri contentUri)
    {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };

        if("content".equalsIgnoreCase(contentUri.getScheme ()))
        {
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                path = cursor.getString(column_index);
            }
            cursor.close();
            return path;
        }
        else if("file".equalsIgnoreCase(contentUri.getScheme()))
        {
            return contentUri.getPath();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        ll = new ArrayList<String>();
        String status = null;
        //   String[] countries=null;
        if(responseType == "JSONARRAY"){
            CommonUtils.getLogs("get teacher Response is success...");
            if(responseArray!=null && responseArray.length()>0) {
                Log.i(TAG, responseArray.toString());
                HashMap<String, String> teacherListForChild = TeacherListForChildParser.getInstance().getTeacherList(responseArray);
                for (int i = 0; i < teacherListForChild.size(); i++) {
                    ll.addAll(teacherListForChild.keySet());
                    //Log.e("--------", teacherListForChild.get(i).get("username").toString());
                    maildMap=teacherListForChild;
                }
            }
            arrayAdapter = new DropDownAdapter(this,
                    R.layout.arealist_suggestion, R.id.searchdropdown,
                    ll);
            textView.setThreshold(1);
            textView.setAdapter(arrayAdapter);
        }
        else {
            CommonUtils.getLogs("posting mail Response is success...");
            Log.i(TAG, responseJson.toString());
            status = getEmailSentStatus(responseJson);
            if(status.contains("success")){
                CommonUtils.getToastMessage(this, "Email Sent.");
                finish();
                Intent i=new Intent(this,ParentInboxActivity.class);
                startActivity(i);

            }
            else {
                CommonUtils.getToastMessage(this, "Email Not Sent.");
            }
        }
        Constants.stopProgress(this);
    }

    @Override
    public void onRequestCompletionError(String error) {
        Constants.stopProgress(this);
        CommonUtils.getLogs("posting mail Failure....");
        Constants.stopProgress(this);
        Constants.showMessage(this, "Sorry", error);

    }

    public void onClickListeners(){
        backButton = (ImageView) findViewById(R.id.back);
        sendMail = (ImageView) findViewById(R.id.send_mail_btn);
        to = (AutoCompleteTextView ) findViewById(R.id.mail_toET);
        subject = (EditText) findViewById(R.id.mail_subjectET);
        message = (EditText) findViewById(R.id.mail_messageET);
        textView = (AutoCompleteTextView) findViewById(R.id.mail_toET);
        // url_bt1=(ImageView)findViewById(R.id.url_mail_btn);
        //url_ed1=(EditText)findViewById(R.id.mail_url);
        date=(TextView)findViewById(R.id.date);
        backButton.setOnClickListener(this);
        sendMail.setOnClickListener(this);
        //   url_bt1.setOnClickListener(this);

    }

    public void setTopBar() {
        topBar = (TopBar1) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.backArrowIV.setVisibility(View.INVISIBLE);
        topBar.titleTV.setText(getString(R.string.inbox));
        //  topBar.logoutIV.setOnClickListener(this);
    }

    public void switchChildBar() {
        // switchChild = (SwitchChildView) findViewById(R.id.switchchildBar);
        //switchChild.initSwitchChildBar();
        //switchChild.childNameTV.setText("Name");
    }


    public void setSwitchChildDialogueData() {

        appController = (AppController) getApplicationContext();
        parentModel = appController.getParentsData();
        if (parentModel != null && parentModel.getNumberOfChildren() >= 0) {
            selectedChildPosition = appController.getSelectedChild();
        }
    }

    public  void getTeacherList(){
        responseType = "JSONARRAY";
        Constants.showProgress(this);
        String Url_teacher_list = null;
        if (CommonUtils.isNetworkAvailable(this)) {

            Url_teacher_list = getString(R.string.base_url) + "/app/parent/getUsers/1";
            Log.i("===list_teacher===", Url_teacher_list);
            WebServiceCall call = new WebServiceCall(this);
            call.getCallRequest(Url_teacher_list);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    public void postEailToServer() {
        responseType = "JSONOBJECT";
        if (CommonUtils.isNetworkAvailable(this)) {
            String mailFrom = StorageManager.readString(this, "username", "");

            String mailTo = to.getText().toString();
            // String mailToString[] = mailTo.split("=");
            Log.i("----->123",mailTo);
            String mailToStringdata = to.getText().toString();
            Log.i("----->1234",mailToStringdata);
            //  pm=  extras.getString("msg");


            String mailSubject = subject.getText().toString();
            String mailMessage = message.getText().toString();
            if (mailTo.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter To Field");
                Constants.stopProgress(this);
            } else if (mailSubject.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Subject");
                Constants.stopProgress(this);
            } else if (mailMessage.equals("")) {
                CommonUtils.getToastMessage(this, "Please Enter Message");
                Constants.stopProgress(this);
            }
            else {
                JSONObject jsonObject = new JSONObject();
                String mailId=maildMap.get(mailToStringdata)!=null?maildMap.get(mailToStringdata):mailToStringdata;
                try {
                    jsonObject.put("toId", mailId);
                    jsonObject.put("fromId", mailFrom);
                    jsonObject.put("title", mailSubject);
                    jsonObject.put("messageText", mailMessage);
                    // jsonObject.put("messageText", mailMessage +"\n"+"\n"+"Reply of... "+"\n" +pm);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommonUtils.getLogs("EMAIL POST PARENT ::" + jsonObject);
                String emailPostURL = getString(R.string.base_url)+getString(R.string.parent_email_to_teahcer);
                WebServiceCall webServiceCall = new WebServiceCall(this);
                webServiceCall.postToServer(jsonObject, emailPostURL);
            }
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }

    }


    public String getEmailSentStatus(JSONObject response){
        String mailSentStatus = null;
        try {
            if (response.has("status")) {
                mailSentStatus = response.getString("status");
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        CommonUtils.getLogs(mailSentStatus);
        return mailSentStatus;
    }

}
