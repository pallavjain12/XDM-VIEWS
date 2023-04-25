package org.example.Execute;

import org.example.Operations.Join.Join;
import org.example.Operations.Union.Union;
import org.example.Source.CSVSource;
import org.example.Source.JSONSource;
import org.example.Source.SQLSource;
import org.example.Source.XMLSource;
import org.example.View.CSVSource.CSVView;
import org.example.View.JSONView.JSONView;
import org.example.View.SQLView.SQLView;
import org.example.View.XMLView.XMLView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ExecuteXML {
    File file;
    String url;
    HashMap<String, JSONSource> jsonSourceHashMap;
    HashMap<String, CSVSource> csvSourceHashMap;
    HashMap<String, XMLSource> xmlSourceHashMap;
    HashMap<String, SQLSource> sqlSourceHashMap;
    HashMap<String, SQLView> sqlViewHashMap;
    HashMap<String, CSVView> csvViewHashMap;
    HashMap<String, JSONView> jsonViewHashMap;
    HashMap<String, XMLView> xmlViewHashMap;
    JSONObject statements;

    HashMap<String, JSONArray> allViews;
    ExecuteXML(String url) {
        this.url = url;
        statements = null;
        allViews = new HashMap<>();
        jsonSourceHashMap = new HashMap<>();
        jsonViewHashMap = new HashMap<>();

        csvSourceHashMap = new HashMap<>();
        csvViewHashMap = new HashMap<>();

        xmlSourceHashMap = new HashMap<>();
        xmlViewHashMap = new HashMap<>();

        sqlSourceHashMap = new HashMap<>();
        sqlViewHashMap = new HashMap<>();
    }



    public void execute() {
        // check if xml files matches with schema

        // Convert to more read able format
        moreReadAbleFormat();
        System.out.println("Converted XML file to more read able format");
        addSources();
        System.out.println("Added sources");
        addViews();
        System.out.println("added views");
        System.out.println(allViews.toString());
        doOperations();
        System.out.println("Completed operations");
    }
    private void moreReadAbleFormat() {
        try {
            File file = new File(url);
            if (file.exists() && !file.isDirectory()) this.file = file;
            else throw new RuntimeException("File not found at " + url);

            String line = "", str = "";
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            while ((line = br.readLine()) != null) {
                str+=line;
            }
            this.statements = XML.toJSONObject(str);
            System.out.println(statements);
            this.filterStatements();
            System.out.println("after filter" + statements.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("current statements = " + statements.toString());
            throw new RuntimeException("Error occurred while reading file. " + e);
        }

    }

    private void addSources() {
        JSONObject sources = statements.getJSONObject("data");
        if (!sources.isNull("xml")) {
            JSONArray xmlArray = sources.getJSONArray("xml");
            for (int i = 0; i < xmlArray.length(); i++) {

                JSONObject obj = xmlArray.getJSONObject(i);
                String name = obj.get("name").toString();

                if (sourceAlreadyDeclared(name)) throw new RuntimeException("Duplicate source name found : " + name);

                XMLSource xmlSource = new XMLSource(obj.get("url").toString(), obj.get("local").toString().equals("true"));
                this.xmlSourceHashMap.put(name, xmlSource);
            }
        }
        if (!sources.isNull("csv")) {
            JSONArray csvArray = sources.getJSONArray("csv");
            for (int i = 0; i < csvArray.length(); i++) {

                JSONObject obj = csvArray.getJSONObject(i);
                String name = obj.get("name").toString();

                if (sourceAlreadyDeclared(name)) throw new RuntimeException("Duplicate source name found : " + name);

                CSVSource csvSource = new CSVSource(obj.get("url").toString(), obj.get("local").toString().equals("true"));
                this.csvSourceHashMap.put(name, csvSource);
            }
        }
        if (!sources.isNull("json")) {
            JSONArray jsonArray = sources.getJSONArray("json");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.get("name").toString();

                if (sourceAlreadyDeclared(name)) throw new RuntimeException("Duplicate source name found : " + name);

                JSONSource source = new JSONSource(obj.get("url").toString(), obj.get("local").toString().equals("true"));
                this.jsonSourceHashMap.put(name, source);
            }
        }
        if (!sources.isNull("mysql")) {
            JSONArray sqlArray = sources.getJSONArray("mysql");
            for (int i = 0; i < sqlArray.length(); i++) {
                JSONObject object = sqlArray.getJSONObject(i);
                String name = object.get("name").toString();

                if (sourceAlreadyDeclared(name)) throw new RuntimeException("Duplicate source name found : " + name);

                SQLSource source = new SQLSource(object.get("url").toString(), object.get("username").toString(), object.get("password").toString());
                this.sqlSourceHashMap.put(name, source);
            }
        }
    }

    private void addViews() {
        JSONObject views = statements.getJSONObject("view");

        // Todo: create view for xml_base

        if (!views.isNull("xml_base")) {
            JSONArray xmlArray = views.getJSONArray("xml_base");
            for (int i = 0; i < xmlArray.length(); i++) {

                JSONObject object = xmlArray.getJSONObject(i);
                if (!xmlSourceHashMap.containsKey(object.get("source").toString())) throw new RuntimeException("Source not found for view : " + object.get("name").toString());
                XMLView view = new XMLView();
                view.addSource(xmlSourceHashMap.get(object.get("source").toString()));

                if (!object.isNull("condition")) view.addCondition(object.getString("condition"));

                JSONArray field = object.getJSONArray("field");
                for (int j = 0; j < field.length(); j++) {
                    if (field.getJSONObject(j).get("selected").toString().equals("true")) view.addSelect(field.getJSONObject(j).get("content").toString());
                }

                view.loadData();
                allViews.put(object.get("name").toString(), view.getJSONArray());
            }
        }

        if (!views.isNull("json_base")) {
            JSONArray jsonArray = views.getJSONArray("json_base");
            for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (object.isNull("name"))  throw new RuntimeException("Name not found for a json view");
                if (!sourceAlreadyDeclared(object.get("source").toString())) throw new RuntimeException("Source not found for view : " + object.get("name").toString());
                if (viewNameAlreadyDeclared(object.get("name").toString())) throw new RuntimeException("Duplicate view name found : " + object.get("name").toString());

                JSONView view = new JSONView();

                view.addSource(jsonSourceHashMap.get(object.get("source").toString()));

                if (!object.isNull("field")) {
                    JSONArray fileds = object.getJSONArray("field");
                    for (int j = 0; j < fileds.length(); j++) {
                        if (fileds.getJSONObject(j).get("selected").toString().equals("true")) {
                            view.addSelect(fileds.getJSONObject(j).get("content").toString());
                        }
                    }
                }

                if (!object.isNull("condition")) {
                    view.addConditions(object.get("condition").toString());
                }

                jsonViewHashMap.put(object.get("name").toString(), view);
                view.loadData();
                allViews.put(object.get("name").toString(), view.getJSONArray());
            }
        }

        if (!views.isNull("mysql_base")) {
            JSONArray sqlArray = views.getJSONArray("mysql_base");
            for (int i = 0; i < sqlArray.length(); i++) {
                JSONObject object = sqlArray.getJSONObject(i);
                if (object.isNull("name"))  throw new RuntimeException("Name not found for a sql view");
                if (!sourceAlreadyDeclared(object.get("source").toString()))  throw new RuntimeException("Source not found for view : " + object.get("name").toString());
                if (viewNameAlreadyDeclared(object.get("name").toString())) throw new RuntimeException("Duplicate view name found : " + object.get("name").toString());

                SQLView sqlView = new SQLView();
                sqlView.addSource(sqlSourceHashMap.get(object.get("source").toString()));
                sqlView.addTable(object.get("table").toString());

                JSONArray fields = object.getJSONArray("field");
                if (!object.isNull("field")) {
                    for (int j = 0; j < fields.length(); j++) {
                        if (fields.getJSONObject(j).get("selected").toString().equals("true")) sqlView.addSelectColumn(fields.getJSONObject(j).get("content").toString());
                    }
                }

                if (!object.isNull("condition")) {
                    sqlView.addCondition(object.getString("condition"));
                }


                sqlView.loadData();

                allViews.put(object.get("name").toString(), sqlView.getJSONArray());
                this.sqlViewHashMap.put(object.get("name").toString(), sqlView);
            }
        }

        if (!views.isNull("csv_base")) {
            JSONArray csvArray = views.getJSONArray("csv_base");
            for (int i = 0; i < csvArray.length(); i++) {
                JSONObject object = csvArray.getJSONObject(i);

                if (object.isNull("name"))  throw new RuntimeException("Name not found for a csv view");
                if (!sourceAlreadyDeclared(object.get("source").toString())) throw new RuntimeException("Source not found for view : " + object.get("name").toString());
                if (viewNameAlreadyDeclared(object.get("name").toString()))  throw new RuntimeException("Duplicate view name found : " + object.get("name").toString());

                CSVView view = new CSVView();
                view.addSource(csvSourceHashMap.get(object.get("source").toString()));

                if (!object.isNull("field")) {
                    JSONArray fields = object.getJSONArray("field");
                    for (int j = 0; j < fields.length(); j++) {
                        if (fields.getJSONObject(j).get("selected").toString().equals("true"))
                            view.addSelect(fields.getJSONObject(j).get("content").toString());
                    }
                }

                if (!object.isNull("condition")) {
                    view.addCondition(object.get("condition").toString());
                }

                view.loadData();
                csvViewHashMap.put(object.get("name").toString(), view);
//                allViews.put(object.get("name").toString(), view.getJSONArray());
            }
        }
    }

    private void doOperations() {
        Queue<Pair> queue = new LinkedList<>();
        int lastKnownSize = 0;

        putAllOperationsInQueue(queue);

        while(queue.size() != lastKnownSize) {
            lastKnownSize = queue.size();
            int counter = queue.size();
            while (counter-- > 0) {
                Pair pair = queue.poll();
                JSONObject object = pair.getObject();

                if (!allSourcesAvailable(pair)) {
                    queue.add(pair);
                    continue;
                }

                if (pair.getOperation().equals("union")) {
                    if (object.isNull("name"))  throw new RuntimeException("Name not found for a union operation");

                    Union unionOperation = new Union();
                    JSONArray unionSource = object.getJSONArray("source");

                    for (int i = 0 ;i < unionSource.length(); i++) {

                        if (!allViews.containsKey(unionSource.get(i).toString())) throw new RuntimeException("View not found : " + object.get("name").toString());
                        unionOperation.addSource(allViews.get(unionSource.get(i).toString()));
                    }
                    allViews.put(object.get("name").toString(), unionOperation.getRows());
                }

                // join operation
                else {
                    if (object.isNull("name"))  throw new RuntimeException("Name not found for join view");

                    Join joinOperation = new Join();
                    JSONArray sourceArray = object.getJSONArray("source");
                    if (sourceArray.length() != 2) throw new RuntimeException("Join only supported for 2 views");
                    if (object.isNull("name"))  throw new RuntimeException("Name not found for join view");

                    for (int i = 0; i < 2; i++) {
                        JSONObject sourceTemp = sourceArray.getJSONObject(i);
                        if (sourceTemp.isNull("type")) throw new RuntimeException("Type required for view : " + object.get("name").toString());
                        if (sourceTemp.get("type").toString().equals("parent")) {
                            joinOperation.addParent(allViews.get(sourceTemp.get("content").toString()), sourceTemp.get("on").toString());
                        }
                        else {
                            joinOperation.addChild(allViews.get(sourceTemp.get("content").toString()), sourceTemp.get("on").toString());
                        }
                    }
                    allViews.put(object.get("name").toString(), joinOperation.getView());
                }
            }
        }
        if (lastKnownSize != 0) throw new RuntimeException("Circular view dependency found. Please check your executable file");
    }

    private boolean sourceAlreadyDeclared(String name) {
        return jsonSourceHashMap.containsKey(name) || csvSourceHashMap.containsKey(name) || xmlSourceHashMap.containsKey(name) || sqlSourceHashMap.containsKey(name);
    }

    private boolean viewNameAlreadyDeclared(String name) {
        return jsonViewHashMap.containsKey(name) || csvViewHashMap.containsKey(name) || xmlViewHashMap.containsKey(name) || sqlViewHashMap.containsKey(name);
    }

    private void putAllOperationsInQueue(Queue<Pair> queue) {
        JSONObject operations = statements.getJSONObject("operation");

        if (!operations.isNull("union")) {
            JSONArray unions = operations.getJSONArray("union");
            for (int i = 0; i < unions.length(); i++) {
                Pair pair = new Pair("union", unions.getJSONObject(i));
                queue.add(pair);
            }
        }
        if (!operations.isNull("join")) {
            JSONArray unions = operations.getJSONArray("join");
            for (int i = 0; i < unions.length(); i++) {
                Pair pair = new Pair("join", unions.getJSONObject(i));
                queue.add(pair);
            }
        }
    }

    private boolean allSourcesAvailable(Pair pair) {
        if (pair.getOperation().equals("union")) {
            JSONArray array = pair.getObject().getJSONArray("source");
            for (int i = 0; i < pair.getObject().getJSONArray("source").length(); i++) {
                if (!allViews.containsKey(array.get(i).toString())) return false;
            }
        }
        else {
            JSONArray source = pair.getObject().getJSONArray("source");
            for (int i = 0; i < source.length(); i++) {
                JSONObject sourceTemp = source.getJSONObject(i);
                if (!allViews.containsKey(sourceTemp.get("content").toString()))    return false;
            }
        }
        return true;
    }

    private void filterStatements() {
        statements = statements.getJSONObject("xdmview");

        JSONObject dataObject = statements.getJSONObject("data");
        if (!dataObject.isNull("xml")) {
            if (dataObject.get("xml") instanceof JSONObject ) {
                JSONArray array = new JSONArray();
                array.put(dataObject.getJSONObject("xml"));
                statements.getJSONObject("data").put("xml", array);
            }
        }
        if (!dataObject.isNull("csv")) {
            if (dataObject.get("csv") instanceof JSONObject ) {
                JSONArray array = new JSONArray();
                array.put(dataObject.getJSONObject("csv"));
                statements.getJSONObject("data").put("csv", array);
            }
        }
        if (!dataObject.isNull("json")) {
            if (dataObject.get("json") instanceof JSONObject ) {
                JSONArray array = new JSONArray();
                array.put(dataObject.getJSONObject("json"));
                statements.getJSONObject("data").put("json", array);
            }
        }
        if (!dataObject.isNull("mysql")) {
            if (dataObject.get("mysql") instanceof JSONObject ) {
                JSONArray array = new JSONArray();
                array.put(dataObject.getJSONObject("mysql"));
                statements.getJSONObject("data").put("mysql", array);
            }
        }

        JSONObject viewObject = statements.getJSONObject("view");

        if (!viewObject.isNull("xml_base")) {
            if (viewObject.get("xml_base") instanceof JSONObject) {
                JSONArray array = new JSONArray();
                array.put(statements.getJSONObject("view").getJSONObject("xml_base"));
                statements.getJSONObject("view").put("xml_base", array);
            }
        }
        if (!viewObject.isNull("json_base")) {
            if (viewObject.get("json_base") instanceof JSONObject) {
                JSONArray array = new JSONArray();
                array.put(statements.getJSONObject("view").getJSONObject("json_base"));
                statements.getJSONObject("view").put("json_base", array);
            }
        }
        if (!viewObject.isNull("mysql_base")) {
            if (viewObject.get("mysql_base") instanceof JSONObject) {
                JSONArray array = new JSONArray();
                array.put(statements.getJSONObject("mysql_base"));
                statements.getJSONObject("view").put("mysql_base", array);
            }
        }
        if (!viewObject.isNull("csv_base")) {
            if (viewObject.get("csv_base") instanceof JSONObject) {
                JSONArray array = new JSONArray();
                array.put(statements.getJSONObject("view").getJSONObject("csv_base"));
                statements.getJSONObject("view").put("csv_base", array);
            }
        }

        String[] bases = new String[]{"xml_base", "json_base", "mysql_base", "csv_base"};

        viewObject = statements.getJSONObject("view");

        for(int i = 0; i < 4; i++) {
            if (viewObject.isNull(bases[i])) continue;
            JSONArray baseArray = viewObject.getJSONArray(bases[i]);

            JSONArray newBaseArray = new JSONArray();
            for (int j = 0; j < baseArray.length(); j++) {
                JSONObject src = baseArray.getJSONObject(j);
                if (src.isNull("field")) continue;
                if (src.get("field") instanceof JSONObject) {
                    JSONArray arr = new JSONArray();
                    arr.put(src.get("field"));
                    src.put("field", arr);
                }
                newBaseArray.put(src);
            }
            statements.getJSONObject("view").put(bases[i], newBaseArray);
        }

        JSONObject operationObj = statements.getJSONObject("operation");
        if (!operationObj.isNull("union")) {
            if (operationObj.get("union") instanceof JSONObject) {
                JSONArray arr = new JSONArray();
                arr.put(operationObj.getJSONObject("union"));
                statements.getJSONObject("operation").put("union", arr);
            }
        }
        if (!operationObj.isNull("join")) {
            if (operationObj.get("join") instanceof JSONObject) {
                JSONArray arr = new JSONArray();
                arr.put(operationObj.getJSONObject("join"));
                statements.getJSONObject("operation").put("join", arr);
            }
        }
    }
    public void displayAllViews() {
        System.out.println(allViews.toString());
    }

    public void display(String viewName) {
        if (allViews.containsKey(viewName)) {
            System.out.println(allViews.get(viewName).toString());
        }
        else {
            System.out.println("View with name " + viewName + " not found.");
        }
    }


    public static void main(String[] args) {
        ExecuteXML execute = new ExecuteXML("/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/Execute/query1.xml");
        execute.execute();
        execute.displayAllViews();
    }
}
