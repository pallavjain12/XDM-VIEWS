package org.example.Operations.Union;

import org.json.JSONArray;

import java.util.ArrayList;

public class Union {
    JSONArray rows;
    public Union() {
        this.rows = new JSONArray();
    }

    public void addSource(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            this.rows.put(array.get(i));
        }
    }

    public JSONArray getRows() {
        return this.rows;
    }
}
