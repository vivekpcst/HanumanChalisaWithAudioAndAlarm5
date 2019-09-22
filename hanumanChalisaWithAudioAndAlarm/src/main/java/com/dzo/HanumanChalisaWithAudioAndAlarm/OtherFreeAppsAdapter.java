package com.dzo.HanumanChalisaWithAudioAndAlarm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Vivek kumar on 5/6/2019 at 1:00 PM.
 * Mobile 7982591863
 * E-mail vivekpcst.kumar@gmail.com
 */
public class OtherFreeAppsAdapter extends RecyclerView.Adapter<OtherFreeAppsAdapter.FreeAppHolder> {

    private Context context;
    private String[] freeAppNames;
    private int[] freeAppIcons;

    public OtherFreeAppsAdapter(Context context, String[] freeAppNames, int[] freeAppIcons) {
        this.context = context;
        this.freeAppNames = freeAppNames;
        this.freeAppIcons = freeAppIcons;
    }

    @NonNull
    @Override
    public FreeAppHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.other_app_row,viewGroup,false);


        return new FreeAppHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FreeAppHolder freeAppHolder, int i) {
            int pos=i;
            freeAppHolder.tvAppName.setText(freeAppNames[i]);
            freeAppHolder.ivAppIcon.setImageResource(freeAppIcons[i]);
            freeAppHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "You have clicked "+freeAppHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    switch (freeAppHolder.getAdapterPosition()){
                        case 0:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.aarti")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.aarti")));
                            }
                            break;
                        case 1:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.gurbani")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.gurbani")));
                            }
                            break;
                        case 2:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.HanumanChalisaWithAudioAndAlarm")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.HanumanChalisaWithAudioAndAlarm")));
                            }
                            break;
                        case 3:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.HanumanChalisaMultilingual")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.HanumanChalisaMultilingual")));
                            }
                            break;
                        case 4:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.saibaba")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.saibaba")));
                            }
                            break;
                        case 6:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mandir.sanatandharmmandir")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mandir.sanatandharmmandir")));
                            }
                            break;
                        case 5:
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dzo.kriana")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dzo.kriana")));
                            }
                            break;
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return freeAppNames.length;
    }

    class FreeAppHolder extends RecyclerView.ViewHolder {
        TextView tvAppName;
        ImageView ivAppIcon;

        FreeAppHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName=itemView.findViewById(R.id.tvAppName);
            ivAppIcon=itemView.findViewById(R.id.ivAppIcon);

        }
    }
}
