package com.kushkumardhawan.inspections.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kushkumardhawan.inspections.Model.User;
import com.kushkumardhawan.inspections.R;

import java.util.List;

public class UserListDataAdapter extends ArrayAdapter<User> {
    private Context context;
    private LayoutInflater inflater;
    private List<User> userDetails_;

    public UserListDataAdapter(Context c, int resource, List<User> c1) {
        super(c, resource, c1);
        this.context = c;
        this.userDetails_ = c1;
        // inflater = ((AppCompatActivity) this.context).getLayoutInflater();
    }


    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_list, parent, false);

        // Rates_Pojo rates_object = rates.get(position);
        User userDetails = userDetails_.get(position);
        TextView name = (TextView) view.findViewById(R.id.full_name);
        TextView mobile = (TextView) view.findViewById(R.id.mobile);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView department = (TextView) view.findViewById(R.id.department);


        name.setText(userDetails.getFull_name());
        department.setText(userDetails.getDepartment());
        mobile.setText(userDetails.getMobile());
        email.setText(userDetails.getEmail());


        return view;
    }
}
