package com.example.comsol;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comsol.utils.Constants;
import com.example.comsol.utils.SharedPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    EditText project, operator, subcon, site;
    Button openCameraBtn, savepicture;
    FrameLayout viewtotakess;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    LinearLayout llform, llDataSet;
    TextView tvProject, tvSite, tvOperator, tvSubcon, tvLat, tvlong, tvRemarks,tvAddress;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //No field is required
        llform = (LinearLayout) findViewById(R.id.llform);
        llDataSet = (LinearLayout) findViewById(R.id.llDataSet);
        viewtotakess = (FrameLayout) findViewById(R.id.viewtotakess);
        project = (EditText) findViewById(R.id.project);
        site = (EditText) findViewById(R.id.site);
        operator = (EditText) findViewById(R.id.operator);
        subcon = (EditText) findViewById(R.id.subcon);

        tvProject = (TextView) findViewById(R.id.tvProject);
        tvSite = (TextView) findViewById(R.id.tvSite);
        tvOperator = (TextView) findViewById(R.id.tvOperator);
        tvSubcon = (TextView) findViewById(R.id.tvSubcon);
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvlong = (TextView) findViewById(R.id.tvlong);
        tvRemarks = (TextView) findViewById(R.id.tvRemarks);
        tvAddress = (TextView) findViewById(R.id.tvAddress);


        imageView = (ImageView) findViewById(R.id.imageView);
        openCameraBtn = (Button) findViewById(R.id.openCameraBtn);
        savepicture = (Button) findViewById(R.id.savepicture);
        openCameraBtn.setOnClickListener(this);
        savepicture.setOnClickListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            try {
                                getAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        } else if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getAddress(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            String address = addresses.get(0).getAddressLine(0);
            SharedPrefManager.getInstance(MainActivity.this).storeAddress(address);
            SharedPrefManager.getInstance(MainActivity.this).storeLatitude(latitude);
            SharedPrefManager.getInstance(MainActivity.this).storeLongitude(longitude);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openCameraBtn:
                if(validate()){

                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }

                }




            case R.id.savepicture:
                tvLat.setText("");
                tvlong.setText("");
                tvAddress.setText("");
                project.setText("");
                site.setText("");
                operator.setText("");
                subcon.setText("");

                openCameraBtn.setVisibility(View.VISIBLE);
                llform.setVisibility(View.VISIBLE);

                savepicture.setVisibility(View.GONE);
                llDataSet.setVisibility(View.GONE);
                imageView.setImageBitmap(null);

                //savePicture();
                break;
        }
    }


    public  boolean validate(){
        if (project.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter project name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (site.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter site name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (operator.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter operator name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (subcon.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter Sub contractor name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return  true;

    }
    private void savePicture() {

        if (isWriteStoragePermissionGranted()) {

            takepic(viewtotakess);

        }
    }

    Bitmap photo;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            llform.setVisibility(View.GONE);
            openCameraBtn.setVisibility(View.GONE);
            savepicture.setVisibility(View.VISIBLE);
            llDataSet.setVisibility(View.VISIBLE);
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setDrawingCacheEnabled(true);

            showpopup();


        }
    }

    void takepic(View v1) {

        try {
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            // image naming and path  to include sd card  appending name you choose for file

            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";


            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(true);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();

            galleryAddPic(mPath);
            // openScreenshot(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }

    }
    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }

    public void showpopup() {
        tvLat.setText("Latitude "+SharedPrefManager.getInstance(this).getLat());
        tvlong.setText("Longitude "+SharedPrefManager.getInstance(this).getLong());
        tvAddress.setText("Address "+SharedPrefManager.getInstance(this).getAddress());
        tvProject.setText("Project name "+project.getText().toString());
        tvSite.setText("Site "+site.getText().toString());
        tvOperator.setText("Operator "+operator.getText().toString());
        tvSubcon.setText("Sub Contractor "+subcon.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Remarks");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                tvRemarks.setText(input.getText().toString());
                savePicture();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savePicture();
                dialog.cancel();
            }
        });

        builder.show();

    }
}
