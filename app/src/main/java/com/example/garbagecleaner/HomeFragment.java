package com.example.garbagecleaner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

    View view;
    private ViewPager2 viewPager2;
    private Handler handler = new Handler();
    SeekBar seekBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        viewPager2 = view.findViewById(R.id.pages);
        seekBar = view.findViewById(R.id.seekBar2);
        seekBar.setEnabled(false);
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);



        Button corona = view.findViewById(R.id.covid);
        corona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://corona.topiks.ai");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button terms = view.findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms.class);
                startActivity(intent);
            }
        });

        List<slider> slideritems = new ArrayList<>();
        slideritems.add(new slider(R.drawable.banner2));
        slideritems.add(new slider(R.drawable.ban2));
        slideritems.add(new slider(R.drawable.com));
        slideritems.add(new slider(R.drawable.aarogya_setu));

        viewPager2.setAdapter(new slideradapter(slideritems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(4);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(1));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });


        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(sliderRunnable);
                handler.postDelayed(sliderRunnable, 4000 );
            }
        });

        return view;
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };


}