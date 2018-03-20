package org.szelc.financial.report.reader;

import org.szelc.financial.report.ReportData;
import org.szelc.financial.report.ReportField;
import org.szelc.financial.report.sqlite.SqliteConnection;
import org.szelc.logger.LOG;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportSqliteReader {

    public static Map<Integer, ReportData> report4QForCompany(Long companyId){
        Map<Integer, ReportData> result = new HashMap();

        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(3);
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        String sql = "select rf.id,  rf.name, sum(value) from report_data rd" +
                " inner join report_field rf on rf.id = rd.report_field_id " +
                " where report_id in (select id from report " +
                " where company_id = "+companyId+" and rf.id < 7 or rf.id=10  order by id desc limit 4 ) group by report_field_id";

        Connection conn= SqliteConnection.connect();
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()){
                Integer id = rs.getInt(1);
                String fieldName = rs.getString(2);
                Float value = rs.getFloat(3);
                result.put(id, new ReportData(new ReportField(id, null), value));
                LOG.i("["+id+"] ["+fieldName+"] ["+df.format(value)+"]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "select rf.id, rf.name, value from report_data rd" +
                " inner join report_field rf on rf.id = rd.report_field_id " +
                " where report_id in (select id from report " +
                " where company_id = "+companyId+" and rf.id in ( 7, 8, 9, 11) order by id desc limit 1 ) ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql2);
            while(rs.next()){
                Integer id = rs.getInt(1);
                String fieldName = rs.getString(2);
                Float value = rs.getFloat(3);
                result.put(id, new ReportData(new ReportField(id, null), value));
                LOG.i("["+id+"] ["+fieldName+"] ["+df.format(value)+"]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
