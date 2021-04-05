package tech.qianmi.flyingmybatis.automapper;

import java.util.List;
import tech.qianmi.flyingmybatis.PrimaryKey.KeyType;

/**
 * Information after converting object into database table information.
 *
 * @author yanan.zhang
 * @since 2021/2/18
 */
class TableInfo {

    /** Database table name */
    private String tableName;

    /** Database primary key */
    private String primaryKey;

    /** The name of the primary key field */
    private String primaryKeyField;

    /** Generation of primary keys */
    private KeyType keyType;

    /** The name of the generated domain object class */
    private String domainName;

    /** Field information for the generated domain object class */
    private List<ColumnInfo> columnInfos;

    /** Basic columns separated by commas, user queries and insert statements */
    private String baseColumns;

    /** Used to insert statements  */
    private String intoValues;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(String primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public String getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(String baseColumns) {
        this.baseColumns = baseColumns;
    }

    public String getIntoValues() {
        return intoValues;
    }

    public void setIntoValues(String intoValues) {
        this.intoValues = intoValues;
    }
}
