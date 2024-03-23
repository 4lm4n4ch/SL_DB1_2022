package model;

import model.allaskerso.Allaskereso;
import model.ceg.Ceg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Kijelentkezes extends JPanel implements ActionListener {

    private JButton kijelentkezes;
    private DatabaseConnection databaseConnection;
    private JFrame frame;

    public Kijelentkezes(JFrame frame, DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        kijelentkezes = new JButton("Kijelentkezés");
        kijelentkezes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(kijelentkezes);
        add(Box.createRigidArea(new Dimension(0, 10)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void allaskeresoTorles(Allaskereso allaskereso) {
        JButton button = new JButton("Fiók törlése");
        JLabel label = new JLabel("Ha szeretné törölni az álláskereső fiókját, nyomja meg a gombot:");

        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emailCim = allaskereso.getEmail();
                databaseConnection.torolAllaskereso(emailCim);

                // Visszatérés a fő képernyőre
                frame.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    public void cegTorles(Ceg ceg) {
        JButton button = new JButton("Fiók törlése");
        JLabel label = new JLabel("Ha szeretné törölni a cég fiókját, nyomja meg a gombot:");

        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cegId = ceg.getCeg_id();
                databaseConnection.torolCeg(cegId);

                // Visszatérés a fő képernyőre
                frame.dispose();
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

}
