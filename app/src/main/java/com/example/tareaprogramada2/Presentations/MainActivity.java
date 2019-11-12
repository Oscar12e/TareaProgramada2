package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
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


    }

    @Override
    public void onStart(){
        super.onStart();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
        }

        ViewPager viewPager = findViewById(R.id.pages_main);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs_main);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setupViewPager(ViewPager viewPager){
        User myUser = Session.instance.currentUser;
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(TimelineFragment.newInstance("", true), "Timeline");
        adapter.addFragment(ProfileFragment.newInstance(myUser._key, true), "Profile");
        adapter.addFragment(new FriendsFragment(), "Amigos");
        adapter.addFragment(new SearchFragment(), "Buscar");
        adapter.addFragment(new NotificationsFragment(), "Notificaciones");
        viewPager.setAdapter(adapter);
    }
}
