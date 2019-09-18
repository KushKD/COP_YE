package com.kushkumardhawan.inspections.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kushkumardhawan.filepicker.activity.FilePickerActivity;
import com.kushkumardhawan.filepicker.config.Configurations;
import com.kushkumardhawan.filepicker.model.MediaFile;
import com.kushkumardhawan.filepicker.utils.FilePickerProvider;
import com.kushkumardhawan.inspections.Abstract.AsyncTaskListener;
import com.kushkumardhawan.inspections.Adapter.DepartmentAdapter;
import com.kushkumardhawan.inspections.Adapter.FileListAdapter;
import com.kushkumardhawan.inspections.Adapter.StaticAdapter;
import com.kushkumardhawan.inspections.Adapter.UserListDataAdapter;
import com.kushkumardhawan.inspections.Enums.TaskType;
import com.kushkumardhawan.inspections.Generic.Custom_Dialog;
import com.kushkumardhawan.inspections.Generic.Generic_Async_Get;
import com.kushkumardhawan.inspections.Generic.Generic_Async_Post;
import com.kushkumardhawan.inspections.Generic.Generic_Async_UploadFiles;
import com.kushkumardhawan.inspections.HTTP.HttpFileUpload;
import com.kushkumardhawan.inspections.JsonManager.JsonParse;
import com.kushkumardhawan.inspections.Model.Department;
import com.kushkumardhawan.inspections.Model.Static;
import com.kushkumardhawan.inspections.Model.User;
import com.kushkumardhawan.inspections.R;
import com.kushkumardhawan.inspections.SamplePresenter;
import com.kushkumardhawan.inspections.Utilities.EConstants;
import com.kushkumardhawan.locationmanager.base.LocationBaseActivity;
import com.kushkumardhawan.locationmanager.configuration.LocationConfiguration;
import com.kushkumardhawan.locationmanager.constants.FailType;
import com.kushkumardhawan.locationmanager.constants.ProcessType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


public class MainActivity extends LocationBaseActivity implements SamplePresenter.SampleView, AsyncTaskListener {
    private final static int FILE_REQUEST_CODE = 1;
    private FileListAdapter fileListAdapter;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private ProgressDialog progressDialog;
    private TextView locationText;
    private EditText description, comments, inspectionplace;
    private Spinner questionone, questiontwo, fordepartment;
    private TextView progressBar;
    Integer count = 1;
    private List<Department> departmentList;
    private List<Static> staticList;
    private StaticAdapter staticAdapter;
    private DepartmentAdapter adapter;
    public String forDepartment_, questionone_, questiontwo_, description_, comments_, userID = null;

    private SamplePresenter samplePresenter;
    Custom_Dialog CD = new Custom_Dialog();
    private String Latitude, Longitude;
    JSONStringer formData = null;
    JSONObject documentsData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getRoomDetailsIntent = getIntent();
        final User userdetails = (User) getRoomDetailsIntent.getSerializableExtra("UserPoJO");
        Latitude = getRoomDetailsIntent.getStringExtra("Latitude");
        Longitude = getRoomDetailsIntent.getStringExtra("Longitude");
        Log.e("Main Activity", userdetails.toString());

        description = (EditText) findViewById(R.id.description);
        comments = (EditText) findViewById(R.id.comment);
        inspectionplace = (EditText) findViewById(R.id.inspectionplace);

        questionone = (Spinner) findViewById(R.id.questionone);
        questiontwo = (Spinner) findViewById(R.id.questiontwo);
        fordepartment = (Spinner) findViewById(R.id.fordepartment);
        progressBar = (TextView) findViewById(R.id.progressBar);

        //List
        staticList = new ArrayList<Static>();
        staticList.add(new Static("", "Please Select"));
        staticList.add(new Static("Yes", "Yes"));
        staticList.add(new Static("No", "No"));

        staticAdapter = new StaticAdapter(MainActivity.this, android.R.layout.simple_spinner_item, staticList);
        questionone.setAdapter(staticAdapter);
        questiontwo.setAdapter(staticAdapter);


        SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0);
        boolean Flag_Turotials = settings.getBoolean(EConstants.TUTORIALS_FLAG, false);

//Get All Departments
        new Generic_Async_Get(
                MainActivity.this,
                MainActivity.this,
                TaskType.GET_DEPARTMENTS).
                execute("http://eypoc.com/cmrelief/odisha/api/GetDepartments");


        RecyclerView recyclerView = findViewById(R.id.file_list);
        fileListAdapter = new FileListAdapter(mediaFiles);
        recyclerView.setAdapter(fileListAdapter);
        locationText = findViewById(R.id.locationText);
        samplePresenter = new SamplePresenter(this);
        getLocation();

        questionone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                Static answer = staticAdapter.getItem(pos);
                questionone_ = answer.getValue();
                Log.e("Selected Year", questionone_);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        questiontwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                Static answer = staticAdapter.getItem(pos);
                questiontwo_ = answer.getValue();
                Log.e("Selected Year", questiontwo_);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        fordepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                Department answer = adapter.getItem(pos);
                forDepartment_ = answer.getId();
                Log.e("Selected Year", questiontwo_);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                description_ = description.getText().toString();
                comments_ = comments.getText().toString();
                userID = userdetails.id;

                formData = createJsonUserFormData(forDepartment_, questionone_, questiontwo_, description_, comments_, userID);
                Log.e("formDaa", formData.toString());
                try {
                    documentsData = createJsonFormDocumentsData(mediaFiles);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("formDaa", formData.toString());
                Log.e("documentsData", documentsData.toString());

                //Upload Data to Server
                new Generic_Async_Post(
                        MainActivity.this,
                        MainActivity.this,
                        TaskType.POST_FORM_DATA).
                        execute("PostData", "http://eypoc.com/cmrelief/odisha/api/PostData", formData.toString(), documentsData.toString());

          //Upload Pictures to Server Working
          new Generic_Async_UploadFiles(  MainActivity.this,  MainActivity.this, TaskType.UPLOAD_FILES).execute(mediaFiles);
            }




        });


        findViewById(R.id.launch_imagePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setSelectedMediaFiles(mediaFiles)
                        .enableImageCapture(true)
                        .setShowVideos(false)
                        .setSkipZeroSizeFiles(true)
                        .setMaxSelection(10)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });

//        findViewById(R.id.launch_videoPicker).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
//                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
//                        .setCheckPermission(true)
//                        .setSelectedMediaFiles(mediaFiles)
//                        .enableVideoCapture(true)
//                        .setShowImages(false)
//                        .setMaxSelection(10)
//                        .setIgnorePaths(".*WhatsApp.*")
//                        .build());
//                startActivityForResult(intent, FILE_REQUEST_CODE);
//            }
//        });

