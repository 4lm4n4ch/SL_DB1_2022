package model.jpanel;

import model.DatabaseConnection;
import model.allaskerso.Allaskereso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class JelentkezesPanel extends JPanel implements ActionListener, ItemListener {
    private Set<Integer> applications;
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
    public String email_cim;
    public Statement stmt = new DatabaseConnection().connectToDatabase();

    public JelentkezesPanel(String email_cim) {
        this.email_cim = email_cim;
        initComponents();
        applications = getAllApplications(email_cim);
        setupTableColumns();
        setupLayouts();
        setupListeners();
    }

    private void initComponents() {

        this.name_field = new TextField(50);
        this.field_choice = createFieldChoice();
        this.field_choice = createFieldChoice();
        this.column_names_vector = createColumnNamesVector();
        this.output_panel = new JPanel();
        this.search_button = new JButton("Search");
        this.table_model = new DefaultTableModel(column_names_vector, 0);
        this.result_table = new JTable(table_model);
        this.scroll_pane = new JScrollPane(result_table);
        this.input_panel = new JPanel();
    }

    private Choice createFieldChoice() {
        Choice choice = new Choice();
        choice.addItemListener(this);
        choice.add("ceg");
        choice.add("allas");
        choice.add("aktiv");
        choice.add("nem aktiv");
        return choice;
    }

    private Vector<String> createColumnNamesVector() {
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Cég");
        columnNames.add("Állás");
        columnNames.add("Bér intervallum");
        columnNames.add("Aktív");
        columnNames.add("Jelentkezés");
        columnNames.add("Cég ID");
        columnNames.add("Állás ID");
        return columnNames;
    }

    private Set<Integer> getAllApplications(String email_cim) {
        Set<Integer> applications = new HashSet<>();
        try {
            String sql = "SELECT allas_id FROM Jelentkezes WHERE allaskereso_email_cim = ?";
            PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql);
            pstmt.setString(1, email_cim);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                applications.add(resultSet.getInt("allas_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }



    private void setupTableColumns() {
        result_table.getColumn("Jelentkezés").setCellRenderer(new ButtonRenderer());
        result_table.getColumn("Jelentkezés").setCellEditor(new ButtonEditor(new JCheckBox(), email_cim, applications));
    }

    private void setupLayouts() {
        input_panel.setLayout(new GridLayout(3, 3));
        input_panel.add(new Label("Content:"));
        input_panel.add(name_field);
        input_panel.add(new Label("Search in field:"));
        input_panel.add(field_choice);
        input_panel.add(search_button);

        output_panel.setLayout(new BorderLayout());
        output_panel.add(scroll_pane);

        setLayout(new BorderLayout());
        add(input_panel, BorderLayout.NORTH);
        add(output_panel, BorderLayout.CENTER);

        result_table.removeColumn(result_table.getColumn("Cég ID")); // Oszlop eltávolítása a táblázatból
        result_table.removeColumn(result_table.getColumn("Állás ID")); // Oszlop eltávolítása a táblázatból
    }


    private void setupListeners() {
        search_button.addActionListener(this);
    }

    // Define event listeners
    public void actionPerformed(ActionEvent e) {
        String sql = generateSqlQuery();
        updateResultTable(sql);
    }

    private String generateSqlQuery() {
        String sql = "";
        if (this.name_field.getText().equals("") && !choice_string.equals("aktiv") && !choice_string.equals("nem aktiv")) {
            sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV, Ceg.ceg_id, Allasajanlat.allas_id  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND Ceg.ceg_nev LIKE '%" + name_field.getText() + "%' ORDER BY Ceg.ceg_nev";
        } else {
            if (choice_string.equals("ceg")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND Ceg.ceg_nev LIKE '%" + name_field.getText() + "%' ORDER BY Ceg.ceg_nev";
            } else if (choice_string.equals("allas")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.leiras LIKE '%" + name_field.getText() + "%' ORDER BY ceg.ceg_nev";
            } else if (choice_string.equals("aktiv")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=1";
            } else if (choice_string.equals("nem aktiv")) {
                sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=0";
            }
        }
        return sql;
    }

    private void updateResultTable(String sql) {
        try {
            rs = stmt.executeQuery(sql);

            int count = table_model.getRowCount();
            for (int i = count - 1; i >= 0; i--) {
                table_model.removeRow(i);
            }
            repaint();

            while (rs.next()) {
                String str = "Nem aktív";
                if (rs.getInt(4) == 1) {
                    str = "aktív";
                }
                String row[] = {rs.getString(1), rs.getString(2), rs.getString(3), str, "", rs.getString(5), rs.getString(6)};
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
























class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        String status = (String) table.getValueAt(row, 3);
        if (status.equals("aktív")) {
            setText("Jelentkezés");
            setEnabled(true);
        } else {
            setText("");
            setEnabled(false);
        }
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {

    private Set<Integer> applications;
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private String email_cim;

    public ButtonEditor(JCheckBox checkBox, String email_cim, Set<Integer> applications) {
        super(checkBox);
        this.email_cim = email_cim;
        this.applications = applications;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        label = (value == null) ? "" : value.toString();
        String status = (String) table.getValueAt(row, 3);
        int allas_id = Integer.parseInt((String) table.getModel().getValueAt(row, 6));
        if (status.equals("aktív")) {
            if (applications.contains(allas_id)) {
                button.setText("Jelentkezés törlése");
            } else {
                button.setText("Jelentkezés");
            }
            button.setEnabled(true);
        } else {
            button.setText("");
            button.setEnabled(false);
        }
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getSelectedRow();
            String allas = (String) table.getValueAt(row, 1);

            System.out.println("Jelentkezés: " + allas);


            String allaskereso_email_cim =email_cim;
            Statement stmt = new DatabaseConnection().connectToDatabase();
            int allas_id = Integer.parseInt((String) table.getModel().getValueAt(row, 6));
            if (applications.contains(allas_id)) {
                // Delete the application from the database
                try {
                    String sql = "DELETE FROM Jelentkezes WHERE allaskereso_email_cim = ? AND allas_id = ?";
                    PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql);
                    pstmt.setString(1, allaskereso_email_cim);
                    pstmt.setInt(2, allas_id);
                    pstmt.executeUpdate();
                    System.out.println("Jelentkezés sikeresen törölve.");
                    applications.remove(allas_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Insert the application into the database
                try {
                    String sql = "INSERT INTO Jelentkezes (allaskereso_email_cim, allas_id) VALUES (?, ?)";
                    PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql);
                    pstmt.setString(1, allaskereso_email_cim);
                    pstmt.setInt(2, allas_id);
                    pstmt.executeUpdate();
                    System.out.println("Jelentkezés sikeresen felvéve.");
                    applications.add(allas_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        isPushed = false;
        return label;
    }


    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}