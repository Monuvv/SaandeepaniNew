package com.sandeepani.view.Teacher;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepani.Networkcall.RequestCompletion;
import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.adapters.CustomAdapter;
import com.sandeepani.adapters.SubjectSpinnerAdapter;
import com.sandeepani.interfaces.AsyncTaskInterface;
import com.sandeepani.model.GradeModel;
import com.sandeepani.model.StudentDTO;
import com.sandeepani.model.SubjectModel;
import com.sandeepani.model.TeacherModel;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.sharedPreference.TeacherDetailsPref;
import com.sandeepani.threads.HttpConnectThread;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.utils.Constants;
import com.sandeepani.utils.TopBar;
import com.sandeepani.view.CommonToApp.BaseActivity;
import com.sandeepani.view.CommonToApp.LoginActivity;
import com.sandeepani.view.R;
import com.sandeepani.webserviceparser.TeacherHomeJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class AssignTaskActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, RequestCompletion, AsyncTaskInterface, AdapterView.OnItemSelectedListener {

    private TopBar topBar;
    private Spinner classSpinner, subjectSpinner;
    private RadioGroup selectStudioRG;
    private Button assignTaskBtn;
    private TextView chooseDateTV;
    private EditText taskET;
    private final int REQUEST_CODE = 1234;
    private boolean updateCheckStatus = false;
    String teacherName = "";
    private String post_url = "/app/teacher/homework/save";
    private TeacherModel teacherModel = null;
    private int selectedGrade = 0, SelectedSubject = 0;
    private CustomAdapter classNamesAdapter = null;
    private SubjectSpinnerAdapter subjectSpinnerAdapter = null;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<StudentDTO> selectedStudents = null;
    private TeacherDetailsPref teacherDetailsPref;

    enum RequestType {
        TYPE_TEACHER_DETAILS, TYPE_SUBJECTS, TYPE_POST_DATA;
    }
    RequestType type = RequestType.TYPE_TEACHER_DETAILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        topBar = (TopBar) findViewById(R.id.topBar);
        topBar.initTopBar();
        topBar.titleTV.setText(getString(R.string.assign_task_title));
    //    topBar.logoutIV.setOnClickListener(this);
        classSpinner = (Spinner) findViewById(R.id.class_spinner);
        classSpinner.setOnItemSelectedListener(this);
        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);
        subjectSpinner.setOnItemSelectedListener(this);
        selectStudioRG = (RadioGroup) findViewById(R.id.select_students_rg);
        assignTaskBtn = (Button) findViewById(R.id.assign_task_btn);
        taskET = (EditText) findViewById(R.id.task_et);
        chooseDateTV = (TextView) findViewById(R.id.choose_date_tv);
        ((TextView) findViewById(R.id.teacher_name_tv)).setText(StorageManager.readString(this, getString(R.string.pref_username), ""));
        chooseDateTV.setOnClickListener(this);
        String teacherName1=StorageManager.readString(this, getString(R.string.pref_username), "");
        teacherName = "/" + teacherName1;
        teacherDetailsPref=new TeacherDetailsPref(getApplicationContext());
           teacherModel =teacherDetailsPref.getChildrenListFromPreference(teacherName1);

         if(teacherModel==null){
        if (CommonUtils.isNetworkAvailable(this)) {
            RequestType type = RequestType.TYPE_TEACHER_DETAILS;
            httpConnectThread = new HttpConnectThread(this, null, this);
            String url = getString(R.string.base_url) + getString(R.string.url_teacher_deatils);


            httpConnectThread.execute(url + teacherName);
           /* Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(AssignTaskActivity.this);
          //  call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);
            call.getCallRequest(test_url);
            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);*/
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }}else{
             setClassSpinner();
         }
        //register with listeners
        assignTaskBtn.setOnClickListener(this);
        topBar.backArrowIV.setOnClickListener(this);
        selectStudioRG.setOnCheckedChangeListener(this);

    }

    private void setClassSpinner() {
        classNamesAdapter = new CustomAdapter(this, R.layout.drop_down_item, teacherModel.getGradeModels());
        classSpinner.setAdapter(classNamesAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_arrow_iv:
                onBackPressed();
                break;
            case R.id.choose_date_tv:
                showDatePicker();
                break;
            case R.id.assign_task_btn:
                postAssignTaskData();
                break;
            case R.id.logoutIV:
                SharedPreferences clearSharedPreferenceForLogout;
                clearSharedPreferenceForLogout = getSharedPreferences("MyChild_Preferences", 0);
                SharedPreferences.Editor editor = clearSharedPreferenceForLogout.edit();
                editor.clear();
                editor.commit();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            default:
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case -1:
                updateCheckStatus = false;
                break;
            case R.id.select_all_rb:
                break;
            case R.id.select_few_rb:
                if (!updateCheckStatus) {
                    Intent intent = new Intent(this, SelectStudentActivity.class);
                    intent.putExtra(getString(R.string.students_data), teacherModel.getGradeModels().get(selectedGrade).getStudentsModels());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
            updateCheckStatus = false;
            selectedStudents = (ArrayList<StudentDTO>) data.getExtras().getSerializable(getString(R.string.students_data));
        } else {
            CommonUtils.getToastMessage(this, "Nothing Selected");
            selectedStudents = null;
            updateCheckStatus = true;
            selectStudioRG.clearCheck();
        }
    }

    @Override
    public void onRequestCompletion(JSONObject responseJson, JSONArray responseArray) {
        Constants.stopProgress(this);

    if(responseJson!=null){
                switch (type) {
                    case TYPE_TEACHER_DETAILS:
                    case TYPE_SUBJECTS:
                        TeacherModel teacherModel = TeacherHomeJsonParser.getInstance().getSubjectListM(responseJson);
                        this.teacherModel.setSubjectsList(teacherModel.getSubjectsList());
                        setSubjectAdapter(teacherModel.getSubjectsList());
                         break;


                }
            }
        }


    private void resetData() {
        calendar = Calendar.getInstance();
        chooseDateTV.setText("");
        taskET.setText("");
        updateCheckStatus = true;
        selectStudioRG.clearCheck();
    }

    @Override
    public void onRequestCompletionError(String error) {
        CommonUtils.getLogs("Response::::" + error);
        Constants.stopProgress(this);
    }

    private void showDatePicker() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        final DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            boolean fired = false;

            public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                CommonUtils.getLogs("Double Fired::" + year + ":" + monthOfYear + ":" + dayOfMonth);
                int month = monthOfYear + 1;
                chooseDateTV.setText(dayOfMonth + "-" + month + "-" + year);
                calendar.set(year, monthOfYear, dayOfMonth);
                if (fired == true) {
                    CommonUtils.getLogs("Double fire occured. Silently-ish returning");
                    return;
                } else {
                    //first time fired
                    fired = true;
                }
                //Normal date picking logic goes here
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void callSubjectsWebService(String classSection) {
        type = RequestType.TYPE_SUBJECTS;
        String postUrl = "/app/subject/" + classSection;
        if (CommonUtils.isNetworkAvailable(this)) {
            Constants.showProgress(this);


            postUrl = getString(R.string.base_url) +postUrl;
            Log.i("TimetableURL", postUrl);
            WebServiceCall call = new WebServiceCall(this);
            call.getJsonObjectResponse(postUrl);
          //  httpConnectThread = new HttpConnectThread(this, null, this);
            ///Log.i("subject::::",postUrl);
            //httpConnectThread.execute(getString(R.string.base_url) + postUrl);
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }

    private void setSubjectAdapter(ArrayList<SubjectModel> list) {
        subjectSpinnerAdapter = new SubjectSpinnerAdapter(this, R.layout.drop_down_item, list);
        subjectSpinner.setAdapter(subjectSpinnerAdapter);
    }

    @Override
    public void setAsyncTaskCompletionListener(String object) {
        CommonUtils.getLogs("Response:::" + object);
        if (object != null && !object.equals("")) {
            switch (type) {
                case TYPE_TEACHER_DETAILS:
                    try {
                        JSONObject obj = new JSONObject(object);
                        teacherModel = TeacherHomeJsonParser.getInstance().getTeacherDetails(obj);
                        setClassSpinner();
                        obj = null;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case TYPE_SUBJECTS:
                    TeacherModel teacherModel = TeacherHomeJsonParser.getInstance().getSubjectsList(object);
                    this.teacherModel.setSubjectsList(teacherModel.getSubjectsList());
                    setSubjectAdapter(teacherModel.getSubjectsList());
                    break;
                case TYPE_POST_DATA:
                    try {
                        JSONObject responseJson = new JSONObject(object);
                        if (responseJson.has("status")) {
                            if (responseJson.getString("status").equals("success")) {
                                if (responseJson.has("message")) {
                                    CommonUtils.getToastMessage(AssignTaskActivity.this, responseJson.getString("message"));
                                    resetData();
                                }
                            } else {
                                if (responseJson.has("message")) {
                                    CommonUtils.getToastMessage(AssignTaskActivity.this, responseJson.getString("message"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnserID = parent.getId();
        switch (spinnserID) {
            case R.id.class_spinner:
                selectedGrade = position;
                TextView tv = (TextView) view.findViewById(R.id.drop_down_item);
                GradeModel gradeModel = (GradeModel) tv.getTag();
                callSubjectsWebService(gradeModel.getGradeName() + "/" + gradeModel.getSection());
                break;
            case R.id.subject_spinner:
                SelectedSubject = position;
                break;
            default:
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void postAssignTaskData() {
        if (CommonUtils.isNetworkAvailable(this)) {
            if (classNamesAdapter == null || classNamesAdapter.getCount() <= 0) {
                CommonUtils.getToastMessage(this, "Please Select Class");
                return;
            } else if (subjectSpinnerAdapter == null || subjectSpinnerAdapter.getCount() <= 0) {
                CommonUtils.getToastMessage(this, "Please Select Subject");
                return;
            } else {
                String messageText = taskET.getText().toString();
                String dueDateText = chooseDateTV.getText().toString();
               /* if(classText.equals("")){
                    CommonUtils.getToastMessage(this, "Please Select Class");
                }else if(subjectText.equals("")){
                    CommonUtils.getToastMessage(this, "Please Select Subject");
                }else */
                if (messageText.equals("")) {
                    CommonUtils.getToastMessage(this, "Please Enter message");
                } else if (dueDateText.equals("")) {
                    CommonUtils.getToastMessage(this, "Please Select Due Date");
                } else if (selectStudioRG.getCheckedRadioButtonId() == -1) {
                    CommonUtils.getToastMessage(this, "Please Select Students");
                } else {
                    type = RequestType.TYPE_POST_DATA;
                    JSONObject jsonObject = new JSONObject();
                    //   Constants.showProgress(AssignTaskActivity.this);
                    try {
                        if (selectStudioRG.getCheckedRadioButtonId() == R.id.select_all_rb) {
                            jsonObject.put("gradeFlag", "g");
                        } else {
                            jsonObject.put("gradeFlag", "s");
                            JSONArray array = new JSONArray();
                            for (StudentDTO dto : selectedStudents) {
                                array.put(dto.getStudentId() + "");
                            }
                            jsonObject.put("studentList", array);
                        }
                        jsonObject.put("grade", teacherModel.getGradeModels().get(selectedGrade).getGradeName());
                        jsonObject.put("section", teacherModel.getGradeModels().get(selectedGrade).getSection());
                        jsonObject.put("subject", teacherModel.getSubjectsList().get(SelectedSubject).getSubjectName());
                        jsonObject.put("homework", teacherModel.getSubjectsList().get(SelectedSubject).getSubjectName() + " Homework");
                        jsonObject.put("dueDate", dueDateText);
                        jsonObject.put("message", messageText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CommonUtils.getLogs("POST data::" + jsonObject);
                  /*  WebServiceCall webServiceCall = new WebServiceCall(this);
                    webServiceCall.postToServer(jsonObject, base_and_post_url);*/
                    httpConnectThread = new HttpConnectThread(this, jsonObject, this);
                    httpConnectThread.execute(getString(R.string.base_url) + getString(R.string.url_post_assign_task));
                    Toast.makeText(getApplicationContext(),"Task Assigned",Toast.LENGTH_LONG).show();
           /* Constants.showProgress(this);
            WebServiceCall call = new WebServiceCall(AssignTaskActivity.this);
          //  call.getCallRequest(getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);
            call.getCallRequest(test_url);
            CommonUtils.getLogs("URL is : " + getString(R.string.base_url) + getString(R.string.url_teacher_deatils) + teacherName);*/
                }
            }
        } else {
            CommonUtils.getToastMessage(this, getString(R.string.no_network_connection));
        }
    }
}
