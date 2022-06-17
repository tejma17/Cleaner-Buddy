package com.example.garbagecleaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    sharedPreferences share;
    DrawerLayout drawer;
    public static DatabaseReference databaseReference;
    public static StorageReference storageReference;
    static File localFile;
    static String Email, mobileNO,pw, WardNo, fullName;
    static int flag;
    Animation fade;

    TextView name, wait;
    View bblanks;;

    ImageView propic;

    LottieAnimationView animationView, connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        share = new sharedPreferences(drawer.this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share.removeUser();
                Intent intent = new Intent(drawer.this, userLogin.class);
                Snackbar.make(view,     "Nothing here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(intent);
                finish();
            }
        });

        animationView = findViewById(R.id.load);
        wait = findViewById(R.id.wait);
        bblanks = findViewById(R.id.blank);
        connection = findViewById(R.id.connection);
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null) {
            connection.setVisibility(View.VISIBLE);
            wait.setVisibility(View.INVISIBLE);
            bblanks.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.INVISIBLE);
        }
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,  R.id.nav_scanning, R.id.nav_mark)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        name = navigationView.getHeaderView(0).findViewById(R.id.naaav);
        propic = navigationView.getHeaderView(0).findViewById(R.id.pro_button);
        propic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),profile.class));
                drawer.closeDrawers();
            }
        });

        if(flag == 0) {
            bblanks.setVisibility(View.VISIBLE);
            wait.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            getData();
        }

    }

    public void getData(){
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cleaners").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mobileNO = dataSnapshot.child("mobile").getValue().toString();
                mobileNO = mobileNO.substring(0,10);
                fullName = dataSnapshot.child("name").getValue().toString();
                name.setText(fullName);
                Email = dataSnapshot.child("email").getValue().toString();
                WardNo = dataSnapshot.child("wardNo").getValue().toString();
                pw = dataSnapshot.child("password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    onBackPressed();
            }
        });


        storageReference = FirebaseStorage.getInstance().getReference().child("Cleaner dps/"+ currentUser);
        try {
            localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            animationView.setVisibility(View.INVISIBLE);
                            bblanks.setVisibility(View.INVISIBLE);
                            bblanks.setAnimation(fade);
                            wait.setVisibility(View.INVISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            propic.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    animationView.setVisibility(View.INVISIBLE);
                    bblanks.setVisibility(View.INVISIBLE);
                    bblanks.setAnimation(fade);
                    wait.setVisibility(View.INVISIBLE);
                    //Toast.makeText(drawer.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        flag = 1;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_scan:
                startActivity(new Intent(drawer.this, MapsActivity.class));
                return true;
            case R.id.action_noti:
                //startActivity(new Intent(getApplicationContext(), scanner.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        return false;
    }
}
