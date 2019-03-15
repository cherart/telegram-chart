package com.cherkashyn.chart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherkashyn.telegramchart.chart.FollowersChart;
import com.cherkashyn.telegramchart.model.Followers;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder> {

    private List<Followers> followersList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (followersList.size() > 0) {
            holder.followersChart.setData(followersList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public void setData(List<Followers> followersList) {
        this.followersList = followersList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FollowersChart followersChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            followersChart = itemView.findViewById(R.id.chart);
        }
    }
}
