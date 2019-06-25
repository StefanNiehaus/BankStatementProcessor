package com.niehaus.modal;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    private static final String DB_NAME = "finance.db";

    private static final String CONNECTION_STRING = "jdbc:sqlite:" + System.getProperty("user.dir") +"/" + DB_NAME;

    /* Table for processed entries with classifications*/
    private static final String TABLE_ENTRIES = "entries";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CREDIT = "debit_or_credit";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_EXPLANATION = "explanation";
    private static final String COLUMN_MAIN_CATEGORY = "main_category";
    private static final String COLUMN_SUB_CATEGORY = "sub_category";
    private static final String COLUMN_SOURCE_STATEMENT = "source_statement";
    private static final String COLUMN_TYPE = "type";

    /* Table for categories to speed up user interface manual classification*/
    private static final String TABLE_CLASSIFICATIONS = "classifications";
    private static final String COLUMN_IDENTIFIER = "identifier";

    private static final String CONSTRUCT_TABLE_PROCESSED_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ENTRIES + " (" +
                    COLUMN_AMOUNT + " DECIMAL, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_MAIN_CATEGORY + " TEXT, " +
                    COLUMN_SUB_CATEGORY + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_EXPLANATION + " TEXT, " +
                    COLUMN_SOURCE_STATEMENT + " TEXT, " +
                    COLUMN_CREDIT + " TEXT, " +
                    COLUMN_TYPE + " TEXT)";


    private static final String CONSTRUCT_TABLE_CLASSIFICATIONS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CLASSIFICATIONS + " (" +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_MAIN_CATEGORY + " TEXT, " +
                    COLUMN_SUB_CATEGORY + " TEXT, " +
                    COLUMN_IDENTIFIER + " TEXT)";

    private Connection conn;

    private void loadDriver() throws Exception {
        URL url = getClass().getResource("sqlite-jdbc-3.27.2.1.jar");
//        URL u = new URL("jar:file:/home/stefan/workplace/lib/SQLite/sqlite-jdbc-3.27.2.1.jar!/");
        String classname = "org.sqlite.JDBC";
        URLClassLoader ucl = new URLClassLoader(new URL[] { url });
        Driver d = (Driver)Class.forName(classname, true, ucl).getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new DriverShim(d));
    }

    public boolean open() {
        try {
            loadDriver();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            createTables();
            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Entry> getClassification(Entry entry) {
        String description = entry.getDescription();
        List<Entry> categories = new ArrayList<>();

        StringBuilder sb = new StringBuilder("SELECT " +
                COLUMN_TYPE + ", " +
                COLUMN_MAIN_CATEGORY + ", " +
                COLUMN_SUB_CATEGORY +
                " FROM " + TABLE_CLASSIFICATIONS +
                " WHERE '" + description + "' LIKE '%' || " + COLUMN_IDENTIFIER + " || '%' COLLATE NOCASE ");

        try {
            Statement statement = conn.createStatement();
            System.out.println("QUERY: " + sb.toString());
            ResultSet results = statement.executeQuery(sb.toString());

            while(results.next()) {
                Entry possibleMatch = new Entry(entry);
                possibleMatch.setType(results.getString(COLUMN_TYPE).trim());
                possibleMatch.setMainCategory(results.getString(COLUMN_MAIN_CATEGORY).trim());
                possibleMatch.setSubCategory(results.getString(COLUMN_SUB_CATEGORY).trim());
                categories.add(possibleMatch);
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return categories;
    }

    private void createTables() {
        try {
            Statement statement = conn.createStatement();
            statement.execute(CONSTRUCT_TABLE_PROCESSED_ENTRIES);
            statement.execute(CONSTRUCT_TABLE_CLASSIFICATIONS);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<String> getEntryTypeList() {
        List<String> typeList = new ArrayList<>();

        String query = "SELECT DISTINCT " + COLUMN_TYPE + " FROM " + TABLE_CLASSIFICATIONS;

        try {
            Statement statement = conn.createStatement();
            System.out.println("QUERY: " + query);
            ResultSet results = statement.executeQuery(query);

            while(results.next()) {
                typeList.add(results.getString(COLUMN_TYPE));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return typeList;
    }

    public List<String> getEntryMainCategoryList(String type) {
        List<String> mainCategoryList = new ArrayList<>();
        System.out.println("In getEntryMainCategoryList");

        String query = "SELECT DISTINCT " + COLUMN_MAIN_CATEGORY +
                " FROM " + TABLE_CLASSIFICATIONS +
                " WHERE " + COLUMN_TYPE + " LIKE '" + type + "'";

        try {
            Statement statement = conn.createStatement();
            System.out.println("QUERY: " + query);
            ResultSet results = statement.executeQuery(query);

            while(results.next()) {
                mainCategoryList.add(results.getString(COLUMN_MAIN_CATEGORY));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Returning from getEntryMainCategoryList");
        return mainCategoryList;
    }

    public List<String> getEntrySubCategoryList(String mainCategory) {
        List<String> subCategoryList = new ArrayList<>();

        String query = "SELECT DISTINCT " + COLUMN_SUB_CATEGORY +
                " FROM " + TABLE_CLASSIFICATIONS +
                " WHERE " + COLUMN_MAIN_CATEGORY + " LIKE '" + mainCategory + "'";

        try {
            Statement statement = conn.createStatement();
            System.out.println("QUERY: " + query);
            ResultSet results = statement.executeQuery(query);

            while(results.next()) {
                subCategoryList.add(results.getString(COLUMN_SUB_CATEGORY));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return subCategoryList;
    }

    public void writeEntry(Entry entry) {
        String command = "INSERT INTO " + TABLE_ENTRIES +
                " (" +
                COLUMN_AMOUNT + ", " +
                COLUMN_DATE + ", " +
                COLUMN_MAIN_CATEGORY + ", " +
                COLUMN_SUB_CATEGORY + ", " +
                COLUMN_DESCRIPTION + ", " +
                COLUMN_EXPLANATION + ", " +
                COLUMN_SOURCE_STATEMENT + ", " +
                COLUMN_CREDIT + ", " +
                COLUMN_TYPE +
                " ) " +
                "VALUES('" +
                entry.getAmount() + "', '" +
                entry.getDate() + "', '" +
                entry.getMainCategory() + "', '" +
                entry.getSubCategory() + "', '" +
                entry.getDescription() + "', '" +
                entry.getExplanation() + "', '" +
                entry.getSourceStatement() + "', '" +
                entry.getDebit() + "', '" +
                entry.getType() + "')";

        try {
            Statement statement = conn.createStatement();
            System.out.println("INSERT: " + command);
            statement.execute(command);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addIdentifier(Entry entry, String identifier) {
        String command = "INSERT INTO " + TABLE_CLASSIFICATIONS +
                " (" +
                COLUMN_TYPE + ", " +
                COLUMN_MAIN_CATEGORY + ", " +
                COLUMN_SUB_CATEGORY + ", " +
                COLUMN_IDENTIFIER +
                " ) " +
                "VALUES('" +
                entry.getType() + "', '" +
                entry.getMainCategory() + "', '" +
                entry.getSubCategory() + "', '" +
                identifier + "')";

        try {
            Statement statement = conn.createStatement();
            System.out.println("INSERT: " + command);
            statement.execute(command);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
