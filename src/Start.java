import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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

public class Start extends JFrame {


	public static final String SQLServer = "10.62.36.151";
	public static final String SQLUser = "root";
	public static final String SQLPwd = "Password1";
	public static final String DBName = "testdb";

	private JPanel contentPane;
	JFrame dialog;

	static Connection connection;

	// Statement to execute SQL commands
	static Statement stmt;
	static Statement stmt2;
	static JButton btnNewButton;
	
	ConnectionListener connListen;
	
	MainFrame mf = new MainFrame(this);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// UIManager.put("TextField.caretForeground", new
					// ColorUIResource(Color.WHITE));
					Start frame = new Start();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					frame.getRootPane().setDefaultButton(btnNewButton);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// initialize Database connection
	public void initializeDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"
					+ SQLServer
					+ "/"+ DBName +"?useUnicode=yes&characterEncoding=UTF-8", ""
					+ SQLUser + "", "" + SQLPwd + "");

			stmt = connection.createStatement();
			stmt2 = connection.createStatement();

		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(dialog,
					"Cannot connect to database server with IP: " + SQLServer
							+ ". \nCheck if server or network is down.");
			System.exit(1);
		}
	}
	
	public void initializeNetListener(){
		connListen = new ConnectionListener(connection);
		connListen.start();
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
		initializeDB();
		initializeNetListener();
		setTitle("Cisco Demo Lab Inventory");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 359, 124);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String sql = "select * from (select c.name AS Category, p.p_code AS Product, p.description AS Description, count(i.p_id) AS Total, sum(case when i.availability = 1 then 1 else 0 end) AS Lab, sum(case when i.availability = 0 then 1 else 0 end) as Demo  from inventory i, product p, category c where c.id = p.c_id AND p.id = i.p_id group by i.p_id) temp order by temp.Category";
				mf.excelSql = sql;
				Date date = new Date();
				String today = date.toString();
				mf.excelFile = "general_lab_inventory_" + today + ".xls";

				Start.showTable(sql, mf.dtm);

				mf.setLocationRelativeTo(null);
				mf.setVisible(true);
				setVisible(false);
			}
		});
		btnNewButton.setBounds(97, 11, 140, 64);
		contentPane.add(btnNewButton);

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
					if (!connectionL.isValid(5)){
						System.out.println("Connection lost");
						pauseNetListener();
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
}
