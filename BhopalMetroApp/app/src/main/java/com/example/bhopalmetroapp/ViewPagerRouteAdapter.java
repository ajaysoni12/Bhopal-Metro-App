package com.example.bhopalmetroapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerRouteAdapter extends FragmentStatePagerAdapter {

    ArrayList<ArrayList<String>> allAvailableRoutes;

    public ViewPagerRouteAdapter(@NonNull FragmentManager fm, ArrayList<ArrayList<String>> allAvailableRoutes) {
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
        if (position == 0) return "Route 1 (shortest path)";
        else if (position == 1) return "Route 2 (time effective path)";
        else return "Route 3 (Normal path)";
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
