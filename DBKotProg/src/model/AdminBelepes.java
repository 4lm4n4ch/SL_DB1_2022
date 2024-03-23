package model;

import model.allaskerso.FooldalAllaskeresoPane;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class AdminBelepes {
    private JTabbedPane tabbedPane;
    private JFrame adminFrame;
    private DatabaseConnection dbConnection;

    public AdminBelepes() {
        adminFrame = new JFrame("Admin belépés");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(900, 500);
        adminFrame.setLocationRelativeTo(null);
        tabbedPane = new JTabbedPane();
        adminFrame.add(tabbedPane);

        dbConnection = new DatabaseConnection();

        // Lekérdezések
        String[][] queries = new String[][]{
                {
                        "Összes állásajánlat, amelyre legalább egy álláskereső jelentkezett, és azokhoz tartozó cégek:",
                        "SELECT a.allas_id, c.ceg_nev, COUNT(j.allaskereso_email_cim) AS jelentkezesek_szama " +
                                "FROM Allasajanlat a " +
                                "JOIN Jelentkezes j ON a.allas_id = j.allas_id " +
                                "JOIN Ceg c ON a.ceg_id = c.ceg_id " +
                                "GROUP BY a.allas_id, c.ceg_nev"
                },
                {
                        "Legtöbb jelentkezést kapott állásajánlat:",
                        "SELECT a.allas_id, COUNT(j.allaskereso_email_cim) AS jelentkezesek_szama " +
                                "FROM Allasajanlat a " +
                                "JOIN Jelentkezes j ON a.allas_id = j.allas_id " +
                                "GROUP BY a.allas_id " +
                                "HAVING COUNT(j.allaskereso_email_cim) = ( " +
                                "  SELECT MAX(COUNT(j2.allaskereso_email_cim)) " +
                                "  FROM Allasajanlat a2 " +
                                "  JOIN Jelentkezes j2 ON a2.allas_id = j2.allas_id " +
                                "  GROUP BY a2.allas_id " +
                                ")"
                },
                {
                        "Legalább két különböző állásajánlatra jelentkező álláskeresők:",
                        "SELECT j.allaskereso_email_cim, COUNT(DISTINCT j.allas_id) AS allasok_szama " +
                                "FROM Jelentkezes j " +
                                "GROUP BY j.allaskereso_email_cim " +
                                "HAVING COUNT(DISTINCT j.allas_id) >= 2"
                },
                {
                        "Cégek által meghirdetett állásajánlatok száma:",
                        "SELECT c.ceg_nev, COUNT(a.allas_id) AS allasajanlatok_szama " +
                                "FROM Ceg c " +
                                "LEFT JOIN Allasajanlat a ON c.ceg_id = a.ceg_id " +
                                "GROUP BY c.ceg_nev"
                },
                {
                        "Több iskolai végzettséggel rendelkező álláskeresők:",
                        "SELECT i.email_cim, COUNT(i.id) AS vegzettsegek_szama " +
                                "FROM Iskolai_vegzettseg i " +
                                "GROUP BY i.email_cim " +
                                "HAVING COUNT(i.id) > 1"
                },
                {
                        "Leggyakoribb iskolai végzettség az álláskeresők között:",
                        "SELECT i.iskolai_vegzettseg, COUNT(i.id) AS vegzettsegek_szama " +
                                "FROM Iskolai_vegzettseg i " +
                                "GROUP BY i.iskolai_vegzettseg " +
                                "HAVING COUNT(i.id) = ( " +
                                "  SELECT MAX(COUNT(i2.id)) " +
                                "  FROM Iskolai_vegzettseg i2 " +
                                " GROUP BY i2.iskolai_vegzettseg " +
                                ")"
                }
        };


        for (String[] queryData : queries) {
            String description = queryData[0];
            String query = queryData[1];
            try {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                JLabel descriptionLabel = new JLabel(description);
                panel.add(descriptionLabel);

                ResultSet resultSet = dbConnection.executeQuery(query);
                JTable table = new JTable(buildTableModel(resultSet));
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 200));
                panel.add(scrollPane);

                tabbedPane.addTab(description, panel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        adminFrame.setVisible(true);
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
