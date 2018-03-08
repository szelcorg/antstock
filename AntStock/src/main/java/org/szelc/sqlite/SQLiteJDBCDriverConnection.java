package org.szelc.sqlite;

import org.sqlite.JDBC;
import org.szelc.logger.LOG;

import java.sql.*;
import java.util.List;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {
    /**
     * Connect to a sample database
     */

    public static void createNewDatabase(String fileName) {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String url = "jdbc:sqlite:C:/github/antstock/AntStock/Storage/db/sqlite/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void select(String dbname) throws SQLException {
        String sql = "select id, name from company";

        Connection conn = connect(dbname);
        ResultSet rs = null;
        try {
            rs = conn.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while(rs.next()){
            Integer id = rs.getInt(0);
            String company = rs.getString(1);
            LOG.i("ID ["+id+"] company ["+company+"]");
        }
    }

    public static boolean createTableCompany(String dbname){
        String sql = "CREATE TABLE 'company' (\n" +
                "\t'id'\tINTEGER NOT NULL,\n" +
                "\t'company'\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(`id`)\n" +
                ");";



        try {
            Connection conn = connect(dbname);
            if(conn==null){
                return false;
            }
        //    Statement stmt = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement(sql);
            if(ps==null){
                return false;
            }
            ps.execute();
            LOG.i("Table company created");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection connect(String dbname) {
        Connection conn = null;
        try {

            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            // db parameters
            String url = "jdbc:sqlite:C:/github/antstock/AntStock/Storage/db/sqlite/"+dbname;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect("d.db");
    }

    public static void insertCompany(List<String> companyList) {
        Connection conn = connect("AntStock.db");
        int i = 0;
        try {
            conn.createStatement().executeUpdate("delete from company");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        for(String company : companyList){
            conn = connect("AntStock.db");
            i++;
            String query = "insert into company(id, name) values ("+(i)+", '"+company+"')";
            try {
                LOG.i("Query ["+query+"]");
                conn.createStatement().executeUpdate("insert into company(id, name) values ("+(i)+", '"+company+"')");

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static Integer selectId(String query) throws SQLException {
        ResultSet rs = connect("AntStock.db").createStatement().executeQuery(query);

        if(rs.next()){
            return rs.getInt(1);
        }
        return null;
    }
}