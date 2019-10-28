package com.example.tareaprogramada2.Presentations.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentsList = new ArrayList<>();
    private List<String> titlesList = new ArrayList<>();

    public SectionsPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        this.fragmentsList.add(fragment);
        this.titlesList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlesList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
