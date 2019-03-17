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
            followers.setListOfX(getArrayOfX(followersJson));
            for (int j = 1; j < followersJson.getJSONArray("columns").length(); j++) {
                followers.addLine(getY(followersJson, j));
            }
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
        lineY.setListOfY(getArrayOfY(columnsJson.getJSONArray(index)));
        JSONObject nameJson = followersJson.getJSONObject("names");
        JSONObject colorJson = followersJson.getJSONObject("colors");
        lineY.setName(nameJson.getString("y" + (index - 1)));
        lineY.setColor(colorJson.getString("y" + (index - 1)));
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
