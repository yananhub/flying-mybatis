package tech.yanand.flyingmybatis;

/**
 * IInformation after converting an object field to database column information.
 */
class ColumnInfo {

    private String fieldName;

    private boolean string;

    /** Database column name */
    private String columnName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isString() {
        return string;
    }

    public void setString(boolean string) {
        this.string = string;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
