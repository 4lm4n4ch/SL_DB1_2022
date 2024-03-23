package model.ceg;


import model.DatabaseConnection;
import model.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CegRegisztracio extends JFrame implements ActionListener {


    private JTextField cegnevTF;
    private JTextField jelszoTF;
    private JTextField emailTf;
    private JTextField telefonszamTF;
    private JTextField kapcsolattartoNev;
    private JButton regisztracioButton;
    private String sql_iskolaiVegzetseg;
    private DatabaseConnection dbConnection = new DatabaseConnection();
    private JFrame newFrame;



    @Override
    public void actionPerformed(ActionEvent e) {
    }
    public void createGui() {
         newFrame = new JFrame("Cég Regisztráció");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(350, 300);
        newFrame.setLocationRelativeTo(null);

        newFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel cegnevLabel = new JLabel("Cég név:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(cegnevLabel, constraints);

        cegnevTF = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(cegnevTF, constraints);

        JLabel jelszoLabel = new JLabel("Jelszó:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(jelszoLabel, constraints);

        jelszoTF = new JPasswordField(15);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(jelszoTF, constraints);

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(emailLabel, constraints);

        emailTf = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(emailTf, constraints);

        JLabel telefonszamLabel = new JLabel("Telefonszám:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(telefonszamLabel, constraints);

        telefonszamTF = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(telefonszamTF, constraints);

        JLabel kapcsolattartoNevLabel = new JLabel("Kapcsolattartó név:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(kapcsolattartoNevLabel, constraints);

        kapcsolattartoNev = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(kapcsolattartoNev, constraints);

        regisztracioButton = new JButton("Regisztráció");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newFrame.add(regisztracioButton, constraints);
        regisztracioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        JButton megseButton = new JButton("Mégse");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newFrame.add(megseButton, constraints);

        megseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFrame.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
        newFrame.setVisible(true);
    }




    private void register() {
        if (cegnevTF.getText().equals("") || cegnevTF.getText() == null) {
            JOptionPane.showMessageDialog(this, "Adj meg cégnévet!", "Hiba", JOptionPane.ERROR_MESSAGE);
        } else if (jelszoTF.getText() == null || jelszoTF.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Adj meg jelszót!", "Hiba", JOptionPane.ERROR_MESSAGE);
        } else if (telefonszamTF.getText() == null || telefonszamTF.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Adj meg telefonszámot!", "Hiba", JOptionPane.ERROR_MESSAGE);
        } else if (emailTf.getText() == null || emailTf.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Adj meg Email címet!", "Hiba", JOptionPane.ERROR_MESSAGE);
        } else if (kapcsolattartoNev.getText() == null || kapcsolattartoNev.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Adj meg kapcsolattartó nevet!", "Hiba", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("ellenorzes kesz");
            String sql = "INSERT INTO Ceg (ceg_nev, kapcsolattarto_email_cim, kapcsolattarto_nev, kapcsolattarto_telefonszam, kapcsolattarto_jelszo) VALUES (?, ?, ?, ?, ?)";



            try (Connection connection = dbConnection.getConnection();
     PreparedStatement pstmt = connection.prepareStatement(sql)) {

    pstmt.setString(1, cegnevTF.getText());
    pstmt.setString(2, emailTf.getText());
    pstmt.setString(3, kapcsolattartoNev.getText());
    pstmt.setString(4, telefonszamTF.getText());
    pstmt.setString(5, jelszoTF.getText());

    pstmt.executeUpdate();
    JOptionPane.showMessageDialog(null, "Sikeres regisztráció!", "Regisztráció", JOptionPane.INFORMATION_MESSAGE);
                newFrame.dispose();
    CegBejelentkezes cegBejelentkezes = new CegBejelentkezes();

    cegBejelentkezes.createGui();
} catch (SQLException ex) {
                System.out.println("Hibakód: " + ex.getErrorCode());
                int errorCode = ex.getErrorCode(); // Ezt a sort később módosíthatod, hogy az aktuális hibakódot használja.

                if (errorCode == 20004) {
                    JOptionPane.showMessageDialog(null, "A jelszonak legalabb 8 karakter hosszunak kell lennie.", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else if (errorCode == 20005) {
                    JOptionPane.showMessageDialog(null, "A telefonszamnak 10 karakter hosszunak kell lennie.", "Hiba", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Hiba tortent a regisztracio soran!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }

            }




        }
    }


}
