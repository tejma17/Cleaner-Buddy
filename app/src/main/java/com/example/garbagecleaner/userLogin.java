package com.example.garbagecleaner;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userLogin extends AppCompatActivity {
    ImageView image;
    TextView welcome;
    String Email, Password;
    Button user_login, register_user;
    TextInputLayout email, password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setTitle("Login");

        image = findViewById(R.id.propic);
        welcome = findViewById(R.id.welcomeid);
        email = findViewById(R.id.nameid);
        password = findViewById(R.id.password);
        user_login = (Button) findViewById(R.id.loginButton);
        register_user = findViewById(R.id.doneButton);

        firebaseAuth = FirebaseAuth.getInstance();
        user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.flag = 0;
                Email = email.getEditText().getText().toString().trim()+"@xyz.com";
                Password = password.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(userLogin.this, "Enter Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(userLogin.this, "Enter Password", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(userLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sharedPreferences share = new sharedPreferences(userLogin.this);
                                    share.setFilename(firebaseAuth.getCurrentUser().getUid());;
                                    startActivity(new Intent(getApplicationContext(), drawer.class));
                                    finish();

                                } else {
                                    Toast.makeText(userLogin.this, "Login Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });


        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLogin.this, UserSignupActivity.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_trans");
                pairs[1] = new Pair<View, String>(welcome, "welcome_trans");
                pairs[2] = new Pair<View, String>(email, "email_trans");
                pairs[3] = new Pair<View, String>(password, "pw_trans");
                pairs[4] = new Pair<View, String>(user_login, "But_trans");
                pairs[5] = new Pair<View, String>(register_user, "But2_trans");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(userLogin.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }
        });
    }
}
