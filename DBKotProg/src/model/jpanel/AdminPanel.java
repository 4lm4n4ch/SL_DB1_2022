package model.jpanel;

import model.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class AdminPanel extends JPanel implements ActionListener, ItemListener {

    private JPanel input_panel;
    private TextField name_field;
    private Choice field_choice;
    private DefaultTableModel table_model;
    private String choice_string = "username";
    private Vector<String> column_names_vector;
    private JTable result_table;
    private JScrollPane scroll_pane;
    private JPanel output_panel;
    private JButton search_button;
    private ResultSet rs;
    private Statement stmt= new DatabaseConnection().connectToDatabase();

    public AdminPanel() {
        setLayout(new BorderLayout());

        this.name_field = new TextField(50);
        this.field_choice = new Choice();
        this.field_choice.addItemListener(this);
        this.field_choice.add("username");
        this.field_choice.add("jelszo");
        this.column_names_vector = new Vector<>();
        this.column_names_vector.add("username");
        this.column_names_vector.add("jelszo");
        this.output_panel = new JPanel();
        this.output_panel.setLayout(new BorderLayout());

        this.search_button = new JButton("Search");
        this.table_model = new DefaultTableModel(column_names_vector, 0);
        this.result_table = new JTable(table_model);
        this.scroll_pane = new JScrollPane(result_table);
        this.input_panel = new JPanel();
        this.input_panel.setLayout(new GridLayout(3, 3));
        this.input_panel.add(new Label("Content:"));
        this.input_panel.add(this.name_field);
        this.input_panel.add(new Label("Search in field:"));
        this.input_panel.add(this.field_choice);
        this.input_panel.add(this.search_button);
        this.output_panel.add(scroll_pane);

        this.setLayout(new BorderLayout());
        this.add(input_panel, BorderLayout.NORTH);
        this.add(output_panel, BorderLayout.CENTER);

        // Add event listeners for user actions
        this.search_button.addActionListener(this);

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connectToDatabase();
        try {
            this.stmt = dbConnection.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadTableData();
    }

    private void loadTableData() {
        try {
            rs = stmt.executeQuery("SELECT * FROM ADMIN");

            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String row[] = {rs.getString("username"), rs.getString("jelszo")};
                this.table_model.addRow(row);
            }
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String sql = "";
        if (this.name_field.getText().equals("")) {
            sql = "SELECT * FROM ADMIN";
        } else {
            if (choice_string.equals("username")) {
                sql = "SELECT * FROM admin WHERE username LIKE '%" + name_field.getText() + "%' ORDER BY username";
            } else if (choice_string.equals("jelszo")) {
                sql = "SELECT * FROM admin WHERE jelszo LIKE '%" + name_field.getText() + "%' ORDER BY jelszo";
            }
        }

        try {
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            System.out.println();

            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String row[] = {rs.getString("username"), rs.getString("jelszo")};
                this.table_model.addRow(row);
            }
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        this.choice_string = field_choice.getSelectedItem();
    }
}