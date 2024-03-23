package model.jpanel;

import model.DatabaseConnection;
import model.ceg.Ceg;
import model.ceg.CegBejelentkezes;

import java.sql.*;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AllasAjanlatPanel extends JPanel implements ActionListener, ItemListener {

    private JPanel input_panel;
    private TextField name_field;
    private Choice field_choice;
    private DefaultTableModel table_model;
    private String choice_string = "allas";
    private Vector<String> column_names_vector;
    private JTable result_table;
    private JScrollPane scroll_pane;
    private JPanel output_panel;
    private JButton search_button;
    private ResultSet rs;
    protected Statement stmt = new DatabaseConnection().connectToDatabase();
    private Ceg ceg;

    public AllasAjanlatPanel(Ceg ceg) {
        this.ceg = ceg;
        setLayout(new BorderLayout());
        this.name_field = new TextField(50);
        this.field_choice = new Choice();
        this.field_choice.addItemListener(this);
        this.field_choice.add("ceg");
        this.field_choice.add("allas");
        this.field_choice.add("aktiv");
        this.field_choice.add("nem aktiv");
        this.column_names_vector = new Vector<>();
        this.column_names_vector.add("Cég");
        this.column_names_vector.add("Állás");
        this.column_names_vector.add("Bér intervallum");
        this.column_names_vector.add("Aktív");
        this.column_names_vector.add("Deaktiválás");
        this.column_names_vector.add("Törlés");


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

        result_table.getColumn("Deaktiválás").setCellRenderer(new ButtonRendererAllasAjanlat());
        result_table.getColumn("Deaktiválás").setCellEditor(new ButtonEditorAllasAjanlat(new DatabaseConnection()));

        result_table.getColumn("Törlés").setCellRenderer(new ButtonRendererAllasAjanlat());
        result_table.getColumn("Törlés").setCellEditor(new DeleteButtonEditor());

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
            rs = stmt.executeQuery("SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND Ceg.ceg_id = " + ceg.getCeg_id() + " ORDER BY  Allasajanlat.leiras");

            // Táblázat sorainak törlése
            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            // Az "Allas" táblából beolvasott adatok hozzáadása a táblázathoz
            while (rs.next()) {
                String str = "Nem aktív";
                if (rs.getInt("AKTIV") == 1) {
                    str = "aktív";
                }
                String row[] = {rs.getString("ceg_nev"), rs.getString("leiras"), rs.getString("berezesi_intervallum"), str, "Deaktivál"};
                this.table_model.addRow(row);
            }
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // Define event listeners
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == null) {
            int selectedRow = result_table.getSelectedRow();
            if (selectedRow != -1) {
                int allas_id = Integer.parseInt((String) result_table.getValueAt(selectedRow, 0));
                try {
                    stmt.executeUpdate("UPDATE Allasajanlat SET aktiv = 0 WHERE allas_id = " + allas_id);
                    loadTableData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        String sql = "";
        if (this.name_field.getText().equals("") && !choice_string.equals("aktiv") && !choice_string.equals("nem aktiv")) {
            sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND Ceg.ceg_id = " + ceg.getCeg_id() + " ORDER BY Ceg.ceg_nev";
        } else {
            if (choice_string.equals("allas")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id  AND Ceg.ceg_id = " + ceg.getCeg_id() + " AND allasajanlat.leiras LIKE '%" + name_field.getText() + "%' ORDER BY ceg.ceg_nev";
            } else if (choice_string.equals("aktiv")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=1 AND Ceg.ceg_id = " + ceg.getCeg_id();
            } else if (choice_string.equals("nem aktiv")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=0 AND Ceg.ceg_id = " + ceg.getCeg_id();
            }
        }

        try {
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            System.out.println();

            // removing all rows from the table
            int count = table_model.getRowCount();
            System.out.println(count);
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String str = "nem aktív";
                if (rs.getInt("aktiv") == 1) {
                    str = "aktív";
                }
                String row[] = {rs.getString("ceg_nev"), rs.getString("leiras"), rs.getString("berezesi_intervallum"), str, "Deaktivál", "Törlés"};
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


    class ButtonRendererAllasAjanlat extends JButton implements TableCellRenderer {

        public ButtonRendererAllasAjanlat() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }


    protected class ButtonEditorAllasAjanlat extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private int row;
        private DatabaseConnection databaseConnection;
        public ButtonEditorAllasAjanlat(DatabaseConnection databaseConnection) {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
            this.databaseConnection = databaseConnection;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Az akció, amely akkor történik, amikor a gombot megnyomják
            }
            isPushed = false;
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            int allas_id = getSelectedAllasId(table, row);
            if (allas_id != -1) {
                databaseConnection.setAllasInaktiv(allas_id);
                ((AllasAjanlatPanel) table.getParent().getParent().getParent().getParent()).loadTableData(); // Frissíti a táblázat adatokat
            }
        }

        private int getSelectedAllasId(JTable table, int row) {
            String allas_nev = (String) table.getValueAt(row, 1);
            int allas_id = -1;
            try {
                ResultSet resultSet = stmt.executeQuery("SELECT allas_id FROM Allasajanlat WHERE leiras = '" + allas_nev + "'");
                if (resultSet.next()) {
                    allas_id = resultSet.getInt("allas_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return allas_id;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
    }

    class DeleteButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private int row;

        public DeleteButtonEditor() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Az akció, amely akkor történik, amikor a gombot megnyomják
            }
            isPushed = false;
            return label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            int allas_id = getSelectedAllasId(table, row);
            if (allas_id != -1) {
                try {
                    stmt.executeUpdate("DELETE FROM Allasajanlat WHERE allas_id = " + allas_id);
                    ((AllasAjanlatPanel) table.getParent().getParent().getParent().getParent()).loadTableData(); // Frissíti a táblázat adatokat
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private int getSelectedAllasId(JTable table, int row) {
            String allas_nev = (String) table.getValueAt(row, 1);
            int allas_id = -1;
            try {
                ResultSet resultSet = stmt.executeQuery("SELECT allas_id FROM Allasajanlat WHERE leiras = '" + allas_nev + "'");
                if (resultSet.next()) {
                    allas_id = resultSet.getInt("allas_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return allas_id;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
    }

/*
* 2. Tárolt eljárás állásajánlat inaktívvá tételéhez:
CREATE OR REPLACE PROCEDURE allasajanlat_inaktiv (p_allas_id IN NUMBER) AS
BEGIN
UPDATE Allasajanlat
SET aktiv = 0
WHERE allas_id = p_allas_id;
END;
* */
    }

