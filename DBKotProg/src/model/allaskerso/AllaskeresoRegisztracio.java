package model.allaskerso;

//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;

import model.DatabaseConnection;
import model.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public  class AllaskeresoRegisztracio extends JFrame implements ActionListener {


private JTextField nevTF;
private JTextField jelszoTF;
private JTextField emailTf;
private JTextField telefonszamTF;
private JTextField iskolaiVegzetseg;
private JButton regisztracioButton;
	private String sql_iskolaiVegzetseg;
private DatabaseConnection dbConnection = new DatabaseConnection();
private JFrame newFrame;

	@Override
	public void actionPerformed(ActionEvent e) {




		}


	public void createRegistration() {
		 newFrame = new JFrame("Álláskereső Regisztráció");
		newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFrame.setSize(350, 300);
		newFrame.setLocationRelativeTo(null);

		newFrame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JLabel nevLabel = new JLabel("Név:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_END;
		newFrame.add(nevLabel, constraints);

		 nevTF = new JTextField(15);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		newFrame.add(nevTF, constraints);

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

		JLabel iskolaiVegzetsegLabel = new JLabel("Iskolai végzettség:");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.LINE_END;
		newFrame.add(iskolaiVegzetsegLabel, constraints);

		 iskolaiVegzetseg = new JTextField(15);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.LINE_START;
		newFrame.add(iskolaiVegzetseg, constraints);

		 regisztracioButton = new JButton("Regisztráció");
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		newFrame.add(regisztracioButton, constraints);
		regisztracioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)  {
				System.out.println("register hívodik");
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
		if (nevTF.getText().equals("") || nevTF.getText() == null) {
			JOptionPane.showMessageDialog(this, "Adj meg nevet!", "Hiba", JOptionPane.ERROR_MESSAGE);
		} else if (jelszoTF.getText() == null || jelszoTF.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Adj meg jelszót!", "Hiba", JOptionPane.ERROR_MESSAGE);
		} else if (telefonszamTF.getText() == null || telefonszamTF.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Adj meg telefonszámot!", "Hiba", JOptionPane.ERROR_MESSAGE);
		} else if (emailTf.getText() == null || emailTf.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Adj meg Email címet!", "Hiba", JOptionPane.ERROR_MESSAGE);
		} else if (iskolaiVegzetseg.getText() == null || iskolaiVegzetseg.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Adj meg iskolai végzettséget!", "Hiba", JOptionPane.ERROR_MESSAGE);
		}
else {
			String sql = "INSERT INTO Allaskereso (email_cim, nev, jelszo, telefonszam) VALUES (?, ?, ?, ?)";


			System.out.println("ellenorzes kesz");

			String sql_iskolaiVegzetseg = "INSERT INTO Iskolai_vegzettseg (email_cim, iskolai_vegzettseg) VALUES (?, ?)";

			try (Connection connection = dbConnection.getConnection();
				 PreparedStatement pstmtAllaskereso = connection.prepareStatement(sql);
				 PreparedStatement pstmtIskolaiVegzetseg = connection.prepareStatement(sql_iskolaiVegzetseg)) {


				pstmtAllaskereso.setString(1, emailTf.getText());
				pstmtAllaskereso.setString(2, nevTF.getText());
				pstmtAllaskereso.setString(3, jelszoTF.getText());
				pstmtAllaskereso.setString(4, telefonszamTF.getText());

				pstmtIskolaiVegzetseg.setString(1, emailTf.getText());
				pstmtIskolaiVegzetseg.setString(2, iskolaiVegzetseg.getText());

				pstmtAllaskereso.executeUpdate();
				pstmtIskolaiVegzetseg.executeUpdate();
				System.out.println("kész patika");

				JOptionPane.showMessageDialog(null, "Sikeres regisztráció!", "Regisztráció", JOptionPane.INFORMATION_MESSAGE);
				newFrame.dispose();
				AllaskeresoBelepes allaskeresoBelepes = new AllaskeresoBelepes();
				allaskeresoBelepes.createGui();
			} catch (SQLException ex) {
				if (ex.getErrorCode() == 20004) {
					JOptionPane.showMessageDialog(null, "Email cím nem megfelelő formátum.", "Hiba", JOptionPane.ERROR_MESSAGE);
				} else if (ex.getErrorCode() == 20003) {
					JOptionPane.showMessageDialog(null, "A jelszónak legalább 8 karakter hosszúnak kell lennie.", "Hiba", JOptionPane.ERROR_MESSAGE);
				} else if (ex.getErrorCode() == 20002) {
					JOptionPane.showMessageDialog(null, "A telefonszámnak 10 karakter hosszúnak kell lennie.", "Hiba", JOptionPane.ERROR_MESSAGE);
				} else if (ex.getErrorCode() == 20001) {
					JOptionPane.showMessageDialog(null, "Az email cím már használatban van!", "Hiba", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Ismeretlen hiba történt: " + ex.getMessage(), "Hiba", JOptionPane.ERROR_MESSAGE);
				}
			}

		}
	}


}
