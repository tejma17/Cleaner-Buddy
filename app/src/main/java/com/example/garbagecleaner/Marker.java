package com.example.garbagecleaner;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Marker extends Fragment {

    Vibrator vibrator;
    View view;
    Button mark;
    DatabaseReference databaseReference1;
    LottieAnimationView animationView;
    Spinner spinner2;
    String houseMark;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mark = view.findViewById(R.id.mark);
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        animationView = view.findViewById(R.id.animate);
        spinner2 = view.findViewById(R.id.houseid);

        ArrayList<String> house_list = new ArrayList<String>();
        for(int i = 1; i<=30; i++){
            house_list.add(Integer.toString(Integer.parseInt(drawer.WardNo)*100 + i));
        }
        spinner2.setAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                house_list));

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                houseMark = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark.setVisibility(View.INVISIBLE);
                spinner2.setVisibility(View.INVISIBLE);
                vibrator.vibrate(300);
                animationView.setVisibility(View.VISIBLE);
                markDone(houseMark);
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
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Collection").child(formattedDate);
        databaseReference1.child(toString).setValue("true");
    }
}
