package org.example.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class View {
    HashMap<String , HashMap<String, String>> map;
    ArrayList<String> columns;

    /*
        TODO:
            - Use print formatter for equal spacing
            - Figure out where max length of any string in a view for formatter
     */
    public void display() {
        if (map == null  || map.isEmpty()) {
            System.out.println("view empty");
            return;
        }
        for (Map.Entry<String, HashMap<String, String>> em : map.entrySet()) {
            for (Map.Entry<String, String> itr : em.getValue().entrySet()) {
                System.out.print(itr.getValue() + " ");
            }
        }
    }

    public void setColumns() {
        for (Map.Entry<String, HashMap<String, String>> em : map.entrySet()) {
            for (Map.Entry<String, String> i : em.getValue().entrySet()) {
                columns.add(i.getValue());
            }
        }
    }
}
