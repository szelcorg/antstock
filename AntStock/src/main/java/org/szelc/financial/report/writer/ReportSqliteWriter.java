package org.szelc.financial.report.writer;

import org.sqlite.JDBC;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.financial.report.Report;
import org.szelc.financial.report.ReportData;
import org.szelc.financial.report.sqlite.SqliteConnection;
import org.szelc.logger.LOG;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportSqliteWriter {


    private static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getCompanyNameIdMap() throws SQLException {
        Map<String, Integer> result = new HashMap();
        Connection conn = SqliteConnection.connect();
        String query = "select name, id from company";
        ResultSet rs = null;
        try {
            if(conn==null || conn.isClosed()){
                LOG.w("SqliteConnection is not opened");
                return null;
            }
            rs = conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        while (rs.next()) {
            String name = rs.getString(1);
            Integer id = rs.getInt(2);
            result.put(name, id);
        }
        close(conn);
        return result;
    }


    public static boolean writetToDB(List<Report> reportList) {
        Map<String, Integer> companyNameIdMap;
        try {
            companyNameIdMap = getCompanyNameIdMap();
            if(companyNameIdMap==null){
                LOG.w("Can't get companies from sqlite");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        Connection conn = SqliteConnection.connect();
        LOG.i("SqliteConnection to SQLite has been established.");

        for (Report report : reportList) {
            Integer companyId = companyNameIdMap.get(report.getCompanyName());
            Integer reportTypeId = report.getType().getId();
            String period = report.getPeriod();
            String query = "insert into report(company_id, report_type_id, period)" +
                    " values('" + companyId + "', " + reportTypeId + ", '" + period + "')";
            Statement stmt = null;
            Integer reportId;
            try {

                stmt = conn.createStatement();
                LOG.i(query);
                stmt.executeUpdate(query);
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reportId = generatedKeys.getInt(1);
                    LOG.i("NEW ID [" + reportId + "]");
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

            boolean result = writeReportData(conn, reportId, report.getReportDataList());
            if (!result) {
                return false;
            }

        }

        close(conn);

        return false;
    }

    private static boolean writeReportData(Connection conn, Integer reportId, List<ReportData> repordDataList) {
        Statement stmt = null;
        try {

            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        for (ReportData data : repordDataList) {
            Integer reportFieldId = data.getField().getId();
            Float value = data.getValue();
            String query = "insert into report_data(report_id, report_field_id, value) values(" +
                    " " + reportId + ", " + reportFieldId + ", " + value + ")";
            try {
                LOG.i("" + query);
                stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();
                close(conn);
                return false;
            }
        }


        return true;
    }
}
