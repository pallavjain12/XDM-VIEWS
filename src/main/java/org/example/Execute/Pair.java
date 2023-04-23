package org.example.Execute;

import org.json.JSONObject;

public class Pair {
    String operation;
    JSONObject object;
    public Pair(String operation, JSONObject object) {
        this.operation = operation;
        this.object = object;
    }

    public String getOperation() {
        return operation;
    }

    public JSONObject getObject() {
        return object;
    }
}
