package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tareaprogramada2.Presentations.Fragments.FriendsFragment;
import com.example.tareaprogramada2.Presentations.Fragments.NotificationsFragment;
import com.example.tareaprogramada2.Presentations.Fragments.ProfileFragment;
import com.example.tareaprogramada2.Presentations.Fragments.SearchFragment;
import com.example.tareaprogramada2.Presentations.Fragments.SectionsPageAdapter;
import com.example.tareaprogramada2.Presentations.Fragments.TimelineFragment;
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
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TimelineFragment(), "Timeline");
        adapter.addFragment(new ProfileFragment(), "Perfil");
        adapter.addFragment(new FriendsFragment(), "Amigos");
        adapter.addFragment(new SearchFragment(), "Buscar");
        adapter.addFragment(new NotificationsFragment(), "Notificaciones");
        viewPager.setAdapter(adapter);
    }
}
