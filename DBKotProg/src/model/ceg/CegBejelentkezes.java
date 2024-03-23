package model.ceg;

//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;

import model.DatabaseConnection;
import model.Kijelentkezes;
import model.MainFrame;
import model.jpanel.AllasAjanlatPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CegBejelentkezes implements ActionListener {

    private JTextField emailTf;
    private JTextField jelszoTf;
    private JButton bejelentkezesButton;
    private DatabaseConnection dbConnection;
    private JTabbedPane tabbedPane;
    private  UjAllasHirdetesPanel  ujAllasHirdetesPanel;
    JFrame newFrame;
private Ceg ceg;
    public void createGui() {
         newFrame = new JFrame("Cég Bejelentkezés");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(300, 200);
        newFrame.setLocationRelativeTo(null);

        newFrame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel emailLabel = new JLabel("Email:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(emailLabel, constraints);


         emailTf = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(emailTf, constraints);

        JLabel jelszoLabel = new JLabel("Jelszó:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        newFrame.add(jelszoLabel, constraints);

         jelszoTf = new JPasswordField(15);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        newFrame.add(jelszoTf, constraints);

         bejelentkezesButton = new JButton("Bejelentkezés");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newFrame.add(bejelentkezesButton, constraints);
        bejelentkezesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
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


    @Override
    public void actionPerformed(ActionEvent e) {

    }
    private void login() {
        String email = emailTf.getText();
        String jelszo = jelszoTf.getText();

        if (email == null || email.equals("")) {
            JOptionPane.showMessageDialog(null, "Kérjük, adj meg egy e-mail címet!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jelszo == null || jelszo.equals("")) {
            JOptionPane.showMessageDialog(null, "Kérjük, adj meg egy jelszót!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM Ceg WHERE ceg.kapcsolattarto_email_cim = '" + email + "' AND ceg.kapcsolattarto_jelszo = '" + jelszo + "'";


        try (Statement stmt = new DatabaseConnection().connectToDatabase()) {


//ceg_id, ceg_nev, kapcsolattarto_email_cim, kapcsolattarto_nev, kapcsolattarto_telefonszam, kapcsolattarto_jelszo
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                // Sikeres bejelentkezés
                 ceg= new Ceg(rs.getInt("ceg_id"),rs.getString("ceg_nev"),rs.getString("kapcsolattarto_nev"),rs.getString("kapcsolattarto_email_cim"),rs.getString("kapcsolattarto_jelszo"),rs.getString("kapcsolattarto_telefonszam"));
                 newFrame.dispose();
                openNewJFrameAfterLogin();
            } else {
                // Sikertelen bejelentkezés
                JOptionPane.showMessageDialog(null, "Hibás e-mail cím vagy jelszó!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openNewJFrameAfterLogin() {
        JFrame newFrame2 = new JFrame();
        newFrame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame2.setSize(500, 500);
        newFrame2.setLocationRelativeTo(null);
       tabbedPane  = new JTabbedPane();
        newFrame2.add(tabbedPane);
        ujAllasHirdetesPanel= new UjAllasHirdetesPanel(ceg);

        tabbedPane.addTab("Új álláshirdetés",ujAllasHirdetesPanel);
        AllasAjanlatPanel allasAjanlatPanel = new AllasAjanlatPanel(ceg);
       tabbedPane.addTab("Hírdetések módosítása",allasAjanlatPanel);

       Kijelentkezes kijelentkezes= new Kijelentkezes(newFrame2,new DatabaseConnection());
       kijelentkezes.cegTorles(ceg);
       tabbedPane.addTab("Kijelentkezes", kijelentkezes);

        newFrame2.setVisible(true);
    }
}

