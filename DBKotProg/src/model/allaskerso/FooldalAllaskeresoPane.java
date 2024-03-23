package model.allaskerso;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class FooldalAllaskeresoPane extends JPanel {

    public FooldalAllaskeresoPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        DatabaseConnection dbConnection = new DatabaseConnection();

        // Lekérdezések
        String[][] queries = new String[][] {
                {
                        "A legtöbb jelentkezőt vonzó cég neve és jelentkezők száma:",
                        "SELECT c.ceg_nev, COUNT(j.allaskereso_email_cim) AS jelentkezo_szam " +
                                "FROM Ceg c " +
                                "INNER JOIN Allasajanlat a ON c.ceg_id = a.ceg_id " +
                                "INNER JOIN Jelentkezes j ON a.allas_id = j.allas_id " +
                                "GROUP BY c.ceg_nev " +
                                "ORDER BY jelentkezo_szam DESC " +
                                "FETCH FIRST 1 ROW ONLY"
                },
                {
                        "A legtöbb jelentkezőt vonzó aktív állásajánlat és a hozzá kapcsolódó jelentkezők száma:",
                        "SELECT Allasajanlat.allas_id, Allasajanlat.leiras, COUNT(Jelentkezes.allaskereso_email_cim) AS jelentkezok_szama " +
                                "FROM Allasajanlat " +
                                "LEFT JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id " +
                                "WHERE Allasajanlat.aktiv = 1 " +
                                "GROUP BY Allasajanlat.allas_id, Allasajanlat.leiras " +
                                "ORDER BY jelentkezok_szama DESC " +
                                "FETCH FIRST 1 ROW ONLY"
                },
                {
                        "Az összes cég és az összes jelentkező száma, akik az általuk meghirdetett állásokra jelentkeztek:",
                        "SELECT Ceg.ceg_nev, COUNT(Jelentkezes.allaskereso_email_cim) AS jelentkezok_szama " +
                                "FROM Ceg " +
                                "JOIN Allasajanlat ON Ceg.ceg_id = Allasajanlat.ceg_id " +
                                "LEFT JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id " +
                                "GROUP BY Ceg.ceg_nev"
                },
                {
                        "A legkevesebb jelentkezőt vonzó aktív állásajánlat és a hozzá kapcsolódó jelentkezők száma:",
                        "SELECT Allasajanlat.allas_id, Allasajanlat.leiras, COUNT(Jelentkezes.allaskereso_email_cim) AS jelentkezok_szama " +
                                "FROM Allasajanlat " +
                                "LEFT JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id " +
                                "WHERE Allasajanlat.aktiv = 1 " +
                                "GROUP BY Allasajanlat.allas_id, Allasajanlat.leiras " +
                                "ORDER BY jelentkezok_szama ASC " +
                                "FETCH FIRST 1 ROW ONLY"
                },
                {
                        "Az aktív állásajánlatok, amelyekre legalább 5 jelentkező jelentkezett:",
                        "SELECT Allasajanlat.leiras, Ceg.ceg_nev " +
                                "FROM Allasajanlat " +
                                "INNER JOIN Ceg ON Allasajanlat.ceg_id = Ceg.ceg_id " +
                                "INNER JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id " +
                                "WHERE Allasajanlat.aktiv = 1 " +
                                "GROUP BY Allasajanlat.allas_id, Allasajanlat.leiras, Ceg.ceg_nev " +
                                "HAVING COUNT(*) >= 5"
                },
                {
                "A legmagasabb fizetési intervallummal rendelkező aktív állásajánlat és a hozzá tartozó cég neve:",
                "SELECT C.ceg_nev, A.berezesi_intervallum " +
                        "FROM Allasajanlat A " +
                        "INNER JOIN Ceg C ON A.ceg_id = C.ceg_id " +
                        "WHERE A.aktiv = 1 AND A.berezesi_intervallum = ( " +
                        "  SELECT MAX(AA.berezesi_intervallum) " +
                        "  FROM Allasajanlat AA " +
                        "  WHERE AA.aktiv = 1 " +
                        ")"
        }
        };

        for (String[] queryData : queries) {
            String description = queryData[0];
            String query = queryData[1];
            try {
                JLabel descriptionLabel = new JLabel(description);
                add(descriptionLabel);

                ResultSet resultSet = dbConnection.executeQuery(query);
                JTable table = new JTable(buildTableModel(resultSet));
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 200));
                add(scrollPane);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        dbConnection.closeConnection();

    }

    private static javax.swing.table.DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        java.sql.ResultSetMetaData metaData = resultSet.getMetaData();

        // Column names
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(resultSet.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}
