package com.kushkumardhawan.inspections.Generic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.kushkumardhawan.inspections.Abstract.AsyncTaskListener;
import com.kushkumardhawan.inspections.Enums.TaskType;
import com.kushkumardhawan.inspections.HTTP.HttpManager;

import org.json.JSONException;

import java.io.IOException;

public class Generic_Async_Post extends AsyncTask<String,Void ,String> {


    String outputStr;
    ProgressDialog dialog;
    Context context;
    AsyncTaskListener taskListener;
    TaskType taskType;

    public Generic_Async_Post(Context context, AsyncTaskListener taskListener, TaskType taskType){
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Loading", "Connecting to Server .. Please Wait", true);
        dialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(String... params) {
        String Data_From_Server = null;
        HttpManager http_manager = null;
        try{
            http_manager = new HttpManager();
            if(params[0].equalsIgnoreCase("getUsers")){
                Data_From_Server = http_manager.PostUsername(params);
                Log.e("Data",Data_From_Server);
                return Data_From_Server;
            }
            if(params[0].equalsIgnoreCase("PostData")){
                Data_From_Server = http_manager.PostFormAndDocuments(params);
                Log.e("Data",Data_From_Server);
                return Data_From_Server;
            }

            else{
                return "Error";
            }

        }catch(Exception e){
            return e.getLocalizedMessage().toString().trim();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            taskListener.onTaskCompleted(result, taskType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }



}