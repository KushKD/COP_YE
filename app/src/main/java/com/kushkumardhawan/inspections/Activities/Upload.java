package com.kushkumardhawan.inspections.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.kushkumardhawan.filepicker.activity.FilePickerActivity;
import com.kushkumardhawan.filepicker.config.Configurations;
import com.kushkumardhawan.filepicker.model.MediaFile;
import com.kushkumardhawan.inspections.Abstract.AsyncTaskListener;
import com.kushkumardhawan.inspections.Adapter.FileListAdapter;
import com.kushkumardhawan.inspections.Enums.TaskType;
import com.kushkumardhawan.inspections.Generic.Generic_Async_Post;
import com.kushkumardhawan.inspections.Generic.Generic_Async_UploadFiles;
import com.kushkumardhawan.inspections.Model.User;
import com.kushkumardhawan.inspections.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class Upload extends AppCompatActivity implements AsyncTaskListener {

    //FormData
    //Latitude
    //Longitude
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private String Latitude, Longitude,FormData;
    private final static int FILE_REQUEST_CODE = 1;
    private FileListAdapter fileListAdapter;
    JSONObject documentsData = null;
    String userID,ForDepartment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        Intent getRoomDetailsIntent = getIntent();
        User userdetails = (User) getRoomDetailsIntent.getSerializableExtra("UserPoJO");
        userID = userdetails.id;
        FormData = getRoomDetailsIntent.getStringExtra("FormData");
        Latitude = getRoomDetailsIntent.getStringExtra("Latitude");
        Longitude = getRoomDetailsIntent.getStringExtra("Longitude");
        ForDepartment = getRoomDetailsIntent.getStringExtra("ForDepartment");
        Log.e("Form Data", FormData);
        Log.e("Latitude", Latitude);
        Log.e("Longitude", Longitude);

        RecyclerView recyclerView = findViewById(R.id.file_list);
        fileListAdapter = new FileListAdapter(mediaFiles);
        recyclerView.setAdapter(fileListAdapter);



                findViewById(R.id.image_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload.this, FilePickerActivity.class);
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


        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {
                    documentsData = createJsonFormDocumentsData(mediaFiles);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("formDaa", FormData);
                Log.e("documentsData", documentsData.toString());

                //Upload Data to Server
                new Generic_Async_Post(
                        Upload.this,
                        Upload.this,
                        TaskType.POST_FORM_DATA).
                        execute("PostData", "http://eypoc.com/cmrelief/odisha/api/PostData", FormData, documentsData.toString());

                //Upload Pictures to Server Working
                 new Generic_Async_UploadFiles(  Upload.this,  Upload.this, TaskType.UPLOAD_FILES).execute(mediaFiles);



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
                obj.put("form_id", ForDepartment);

                jsonArray.put(obj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject finalobject = new JSONObject();
        finalobject.put("form_documents", jsonArray);
        return finalobject;
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
    public void onTaskCompleted(String result, TaskType taskType) throws JSONException {

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
            Upload.this.finish();

        }

    }

    @Override
    public void onTaskCompleted(Activity activity, String result, TaskType taskType) {

    }
}
