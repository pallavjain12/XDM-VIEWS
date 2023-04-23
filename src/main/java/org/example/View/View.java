package org.example.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class View {
    public JSONObject rows;
    /*
        TODO:
            - Use print formatter for equal spacing
            - Figure out where max length of any string in a view for formatter
     */
    public void display() {
    }
}
