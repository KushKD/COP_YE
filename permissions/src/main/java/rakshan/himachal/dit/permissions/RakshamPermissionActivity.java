package rakshan.himachal.dit.permissions;

import android.app.Activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class RakshamPermissionActivity extends Activity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 323;
    private List<String> mPermissionsList;
    private ArrayList<String> permissions;
    private int requestId;
    private Map<String, Integer> perms = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        init();
        checkPermissions();

    }
    private void init() {

        Intent intent = getIntent();
        permissions = intent.getStringArrayListExtra(RakshamPermissionsTags.PERMISSIONS);
        requestId = intent.getIntExtra(RakshamPermissionsTags.REQ_ID,-1);
        mPermissionsList=new ArrayList<>();

        for(int i=0;i<permissions.size();i++){
            perms.put(permissions.get(i), PackageManager.PERMISSION_GRANTED);
        }
        for(int i=0;i<permissions.size();i++){
            addPermission(mPermissionsList, permissions.get(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        moveTaskToBack(true);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mPermissionsList.size() > 0) {
                ActivityCompat.requestPermissions(this, mPermissionsList.toArray(new String[mPermissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
            }else {
                RakshamPermissions.listener.onRequestBack(new RakshamPermissionResponse(perms,this.permissions,this,requestId));
                finish();
            }

        }else{
            RakshamPermissions.listener.onRequestBack(new RakshamPermissionResponse(perms,this.permissions,this,requestId));
            finish();
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            RakshamPermissions.listener.onRequestBack(new RakshamPermissionResponse(perms,this.permissions,this,requestId));
            finish();
        }
    }


    /**
     * Get calling intent intent.
     *
     * @param activity    the activity
     * @param permissions the permissions
     * @return the intent
     */
    protected static Intent getCallingIntent(Context activity, ArrayList permissions,int requestId){
        Intent intent = new Intent(activity, RakshamPermissionActivity.class);
        intent.putExtra(RakshamPermissionsTags.PERMISSIONS,permissions);
        intent.putExtra(RakshamPermissionsTags.REQ_ID,requestId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;

    }

}
