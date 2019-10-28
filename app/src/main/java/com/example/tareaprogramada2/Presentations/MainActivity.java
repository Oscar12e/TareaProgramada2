package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tareaprogramada2.Presentations.Fragments.SectionsPageAdapter;
import com.example.tareaprogramada2.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public SectionsPageAdapter pagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagesAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.pages_main);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs_main);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupViewPager(ViewPager viewPager){

    }
}
