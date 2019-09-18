package com.kushkumardhawan.inspections.Abstract;

import android.app.Activity;

import com.kushkumardhawan.inspections.Enums.TaskType;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by kuush on 6/17/2016.
 */
public interface AsyncTaskListener {

    void onTaskCompleted(String result, TaskType taskType) throws JSONException;
    void onTaskCompleted(ArrayList<String> result, TaskType taskType) throws JSONException;

    void onTaskCompleted(Activity activity, String result, TaskType taskType);

}