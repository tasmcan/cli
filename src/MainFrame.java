import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	Start login;
	DefaultTableModel dtm = new DefaultTableModel();
	Add addnew = new Add(this);
	Delete delete = new Delete(this);
	Find find = new Find(this);
	JComboBox comboBox;
	private JTextField filterField;
	SendDemo send = new SendDemo();
	ReceiveDemo receive = new ReceiveDemo();
	Edit edit = new Edit(this);
	About about = new About();
	JPopupMenu popup;
	String excelSql;
	String excelFile;
	int selectedId = -1;
	String selectedPID;
	public ArrayList<Integer> demoChartArList = new ArrayList<Integer>();
	JLabel lblChartSize ;
	int rowNumber;

	/**
	 * Create the frame.
	 */
	public MainFrame(Start l) {
		setTitle("Demo Lab Inventory System Software");
		login = l;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 840, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 834, 30);
		contentPane.add(menuBar);

		JButton btnAddNew = new JButton("Add item");
		menuBar.add(btnAddNew);

		JButton btnDelete = new JButton("Delete");
		menuBar.add(btnDelete);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				System.out.println("closing....");
				try {
					Start.connection.close();
					Start.stmt.close();
					Start.stmt2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("closed....");
				System.exit(0);
			}
		});

		// Edit Button onClick

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillComboBox(edit.getComboBoxProduct(),
						getList("select * from product", 2));
				fillComboBox(edit.getComboBoxLocation(),
						getList("select * from location", 2));

				edit.id = -1;
				edit.setLocationRelativeTo(null);
				edit.setVisible(true);
			}
		});
		menuBar.add(btnEdit);
		// Disable Save Button on EditFrame
		edit.btnFinish.setEnabled(false);

		// Send Button OnClick
		JButton btnRent = new JButton("Send Demo");
		menuBar.add(btnRent);

		JButton btnReceiveDemo = new JButton("Receive Demo");
		btnReceiveDemo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				receive.inid = -1;
				receive.setLocationRelativeTo(null);
				receive.setVisible(true);
			}
		});
		menuBar.add(btnReceiveDemo);

		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about.setLocationRelativeTo(null);
				about.setVisible(true);
			}
		});
		menuBar.add(btnAbout);
		btnRent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send.inid = -1;
				send.setLocationRelativeTo(null);
				send.setVisible(true);
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delete.id = -1;
				delete.setLocationRelativeTo(null);
				delete.setVisible(true);
			}
		});

		// Disable Delete Button on DeleteFrame
		delete.btnDeleteNow.setEnabled(false);

		btnAddNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillComboBox(addnew.getComboBox(),
						getList("select * from product order by p_code", 2));
				fillComboBox(addnew.getComboBox_1(),
						getList("select * from io_type where io=1", 2));
				fillComboBox(addnew.getComboBox_2(),
						getList("select * from location order by loc_code", 2));
				addnew.setLocationRelativeTo(null);
				addnew.setVisible(true);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 64, 824, 377);
		contentPane.add(scrollPane);

		table = new JTable(dtm);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setBorder(new EmptyBorder(1, 1, 1, 1));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(table);

		JLabel lblChangeDisplay = new JLabel("Change display:");
		lblChangeDisplay.setBounds(10, 36, 112, 24);
		contentPane.add(lblChangeDisplay);

		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql;

				DateFormat dateFormat = new SimpleDateFormat(
						"dd-MM-yyyy_HH-mm-ss");
				Date date = new Date();

				filterField.setToolTipText("Press enter to filter/search.");
				filterField.setText("");

				switch (comboBox.getSelectedIndex()) {
				case 0: // show default display table
					sql = "select * from (select c.name AS Category, p.p_code AS Product, p.description AS Description, count(i.p_id) AS Total, sum(case when i.availability = 1 then 1 else 0 end) AS Lab, sum(case when i.availability = 0 then 1 else 0 end) as Demo  from inventory i, product p, category c where c.id = p.c_id AND p.id = i.p_id group by i.p_id) temp order by temp.Category";
					excelSql = sql;

					excelFile = "general_lab_inventory_"
							+ dateFormat.format(date) + ".xls";
					Start.showTable(sql, dtm);
					break;
				case 1: // show items in the lab at the moment
					sql = "select * from (select i.id AS ID, c.name AS Category, p.p_code AS Product, i.sn AS SN, i.notes AS Notes, l.loc_code AS Location from inventory i, product p, category c, location l where c.id = p.c_id AND p.id = i.p_id AND i.location = l.id AND i.availability=1) temp order by temp.Category";
					excelSql = sql;
					excelFile = "items_at_lab_" + dateFormat.format(date)
							+ ".xls";
					Start.showTable(sql, dtm);
					break;
				case 2: // show items at demo
					sql = "select * from (select i.id, p.p_code AS Product, i.sn AS SN, m.sender AS Sender, m.receiver AS Receiver, m.organization AS Organization, m.senddate AS 'Send Date', m.promiseddate AS 'Expire Date' from movement m, inventory i, product p, category c where c.id = p.c_id AND p.id=i.p_id AND m.i_id = i.id AND m.io = 0) temp order by temp.Product";
					excelSql = sql;
					excelFile = "items_at_demo_" + dateFormat.format(date)
							+ ".xls";
					Start.showTable(sql, dtm);
					break;
				case 3: // show expired items at demo
					sql = "select * from (select i.id, p.p_code AS Product, i.sn AS SN, m.sender AS Sender, m.receiver AS Receiver, m.organization AS Organization, m.senddate AS 'Send Date', m.promiseddate AS 'Expire Date'"
							+ "from movement m, inventory i, product p, category c "
							+ "where c.id = p.c_id AND p.id=i.p_id AND m.i_id = i.id AND m.io = 0 AND m.promiseddate <= NOW() ) temp order by temp.Product";
					excelSql = sql;
					excelFile = "expired_demo_list_" + dateFormat.format(date)
							+ ".xls";
					Start.showTable(sql, dtm);
					break;
				case 4: // show full inventory
					sql = "select * from (select i.id, c.name AS Category, p.p_code AS Product, i.sn AS SN, i.availability AS Available, i.notes AS Notes from inventory i, product p, category c where c.id = p.c_id AND p.id = i.p_id) temp order by temp.Category";
					excelSql = sql;
					excelFile = "full_inventory_data_"
							+ dateFormat.format(date) + ".xls";
					Start.showTable(sql, dtm);
					break;
				/*case 5:
					sql = "";
					excelSql = sql;
					excelFile = "deleted_returned_items_" + dateFormat.format(date) + ".xls";
					Start.showTable(sql, dtm);
					break;*/
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Default",
				"At lab", "At demo", "Expired demo", "Full inventory"}));
		comboBox.setBounds(124, 36, 126, 24);
		contentPane.add(comboBox);

		JLabel lblSearch = new JLabel("Filter:");
		lblSearch.setBounds(613, 36, 47, 24);
		contentPane.add(lblSearch);

		filterField = new JTextField();
		filterField.setToolTipText("Press enter to filter");
		filterField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String filter = filterField.getText().trim();
				if (filterField.getText().compareTo("") == 0)
					comboBox.setSelectedIndex(0);
				else {
					String sql = "select i.id, c.name, p.p_code, i.sn, l.loc_code, i.availability from inventory i, product p, category c, location l "
							+ "where c.id = p.c_id AND p.id=i.p_id AND i.location = l.id AND ((p.description LIKE '%"
							+ filter
							+ "%') OR (p.p_code LIKE '%"
							+ filter
							+ "%')"
							+ " OR (i.notes LIKE '%"
							+ filter
							+ "%') OR (i.sn LIKE '%"
							+ filter
							+ "%') OR (c.name LIKE '%" + filter + "%'))";
					Start.showTable(sql, dtm);
				}
			}
		});
		
		filterField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				filterField.setText("");
			}
		});
		
		filterField.setBounds(672, 36, 162, 24);
		contentPane.add(filterField);
		filterField.setColumns(10);

		
	
		// Extract as a Excel File -- current-date-and-time.xls
		JButton btnToExcel = new JButton("To Excel");
		btnToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Start.sqlToExcel(excelSql, excelFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnToExcel.setBounds(262, 36, 117, 24);
		contentPane.add(btnToExcel);

		table.setComponentPopupMenu(popup);
		
		/*JLabel lblChartIcon = new JLabel("New label");
		
		lblChartIcon.setIcon(new ImageIcon("..\\\\safato-cli-bc247b13f1e8\\images\\64_64_empty-cart-dark.png"));
		lblChartIcon.setBounds(546, 28, 57, 39);
		contentPane.add(lblChartIcon);
		
		lblChartSize = new JLabel("N");
		lblChartSize.setBounds(567, 40, 35, 10);
		contentPane.add(lblChartSize);*/

		// Right Click PopUp Menu-- Start
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopUp(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopUp(e);
			}
		});

	}

	// RightClick PopUp Menu-- End
	public ArrayList getList(String sql, int index) {
		ArrayList al = new ArrayList();
		ResultSet rs;
		try {
			rs = login.stmt.executeQuery(sql);
			while (rs.next()) {
				al.add((String) rs.getObject(index));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return al;
	}

	/**
	 * @param sql
	 *            -- executes sql - generally for "search"
	 * @return returns a ResultSet
	 */
	public ResultSet find(String sql) {
		ResultSet rs = null;
		try {
			rs = Start.stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * @param combo
	 *            -- name of ComboBox which will be field
	 * @param al
	 *            -- ArrayList which will be elements of ComboBox
	 * 
	 */
	public void fillComboBox(JComboBox combo, ArrayList al) {
		combo.removeAllItems();
		for (int i = 0; i < al.size(); i++) {
			combo.addItem(al.get(i));
		}
	}


	public void showPopUp(MouseEvent e) {
		

		// Right mouse click
		if (e.isPopupTrigger()) {
			// get the coordinates of the mouse click
			Point p = e.getPoint();

			// get the row index that contains that coordinate
			rowNumber = table.rowAtPoint(p);

			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = table.getSelectionModel();

			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one
			// row.
			model.setSelectionInterval(rowNumber, rowNumber);

			if (comboBox.getSelectedIndex() == 0
					&& filterField.getText().equals("")) {
				selectedPID = (String) table.getModel()
						.getValueAt(rowNumber, 1);
				System.out.println("" + selectedPID);
				//JOptionPane.showMessageDialog(null, "" + selectedPID);
			} else {
				selectedId = (int) table.getModel().getValueAt(rowNumber, 0);
				System.out.println("" + selectedId);
			}

			popup = new JPopupMenu();
			JMenuItem addMenu = new JMenuItem("Add New");
			JMenuItem deleteMenu = new JMenuItem("Delete");
			JMenuItem editMenu = new JMenuItem("Edit");
			JMenuItem sendMenu = new JMenuItem("Send Demo");
			JMenuItem receiveMenu = new JMenuItem("Receive Demo");
			JMenuItem addToDemoMenu = new JMenuItem("Add to Demo Cart");

			addMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
				}
			});

			deleteMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
					delete.id = selectedId;
					delete.btnFind.doClick();
					delete.setVisible(true);
				}
			});

			editMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
					edit.id = selectedId;
					fillComboBox(edit.getComboBoxProduct(),
							getList("select * from product", 2));
					fillComboBox(edit.getComboBoxLocation(),
							getList("select * from location", 2));

					edit.setLocationRelativeTo(null);
					edit.btnNewButton.doClick();
					edit.setVisible(true);
				}
			});

			sendMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
				}
			});

			receiveMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
					receive.inid = selectedId;
					receive.setLocationRelativeTo(null);
					receive.btnFind.doClick();
					receive.setVisible(true);
				}
			});

			addToDemoMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
					int dbLocalId = (int) table.getModel().getValueAt(rowNumber, 0);
					
					demoChartArList.add(dbLocalId);
					lblChartSize.setText(demoChartArList.size()+"");
					
					System.out.println(((JMenuItem) e.getSource()).getText().toString()+" test "+dbLocalId);
				}
			});

			switch (comboBox.getSelectedIndex()) {
			case 0: // show default display table
				//popup.add(deleteMenu);
				//popup.add(editMenu);
				//popup.add(sendMenu);
				//popup.add(addToDemoMenu);
				break;
			case 1: // show at lab
				popup.add(deleteMenu);
				popup.add(editMenu);
				popup.add(sendMenu);
				popup.add(addToDemoMenu);
				break;
			case 2: // show items at demo
				popup.add(receiveMenu);
				popup.add(deleteMenu);
				popup.add(editMenu);
				break;
			case 3: // show expired demo items
				popup.add(receiveMenu);
				popup.add(deleteMenu);
				popup.add(editMenu);
				break;
			case 4: // show full inventory
				popup.add(deleteMenu);
				popup.add(editMenu);
				//popup.add(editMenu).setEnabled(false);
				break;
			}
			
			popup.show(e.getComponent(), e.getX(), e.getY());

		}

	}
}
