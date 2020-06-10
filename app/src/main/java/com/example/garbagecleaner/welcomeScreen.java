package com.example.garbagecleaner;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class welcomeScreen<Imageview> extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;
    Animation top, bottom, fade;
    ImageView image;
    TextView sub;
    sharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);
        share = new sharedPreferences(welcomeScreen.this);
        //Animations
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bot_animation);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        image = findViewById(R.id.logo);
        sub = findViewById(R.id.textView2);
        drawer.flag = 0;
        /*image.setAnimation(fade);
        name.setAnimation(fade);
        mean.setAnimation(fade);
        copy.setAnimation(fade);
        sub.setAnimation(fade);*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(welcomeScreen.this, userLogin.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(image, "logo_trans");
                pairs[1] = new Pair<View, String>(sub, "welcome_trans");

                if(share.getFilename()== "") {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(welcomeScreen.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    }
                }
                else{
                    startActivity(new Intent(getApplicationContext(), drawer.class));
                    finish();
                }

            }
        },SPLASH_SCREEN);
    }
}
