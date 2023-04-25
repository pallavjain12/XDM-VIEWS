package org.example.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Condition {
    public static JSONArray filterBasedOnCondition(JSONArray array, String conditions) {
        if (conditions == null) return array;
        String[] condition = conditions.split("=");
        if (condition[0].split("/").length > 1) {
            String[] path = condition[0].split("/");
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject temp = array.getJSONObject(i);
                for (int j = 1; j < path.length - 1; j++) {
                    if (temp.isNull(path[j])) break;
                    temp = temp.getJSONObject(path[j]);
                }

                if (temp.get(path[path.length - 1]).toString().equals(condition[1])) newArray.put(array.getJSONObject(i));
            }
            return newArray;
        }
        else {
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject temp = array.getJSONObject(i);
                if (temp.get(condition[0]).toString().equalsIgnoreCase(condition[1])) newArray.put(temp);
            }
            return newArray;
        }
    }

    public static JSONArray selectSelectedAttributes(JSONArray array, ArrayList<String> selected) {
        JSONArray newArray = new JSONArray();
        if (array.length() == 0) return new JSONArray();
        ArrayList<String> keys = new ArrayList<>(array.getJSONObject(0).keySet());
        for (int i = 0; i < array.length(); i++) {
            JSONObject dataRow = array.getJSONObject(i);
            JSONObject newRow = new JSONObject();
            for (int j = 0; j < selected.size(); j++) {
                if (!dataRow.isNull(selected.get(j))) newRow.put(selected.get(j), dataRow.get(selected.get(j)));
                else newRow.put(selected.get(j), "");
            }
            newArray.put(newRow);
        }
        return newArray;
    }
}
