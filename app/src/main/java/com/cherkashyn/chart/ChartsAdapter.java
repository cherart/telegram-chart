package com.cherkashyn.chart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherkashyn.telegramchart.chart.FollowersChart;
import com.cherkashyn.telegramchart.model.Followers;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder> {

    private List<Followers> followersList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!followersList.isEmpty()) {
            holder.followersChart.setData(followersList.get(position));
            holder.followersChart.setDarkTheme();
        }
    }

    @Override
    public int getItemCount() {
        if (followersList.isEmpty())
            return 0;
        else
            return followersList.size();
    }

    void setData(List<Followers> followersList) {
        this.followersList = followersList;
        notifyDataSetChanged();
    }

    void changeTheme() {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FollowersChart followersChart;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            followersChart = itemView.findViewById(R.id.followers_chart);
        }
    }
}
