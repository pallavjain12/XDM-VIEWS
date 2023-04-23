package org.example.Operations.Join;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class Join {
    private JSONArray parent;
    private JSONArray child;

    private String parentColumn;
    private String childColumn;
    private JSONArray rows;
    public Join() {
        parent = null;
        child = null;
        parentColumn = null;
        childColumn = null;
    }

    public void addParent(JSONArray parent, String parentColumn) {
        this.parent = parent;
        this.parentColumn = parentColumn;
    }

    public void addChild(JSONArray child, String childColumn) {
        this.child = child;
        this.childColumn = childColumn;
    }

    public JSONArray getView() {
        if (parentColumn.split("/").length == 1 && childColumn.split("/").length == 1)
            return bothLinear();
        else if (parentColumn.split("/").length != 1 && childColumn.split("/").length == 1) {
            return parentComplexChildLinear();
        }
        else if (parentColumn.split("/").length == 1 && childColumn.split("/").length != 1) {
            return parentLinearChildComplex();
        }
        else {
            return bothComplex();
        }
    }

    private JSONArray bothLinear() {
        JSONArray ans = new JSONArray();

        for (int i = 0; i < parent.length(); i++) {
            JSONObject parentObject = parent.getJSONObject(i);
            String value = parentObject.get(parentColumn).toString();

            for (int j = 0; j < child.length(); j++) {
                JSONObject childObject = child.getJSONObject(j);

                if (childObject.get(childColumn).equals(value)) {
                    JSONObject obj = new JSONObject(parentObject.toString());

                    ArrayList<String> keys = new ArrayList<>(childObject.keySet());

                    for (String key : keys) {
                        obj.put("child_" + key, childObject.get(key));
                    }
                    ans.put(obj);
                }
            }
        }
        return ans;
    }

    private JSONArray parentComplexChildLinear() {
        JSONArray ans = new JSONArray();

        for (int i = 0; i < parent.length(); i++) {

            JSONObject parentObject = parent.getJSONObject(i);
            String value = getValue(parentObject, parentColumn);

            for (int j = 0; j < child.length(); j++) {

                JSONObject childObj = child.getJSONObject(j);

                if (childObj.get(childColumn).equals(value)) {
                    ans.put(fillComplexInLinear(parentObject, childObj));
                }
            }
        }
        return  ans;
    }

    private String getValue(JSONObject obj, String path) {
        String[] arr = path.split("/");

        for (int i = 1; i < arr.length - 1; i++) {
            obj = obj.getJSONObject(arr[i]);
        }
        return obj.get(arr[arr.length - 1]).toString();
    }

    private JSONObject fillComplexInLinear(JSONObject parentObj, JSONObject childObj) {
        JSONObject temp = new JSONObject(parentObj.toString());
        String[] arr = parentColumn.split("/");
        for (int i = 1; i < arr.length - 1; i++) {
            temp = temp.getJSONObject(arr[i]);
        }

        ArrayList<String> keys = new ArrayList<>(childObj.keySet());
        for (int i = 0; i < keys.size(); i++) {
            temp.put("child_" + keys.get(i), childObj.get(keys.get(i)));
        }

        JSONObject ans = new JSONObject(parentObj.toString());
        return updateJSONRecursively(arr, 1, ans, temp);
    }

    private JSONObject updateJSONRecursively(String[] arr, int index, JSONObject ans, JSONObject newValue) {
        if (index == arr.length - 2) {
            ans.put(arr[arr.length - 2], newValue);
            return ans;
        }
        else {
            ans.put(arr[index], updateJSONRecursively(arr, index + 1, ans.getJSONObject(arr[index]), newValue));
            return ans;
        }
    }

    private JSONArray parentLinearChildComplex() {
        JSONArray ans = new JSONArray();

        for (int i = 0; i < parent.length(); i++) {

            JSONObject parentObj = parent.getJSONObject(i);
            String value = parentObj.get(parentColumn).toString();
            String[] childColumnArr = childColumn.split("/");

            for (int j = 0; j < child.length(); j++) {
                JSONObject childObject = child.getJSONObject(j);
                JSONObject getObj = getChildNestedObj(childObject);
                if (getObj.get(childColumnArr[childColumnArr.length - 1]).equals(value)) {
                    JSONObject temp = new JSONObject(parentObj.toString());
                    temp.put(childColumnArr[childColumnArr.length - 1], getObj);
                    ans.put(temp);
                }
            }
        }
        return ans;
    }

    private JSONObject getChildNestedObj(JSONObject object) {
        String[] arr = childColumn.split("/");
        for (int i = 1; i < arr.length - 1; i++) {
            object = object.getJSONObject(arr[i]);
        }
        return object;
    }

    private JSONArray bothComplex() {
        JSONArray ans = new JSONArray();
        System.out.println("both complex");
        return ans;
    }
}
