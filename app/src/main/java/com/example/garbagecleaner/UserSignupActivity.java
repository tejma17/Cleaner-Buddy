package com.example.garbagecleaner;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UserSignupActivity extends AppCompatActivity {
    Spinner spinner1;
    String textWard, fullName, wardNumber, Email, Password;
    String wards[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    TextInputLayout email, password, name;
    Button register, login, file_upload;
    ImageButton select;
    ImageView image;
    TextView welcome;
    int flag = 0;

    Uri filepath;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        image = findViewById(R.id.propic);
        welcome = findViewById(R.id.welcomeid);
        spinner1 = findViewById(R.id.wardid);
        name = findViewById(R.id.nameid);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        file_upload = findViewById(R.id.upload);
        select = findViewById(R.id.profilepic);
        register = findViewById(R.id.doneButton);
        login = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cleaners");
        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnners, wards);

        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textWard = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawer.flag = 0;
                fullName = name.getEditText().getText().toString().trim();
                wardNumber = spinner1.getSelectedItem().toString().trim();
                Email = email.getEditText().getText().toString().trim() + "@xyz.com";
                Password = password.getEditText().getText().toString().trim();



                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(UserSignupActivity.this,"Enter Email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(UserSignupActivity.this,"Enter Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(UserSignupActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(UserSignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue("---");
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").setValue(fullName);
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mobile").setValue(Email);
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("password").setValue(Password);
                                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("wardNo").setValue(wardNumber);
                                    startActivity(new Intent(getApplicationContext(),userLogin.class));
                                    Toast.makeText(UserSignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(UserSignupActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignupActivity.this, userLogin.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_trans");
                pairs[1] = new Pair<View, String>(welcome, "welcome_trans");
                pairs[2] = new Pair<View, String>(email, "email_trans");
                pairs[3] = new Pair<View, String>(password, "pw_trans");
                pairs[4] = new Pair<View, String>(login, "But_trans");
                pairs[5] = new Pair<View, String>(register, "But2_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserSignupActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        });

        file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            filepath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                select.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filepath != null){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference reference = storageReference.child("Cleaner dps/" + firebaseAuth.getCurrentUser().getUid());
            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(UserSignupActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+ (int)progress+"%");
                }
            });
        }
    }


}
