package com.cherkashyn.chart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cherkashyn.telegramchart.utils.JSONParser;
import com.cherkashyn.telegramchart.chart.FollowersLineChart;
import com.cherkashyn.telegramchart.model.Followers;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout background;
    TextView joined;
    TextView left;
    FollowersLineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = findViewById(R.id.background);
        joined = findViewById(R.id.textview_joined);
        left = findViewById(R.id.textview_left);
        chart = findViewById(R.id.chart);
        getSupportActionBar().setTitle("Statistics");


        try {
            String json = readJSONFromFile();
            List<Followers> followersList = JSONParser.parseJSONToListOfFollowers(json);
            chart.setData(followersList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.ActivityTheme_Dark);
        else
            setTheme(R.style.ActivityTheme_Light);
    }

    private String readJSONFromFile() throws IOException {
        InputStream is = getResources().getAssets().open("chart_data.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_mode) {
            changeTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {
        int colorFromStatusBar, colorToStatusBar, colorFromToolbar, colorToToolbar, colorFromBackground, colorToBackground;

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            colorFromStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryNightDark);
            colorToStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryDayDark);
            colorFromToolbar = ContextCompat.getColor(this, R.color.colorPrimaryNight);
            colorToToolbar = ContextCompat.getColor(this, R.color.colorPrimaryDay);
            colorFromBackground = ContextCompat.getColor(this, R.color.colorBackgroundNight);
            colorToBackground = ContextCompat.getColor(this, R.color.colorBackgroundDay);
            joined.setTextColor(ContextCompat.getColor(this, R.color.colorTextDay));
            left.setTextColor(ContextCompat.getColor(this, R.color.colorTextDay));
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            colorFromStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryDayDark);
            colorToStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryNightDark);
            colorFromToolbar = ContextCompat.getColor(this, R.color.colorPrimaryDay);
            colorToToolbar = ContextCompat.getColor(this, R.color.colorPrimaryNight);
            colorFromBackground = ContextCompat.getColor(this, R.color.colorBackgroundDay);
            colorToBackground = ContextCompat.getColor(this, R.color.colorBackgroundNight);
            joined.setTextColor(ContextCompat.getColor(this, R.color.colorTextNight));
            left.setTextColor(ContextCompat.getColor(this, R.color.colorTextNight));
        }

        changeStatusBarColor(colorFromStatusBar, colorToStatusBar);
        changeToolbarColor(colorFromToolbar, colorToToolbar);
        changeBackgroundColor(colorFromBackground, colorToBackground);

    }

    private void changeStatusBarColor(int colorFrom, int colorTo) {
        ValueAnimator statusBarAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        statusBarAnimator.setDuration(200);
        statusBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
            }
        });
        statusBarAnimator.start();
    }

    private void changeToolbarColor(int colorFrom, int colorTo) {
        ValueAnimator toolbarAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        toolbarAnimator.setDuration(200);
        toolbarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable((Integer) animation.getAnimatedValue()));
            }
        });
        toolbarAnimator.start();
    }

    private void changeBackgroundColor(int colorFrom, int colorTo) {
        final ValueAnimator backgroundAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        backgroundAnimator.setDuration(200);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        backgroundAnimator.start();
    }
}
