package model;

import model.allaskerso.AllaskeresoBelepes;
import model.allaskerso.AllaskeresoRegisztracio;
import model.ceg.CegBejelentkezes;
import model.ceg.CegRegisztracio;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private AllaskeresoBelepes belepesPanel;
    private AllaskeresoRegisztracio regisztracioPanel;
    private CegRegisztracio cegregisztracioPanel;
    private CegBejelentkezes cegBelepes;
    private DatabaseConnection databaseConnection;

    public JFrame mainFrame;

    public MainFrame() {
        belepesPanel = new AllaskeresoBelepes();
        regisztracioPanel = new AllaskeresoRegisztracio();

        cegBelepes = new CegBejelentkezes();
        cegregisztracioPanel = new CegRegisztracio();
        mainFrame = new JFrame("Álláskereső");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel jobSeekerLabel = new JLabel("Álláskereső");
        JButton jobSeekerLogin = new JButton("Belépés");
        jobSeekerLogin.setBackground(Color.GREEN);

        jobSeekerLogin.addActionListener(e -> {
            mainFrame.dispose();
            belepesPanel.createGui();
        });
        JButton jobSeekerRegister = new JButton("Regisztráció");
        jobSeekerRegister.setBackground(Color.YELLOW);
        jobSeekerRegister.addActionListener(e -> {
            mainFrame.dispose();
            regisztracioPanel.createRegistration();
        });

        JLabel companyLabel = new JLabel("Cég");
        JButton companyLogin = new JButton("Belépés");
        companyLogin.setBackground(Color.GREEN); // Zöld háttérszín a bejelentkezés gombhoz
        companyLogin.addActionListener(e -> {
            mainFrame.dispose();
            cegBelepes.createGui();
        });
        JButton companyRegister = new JButton("Regisztráció");
        companyRegister.setBackground(Color.YELLOW); // Sárga háttérszín a regisztráció gombhoz
        companyRegister.addActionListener(e -> {
            mainFrame.dispose();
            cegregisztracioPanel.createGui();
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jobSeekerLabel)
                        .addComponent(companyLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jobSeekerLogin)
                        .addComponent(companyLogin))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jobSeekerRegister)
                        .addComponent(companyRegister)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jobSeekerLabel)
                        .addComponent(jobSeekerLogin)
                        .addComponent(jobSeekerRegister))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(companyLabel)
                        .addComponent(companyLogin)));
        mainFrame.add(mainPanel);

        // Initialize panels
        try {
            databaseConnection = new DatabaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel totalJobsLabel;
        int activeJobsCount = databaseConnection.getAktivAllasajanlatokSzama();

        totalJobsLabel = new JLabel("Összesen: " + activeJobsCount + " aktív állás ajánlatunk van.");
        totalJobsLabel.setHorizontalAlignment(SwingConstants.CENTER);


        mainPanel.add(totalJobsLabel);
        mainPanel.setSize(1000, 800);
        totalJobsLabel.setPreferredSize(totalJobsLabel.getPreferredSize());

        mainFrame.setLocationRelativeTo(null); // Középre igazítás
        mainFrame.getContentPane().setBackground(Color.DARK_GRAY); // Háttér szín beállítása
        mainPanel.setBackground(Color.WHITE); // MainPanel háttér szín beállítása

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jobSeekerLabel)
                                .addComponent(companyLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jobSeekerLogin)
                                .addComponent(companyLogin))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jobSeekerRegister)
                                .addComponent(companyRegister)))
                .addComponent(totalJobsLabel));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jobSeekerLabel)
                        .addComponent(jobSeekerLogin)
                        .addComponent(jobSeekerRegister))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(companyLabel)
                        .addComponent(companyLogin)
                        .addComponent(companyRegister))
                .addComponent(totalJobsLabel));


        mainFrame.pack();
        setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

}
/* //Todo öszetett lekérdezés
SELECT c.ceg_nev, COUNT(j.allaskereso_email_cim) AS jelentkezo_szam
FROM Ceg c
INNER JOIN Allasajanlat a ON c.ceg_id = a.ceg_id
INNER JOIN Jelentkezes j ON c.ceg_id = j.ceg_id
GROUP BY c.ceg_nev
ORDER BY jelentkezo_szam DESC
FETCH FIRST 1 ROW ONLY;

SELECT Allasajanlat.leiras, COUNT(Jelentkezes.allaskereso_email_cim) AS jelentkezo_szam
FROM Allasajanlat
LEFT JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id
WHERE Allasajanlat.aktiv = 1
GROUP BY Allasajanlat.allas_id, Allasajanlat.leiras
ORDER BY jelentkezo_szam DESC
FETCH FIRST 1 ROW ONLY;

SELECT Allasajanlat.allas_id, Allasajanlat.leiras, COUNT(*) AS jelentkezo_szam
FROM Allasajanlat
INNER JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id
GROUP BY Allasajanlat.allas_id, Allasajanlat.leiras
HAVING COUNT(*) >= 5
ORDER BY jelentkezo_szam DESC
FETCH FIRST 1 ROW ONLY;

Kiírja azoknak az álláshirdetéseknek a nevét és a hozzájuk tartozó cégek nevét, amelyek legalább 10 jelentkezővel rendelkeznek:
sql
Copy code
SELECT Allasajanlat.leiras, Ceg.ceg_nev
FROM Allasajanlat
INNER JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id
INNER JOIN Ceg ON Allasajanlat.ceg_id = Ceg.ceg_id
GROUP BY Allasajanlat.allas_id
HAVING COUNT(*) >= 10;
Kiírja azon álláskeresők email címét, akiknek van iskolai végzettsége, és a végzettségük között szerepel a "mérnök" szó:
sql
Copy code
SELECT Iskolai_vegzettseg.email_cim
FROM Iskolai_vegzettseg
WHERE iskolai_vegzettseg LIKE '%mérnök%'
AND email_cim IN (SELECT email_cim FROM Allaskereso);
Kiírja a legnépszerűbb cég nevét és az általuk kínált állások számát:
sql
Copy code
SELECT Ceg.ceg_nev, COUNT(*) AS allasok_szama
FROM Ceg
INNER JOIN Allasajanlat ON Ceg.ceg_id = Allasajanlat.ceg_id
GROUP BY Ceg.ceg_nev
ORDER BY allasok_szama DESC
FETCH FIRST 1 ROW ONLY;
Kiírja azoknak az álláskeresőknek az email címét, akik legalább két különböző cég állásaira jelentkeztek:
sql
Copy code
SELECT Jelentkezes.allaskereso_email_cim
FROM Jelentkezes
GROUP BY Jelentkezes.allaskereso_email_cim
HAVING COUNT(DISTINCT Jelentkezes.ceg_id) >= 2;
Kiírja azon állások leírását, amelyek aktívak és legalább 5 jelentkezőjük van, valamint azt, hogy mely cégek kínálják ezeket az állásokat:
sql
Copy code
SELECT Allasajanlat.leiras, Ceg.ceg_nev
FROM Allasajanlat
INNER JOIN Ceg ON Allasajanlat.ceg_id = Ceg.ceg_id
INNER JOIN Jelentkezes ON Allasajanlat.allas_id = Jelentkezes.allas_id
WHERE Allasajanlat.aktiv = 1
GROUP BY Allasajanlat.allas_id, Ceg.ceg_nev
HAVING COUNT(*) >= 5;
* */