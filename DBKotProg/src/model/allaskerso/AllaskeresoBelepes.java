package model.allaskerso;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
import model.AdminBelepes;
import model.Kijelentkezes;
import model.ceg.Ceg;
import model.jpanel.AllasAjanlatPanel;
import model.jpanel.JelentkezesPanel;

import model.DatabaseConnection;
import model.MainFrame;
import model.jpanel.AllasAjanlatPanel;
import model.jpanel.JelentkezesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AllaskeresoBelepes extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private JTextField emailTf;
    private JTextField jelszoTf;
    private JButton bejelentkezesButton;
    private ResultSet rs;
    private Statement stmt;
    private DatabaseConnection dbConnection = new DatabaseConnection();
    private JFrame newFrame;
    private JelentkezesPanel jelentkezesPanel;
    private Allaskereso allaskereso;

    public void createGui() {
        newFrame = new JFrame("Belépés vagy Regisztráció");
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



                        String input = emailTf.getText();
                        String regex = "admin[123]"; // regex: "admin" string, majd az 1, 2, vagy 3 számjegy

                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(input);

                        if (matcher.matches()) {
                            String admin =   emailLabel.getText();
                            String adminjelszo = jelszoTf.getText();
                            String sql = "SELECT * FROM ADMIN WHERE admin.username = '" + admin + "' AND admin.jelszo = '" + adminjelszo + "'";

                            try (Statement stmt = new DatabaseConnection().connectToDatabase()) {

                                ResultSet rs = stmt.executeQuery(sql);

                                if (rs.next()) {
                                    // Sikeres bejelentkezés
                                    newFrame.dispose();
                                    AdminBelepes adminBelepes= new AdminBelepes();
                                } else {
                                    // Sikertelen bejelentkezés
                                    JOptionPane.showMessageDialog(null, "Hibás e-mail cím vagy jelszó!", "Hiba", JOptionPane.ERROR_MESSAGE);
                                }

                            } catch (SQLException ex) {

                            }
                        }
                        else
                        {login();}
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

    };

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

        String sql = "SELECT * FROM Allaskereso WHERE allaskereso.email_cim = '" + email + "' AND allaskereso.jelszo = '" + jelszo + "'";

        try (Statement stmt = new DatabaseConnection().connectToDatabase()) {

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                // Sikeres bejelentkezés
                allaskereso = new Allaskereso(rs.getString("email_cim"),rs.getString("nev"), rs.getString("jelszo"),rs.getString("telefonszam"));
                 openNewJFrameAfterLogin();
            } else {
                // Sikertelen bejelentkezés
                JOptionPane.showMessageDialog(null, "Hibás e-mail cím vagy jelszó!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            if (ex.getErrorCode() == -20001) {
                JOptionPane.showMessageDialog(null, "A jelszónak legalább 8 karakter hosszúnak kell lennie.", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Ismeretlen hiba történt: " + ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void openNewJFrameAfterLogin() {
        JFrame newFrame = new JFrame();
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(500, 500);
        newFrame.setLocationRelativeTo(null);
        tabbedPane  = new JTabbedPane();
        newFrame.add(tabbedPane);

        FooldalAllaskeresoPane fooldalAllaskeresoPane= new FooldalAllaskeresoPane();
        tabbedPane.addTab("Főoldal",fooldalAllaskeresoPane);

        jelentkezesPanel =  new JelentkezesPanel(emailTf.getText());
        tabbedPane.addTab("Jelentkezés hirdetésre",jelentkezesPanel);

        JelentkezeseimPanel jelentkezesimPanel = new JelentkezeseimPanel(allaskereso.getEmail(),new DatabaseConnection().getConnection());
        tabbedPane.addTab("Jelentkezéseim",jelentkezesimPanel);

        AllaskeresoModositasPanel allaskeresoModositasPanel = new AllaskeresoModositasPanel(allaskereso,dbConnection);
        tabbedPane.addTab("Adataim módosítása", allaskeresoModositasPanel);

        Kijelentkezes kijelentkezes= new Kijelentkezes(newFrame,new DatabaseConnection());
        kijelentkezes.allaskeresoTorles(allaskereso);
        tabbedPane.addTab("Kijelentkezes", kijelentkezes);

        newFrame.setVisible(true);
    }

}

