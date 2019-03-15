package com.cherkashyn.telegramchart.chart;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cherkashyn.telegramchart.R;
import com.cherkashyn.telegramchart.model.Followers;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import static com.cherkashyn.telegramchart.utils.DensityConverter.dpToPx;

public class FollowersChart extends LinearLayout {

    private Followers followers;
    private LineChart lineChart;
    private TextView textViewFollowers;
    private RelativeLayout relativeLayout;
    private List<CheckBox> checkBoxes = new ArrayList<>();

    public FollowersChart(Context context) {
        super(context);
        init();
        requestDisallowInterceptTouchEvent(false);
    }

    public FollowersChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowersChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(VERTICAL);
        initTextViewFollowers();
        initLineChart();
        relativeLayout = new RelativeLayout(getContext());
        addView(relativeLayout);
    }

    private void initLineChart() {
        lineChart = new LineChart(getContext());
        int margin = (int) dpToPx(16);
        LayoutParams paramsLineChart = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dpToPx(342));
        paramsLineChart.setMargins(margin, margin, margin, 0);
        lineChart.setLayoutParams(paramsLineChart);
        addView(lineChart);
    }

    private void initTextViewFollowers() {
        textViewFollowers = new TextView(getContext());
        textViewFollowers.setText("Followers");
        textViewFollowers.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        textViewFollowers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        int margin = (int) dpToPx(16);
        LayoutParams paramsTextViewFollowers = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTextViewFollowers.setMargins(margin, margin, 0, 0);
        textViewFollowers.setLayoutParams(paramsTextViewFollowers);
        textViewFollowers.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDayFollowers));
        addView(textViewFollowers);
    }

    private void initCheckBoxes() {
        int marginBetween = (int) dpToPx(16);
        int marginLeft = (int) dpToPx(16);
        int marginTopAndBottom = (int) dpToPx(16);

        LayoutParams paramsRelativeLayout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsRelativeLayout.setMargins(marginLeft, marginTopAndBottom, 0, marginTopAndBottom);
        relativeLayout.setLayoutParams(paramsRelativeLayout);
        for (int i = 0; i < followers.getLines().size(); i++) {

            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(i + 1);
            checkBox.setChecked(true);
            TextView textView = new TextView(getContext());
            textView.setText("Joined");
            textView.setText(followers.getLines().get(i).getName());
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.parseColor(followers.getLines().get(i).getColor())));
            RelativeLayout.LayoutParams layoutParamsCheckBox = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i != 0) {
                layoutParamsCheckBox.addRule(RelativeLayout.BELOW, i);
                layoutParamsCheckBox.setMargins(0, marginBetween, 0, 0);
            }
            layoutParamsTextView.addRule(RelativeLayout.RIGHT_OF, i + 1);
            layoutParamsTextView.addRule(RelativeLayout.ALIGN_BASELINE, i + 1);
            layoutParamsTextView.setMargins(marginLeft, 0, 0, 0);

            textView.setLayoutParams(layoutParamsTextView);
            checkBox.setLayoutParams(layoutParamsCheckBox);
            relativeLayout.addView(checkBox);
            relativeLayout.addView(textView);
        }
    }

    public void setData(Followers followers) {
        this.followers = followers;
        lineChart.setData(followers);
        initCheckBoxes();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (lineChart.isWindowTouched(ev))
            requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }
}
