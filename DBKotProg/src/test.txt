package model.ceg;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UjAllasHirdetesPanel extends JPanel implements ActionListener {

    private JPanel input_panel;
    private TextField leiras_field;
    private TextField berezesi_intervallum_field;
    private JButton submit_button;
    private Connection connection;
    int ceg_id;

    public UjAllasHirdetesPanel(Ceg ceg) {
        this.ceg_id=ceg.getCeg_id();
        setLayout(new BorderLayout());
        connection = new DatabaseConnection().getConnection();

        this.input_panel = new JPanel();
        this.input_panel.setLayout(new GridLayout(3, 2));

        this.leiras_field = new TextField(50);
        this.berezesi_intervallum_field = new TextField(50);

        this.input_panel.add(new Label("Leírás:"));
        this.input_panel.add(this.leiras_field);
        this.input_panel.add(new Label("Bérezési intervallum:"));
        this.input_panel.add(this.berezesi_intervallum_field);

        this.submit_button = new JButton("Hozzáad");
        this.input_panel.add(this.submit_button);

        this.add(input_panel, BorderLayout.CENTER);

        // Add event listeners for user actions
        this.submit_button.addActionListener(this);
    }

    // Define event listeners
  /*  public void actionPerformed(ActionEvent e) {
        String leiras = leiras_field.getText();
        String berezesi_intervallum = berezesi_intervallum_field.getText();

        String sql = "INSERT INTO allasajanlat (ceg_id, leiras, berezesi_intervallum, AKTIV,torlo_username)\n" +
                "VALUES (?, ?,?,?,?);\n";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, ceg_id);
            pstmt.setString(2, leiras);
            pstmt.setString(3, berezesi_intervallum);
            pstmt.setInt(4, 1); // állapot 1 (aktív)
            pstmt.setString(5, "admin");

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Az álláshirdetés sikeresen létrehozva!");
            } else {
                JOptionPane.showMessageDialog(this, "Hiba történt az álláshirdetés létrehozása közben.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Hiba történt az álláshirdetés létrehozása közben: " + ex.getMessage());
        }
    }*/

    public void actionPerformed(ActionEvent e) {
        String leiras = leiras_field.getText();
        String berezesi_intervallum = berezesi_intervallum_field.getText();

        String sql = "{CALL add_allasajanlat(?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(sql)) {
            cstmt.setInt(1, ceg_id);
            cstmt.setString(2, leiras);
            cstmt.setString(3, berezesi_intervallum);
            cstmt.setInt(4, 1);
            cstmt.setString(5, "admin");

            int affectedRows = cstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Az álláshirdetés sikeresen létrehozva!");
            } else {
                JOptionPane.showMessageDialog(this, "Hiba történt az álláshirdetés létrehozása közben.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Hiba történt az álláshirdetés létrehozása közben: " + ex.getMessage());
        }
    }





}
