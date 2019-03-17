package com.cherkashyn.telegramchart.chart;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cherkashyn.telegramchart.R;
import com.cherkashyn.telegramchart.model.Followers;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import static com.cherkashyn.telegramchart.utils.Utils.dpToPx;

public class FollowersChart extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private Followers followers;
    private LineChart lineChart;
    private TextView textViewFollowers;
    private RelativeLayout relativeLayout;

    private int marginSixteenDp = (int) dpToPx(16);

    public FollowersChart(Context context) {
        super(context);
        init();
    }

    public FollowersChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setOrientation(VERTICAL);
        initTextViewFollowers();
        initLineChart();
        initRelativeLayout();
    }

    private void initTextViewFollowers() {
        textViewFollowers = new TextView(getContext());
        textViewFollowers.setText("Followers");
        textViewFollowers.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        textViewFollowers.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LayoutParams paramsTextViewFollowers = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsTextViewFollowers.setMargins(marginSixteenDp, marginSixteenDp, 0, 0);
        textViewFollowers.setLayoutParams(paramsTextViewFollowers);
        textViewFollowers.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDayFollowers));
        addView(textViewFollowers);
    }

    private void initLineChart() {
        lineChart = new LineChart(getContext());
        int margin = (int) dpToPx(16);
        LayoutParams paramsLineChart = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dpToPx(342));
        paramsLineChart.setMargins(margin, margin, margin, 0);
        lineChart.setLayoutParams(paramsLineChart);
        addView(lineChart);
    }

    private void initRelativeLayout() {
        relativeLayout = new RelativeLayout(getContext());
        LayoutParams paramsRelativeLayout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsRelativeLayout.setMargins(marginSixteenDp, marginSixteenDp, 0, marginSixteenDp);
        relativeLayout.setLayoutParams(paramsRelativeLayout);
        addView(relativeLayout);
    }

    public void setData(Followers followers) {
        this.followers = followers;
        lineChart.setData(followers);
        initCheckBoxes();
    }

    private void initCheckBoxes() {
        for (int i = 0; i < followers.getLines().size(); i++) {

            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setId(i + 1);
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(this);
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.parseColor(followers.getLines().get(i).getColor())));
            RelativeLayout.LayoutParams layoutParamsCheckBox = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParamsCheckBox);
            if (i != 0) {
                layoutParamsCheckBox.addRule(RelativeLayout.BELOW, i);
                layoutParamsCheckBox.setMargins(0, marginSixteenDp, 0, 0);
            }

            TextView textView = new TextView(getContext());
            textView.setText(followers.getLines().get(i).getName());
            RelativeLayout.LayoutParams layoutParamsTextView = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTextView.addRule(RelativeLayout.RIGHT_OF, i + 1);
            layoutParamsTextView.addRule(RelativeLayout.ALIGN_BASELINE, i + 1);
            layoutParamsTextView.setMargins(marginSixteenDp, 0, 0, 0);
            textView.setLayoutParams(layoutParamsTextView);

            relativeLayout.addView(checkBox);
            relativeLayout.addView(textView);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (lineChart.isWindowTouched())
            requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            lineChart.removeLine(buttonView.getId());
        } else {
            lineChart.showLine(buttonView.getId());
        }
    }
}
