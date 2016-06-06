package com.xbpsolutions.appsadviseradmin.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.xbpsolutions.appsadviseradmin.R;

import java.util.List;


public class AppItemRecyclerViewAdapter extends RecyclerView.Adapter<AppItemRecyclerViewAdapter.ViewHolder> {

    private final List<AppInfo> mValues;
    private Context context;

    public AppItemRecyclerViewAdapter(Context activity, List<AppInfo> items) {
        mValues = items;
        this.context = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_appitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
//        String appName = context.getPackageManager().getApplicationLabel(
//                holder.mItem.applicationInfo).toString();
        holder.mIdView.setText(holder.mItem.title);
//
//        Drawable appIcon = context.getPackageManager()
//                .getApplicationIcon(holder.mItem.applicationInfo);
//        holder.imgLogo.setImageDrawable(appIcon);
        Glide.with(context).load(holder.mItem.icon_url).asBitmap().into(holder.imgLogo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent startIntent = context.getPackageManager().getLaunchIntentForPackage(holder.mItem.applicationInfo.packageName);
//                if (startIntent != null) {
//                    context.startActivity(startIntent);
//                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public AppInfo mItem;
        public final ImageView imgLogo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
        }

    }

}
