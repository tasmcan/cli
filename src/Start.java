import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JLabel;

public class Start extends JFrame {


	public static final String SQLServer = "10.62.36.151";
	public static final String SQLUser = "root";
	public static final String SQLPwd = "Password1";
	public static final String DBName = "demolab";

	private JPanel contentPane;
	JFrame dialog;

	static Connection connection;

	// Statement to execute SQL commands
	static Statement stmt;
	static Statement stmt2;
	
	ConnectionListener connListen;
	JLabel lblStartingTheEngines;
	MainFrame mf = new MainFrame(this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start frame = new Start();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					frame.initializeDB();
					frame.initializeNetListener();
					frame.initializeMain();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// initialize Database connection
	public void initializeDB() {
		try {
			lblStartingTheEngines.setText("Initializing Database Connections...");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"
					+ SQLServer
					+ "/"+ DBName +"?useUnicode=yes&characterEncoding=UTF-8", ""
					+ SQLUser + "", "" + SQLPwd + "");

			stmt = connection.createStatement();
			stmt2 = connection.createStatement();
			lblStartingTheEngines.setText("Initialized DB Connections...");

		} catch (java.lang.Exception ex) {
			JOptionPane.showMessageDialog(dialog,
					"Cannot connect to database server with IP: " + SQLServer
							+ ". \nCheck if server or network is down.");
			lblStartingTheEngines.setText("Initializing Database Connections failed...");
			System.exit(1);
		}
	}
	
	public void initializeNetListener(){
		lblStartingTheEngines.setText("Initializing connection listeners...");
		connListen = new ConnectionListener(connection);
		connListen.start();
		lblStartingTheEngines.setText("Connection listener initialized...");
	}
	
	public void initializeMain(){
		String sql = "select * from (select c.name AS Category, p.p_code AS Product, p.description AS Description, count(i.p_id) AS Total, sum(case when i.availability = 1 then 1 else 0 end) AS Lab, sum(case when i.availability = 0 then 1 else 0 end) as Demo  from inventory i, product p, category c where c.id = p.c_id AND p.id = i.p_id group by i.p_id) temp order by temp.Category";
		mf.excelSql = sql;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		Date date = new Date();
		
		mf.excelFile = "general_lab_inventory_" + dateFormat.format(date) + ".xls";

		Start.showTable(sql, mf.dtm);

		mf.setLocationRelativeTo(null);
		mf.setVisible(true);
	}
	
	public void pauseNetListener(){
		try {
			connListen.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void resumeNetListener(){
		connListen.notify();
	}

	static void showTable(String sql, DefaultTableModel deteme) {
		Vector rows = new Vector();
		Vector header = new Vector();
		Vector tmp;

		rows.clear();
		header.clear();

		try {
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++)
				header.addElement(rsMetaData.getColumnName(i));

			while (rs.next()) {
				tmp = new Vector();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					tmp.addElement(rs.getObject(i));
				}
				// System.out.println(tmp.toString());
				rows.add(tmp);
			}
			deteme.setDataVector(rows, header);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void sqlToExcel(String sql, String filename) throws SQLException,
			FileNotFoundException, IOException {

		// Create new Excel workbook and sheet
		HSSFWorkbook xlsWorkbook = new HSSFWorkbook();
		HSSFSheet xlsSheet = xlsWorkbook.createSheet();
		short rowIndex = 0;

		// Execute SQL query
		ResultSet rs = stmt.executeQuery(sql);

		// Get the list of column names and store them as the first
		// row of the spreadsheet.
		ResultSetMetaData colInfo = rs.getMetaData();
		List<String> colNames = new ArrayList<String>();
		HSSFRow titleRow = xlsSheet.createRow(rowIndex++);

		for (int i = 1; i <= colInfo.getColumnCount(); i++) {
			colNames.add(colInfo.getColumnName(i));
			titleRow.createCell((short) (i - 1)).setCellValue(
					new HSSFRichTextString(colInfo.getColumnName(i)));
			xlsSheet.setColumnWidth((short) (i - 1), (short) 4000);
		}

		// Save all the data from the database table rows
		while (rs.next()) {
			HSSFRow dataRow = xlsSheet.createRow(rowIndex++);
			short colIndex = 0;
			for (String colName : colNames) {
				dataRow.createCell(colIndex++).setCellValue(
						new HSSFRichTextString(rs.getString(colName)));
			}
		}
		// Write to disk
		xlsWorkbook.write(new FileOutputStream(filename));
	}

	/**
	 * Create the frame.
	 */
	public Start() {
		
		setTitle("Cisco Demo Lab Inventory");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 359, 115);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblStartingTheEngines = new JLabel("Starting the engines...");
		lblStartingTheEngines.setBounds(43, 33, 287, 16);
		contentPane.add(lblStartingTheEngines);
	}

	class ConnectionListener extends Thread {
		private Connection connectionL;

		public ConnectionListener(Connection connection) {
			this.connectionL = connection;
		}

		// check for incoming messages and broadcast
		public void run() {
			while (true) {
				// if connection terminated, remove from list of active
				// connections
				try {
					if (!connectionL.isValid(3)){
						System.out.println("Connection lost");
						showConnectionLostDialog();
						break;
					}
						
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// don't monopolize processor
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean checkConn() throws SQLException {
		if (connection.isValid(2000)) {
			return true;
		} else {
			return false;
		}

	}
	
	
	public void showConnectionLostDialog(){
		String[] buttons = {"OK, close", "Retry"};
		int choice = JOptionPane.showOptionDialog(null, "Retry or close?!", "Connection error!", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, buttons, buttons[1]);
		
		
		if (choice == JOptionPane.YES_OPTION) {
		    System.out.println("close choice");
		    try {
				connection.close();
				stmt.close();
				stmt2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    System.exit(0);
		} else if (choice == JOptionPane.NO_OPTION) {
		    System.out.println("retry choice");
		    initializeDB();
		    initializeNetListener();
		    
		} else if (choice == JOptionPane.CLOSED_OPTION){
			System.out.println("close option");
			try {
				connection.close();
				stmt.close();
				stmt2.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.exit(0);
		}
	}
}
