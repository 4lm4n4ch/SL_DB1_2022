package model;

import oracle.jdbc.pool.OracleDataSource;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection(String url, String user, String password) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DatabaseConnection() {
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system", "oracle");
        } catch (Error | SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            String pass = "oracle";
            String user = "system";
            /* Connect to the Oracle Database and using the "HR" user's schema */
            OracleDataSource ods = new OracleDataSource();
            Class.forName("oracle.jdbc.OracleDriver");

            ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
            connection = ods.getConnection(user, pass);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }


    public Statement connectToDatabase() {
        try {
            String pass="oracle";
            String user="system";
            /* Connect to the Oracle Database and using the "HR" user's schema */
            OracleDataSource ods = new OracleDataSource();
            Class.forName ("oracle.jdbc.OracleDriver");

            ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
            Connection conn = ods.getConnection(user,pass);
            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getAktivAllasajanlatokSzama() {
        int count = 0;
        try {
            String query = "SELECT aktiv_allasajanlatok_szama() FROM dual";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public void setAllasInaktiv(int allas_id) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call allasajanlat_inaktiv(?)}");
            callableStatement.setInt(1, allas_id);
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void torolAllaskereso(String emailCim) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call allaskereso_torles(?)}");
            callableStatement.setString(1, emailCim);
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void torolCeg(int cegId) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call ceg_torles(?)}");
            callableStatement.setInt(1, cegId);
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
