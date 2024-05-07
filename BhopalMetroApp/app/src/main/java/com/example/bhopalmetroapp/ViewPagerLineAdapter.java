package com.example.bhopalmetroapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerLineAdapter extends FragmentStatePagerAdapter {
    ArrayList<ArrayList<String>> allAvailableRoutes;

    public ViewPagerLineAdapter(@NonNull FragmentManager fm, ArrayList<ArrayList<String>> allAvailableRoutes) {
        super(fm);
        this.allAvailableRoutes = allAvailableRoutes;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new RouteFragment(allAvailableRoutes.get(position));
    }

    @Override
    public int getCount() {
        return allAvailableRoutes.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Green Line(1)";
        else if (position == 1) return "Orange Line(2)";
        else if (position == 2) return "Red Line(3)";
        else if (position == 3) return "Yellow Line(4)";
        else if (position == 4) return "Blue Line(5)";
        else return "Brown Line(6)";
    }

    public void updateData(ArrayList<ArrayList<String>> newData) {
        this.allAvailableRoutes.clear();
        this.allAvailableRoutes.addAll(newData);
        notifyDataSetChanged(); // Notify adapter about data change
    }

    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


}
