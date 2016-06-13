package com.example.ben_tseng.askingformultiplepermissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAskPermissions = (Button) findViewById(R.id.butAskPermissions);
        btnAskPermissions.setOnClickListener(onbtnAskPermissions);

        //if SDK > 23 ,do some thing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkAndRequestPermissions()) {
                // carry on the normal flow, as the case of  permissions  granted.
                //If the permission is no problem, the direct execution
            }
        }
}
    //Ask Multiple permission
    private  boolean checkAndRequestPermissions() {
        Log.v("Permission","PackageManager.PERMISSION_GRANTED="+PackageManager.PERMISSION_GRANTED);

        int permissionBLUETOOTH = ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH);
        int permissionBLUETOOTH_ADMIN = ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN);
        int PermissionACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.v("Permission","permissionBLUETOOTH="+permissionBLUETOOTH);
        Log.v("Permission","permissionBLUETOOTH_ADMIN="+permissionBLUETOOTH_ADMIN);
        Log.v("Permission","PermissionACCESS_COARSE_LOCATION="+PermissionACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (PermissionACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionBLUETOOTH != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH);
        }
        if (permissionBLUETOOTH_ADMIN != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.BLUETOOTH_ADMIN);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("TAG", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.BLUETOOTH, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.BLUETOOTH_ADMIN, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("TAG", "BLUETOOTH & ACCESS_COARSE_LOCATION services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        //If the permission is no problem, the direct execution
                    } else {
                        Log.d("TAG", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_ADMIN)) {
                            showDialogOK("BLUETOOTH and ACCESS_COARSE_LOCATION Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    //Single permission
    private void createDummyFileWrapper() {
        int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        Log.v("Permission","hasWriteContactsPermission="+hasWriteContactsPermission);
        Log.v("Permission","PackageManager.PERMISSION_GRANTED="+PackageManager.PERMISSION_GRANTED);
    }

    private View.OnClickListener onbtnAskPermissions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAndRequestPermissions();
        }
    };

    //show basic Dialog
    private void openAskPermissionsDialog() {
        AlertDialog.Builder mDialog = new  AlertDialog.Builder(MainActivity.this);
        mDialog.setTitle("AskPermissions");
        mDialog.setMessage("Message");
        mDialog.setCancelable(true);
        mDialog.show();
    }
}
