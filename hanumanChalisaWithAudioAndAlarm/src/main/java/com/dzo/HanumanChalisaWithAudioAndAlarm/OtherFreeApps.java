package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Objects;

public class OtherFreeApps extends DialogFragment {

    RecyclerView rvOtherFreeApps;

    public OtherFreeApps() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Other apps");
        WindowManager.LayoutParams params = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
            params.width = width;
//            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }

        View  view=inflater.inflate(R.layout.other_free_apps,container,false);
        rvOtherFreeApps=view.findViewById(R.id.rvOtherApps);
        rvOtherFreeApps.hasFixedSize();
        rvOtherFreeApps.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        OtherFreeAppsAdapter adapter=new OtherFreeAppsAdapter(getContext(),OtherAppList.apps_name,OtherAppList.app_icons);
        rvOtherFreeApps.setAdapter(adapter);

        return view;
    }
}
