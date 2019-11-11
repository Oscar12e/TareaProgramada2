package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.Fragments.FriendsFragment;
import com.example.tareaprogramada2.Presentations.Fragments.NotificationsFragment;
import com.example.tareaprogramada2.Presentations.Fragments.ProfileFragment;
import com.example.tareaprogramada2.Presentations.Fragments.SearchFragment;
import com.example.tareaprogramada2.Presentations.Fragments.SectionsPageAdapter;
import com.example.tareaprogramada2.Presentations.Fragments.TimelineFragment;
import com.example.tareaprogramada2.R;

public class ProfileActivity extends AppCompatActivity {

    public SectionsPageAdapter pagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userKey = getIntent().getStringExtra("USER_KEY");

        LinearLayout root = findViewById(R.id.layout_root);

        ViewPager viewPager = findViewById(R.id.pages_default);

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(ProfileFragment.newInstance(userKey, false), "Profile");

        viewPager.setAdapter(adapter);
    }

    public void setupViewPager(ViewPager viewPager){
        User myUser = Session.instance.currentUser;
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

       // adapter.addFragment(ProfileFragment.newInstance(userKey, false), "Profile");

        viewPager.setAdapter(adapter);
    }
}
