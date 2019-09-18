package com.kushkumardhawan.inspections.JsonManager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse {
    public static String GetUserResponse(String s) {

        String g_Table = null;
        try {
            JSONObject obj = new JSONObject(s);
            g_Table = obj.optString("RESPONSE");
            return g_Table;

        } catch (JSONException e) {
            e.printStackTrace();
            return g_Table = "Something went wrong.";
        }
    }



    //FinancialValue
    public static String FinancialValue(String s) {

        String g_Table = null;
        try {
            JSONObject obj = new JSONObject(s);
            g_Table = obj.optString("RESPONSE");
            return g_Table;

        } catch (JSONException e) {
            e.printStackTrace();
            return g_Table = "Something went wrong.";
        }
    }

    //farwarded-todepartment-caf
//    public static String farwardedtodepartmentcaf(String s) {
//
//        String g_Table = null;
//        try {
//            JSONObject obj = new JSONObject(s);
//            g_Table = obj.optString("RESPONSE");
//            Log.e("g_Table",g_Table);
//            return g_Table;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return g_Table = "Something went wrong.";
//        }
//    }





    //Get Array Detaisl
    public static String getArrayDetails(String s) {

        String g_Table = null;
        try {
            JSONObject obj = new JSONObject(s);
            g_Table = obj.optString("ARRAYDATA");
            Log.e("Array Data",g_Table);
            return g_Table;

        } catch (JSONException e) {
            e.printStackTrace();
            return g_Table = "Something went wrong.";
        }
    }

    //getKeyData
    public static String getKeyData(String s, String Data) {

        String g_Table = null;
        try {
            JSONObject obj = new JSONObject(Data);
            g_Table = obj.optString(s);
            //Log.e("KEY DATA",g_Table);
            return g_Table;

        } catch (JSONException e) {
            e.printStackTrace();
            return g_Table = "Something went wrong.";
        }
    }









}
