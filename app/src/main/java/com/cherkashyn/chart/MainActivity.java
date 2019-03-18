package com.cherkashyn.chart;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cherkashyn.telegramchart.model.Followers;
import com.cherkashyn.telegramchart.utils.JSONParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChartsAdapter chartsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Statistics");

        chartsAdapter = new ChartsAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chartsAdapter);

        parseJSON();
    }

    private void parseJSON() {
        try {
            List<Followers> followersList =
                    JSONParser.parseJSONToListOfFollowers(getJSONStringFromFile());
            chartsAdapter.setData(followersList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getJSONStringFromFile() throws IOException {
        InputStream is = getResources().getAssets().open("chart_data.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s).append("\n");
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
            chartsAdapter.changeTheme();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            colorFromStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryDayDark);
            colorToStatusBar = ContextCompat.getColor(this, R.color.colorPrimaryNightDark);
            colorFromToolbar = ContextCompat.getColor(this, R.color.colorPrimaryDay);
            colorToToolbar = ContextCompat.getColor(this, R.color.colorPrimaryNight);
            colorFromBackground = ContextCompat.getColor(this, R.color.colorBackgroundDay);
            colorToBackground = ContextCompat.getColor(this, R.color.colorBackgroundNight);

            chartsAdapter.changeTheme();
        }

        getWindow().setStatusBarColor(colorToStatusBar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorToToolbar));
        recyclerView.setBackgroundColor(colorToBackground);
    }
}
