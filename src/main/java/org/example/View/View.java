package org.example.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class View {
    public ArrayList<HashMap<String, String>> rows;
    /*
        TODO:
            - Use print formatter for equal spacing
            - Figure out where max length of any string in a view for formatter
     */
    public void display() {
        if (rows.size() == 0) {
            System.out.println("view empty");
            return;
        }
        ArrayList<String> columns = new ArrayList<>(rows.get(0).keySet());
        for (String column : columns) {
            System.out.print(column + "\t");
        }
        System.out.println();
        for (HashMap<String, String> map : rows) {
            for (String column : columns) {
                System.out.print(map.get(column) + "\t");
            }
            System.out.println();
        }
    }
}
