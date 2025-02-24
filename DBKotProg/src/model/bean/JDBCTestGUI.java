package model.bean;

import java.sql.*;

import oracle.jdbc.pool.OracleDataSource;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class JDBCTestGUI extends JFrame implements ActionListener, ItemListener
{
  private JTable result_table;
	private JTable result_table2;
	private JTable result_table3;
	private JTable result_table4;
	private JTable result_table5;
	private JTable result_table6;
  private JButton search_button;
	private JButton search_button2;
	private JButton search_button3;
	private JButton search_button4;
	private JButton search_button5;
	private JButton search_button6;
  private TextField name_field;
	private TextField name_field2;
	private TextField name_field3;
	private TextField name_field4;
	private TextField name_field5;
	private TextField name_field6;
  private JPanel input_panel;
  private JPanel output_panel;
	private JPanel input_panel2;
	private JPanel output_panel2;
	private JPanel input_panel3;
	private JPanel output_panel3;
	private JPanel input_panel4;
	private JPanel output_panel4;
	private JPanel input_panel5;
	private JPanel output_panel5;
	private JPanel input_panel6;
	private JPanel output_panel6;
  private JPanel admin;
	private JPanel allasAjanlat;
	private JPanel allasKereso;
	private JPanel ceg;
	private JPanel iskolaiVegzettsed;
	private JPanel jelentkezes;

  private DefaultTableModel table_model;
	private DefaultTableModel table_model2;
	private DefaultTableModel table_model3;
	private DefaultTableModel table_model4;
	private DefaultTableModel table_model5;
	private DefaultTableModel table_model6;
  private JScrollPane scroll_pane;
	private JScrollPane scroll_pane6;
	private JScrollPane scroll_pane2;
	private JScrollPane scroll_pane3;
	private JScrollPane scroll_pane4;
	private JScrollPane scroll_pane5;
  private Vector<String> column_names_vector;
	private Vector<String> column_names_vector2;
	private Vector<String> column_names_vector3;
	private Vector<String> column_names_vector4;
	private Vector<String> column_names_vector5;
	private Vector<String> column_names_vector6;
  private Choice field_choice;
	private Choice field_choice2;
	private Choice field_choice3;
	private Choice field_choice4;
	private Choice field_choice5;
	private Choice field_choice6;
  private String choice_string;
	private String choice_string2;
	private String choice_string3;
	private String choice_string4;
	private String choice_string5;
	private String choice_string6;
  private JTabbedPane tabbed_pane;
  
  private ResultSet rs;
  private Statement stmt;
  
  private List<String> a;
  /**
   *  Constuctor. Creating and initializing objects.
   * */
  public JDBCTestGUI(String title) {
	  super( title );
	  this.setSize( 600, 400 );
	  this.admin = new JPanel();
	  this.allasAjanlat = new JPanel();
	  this.allasKereso = new JPanel();
	  this.ceg = new JPanel();
	  this.iskolaiVegzettsed = new JPanel();
	  this.jelentkezes = new JPanel();
	  this.output_panel = new JPanel();
	  this.input_panel = new JPanel();
	  this.output_panel2 = new JPanel();
	  this.input_panel2 = new JPanel();
	  this.output_panel3 = new JPanel();
	  this.input_panel3 = new JPanel();
	  this.output_panel4 = new JPanel();
	  this.input_panel4 = new JPanel();
	  this.output_panel5 = new JPanel();
	  this.input_panel5 = new JPanel();
	  this.output_panel6 = new JPanel();
	  this.input_panel6 = new JPanel();
	  this.name_field = new TextField( 50 );
	  this.name_field2 = new TextField( 50 );
	  this.name_field3 = new TextField( 50 );
	  this.name_field4 = new TextField( 50 );
	  this.name_field5 = new TextField( 50 );
	  this.name_field6= new TextField( 50 );
	  this.search_button = new JButton( "Search" );
	  this.search_button2 = new JButton( "Search" );
	  this.search_button3 = new JButton( "Search" );
	  this.search_button4 = new JButton( "Search" );
	  this.search_button5 = new JButton( "Search" );
	  this.search_button6 = new JButton( "Search" );
	  this.field_choice = new Choice();
	  this.field_choice2 = new Choice();
	  this.field_choice3 = new Choice();
	  this.field_choice4 = new Choice();
	  this.field_choice5 = new Choice();
	  this.field_choice6 = new Choice();
	   
	 
	  createGUI();
	  
	  try {
		  String pass="oracle";
		  String user="system";
		  /* Connect to the Oracle Database and using the "HR" user's schema */
		  OracleDataSource ods = new OracleDataSource();
  		  Class.forName ("oracle.jdbc.OracleDriver");

		  ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
		  Connection conn = ods.getConnection(user,pass);
		  stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	  } catch ( Exception ex ) {
		  ex.printStackTrace();
	  }
	  
	  
	  
  }
  /**
   *    Creating the graphical user interface for the program.
   **/
  public void createGUI() {
	
	tabbed_pane = new JTabbedPane(JTabbedPane.TOP);
	tabbed_pane.addTab( "Admin", this.admin);
	tabbed_pane.addTab( "Állás ajánlat", this.allasAjanlat);
	tabbed_pane.addTab( "Állás kereső", this.allasKereso);
	tabbed_pane.addTab( "Cég", this.ceg);
	tabbed_pane.addTab( "Jelentkezés", this.jelentkezes);
	tabbed_pane.addTab( "Iskolai Végzettség", this.iskolaiVegzettsed);


	this.admin.setLayout( new BorderLayout() );
	  this.allasKereso.setLayout( new BorderLayout() );
	  this.allasAjanlat.setLayout( new BorderLayout() );
	  this.ceg.setLayout( new BorderLayout() );
	  this.jelentkezes.setLayout( new BorderLayout() );
	  this.iskolaiVegzettsed.setLayout( new BorderLayout() );

	this.input_panel.setLayout( new GridLayout(3,3) );
	this.output_panel.setLayout( new BorderLayout() );
	  this.input_panel2.setLayout( new GridLayout(3,3) );
	  this.output_panel2.setLayout( new BorderLayout() );
	  this.input_panel3.setLayout( new GridLayout(3,3) );
	  this.output_panel3.setLayout( new BorderLayout() );
	  this.input_panel4.setLayout( new GridLayout(3,3) );
	  this.output_panel4.setLayout( new BorderLayout() );
	  this.input_panel5.setLayout( new GridLayout(3,3) );
	  this.output_panel5.setLayout( new BorderLayout() );
	  this.input_panel6.setLayout( new GridLayout(3,3) );
	  this.output_panel6.setLayout( new BorderLayout() );
	
	
	// the labels and input fields
	this.input_panel.add( new Label("Content:") );
	this.input_panel.add( this.name_field );
	this.input_panel.add( new Label("Search in field:") );
	this.input_panel.add( this.field_choice );
	this.input_panel.add( this.search_button );
	  this.input_panel2.add( new Label("Content:") );
	  this.input_panel2.add( this.name_field2 );
	  this.input_panel2.add( new Label("Search in field:") );
	  this.input_panel2.add( this.field_choice2 );
	  this.input_panel2.add( this.search_button2 );
	  this.input_panel3.add( new Label("Content:") );
	  this.input_panel3.add( this.name_field3 );
	  this.input_panel3.add( new Label("Search in field:") );
	  this.input_panel3.add( this.field_choice3 );
	  this.input_panel3.add( this.search_button3 );
	  this.input_panel4.add( new Label("Content:") );
	  this.input_panel4.add( this.name_field4 );
	  this.input_panel4.add( new Label("Search in field:") );
	  this.input_panel4.add( this.field_choice4 );
	  this.input_panel4.add( this.search_button4 );
	  this.input_panel5.add( new Label("Content:") );
	  this.input_panel5.add( this.name_field5 );
	  this.input_panel5.add( new Label("Search in field:") );
	  this.input_panel5.add( this.field_choice5 );
	  this.input_panel5.add( this.search_button5 );
	  this.input_panel6.add( new Label("Content:") );
	  this.input_panel6.add( this.name_field6 );
	  this.input_panel6.add( new Label("Search in field:") );
	  this.input_panel6.add( this.field_choice6 );
	  this.input_panel6.add( this.search_button6 );
	
	
	// adding event listeners
	this.field_choice.addItemListener(this);	
	this.search_button.addActionListener(this);
	  this.field_choice2.addItemListener(this);
	  this.search_button2.addActionListener(this);
	  this.field_choice3.addItemListener(this);
	  this.search_button3.addActionListener(this);
	  this.field_choice4.addItemListener(this);
	  this.search_button4.addActionListener(this);
	  this.field_choice5.addItemListener(this);
	  this.search_button5.addActionListener(this);
	  this.field_choice6.addItemListener(this);
	  this.search_button6.addActionListener(this);
	
	this.getContentPane().add( tabbed_pane );
	this.admin.add( input_panel, BorderLayout.NORTH );
	this.admin.add( output_panel, BorderLayout.CENTER);
	  this.allasAjanlat.add( input_panel2, BorderLayout.NORTH );
	  this.allasAjanlat.add( output_panel2, BorderLayout.CENTER);
	  this.allasKereso.add( input_panel3, BorderLayout.NORTH );
	  this.allasKereso.add( output_panel3, BorderLayout.CENTER);
	  this.ceg.add( input_panel4, BorderLayout.NORTH );
	  this.ceg.add( output_panel4, BorderLayout.CENTER);
	  this.jelentkezes.add( input_panel5, BorderLayout.NORTH );
	  this.jelentkezes.add( output_panel5, BorderLayout.CENTER);
	  this.iskolaiVegzettsed.add( input_panel6, BorderLayout.NORTH );
	  this.iskolaiVegzettsed.add( output_panel6, BorderLayout.CENTER);

	
	// column names in a vector
	this.column_names_vector = new Vector<>();
	this.column_names_vector.add( "username" );
	this.column_names_vector.add( "jelszo" );
	  this.column_names_vector2 = new Vector<>();
	  this.column_names_vector2.add( "Cég neve" );
	  this.column_names_vector2.add( "Munka neve" );
	  this.column_names_vector2.add( "Bérezési intervallum" );
	  this.column_names_vector2.add( "Aktív" );
	  this.column_names_vector3 = new Vector<>();
	  this.column_names_vector3.add("Név");
	  this.column_names_vector3.add("Email");
	  this.column_names_vector3.add("Telefon");
	  this.column_names_vector4 = new Vector<>();
	  this.column_names_vector4.add("Cégnév");
	  this.column_names_vector4.add("Kapcsolattartó Név");
	  this.column_names_vector4.add("Kapcsolattartó Email");
	  this.column_names_vector4.add("Kapcsolattartó  Telefon");
	  this.column_names_vector5 = new Vector<>();
	  this.column_names_vector5.add("Email");
	  this.column_names_vector6 = new Vector<>();
	  this.column_names_vector6.add("Email");
	  this.column_names_vector6.add("Iskolai Végzettség");


	
	// need to use a table model for dynamic table handling
	this.table_model = new DefaultTableModel( column_names_vector, 0 );
	  this.table_model2 = new DefaultTableModel( column_names_vector2, 0 );
	  this.table_model3 = new DefaultTableModel( column_names_vector3, 0 );
	  this.table_model4 = new DefaultTableModel( column_names_vector4, 0 );
	  this.table_model5 = new DefaultTableModel( column_names_vector5, 0 );
	  this.table_model6 = new DefaultTableModel( column_names_vector6, 0 );
	this.result_table = new JTable( table_model );
	  this.result_table = new JTable( table_model );
	  this.result_table2 = new JTable( table_model2 );
	  this.result_table3 = new JTable( table_model3 );
	  this.result_table4 = new JTable( table_model4 );
	  this.result_table5 = new JTable( table_model5 );
	  this.result_table6 = new JTable( table_model6 );
    this.scroll_pane = new JScrollPane(result_table);
	  this.scroll_pane6 = new JScrollPane(result_table6);
	  this.scroll_pane2 = new JScrollPane(result_table2);
	  this.scroll_pane3 = new JScrollPane(result_table3);
	  this.scroll_pane4 = new JScrollPane(result_table4);
	  this.scroll_pane5 = new JScrollPane(result_table5);
	this.output_panel.add( scroll_pane );
	  this.output_panel2.add( scroll_pane2 );
	  this.output_panel3.add( scroll_pane3 );
	  this.output_panel4.add( scroll_pane4 );
	  this.output_panel5.add( scroll_pane5 );
	  this.output_panel6.add( scroll_pane6 );
	
	
	// the column names listed into a drop-down list
	this.field_choice.add( "username" );
	this.field_choice.add( "jelszo" );
	  this.field_choice2.add( "allas" );
	  this.field_choice2.add( "ceg" );
	  this.field_choice2.add( "aktiv" );
	  this.field_choice2.add( "nem aktiv" );
	  this.field_choice3.add( "email" );
	  this.field_choice3.add( "nev" );
	  this.field_choice3.add( "telefonszam" );
	  this.field_choice4.add( "cegNev" );
	  this.field_choice4.add( "kapcsolattartoEmail" );
	  this.field_choice4.add( "kapcsolattartoNev" );
	  this.field_choice4.add( "kapcsolattartoTelefon" );
	  this.field_choice5.add( "email" );
	  this.field_choice6.add( "email" );
	  this.field_choice6.add( "vegzettseg" );




	choice_string = "username";
	  choice_string2 = "allas";
	  choice_string3 = "email";
	  choice_string4 = "ceg nev";
	  choice_string5 = "email";
	  choice_string6 = "email";

	 
  }
	
	
  public void actionPerformed( ActionEvent e ) {
	if ( e.getSource() == this.search_button ) {
		
		/******************* Starting queries ********************/


		String sql = "";
		if ( this.name_field.getText().equals("") ) {
			
    
			sql = "SELECT * FROM ADMIN";
    
    
		} else {
			if ( choice_string == "username" ) {
				sql = "SELECT * FROM admin WHERE username LIKE '"+ name_field.getText() +"' ORDER BY username";
			} else if (choice_string == "jelszo") {
				sql = "SELECT * FROM admin WHERE jelszo LIKE '"+ name_field.getText() +"' ORDER BY jelszo";
			}
			
		}



		try {

			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			System.out.println();
			//rs = stmt.executeQuery(sql)
			System.out.println(sql);
			// removing all rows from the table
			int count = table_model.getRowCount();
			System.out.println(count);
			for ( int i = count-1; i>=0; i-- ) {
				table_model.removeRow(i);
			}
			repaint();
		
			while (rs.next()) {
				String row[] = {rs.getString("username"), rs.getString("jelszo")};
				System.out.println(row.length);
				
				this.table_model.addRow( row ); // adding new row into the table
			}
			repaint();
		}catch ( SQLException ex ) {
			ex.printStackTrace();
		}
		
	}//admins
	if ( e.getSource() == this.search_button2 ) {

		  /******************* Starting queries ********************/


		  String sql = "";
		  if ( this.name_field2.getText().equals("") && !Objects.equals(choice_string2, "aktiv") && !Objects.equals(choice_string2, "nem aktiv")) {


			  sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id";


		  } else {
			  if ( choice_string2 == "ceg" ) {
				  sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND Ceg.ceg_nev LIKE '%"+ name_field2.getText() +"%' ORDER BY Ceg.ceg_nev";
			  } else if (choice_string2 == "allas") {

				  sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.leiras LIKE '%"+ name_field2.getText() +"%' ORDER BY ceg.ceg_nev";
			  }else if (choice_string2 == "aktiv") {
				  sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=1";
			  }
			  else if (choice_string2 == "nem aktiv") {
				  sql = "SELECT Ceg.ceg_nev, Allasajanlat.leiras, allasajanlat.berezesi_intervallum, allasajanlat.AKTIV  FROM Ceg, Allasajanlat WHERE Ceg.ceg_id = Allasajanlat.ceg_id AND allasajanlat.aktiv=0";
			  }


		  }



		  try {

			  rs = stmt.executeQuery(sql);
			  System.out.println(sql);
			  System.out.println();
			  //rs = stmt.executeQuery(sql)
			  System.out.println(sql);
			  // removing all rows from the table
			  int count = table_model2.getRowCount();
			  System.out.println(count);
			  for ( int i = count-1; i>=0; i-- ) {
				  table_model2.removeRow(i);
			  }
			  repaint();

			  while (rs.next()) {
				  String str= "Nem aktív";
				  if(rs.getInt(4)==1){
					  str="aktív";
				  }
				  String row[] = {rs.getString(1), rs.getString(2),rs.getString(3),str};
				  this.table_model2.addRow( row ); // adding new row into the table
			  }
			  repaint();
		  }catch ( SQLException ex ) {
			  ex.printStackTrace();
		  }

	  }//allasajanlatok
	  if ( e.getSource() == this.search_button3 ) {


		  /******************* Starting queries ********************/


		  String sql = "";
		  if ( this.name_field3.getText().equals("") ) {


			  sql = "SELECT Allaskereso.nev, Allaskereso.email_cim, Allaskereso.telefonszam  FROM Allaskereso";


		  } else {
			  if ( choice_string3 == "nev" ) {
				  sql = "SELECT Allaskereso.nev,Allaskereso.email_cim, Allaskereso.telefonszam  FROM Allaskereso WHERE Allaskereso.nev  LIKE '%"+ name_field3.getText() +"%' ORDER BY Allaskereso.nev";
			  } else if (choice_string3 == "email") {

				  sql = "SELECT Allaskereso.nev,Allaskereso.email_cim, Allaskereso.telefonszam FROM Allaskereso WHERE Allaskereso.email_cim  LIKE '%"+ name_field3.getText() +"%' ORDER BY Allaskereso.email_cim";
			  }else if (choice_string3 == "telefonszam") {
				  sql = "SELECT Allaskereso.nev,Allaskereso.email_cim, Allaskereso.telefonszam FROM Allaskereso WHERE Allaskereso.telefonszam LIKE '%"+ name_field3.getText() +"%' ORDER BY Allaskereso.telefonszam";
			  }

		  }

		  try {

			  rs = stmt.executeQuery(sql);
			  System.out.println(sql);
			  System.out.println();
			  //rs = stmt.executeQuery(sql)
			  System.out.println(sql);
			  // removing all rows from the table
			  int count = table_model3.getRowCount();
			  System.out.println(count);
			  for ( int i = count-1; i>=0; i-- ) {
				  table_model3.removeRow(i);
			  }
			  repaint();

			  while (rs.next()) {
				  String str= "Nem aktív";
				  String row[] = {rs.getString(1), rs.getString(2),rs.getString(3),str};
				  this.table_model3.addRow( row ); // adding new row into the table
			  }
			  repaint();
		  }catch ( SQLException ex ) {
			  ex.printStackTrace();
		  }

	  }

	  if ( e.getSource() == this.search_button4 ) {


		  /******************* Starting queries ********************/


		  String sql = "";
		  if ( this.name_field4.getText().equals("") ) {


			  sql = "SELECT Ceg.Ceg_nev,Ceg.kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg";


		  } else {
			  if ( choice_string4 == "cegNev" ) {
				  sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Ceg_nev  LIKE '%"+ name_field4.getText() +"%' ORDER BY Ceg.Ceg_nev";
			  } else if (choice_string4 == "kapcsolattartoNev") {
				  sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_nev  LIKE '%"+ name_field4.getText() +"%' ORDER BY Ceg.Kapcsolattarto_nev";
			  }else if (choice_string4 == "kapcsolattartoEmail") {
				  sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_email_cim LIKE '%"+ name_field4.getText() +"%' ORDER BY Ceg.Kapcsolattarto_email_cim";
			  }else if (choice_string4 == "kapcsolattartoTelefon") {
				  sql = "SELECT Ceg.Ceg_nev ,  Ceg.Kapcsolattarto_nev ,Ceg.Kapcsolattarto_email_cim ,Ceg.Kapcsolattarto_telefonszam FROM Ceg WHERE Ceg.Kapcsolattarto_telefonszam LIKE '%"+ name_field4.getText() +"%' ORDER BY Ceg.Kapcsolattarto_telefonszam";
			  }

		  }

		  try {

			  rs = stmt.executeQuery(sql);
			  System.out.println(sql);
			  System.out.println();
			  //rs = stmt.executeQuery(sql)
			  System.out.println(sql);
			  // removing all rows from the table
			  int count = table_model4.getRowCount();
			  System.out.println(count);
			  for ( int i = count-1; i>=0; i-- ) {
				  table_model4.removeRow(i);
			  }
			  repaint();

			  while (rs.next()) {
				  String row[] = {rs.getString(1), rs.getString(2),rs.getString(3), rs.getString(4)};
				  this.table_model4.addRow( row ); // adding new row into the table
			  }
			  repaint();
		  }catch ( SQLException ex ) {
			  ex.printStackTrace();
		  }

	  }

	  if ( e.getSource() == this.search_button5 ) {


		  /******************* Starting queries ********************/


		  String sql = "";
		  if ( this.name_field5.getText().equals("") ) {


			  sql = "SELECT Jelentkezes.allaskereso_email_cim FROM Jelentkezes";


		  } else {
			  if ( choice_string5 == "email" ) {
				  sql = "SELECT Jelentkezes.allaskereso_email_cim FROM Jelentkezes WHERE  Jelentkezes.allaskereso_email_cim LIKE '%"+ name_field5.getText() +"%' ORDER BY Jelentkezes.allaskereso_email_cim";
			  }
		  }

		  try {

			  rs = stmt.executeQuery(sql);
			  System.out.println(sql);
			  System.out.println();
			  //rs = stmt.executeQuery(sql)
			  System.out.println(sql);
			  // removing all rows from the table
			  int count = table_model5.getRowCount();
			  System.out.println(count);
			  for ( int i = count-1; i>=0; i-- ) {
				  table_model5.removeRow(i);
			  }
			  repaint();

			  while (rs.next()) {
				  String row[] = {rs.getString(1)};
				  this.table_model5.addRow( row ); // adding new row into the table
			  }
			  repaint();
		  }catch ( SQLException ex ) {
			  ex.printStackTrace();
		  }

	  }
	  if ( e.getSource() == this.search_button6 ) {


		  /******************* Starting queries ********************/


		  String sql = "";
		  if ( this.name_field6.getText().equals("") ) {


			  sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg";


		  } else {
			  if ( choice_string6 == "email" ) {
				  sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg WHERE  Iskolai_vegzettseg.email_cim LIKE '%"+ name_field6.getText() +"%' ORDER BY Iskolai_vegzettseg.email_cim";
			  }else if (choice_string6 == "vegzettseg") {
				  sql = "SELECT Iskolai_vegzettseg.email_cim ,Iskolai_vegzettseg.Iskolai_vegzettseg FROM Iskolai_vegzettseg WHERE Iskolai_vegzettseg.Iskolai_vegzettseg LIKE '%"+ name_field6.getText() +"%' ORDER BY Iskolai_vegzettseg.Iskolai_vegzettseg";
			  }
		  }

		  try {

			  rs = stmt.executeQuery(sql);
			  System.out.println(sql);
			  System.out.println();
			  //rs = stmt.executeQuery(sql)
			  System.out.println(sql);
			  // removing all rows from the table
			  int count = table_model6.getRowCount();
			  System.out.println(count);
			  for ( int i = count-1; i>=0; i-- ) {
				  table_model6.removeRow(i);
			  }
			  repaint();

			  while (rs.next()) {
				  String row[] = {rs.getString(1),rs.getString(2)};
				  this.table_model6.addRow( row ); // adding new row into the table
			  }
			  repaint();
		  }catch ( SQLException ex ) {
			  ex.printStackTrace();
		  }

	  }


  }
  
  public void itemStateChanged( ItemEvent e ) {
	  this.choice_string = field_choice.getSelectedItem();
	  this.choice_string2 = field_choice2.getSelectedItem();
	  this.choice_string3 = field_choice3.getSelectedItem();
	  this.choice_string4 = field_choice4.getSelectedItem();
	  this.choice_string5 = field_choice5.getSelectedItem();
	  this.choice_string6 = field_choice6.getSelectedItem();
  }
	
	
  public static void main (String args[]) throws SQLException
  {


    
    JDBCTestGUI gui = new JDBCTestGUI( "Test application to try JDBC with Oracle" );
    
    gui.setVisible(true);
    gui.show();
    

      WindowListener listener = new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          System.exit(0);
        }
      };
      gui.addWindowListener(listener);

  }
}
