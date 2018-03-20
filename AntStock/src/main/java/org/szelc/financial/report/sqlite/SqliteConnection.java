package org.szelc.financial.report.sqlite;

import org.szelc.app.antstock.settings.Settings;
import org.szelc.logger.LOG;

import java.sql.DriverManager;

public class SqliteConnection {

    public static java.sql.Connection connect() {
        java.sql.Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = Settings.DATABASE_SQLITE_PATH;//DriverManager.registerDriver(new JDBC());
            LOG.i("URL ["+url+"]");
            return DriverManager.getConnection(url);

        } catch (Exception e) {
            return null;
        }
    }

}
