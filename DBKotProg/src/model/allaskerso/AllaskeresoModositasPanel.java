package model.allaskerso;

import model.DatabaseConnection;
import model.allaskerso.Allaskereso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllaskeresoModositasPanel extends JPanel {
    private Allaskereso currentUser;
    private DatabaseConnection dbConnection;
    private JTextField jelszoField, telefonszamField, iskolaiVegzetsegField;
    private JButton updateButton;
    private JLabel emailLabel, nameLabel, phoneNumberLabel, educationLabel; // Hozzáadva: Az adatok megjelenítéséhez szükséges JLabel-ek

    public AllaskeresoModositasPanel(Allaskereso user, DatabaseConnection dbConnection) {
        this.currentUser = user;
        this.dbConnection = dbConnection;

        loadData();

        setLayout(new GridLayout(5, 2)); // Módosítva: 5 sor és 2 oszlop

        // Hozzáadva: Az adatok megjelenítése JLabel-eken
        add(new JLabel("Email: "));
        emailLabel = new JLabel(currentUser.getEmail());
        add(emailLabel);

        add(new JLabel("Név: "));
        nameLabel = new JLabel(currentUser.getNev());
        add(nameLabel);

        add(new JLabel("Jelszó: "));
        jelszoField = new JTextField(currentUser.getJelszo());
        add(jelszoField);

        add(new JLabel("Telefonszám: "));
        telefonszamField = new JTextField(currentUser.getTelefonszam());
        add(telefonszamField);

        add(new JLabel("Iskolai végzettség: "));
        iskolaiVegzetsegField = new JTextField();
        add(iskolaiVegzetsegField);

        updateButton = new JButton("Frissítés");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                  if(iskolaiVegzetsegField.getText().equals("") || iskolaiVegzetsegField.getText()==null ){updateUserInfo();}
                  else{
                      updateUserInfo();
                    insertEducation();}
                    JOptionPane.showMessageDialog(null, "Sikeres frissítés!", "Siker", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Frissítés sikertelen: " + ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(updateButton);
    }

    private void loadData() {
        String query = "SELECT a.email_cim, a.nev, a.jelszo, a.telefonszam, iv.iskolai_vegzettseg " +
                "FROM Allaskereso a " +
                "INNER JOIN Iskolai_vegzettseg iv ON a.email_cim = iv.email_cim " +
                "WHERE a.email_cim = ?";
        try {
            java.sql.PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, currentUser.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                currentUser.setNev(resultSet.getString("nev"));
                currentUser.setJelszo(resultSet.getString("jelszo"));
                currentUser.setTelefonszam(resultSet.getString("telefonszam"));
                currentUser.setIskolaiVegzetseg(resultSet.getString("iskolai_vegzettseg"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void updateUserInfo() throws SQLException {
        String newPassword = jelszoField.getText();
        String newPhone = telefonszamField.getText();

        try {
            // Hívja a tárolt eljárást
            CallableStatement callableStatement = dbConnection.getConnection().prepareCall("{call update_user_info(?, ?, ?)}");
            callableStatement.setString(1, currentUser.getEmail());
            callableStatement.setString(2, newPassword);
            callableStatement.setString(3, newPhone);
            callableStatement.execute();
            callableStatement.close();

            // Frissítse a currentUser objektumot
            currentUser.setJelszo(newPassword);
            currentUser.setTelefonszam(newPhone);

        } catch (SQLException e) {
            throw e;
        }
    }
    private void insertEducation() throws SQLException {
        String newEducation = iskolaiVegzetsegField.getText();

        try {
            // Új rekord létrehozása az Iskolai_vegzettseg táblában
            String insertQuery = "INSERT INTO Iskolai_vegzettseg (email_cim, iskolai_vegzettseg) VALUES (?, ?)";
            java.sql.PreparedStatement insertStatement = dbConnection.getConnection().prepareStatement(insertQuery);
            insertStatement.setString(1, currentUser.getEmail());
            insertStatement.setString(2, newEducation);
            insertStatement.executeUpdate();
            insertStatement.close();

        } catch (SQLException e) {
            throw e;
        }
    }


}
