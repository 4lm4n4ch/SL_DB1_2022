package model.jpanel;

import model.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Vector;

public class CegPanel extends JPanel implements ActionListener, ItemListener {
    private JPanel input_panel;
    private TextField name_field;
    private Choice field_choice;
    private DefaultTableModel table_model;
    private String choice_string = "ceg";
    private Vector<String> column_names_vector;
    private JTable result_table;
    private JScrollPane scroll_pane;
    private JPanel output_panel;
    private JButton search_button;
    private ResultSet rs;
    private Statement stmt = new DatabaseConnection().connectToDatabase();

    public CegPanel() {
        setLayout(new BorderLayout());

        this.name_field = new TextField(50);
        this.field_choice = new Choice();
        this.field_choice.addItemListener(this);

        this.field_choice.add("cegNev");
        this.field_choice.add("kapcsolattartoNev");
        this.field_choice.add("kapcsolattartoEmail");
        this.field_choice.add("kapcsolattartoTelefon");

        this.column_names_vector = new Vector<>();
        this.column_names_vector.add("Cég Név");
        this.column_names_vector.add("Kapcsolattartó Nev");
        this.column_names_vector.add("Kapcsolattartó Email cím");
        this.column_names_vector.add("Kapcsolattartó Telefonszám");
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


    }

    // Define event listeners
    public void actionPerformed(ActionEvent e) {


        /******************* Starting queries ********************/


        String sql = "";
        if ( this.name_field.getText().equals("") ) {


            sql = "SELECT Ceg.Ceg_nev,Ceg.kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg";


        } else {
            if (Objects.equals(choice_string, "cegNev")) {
                sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Ceg_nev  LIKE '%"+ name_field.getText() +"%' ORDER BY Ceg.Ceg_nev";
            } else if (Objects.equals(choice_string, "kapcsolattartoNev")) {
                sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_nev  LIKE '%"+ name_field.getText() +"%' ORDER BY Ceg.Kapcsolattarto_nev";
            }else if (Objects.equals(choice_string, "kapcsolattartoEmail")) {
                sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_email_cim LIKE '%"+ name_field.getText() +"%' ORDER BY Ceg.Kapcsolattarto_email_cim";
            }else if (Objects.equals(choice_string, "kapcsolattartoTelefon")) {
                sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_telefonszam LIKE '%"+ name_field.getText() +"%' ORDER BY Ceg.Kapcsolattarto_telefonszam";
            }

        }

        try {
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            //rs = stmt.executeQuery(sql)
            // removing all rows from the table
            int count = table_model.getRowCount();
            System.out.println(count);
            for ( int i = count-1; i>=0; i-- ) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String row[] = {rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4)};
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

