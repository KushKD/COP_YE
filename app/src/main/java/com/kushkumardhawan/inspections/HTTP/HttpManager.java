package com.kushkumardhawan.inspections.HTTP;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager {

    public static String get_Data(String uri) {

        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

    public String GetData(String url) {
        System.out.print(url);
        BufferedReader reader = null;

        try {
            URL url_ = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_.openConnection();

            if (con.getResponseCode() != 200) {
                return "Unable to connect to Service";
            }else {


                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                con.disconnect();
                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Timeout";
        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            }
        }
    }

    public String Customer_Updated(Object... objects){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder response = null;
        //  String jsonInString = null;



        //Customer customer_to_send = (Customer)objects[0];

        try {


            url_ =new URL("http://164.100.138.204/eServices/updateUserDetails/");
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn_.connect();

            //Convert the Object into Json Object
//            ObjectMapper mapper = new ObjectMapper();
//            String jsonInString = mapper.writeValueAsString(customer_to_send);
//            Log.e("String",jsonInString);
//            StringBuilder New_String = new StringBuilder();
//            New_String.append("jsonData=");
//            New_String.append(jsonInString);

            //  JSONObject obj = new JSONObject(New_String.toString());

           // Log.e("My App data in Json", New_String.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
           // out.write(New_String.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                Log.e("Server Code",Integer.toString(HttpResult));
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    response = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        response.append(line + "\n");
                    }
                    br.close();
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return response.toString().trim();
    }

  //PostFormAndDocuments
  public String PostFormAndDocuments(String... params){

      URL url_ = null;
      HttpURLConnection conn_ = null;
      StringBuilder sb = null;
      JSONStringer userJson = null;

      String URL = null;
      String formData = null;
      String DocumentsData = null;


      try {

          URL = params[1];
          formData = params[2];
          DocumentsData = params[3];



          url_ =new URL(URL);
          conn_ = (HttpURLConnection)url_.openConnection();
          conn_.setDoOutput(true);
          conn_.setRequestMethod("POST");
          conn_.setUseCaches(false);
          conn_.setConnectTimeout(20000);
          conn_.setReadTimeout(20000);
          conn_.setRequestProperty("Content-Type", "application/json");
          conn_.connect();

          userJson = new JSONStringer()
                  .object()
                  .key("InspectionData")
                  .object()
                  .key("form_data").value(formData)
                  .key("documents_data").value(DocumentsData)
                  .endObject()
                  .endObject();


          System.out.println(userJson.toString());
          Log.e("Object",userJson.toString());
          OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
          out.write(userJson.toString());
          out.close();

          try{
              int HttpResult =conn_.getResponseCode();
              if(HttpResult !=HttpURLConnection.HTTP_OK){
                  return "Timeout.";

              }else{
                  BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                  String line = null;
                  sb = new StringBuilder();
                  while ((line = br.readLine()) != null) {
                      sb.append(line + "\n");
                  }
                  br.close();
                  System.out.println(sb.toString());
                  Log.e("Data from Service",sb.toString());
              }

          } catch(Exception e){
              return "Error";
          }

      }
      catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (JSONException e) {
          e.printStackTrace();
      } finally{
          if(conn_!=null)
              conn_.disconnect();
      }
      return sb.toString();
  }



    public String PostUsername(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String UserName = null;


        try {

            URL = params[1];
            UserName = params[2];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("searchuserby").value(UserName)
                    .endObject();


              System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Data from Service",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }


    //PostCafDetails
    public String PostCafDetails(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String CafId = null;
        String IUID = null;


        try {

            URL = params[1];
            CafId = params[2];
            IUID = params[3];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("caf_id").value(CafId)
                    .key("iuid").value(IUID)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Data from Service",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //PostGrevienceReply
    public String PostGrevienceReply(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String apiHash = null;
        String roleId = null;
        String userId = null;
        String GrevienceId = null;
        String text = null;


        try {

            URL = params[1];
            apiHash = params[2];
            roleId = params[3];
            userId = params[4];
            GrevienceId = params[5];
            text = params[6];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("api_hash").value(apiHash)
                    .key("role_id").value(roleId)
                    .key("user_id").value(userId)
                    .key("grievance_id").value(GrevienceId)
                    .key("reply_text").value(text)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Data from Service",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //CloseGrevienceReply
    public String CloseGrevienceReply(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String apiHash = null;
        String roleId = null;
        String userId = null;
        String GrevienceId = null;
        String text = null;


        try {

            URL = params[1];
            apiHash = params[2];
            roleId = params[3];
            userId = params[4];
            GrevienceId = params[5];
            text = params[6];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("api_hash").value(apiHash)
                    .key("role_id").value(roleId)
                    .key("user_id").value(userId)
                    .key("grievance_id").value(GrevienceId)
                    .key("reply_text").value(text)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Data from Service",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //Post adddComments
    public String adddComments(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String app_id = null;
        String sub_id = null;
        String comments = null;
        String uid = null;



        try {

            URL = params[1];
            app_id = params[2];
            sub_id = params[3];
            comments = params[4];
            uid = params[5];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("app_id").value(app_id)
                    .key("sub_id").value(sub_id)
                    .key("comments").value(comments)
                    .key("uid").value(uid)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                   // System.out.println(sb.toString());
                    Log.e("Dat Service Comments",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //Post postNewCafComments
    public String postNewCafComments(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String ConcentValue= null;
        String Comments= null;
        String  service_id= null;
        String  RoleId = null;
        String  appStatus= null;
        String  form_Id= null;
        String  submission_id= null;
        String  departmentId = null;
        String  userId= null;



        try {

            URL = params[1];
            ConcentValue = params[2];
            Comments = params[3];
            service_id = params[4];
            RoleId = params[5];
            appStatus = params[6];
            form_Id = params[7];
            submission_id = params[8];
            departmentId = params[9];
            userId = params[10];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("UK-FCL-00181_11").value(ConcentValue)
                    .key("UK-FCL-00181_2").value(Comments)
                    .key("service_id").value(service_id)
                    .key("role_id").value(RoleId)
                    .key("app_status").value(appStatus)
                    .key("form_id").value(form_Id)
                    .key("app_Sub_id").value(submission_id)
                    .key("dept_id").value(departmentId)
                    .key("uid").value(userId)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    // System.out.println(sb.toString());
                    Log.e("Dat Service Comments",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //Post getInvestorDashboard
    public String getInvestorDashboard(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String financialYear = null;
        String email = null;
        String userId = null;



        try {

            URL = params[1];
            financialYear = params[2];
            userId = params[3];
            email = params[4];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("financial_year").value(financialYear)
                    .key("user_id").value(userId)
                    .key("email").value(email)
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Dat Service Comments",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //getPSDashboard
    public String getPSDashboard(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String financialYear = null;



        try {

            URL = params[1];
            financialYear = params[2];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(15000);
            conn_.setReadTimeout(15000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object()
                    .key("financial_year").value(financialYear)

                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Dat Service Comments",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    //postQuery
    public String postQuery(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String  queryId,
                subQueryId,
                user_id,
                new_username,
                new_useremail,
                new_usermobile,
                ruser_name,
                querySummery,
                queryDesc = null;



        try {

            URL = params[1];
            queryId = params[2];
            subQueryId = params[3];
            user_id = params[4];
            new_username = params[5];
            new_useremail = params[6];
            new_usermobile = params[7];
            ruser_name = params[8];
            querySummery = params[9];
            queryDesc = params[10];



            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(15000);
            conn_.setReadTimeout(15000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();


            userJson = new JSONStringer()
                    //.object().key("app")
                    .object()
                    .key("topicId_sub").value(queryId)
                    .key("topicId").value(subQueryId)
                    .key("behalf_of").value(user_id)
                    .key("investor_iuid").value(ruser_name)
                    .key("investor_name").value(new_username)
                    .key("investor_email").value(new_useremail)
                    .key("investor_mobile").value(new_usermobile)
                    .key("department_id").value("")
                    .key("461966359e20eca5").value("demo")
                    .key("message").value(queryDesc)
                    .key("draft_id").value("")
                    .key("emailId").value("0")
                    .key("deptId").value("0")
                    .key("uid").value("")
                    //.endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                    Log.e("Dat Service Comments",sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }


}
