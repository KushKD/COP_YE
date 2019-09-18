package com.kushkumardhawan.inspections.Generic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kushkumardhawan.filepicker.model.MediaFile;
import com.kushkumardhawan.inspections.Abstract.AsyncTaskListener;
import com.kushkumardhawan.inspections.Enums.TaskType;
import com.kushkumardhawan.inspections.HTTP.HttpFileUpload;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Generic_Async_UploadFiles extends AsyncTask<ArrayList<MediaFile>, Integer, ArrayList<String>> {


    String outputStr;
    ProgressDialog dialog;
    Context context;
    AsyncTaskListener taskListener;
    TaskType taskType;
    private ProgressDialog mProgressDialog;
    ArrayList<String> imagename = new ArrayList<>();


    public Generic_Async_UploadFiles(Context context, AsyncTaskListener taskListener, TaskType taskType){
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Uploading");
        mProgressDialog.setMessage("Uploading Files and Images, Please Wait!");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<MediaFile>... mediaFiles) {
        HttpFileUpload hfu = null;
        try {



            FileInputStream fstrm = null;
            try {
                //  mediaFiles.
                ArrayList<MediaFile> myList = mediaFiles[0];
                mProgressDialog.setMax((int)(myList.size()));

                Log.e("List Size", Integer.toString(myList.size()));
                for (int i = 0; i < myList.size(); i++) {
                    publishProgress((int) i+1);
                    Log.e("File Name", myList.get(i).getName());

                    //name, path
                    Log.e("name",myList.get(i).getName());
                    Log.e("path",myList.get(i).getPath());
                    File f = new File(myList.get(i).getPath());
                    Log.e("file Name", f.getName());
                    fstrm = new FileInputStream(myList.get(i).getPath());
                    hfu = new HttpFileUpload("http://eypoc.com/cmrelief/odisha/api/UploadImages",  f.getName(),myList.get(i).getPath());
                    System.out.println("Connection ...Please wait");
                    hfu.Send_Now(fstrm);
                    imagename.add(myList.get(i).getName()+" \t"+hfu.ResponceCode);

                }

            } catch (FileNotFoundException e) {   //FileNotFoundException
                e.printStackTrace();
            }



        } catch (Exception e) {
            // Error: File not found
            System.out.println("Exception is"+e.getMessage());
        }

        // hfu.ResponceCode;
        return imagename;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
        Log.e("Progress",Integer.toString(values[0]));
        mProgressDialog.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);

        try {
            taskListener.onTaskCompleted(result, taskType);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mProgressDialog.dismiss();
    }



}