//        findViewById(R.id.launch_audioPicker).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
//                MediaFile file = null;
//                for (int i = 0; i < mediaFiles.size(); i++) {
//                    if (mediaFiles.get(i).getMediaType() == MediaFile.TYPE_AUDIO) {
//                        file = mediaFiles.get(i);
//                    }
//                }
//                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
//                        .setCheckPermission(true)
//                        .setShowImages(false)
//                        .setShowVideos(false)
//                        .setShowAudios(true)
//                        .setSingleChoiceMode(true)
//                        .setSelectedMediaFile(file)
//                        .build());
//                startActivityForResult(intent, FILE_REQUEST_CODE);
//            }
//        });

        findViewById(R.id.launch_filePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setSelectedMediaFiles(mediaFiles)
                        .setShowFiles(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setMaxSelection(10)
                        .setRootPath(Environment.getExternalStorageDirectory().getPath() + "/Download")
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });
    }

    private JSONObject createJsonFormDocumentsData(ArrayList<MediaFile> mediaFiles) throws JSONException {
        JSONStringer userJson = null;
        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < mediaFiles.size(); i++) {
                obj = new JSONObject();

                obj.put("document_name", mediaFiles.get(i).getName());
                obj.put("mime_type", mediaFiles.get(i).getMimeType());
                obj.put("date", mediaFiles.get(i).getDate());
                obj.put("bucket_name", mediaFiles.get(i).getBucketName());
                obj.put("path", mediaFiles.get(i).getPath());
                obj.put("size", mediaFiles.get(i).getSize());
                obj.put("latitude", Latitude);
                obj.put("longitude", Longitude);
                obj.put("user_id", userID);
                obj.put("form_id", forDepartment_);

                jsonArray.put(obj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject finalobject = new JSONObject();
        finalobject.put("form_documents", jsonArray);
        return finalobject;
    }

    private JSONStringer createJsonUserFormData(String forDepartment_, String questionone_, String questiontwo_, String description_, String comments_, String userID) {
        JSONStringer userJson = null;
        try {
            userJson = new JSONStringer()
                    .object()
                    .key("formdata")
                    .object()
                    .key("forDepartment").value(forDepartment_)
                    .key("questionone").value(questionone_)
                    .key("questiontwo").value(questiontwo_)
                    .key("description").value(description_)
                    .key("comments").value(comments_)
                    .key("usr_id").value(userID)
                    .key("latitude").value(Latitude)
                    .key("longitude").value(Longitude)
                    .key("form_id").value("1")
                    .endObject()
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println(userJson.toString());
        //Log.e("Object",userJson.toString());
        return userJson;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            mediaFiles.clear();
            mediaFiles.addAll(data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES));
            fileListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_log) {
            shareLogFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareLogFile() {
        File logFile = new File(getExternalCacheDir(), "logcat.txt");
        try {
            if (logFile.exists())
                logFile.delete();
            logFile.createNewFile();
            Runtime.getRuntime().exec("logcat -f " + logFile.getAbsolutePath() + " -t 100 *:W Glide:S " + FilePickerActivity.TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (logFile.exists()) {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("text/plain");
            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    FilePickerProvider.getUriForFile(this, logFile));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "FilePicker Log File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "FilePicker Log File");
            startActivity(Intent.createChooser(intentShareFile, "Share"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        samplePresenter.destroy();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return com.kushkumardhawan.locationmanager.configuration.Configurations.defaultConfiguration("Gimme the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {
        samplePresenter.onLocationChanged(location);
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        samplePresenter.onLocationFailed(failType);
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        samplePresenter.onProcessTypeChanged(processType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            displayProgress();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissProgress();
    }

    private void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
            progressDialog.setMessage("Getting location...");
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public String getText() {
        return locationText.getText().toString();
    }

    @Override
    public void setText(String text) {
        locationText.setText(text);
    }

    @Override
    public void updateProgress(String text) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(text);
        }
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) throws JSONException {
        if (taskType == TaskType.GET_DEPARTMENTS) {
            Log.e("Results", result);
            JSONObject jsonResponse;
            String Data = JsonParse.GetUserResponse(result);
            JSONArray jsonarray = new JSONArray(Data);
            departmentList = new ArrayList<Department>();
            for (int i = 0; i < jsonarray.length(); i++) {
                Department deoartmentDetails = new Department();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                deoartmentDetails.setId(jsonobject.getString("dept_id"));
                deoartmentDetails.setName(jsonobject.getString("department_name"));

                departmentList.add(deoartmentDetails);
            }


            Log.e("Length", Integer.toString(departmentList.size()));

            if (departmentList.size() > 0) {
                adapter = new DepartmentAdapter(this, R.layout.user_list, departmentList);
                fordepartment.setAdapter(adapter);
            } else {
                CD.showDialog(MainActivity.this, "Unable to fetch the Departments from the Server");
            }
        }

    }

    @Override
    public void onTaskCompleted(ArrayList<String> result, TaskType taskType) throws JSONException {
        if (taskType == TaskType.UPLOAD_FILES) {
            ArrayList<String> resultServer = result;
            for (int i = 0; i < resultServer.size(); i++) {
                Log.e("Message", resultServer.get(i).toString());
            }
            mediaFiles.clear();
            fileListAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onTaskCompleted(Activity activity, String result, TaskType taskType) {

    }
}

