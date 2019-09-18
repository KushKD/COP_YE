package com.kushkumardhawan.inspections.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kushkumardhawan.inspections.Abstract.AsyncTaskListener;
import com.kushkumardhawan.inspections.Adapter.UserListDataAdapter;
import com.kushkumardhawan.inspections.Enums.TaskType;
import com.kushkumardhawan.inspections.Generic.Custom_Dialog;
import com.kushkumardhawan.inspections.Generic.Generic_Async_Post;
import com.kushkumardhawan.inspections.JsonManager.JsonParse;
import com.kushkumardhawan.inspections.Model.User;
import com.kushkumardhawan.inspections.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cmreliefdund.kushkumardhawan.com.instructions.MaterialTutorialActivity;
import cmreliefdund.kushkumardhawan.com.instructions.TutorialItem;

public class SelectUser extends AppCompatActivity implements AsyncTaskListener {

    EditText username;
    Button search;
    LinearLayout layout_list;
    ListView list;
    Custom_Dialog CD =  new Custom_Dialog();
    private List<User> userList;
    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);



            loadTutorial();


        username = (EditText)findViewById(R.id.username);
        search = (Button) findViewById(R.id.search);
        layout_list = (LinearLayout) findViewById(R.id.layout_list);
        list = (ListView) findViewById(R.id.list);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().equalsIgnoreCase("") || username.getText().toString().isEmpty() ){
                    CD.showDialog(SelectUser.this,"Username cannot be empty");
                }else{
                    new Generic_Async_Post(
                            SelectUser.this,
                            SelectUser.this,
                            TaskType.GET_USER).
                            execute("getUsers","http://eypoc.com/cmrelief/odisha/api/GetUserDetails",username.getText().toString().trim());
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                User UserPojo = (User)	parent.getItemAtPosition(position);

                Intent GoToMaps = new Intent();
                GoToMaps.putExtra("UserPoJO", UserPojo);
                GoToMaps.setClass(SelectUser.this, MapsActivity.class);
                startActivity(GoToMaps);

            }
        });

    }

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("Health", "Life and death are in the hands of God but to help the needy during illness is our moral responsibility. In my state there should not be any case where due to lack of fund the treatment of the sick person could not be done. I appeal to the citizens to donate for the cause generously so that the needy people can be helped. ",
                R.color.g_dark_blue, R.drawable.tut_page_1_background,R.drawable.tut_page_1_front);

        TutorialItem tutorialItem2 = new TutorialItem("Education", "Education Content...",
                R.color.slide_1,  R.drawable.tut_page_2_background, R.drawable.tut_page_2_front);

        TutorialItem tutorialItem3 = new TutorialItem("Disaster", "Disaster Content...",
                R.color.slide_4, R.drawable.tut_page_3_background, R.drawable.tut_page_3_foreground);

        TutorialItem tutorialItem4 = new TutorialItem("Needy", "Needy Content...",
                R.color.slide_3, R.drawable.tut_page_4_foreground,R.drawable.tut_page_4_background);

//        TutorialItem tutorialItem5 = new TutorialItem("Girl Child", "Girl Child Content ....",
//                R.color.slide_3, R.drawable.splash_back);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);
        // tutorialItems.add(tutorialItem5);

        return tutorialItems;
    }
    @Override
    public void onTaskCompleted(String result, TaskType taskType) throws JSONException {
        if (taskType == TaskType.GET_USER) {

            JSONObject jsonResponse;
            String Data = JsonParse.GetUserResponse(result);
            JSONArray jsonarray = new JSONArray(Data);
            userList = new ArrayList<User>();
            for (int i = 0; i < jsonarray.length(); i++) {
                User userDetails =new User();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                userDetails.setFull_name(jsonobject.getString("full_name"));
                userDetails.setEmail(jsonobject.getString("email"));
                userDetails.setDepartment(jsonobject.getString("department_name"));
                userDetails.setMobile(jsonobject.getString("mobile"));
                userDetails.setId(jsonobject.getString("uid"));

                userList.add(userDetails);
            }



            Log.e("Length", Integer.toString(userList.size()));

            if(userList.size()>0){
                UserListDataAdapter adapter = new UserListDataAdapter(this, R.layout.user_list ,userList);
                list.setAdapter(adapter);
                layout_list.setVisibility(View.VISIBLE);
            }else{
                CD.showDialog(SelectUser.this, "No User Found with this name");
            }



        }

    }

    @Override
    public void onTaskCompleted(ArrayList<String> result, TaskType taskType) throws JSONException {

    }

    @Override
    public void onTaskCompleted(Activity activity, String result, TaskType taskType) {

    }
}
