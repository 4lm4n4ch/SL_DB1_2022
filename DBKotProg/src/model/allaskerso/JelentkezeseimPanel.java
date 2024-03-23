package model.allaskerso;

import model.DatabaseConnection;
import oracle.jdbc.internal.OracleTypes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class JelentkezeseimPanel extends JPanel {
    private JTable result_table;
    private DefaultTableModel table_model;
    private JScrollPane scroll_pane;
    private String email_cim;
    private Statement stmt;

    private JButton refreshButton;
    private JPanel topPanel;
    private Connection conn;
    private JButton deleteButton;



    public JelentkezeseimPanel(String email_cim,   Connection conn) {



        this.conn = conn;

        this.email_cim = email_cim;
        this.stmt = new DatabaseConnection().connectToDatabase();

        setLayout(new BorderLayout());

        Vector<String> column_names_vector = new Vector<>();
        column_names_vector.add("Cég név");
        column_names_vector.add("Állásleírás");
        column_names_vector.add("Bér intervallum");

        table_model = new DefaultTableModel(column_names_vector, 0);
        result_table = new JTable(table_model);
        scroll_pane = new JScrollPane(result_table);

        refreshButton = new JButton("Frissítés");
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(refreshButton);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scroll_pane, BorderLayout.CENTER);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadJelentkezesData();
            }
        });

        loadJelentkezesData();
    }


    private void loadJelentkezesData() {
        try {
            Connection conn = new DatabaseConnection().getConnection();
            CallableStatement callableStatement = conn.prepareCall("{CALL get_all_jelentkezes(?, ?)}");
            callableStatement.setString(1, email_cim);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }



            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                String cegNev = resultSet.getString(1);
                String allasLeiras = resultSet.getString(2);
                String berIntervallum = resultSet.getString(3);
                String row[] = {cegNev, allasLeiras, berIntervallum};
                table_model.addRow(row);
            }

            if (!hasResults) {
                System.out.println("Nincsenek eredmények a lekérdezésben.");
            }
            resultSet.close();
            callableStatement.close();

        } catch (SQLException e) {
            System.out.println("Nincsenek eredmények a lekérdezésben. hibával");
        }
    }


    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Statement stmt = dbConnection.connectToDatabase();

        String email_cim = "qwer@qwer.qwer";//TODO
        JelentkezeseimPanel jelentkezeseimPanel = new JelentkezeseimPanel(email_cim, new DatabaseConnection().getConnection());

        JFrame frame = new JFrame("Jelentkezéseim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(jelentkezeseimPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
