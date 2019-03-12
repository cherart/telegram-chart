package com.cherkashyn.telegramchart;

import android.content.Context;
import android.util.Log;

import com.cherkashyn.telegramchart.model.Followers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JSONParser {

    public static Followers readJSONFile(Context context) throws IOException, JSONException {
        String json = readText(context);
        JSONArray jsonArray = new JSONArray(json);
        JSONObject object = jsonArray.getJSONObject(0);
        JSONArray columns = object.getJSONArray("columns");
        JSONArray x = columns.getJSONArray(0);
        x.remove(0);
        long[] xArray = new long[x.length()];
        for (int i = 0; i < xArray.length; i++) {
            xArray[i] = x.getLong(i);
        }



        Log.i("Parser", String.valueOf(xArray[0]));
//        Log.i("Parser", String.valueOf(jsonArray.length()));
//        Log.i("Parser", String.valueOf(columns.length()));

        return null;
    }

    private static String readText(Context context) throws IOException {
        InputStream is = context.getResources().getAssets().open("chart_data.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
