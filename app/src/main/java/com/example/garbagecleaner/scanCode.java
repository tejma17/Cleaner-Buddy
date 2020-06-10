package com.example.garbagecleaner;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanCode extends Fragment implements ZXingScannerView.ResultHandler {

    Vibrator vibrator;
    ZXingScannerView scannerView;
    LottieAnimationView animationView;
    Button on, off;
    DatabaseReference databaseReference1;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        view =  inflater.inflate(R.layout.scan, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        scannerView = new ZXingScannerView(getContext());
        scannerView = view.findViewById(R.id.surfaceView);
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        animationView = view.findViewById(R.id.animate);
        scannerView.startCamera();
        on = view.findViewById(R.id.flashon);
        off = view.findViewById(R.id.flashoff);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.setFlash(true);
                off.setVisibility(View.VISIBLE);
                on.setVisibility(View.INVISIBLE);
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.setFlash(false);
                off.setVisibility(View.INVISIBLE);
                on.setVisibility(View.VISIBLE);
            }
        });
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getActivity().onBackPressed();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return view;
    }

    public void markDone(String toString) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        Toast.makeText(getContext(), formattedDate, Toast.LENGTH_SHORT).show();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Collection").child(formattedDate);

        //Toast.makeText(this, toString+ "fff", Toast.LENGTH_SHORT).show();
            databaseReference1.child(toString).setValue("true");
    }


    @Override
    public void handleResult(Result result) {
        scannerView.setVisibility(View.INVISIBLE);
        scannerView.setSoundEffectsEnabled(true);
        vibrator.vibrate(300);
        markDone(result.getText());
        animationView.setVisibility(View.VISIBLE);
    }



    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

        scannerView.setVisibility(View.VISIBLE);
    }

}
