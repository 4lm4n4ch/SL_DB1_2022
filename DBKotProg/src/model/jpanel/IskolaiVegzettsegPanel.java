package model.jpanel;

import model.DatabaseConnection;

import java.sql.*;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class IskolaiVegzettsegPanel extends JPanel implements ActionListener, ItemListener {

    private JPanel input_panel;
    private TextField name_field;
    private Choice field_choice;
    private DefaultTableModel table_model;
    private String choice_string = "iskolai_vegzettseg";
    private Vector<String> column_names_vector;
    private JTable result_table;
    private JScrollPane scroll_pane;
    private JPanel output_panel;
    private JButton search_button;
    private ResultSet rs;
    private Statement stmt= new DatabaseConnection().connectToDatabase();


    public IskolaiVegzettsegPanel() {
        setLayout(new BorderLayout());
        this.name_field = new TextField(50);
        this.field_choice = new Choice();
        this.field_choice.addItemListener(this);
        this.field_choice.add("email_cim");
        this.field_choice.add("iskolai_vegzettseg");
        this.column_names_vector = new Vector<>();
        this.column_names_vector.add("email");
        this.column_names_vector.add("vegzettseg");
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
            // Módosított SQL lekérdezés, hogy az "Allas" táblából keressen
            rs = stmt.executeQuery("SELECT * FROM iskolai_vegzettseg");

            // Táblázat sorainak törlése
            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            // Az "Allas" táblából beolvasott adatok hozzáadása a táblázathoz
            while (rs.next()) {
                // Feltételezve, hogy az "Allas" táblában van "allas_nev" és "leiras" mező (módosítsa a mezőneveket, ha szükséges)
                String row[] = {rs.getString("email_cim"), rs.getString("iskolai_vegzettseg")};
                this.table_model.addRow(row);
            }
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    // Define event listeners
    public void actionPerformed(ActionEvent e) {


        /******************* Starting queries ********************/


        String sql = "";
        if ( this.name_field.getText().equals("") ) {


            sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg";


        } else {
            if ( choice_string == "email_cim" ) {
                sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg WHERE  Iskolai_vegzettseg.email_cim LIKE '%"+ name_field.getText() +"%' ORDER BY Iskolai_vegzettseg.email_cim";
            }else if (choice_string == "iskolai_vegzettseg") {
                sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg WHERE Iskolai_vegzettseg.Iskolai_vegzettseg LIKE '%"+ name_field.getText() +"%' ORDER BY Iskolai_vegzettseg.Iskolai_vegzettseg";
            }
        }

        try {

            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            System.out.println();
            //rs = stmt.executeQuery(sql)
            System.out.println(sql);
            // removing all rows from the table
            int count = table_model.getRowCount();
            System.out.println(count);
            for ( int i = count-1; i>=0; i-- ) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String row[] = {rs.getString(1),rs.getString(2)};
                this.table_model.addRow( row ); // adding new row into the table
            }
            repaint();
        }catch ( SQLException ex ) {
            ex.printStackTrace();
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        this.choice_string = field_choice.getSelectedItem();
    }
}

