package com.hixel.hixel.view.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.hixel.hixel.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    TextView userName_TV, userEmail_TV, changePasswordLink_TV;
    TextInputLayout newPassword_Text, confirmPassword_Text;
    Button cancel_BT, submit_BT, logout_BT;
    CircleImageView circleImageView;
    ImageView edit_IW;
    private int PICK_IMAGE_REQUEST = 1;
    FileOutputStream outputStream;
    String fileName="profile_photo.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //TODO: implement get user info from server
        userName_TV = findViewById(R.id.userFullName);
        userEmail_TV = findViewById(R.id.email);

        changePasswordLink_TV= findViewById(R.id.textView5);
        newPassword_Text = findViewById(R.id.profile_PassWrapper);
        newPassword_Text.setVisibility(View.INVISIBLE);
        confirmPassword_Text= findViewById(R.id.profile_ConfirmPassWrapper);
        confirmPassword_Text.setVisibility(View.INVISIBLE);

        cancel_BT = findViewById(R.id.cancel_BT);
        cancel_BT.setVisibility(View.INVISIBLE);

        submit_BT = findViewById(R.id.submit_BT);
        submit_BT.setVisibility(View.INVISIBLE);
        logout_BT= (Button)findViewById(R.id.logout_BT);

        circleImageView = findViewById(R.id.profile);
        edit_IW= findViewById(R.id.edit);
        // dummy data just for now
        userName_TV.setText("Laxman Marothiya");
        userEmail_TV.setText("john1993@hixel.com.au");
        setupBottomNavigationView();
        changePasswordLink_TV.setOnClickListener(event->{
            setViewVisible();
        });

        cancel_BT.setOnClickListener(event->{
            setViewInvisible();
        });

        submit_BT.setOnClickListener(event->{
            if(newPassword_Text.getEditText().getText().toString().compareTo(confirmPassword_Text.getEditText().getText().toString())!=0){
                confirmPassword_Text.setError("Your Passwords do not match");
            } else {
                //TODO update password on server
            }
        });
        logout_BT.setOnClickListener(event->{
            Intent moveToLogin = new Intent(this, LoginActivity.class);
            startActivity(moveToLogin);
        });

        edit_IW.setOnClickListener(event->{

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
        });
        if(photoExists()){
            loadProfilePhoto();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                circleImageView.setImageBitmap(bitmap);
                saveProfilePhoto(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.profile_navigator);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.home_button:
                    Intent moveToDashBoard = new Intent(this, DashboardActivity.class);
                    startActivity(moveToDashBoard);
                    break;
                case R.id.compare_button:
                    Intent moveToCompare = new Intent(this, ComparisonActivity.class);
                    startActivity(moveToCompare);
                    break;
                case R.id.settings_button:
                    // Already on this screen.
                    break;
            }

            return true;
        });
    }
    public void saveProfilePhoto(Bitmap myFile){
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            myFile.compress(CompressFormat.JPEG,100,outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadProfilePhoto(){
        try {
            File f=getBaseContext().getFileStreamPath(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            circleImageView.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    public boolean photoExists(){
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }
    public void setViewVisible(){
        newPassword_Text.setVisibility(View.VISIBLE);
        confirmPassword_Text.setVisibility(View.VISIBLE);
        cancel_BT.setVisibility(View.VISIBLE);
        submit_BT.setVisibility(View.VISIBLE);
        logout_BT.setVisibility(View.GONE);
    }
    public void setViewInvisible(){
        newPassword_Text.setVisibility(View.INVISIBLE);
        confirmPassword_Text.setVisibility(View.INVISIBLE);
        cancel_BT.setVisibility(View.INVISIBLE);
        submit_BT.setVisibility(View.INVISIBLE);
        logout_BT.setVisibility(View.VISIBLE);
    }
}
