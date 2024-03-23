package model.jpanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleDataSource;

public class SearchPanel extends JPanel implements ActionListener {

    // UI components
    private JTextField name_field;
    private JButton search_button;
    private JComboBox<String> choice_field;


    // Database related components
    private Statement stmt;
    private ResultSet rs;


    // Table model for the search result
    private DefaultTableModel table_model;

    public SearchPanel(Statement stmt, DefaultTableModel table_model) {
        this.stmt = stmt;
        this.table_model = table_model;
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        // Initialize UI components
        name_field = new JTextField(20);
        search_button = new JButton("Search");
        choice_field = new JComboBox<>(new String[]{"username", "jelszo"});

        // Add action listener to the search button
        search_button.addActionListener(this);

        // Add components to the panel
        this.setLayout(new FlowLayout());
        this.add(name_field);
        this.add(search_button);
        this.add(choice_field);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search_button) {
            // Perform search and update table model
            performSearchAndUpdateTable();
        }
    }

    private void performSearchAndUpdateTable() {
        // Perform search based on the selected choice and input text
        String sql = generateSQL();
        try {
            rs = stmt.executeQuery(sql);
            // Update table model with the new search result
            updateTableModel();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String generateSQL() {
        String sql = "";
        String choice_string = (String) choice_field.getSelectedItem();

        if (name_field.getText().equals("")) {
            sql = "SELECT * FROM ADMIN";
        } else {
            if (choice_string.equals("username")) {
                sql = "SELECT * FROM admin WHERE username LIKE '" + name_field.getText() + "' ORDER BY username";
            } else if (choice_string.equals("jelszo")) {
                sql = "SELECT * FROM admin WHERE jelszo LIKE '" + name_field.getText() + "' ORDER BY jelszo";
            }
        }

        return sql;
    }

    private void updateTableModel() throws SQLException {
        // Remove all rows from the table
        int count = table_model.getRowCount();
        for (int i = count - 1; i >= 0; i--) {
            table_model.removeRow(i);
        }

        // Add new rows to the table
        while (rs.next()) {
            String row[] = {rs.getString("username"), rs.getString("jelszo")};
            table_model.addRow(row);
        }
    }
}
