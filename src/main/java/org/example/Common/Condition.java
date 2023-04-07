package org.example.Common;

public class Condition {
    private String Column;

    /*
        TODO: Use enum key value pair for operator to provide sanitization of input
     */
    private String operator;
    private String value;

    public String getColumn() {
        return Column;
    }

    public void setColumn(String column) {
        Column = column;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
