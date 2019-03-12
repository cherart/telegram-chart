package com.cherkashyn.telegramchart.utils;

import com.cherkashyn.telegramchart.model.Followers;
import com.cherkashyn.telegramchart.model.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<Followers> parseJSONToListOfFollowers(String json) throws JSONException {
        JSONArray followersArray = new JSONArray(json);
        List<Followers> followersList = new ArrayList<>();

        for (int i = 0; i < followersArray.length(); i++) {
            JSONObject followersJson = followersArray.getJSONObject(i);

            Followers followers = new Followers();
            followers.setX(getArrayOfX(followersJson));
            followers.setLineYZero(getY(followersJson, 0));
            followers.setLineYOne(getY(followersJson, 1));

            followersList.add(followers);
        }

        return followersList;
    }

    private static List<Long> getArrayOfX(JSONObject followersJson) throws JSONException {
        JSONArray columnsJson = followersJson.getJSONArray("columns");
        JSONArray x = columnsJson.getJSONArray(0);
        x.remove(0);
        List<Long> listOfX = new ArrayList<>();
        for (int i = 0; i < x.length(); i++) {
            listOfX.add(x.getLong(i));
        }
        return listOfX;
    }

    private static Line getY(JSONObject followersJson, int index) throws JSONException {
        JSONArray columnsJson = followersJson.getJSONArray("columns");

        Line lineY = new Line();
        lineY.setY(getArrayOfY(columnsJson.getJSONArray(index + 1)));

        JSONObject nameJson = followersJson.getJSONObject("names");
        JSONObject colorJson = followersJson.getJSONObject("colors");
        if (index == 0) {
            lineY.setName(nameJson.getString("y0"));
            lineY.setColor(colorJson.getString("y0"));
        } else {
            lineY.setName(nameJson.getString("y1"));
            lineY.setColor(colorJson.getString("y1"));
        }

        return lineY;
    }

    private static List<Integer> getArrayOfY(JSONArray y) throws JSONException {
        y.remove(0);
        List<Integer> listOfY = new ArrayList<>();
        for (int i = 0; i < y.length(); i++) {
            listOfY.add(y.getInt(i));
        }
        return listOfY;
    }
}
