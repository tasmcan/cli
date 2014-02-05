import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Add extends JFrame {

	private JPanel contentPane;
	private JTextField textField_1;
	AddPID addp = new AddPID(this);
	AddLoc addl = new AddLoc(this);
	MainFrame mainframe;
	private JComboBox comboBox;
	JTextArea textArea;
	private JTextField textField;
	private JTextField textField_4;
	private JTextField textField_5;
	private JComboBox comboBox_1;
	JLabel message;
	private JButton btnCancel;
	private JComboBox comboBox_2;
	JCheckBox chckbxNoSn;

	/**
	 * Create the frame.
	 */
	public Add(MainFrame mf) {
		setTitle("Add Item");
		mainframe = mf;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 444, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Product ID: *");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 23, 91, 14);
		contentPane.add(lblNewLabel);

		JLabel lblSerialNumber = new JLabel("Serial Number: *");
		lblSerialNumber.setForeground(Color.RED);
		lblSerialNumber.setBounds(10, 48, 105, 14);
		contentPane.add(lblSerialNumber);

		JLabel lblDescription = new JLabel("Notes:");
		lblDescription.setBounds(10, 120, 91, 14);
		contentPane.add(lblDescription);

		JButton btnNewButton = new JButton("ADD");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sn = textField_1.getText().trim();
				String product = (String) comboBox.getSelectedItem();
				String location = (String) comboBox_2.getSelectedItem();
				String sql_exists = "select * from inventory where sn LIKE '%"
						+ sn + "%'";
				String sql_location = "select id from location where loc_code LIKE '%"
						+ location + "%'";
				String sql = "select id from product where p_code LIKE '%"
						+ product + "%'";
				ResultSet rs;
				ResultSet rs_exists;
				boolean exists = false;

				try {
					if (chckbxNoSn.isSelected()) {
						sn = "NONE";
						sql_exists = "select * from inventory where sn LIKE '%"
								+ sn + "%'";
					}

					rs = Start.stmt.executeQuery(sql);
					rs_exists = Start.stmt2.executeQuery(sql_exists);

					if (rs_exists.next() && !chckbxNoSn.isSelected()) {
						exists = true;
					} else if (chckbxNoSn.isSelected()) {
						exists = false;
					}

					if (rs.next() && !exists
							&& (sn.length() == 11 || chckbxNoSn.isSelected())) {

						// insert into inventory table first.
						int pid = (Integer) rs.getObject(1);

						rs = Start.stmt.executeQuery(sql_location);

						int loc_id = 0;
						if (rs.next()) {
							loc_id = (Integer) rs.getObject(1);
						}

						sql = "insert into inventory (p_id, sn, notes, location) values ("
								+ pid
								+ ", '"
								+ sn
								+ "', '"
								+ textArea.getText() + "', " + loc_id + ")";

						System.out.println(sql + "");
						Start.stmt.executeUpdate(sql,
								Statement.RETURN_GENERATED_KEYS);

						ResultSet generatedKey = null;
						generatedKey = Start.stmt.getGeneratedKeys();
						
						generatedKey.next();
						int id = generatedKey.getInt(1);

						System.out.println("" + id);
						
						sql = "select id from io_type where name LIKE '%"
								+ comboBox_1.getSelectedItem() + "%'";
						rs = Start.stmt.executeQuery(sql);
						if (rs.next()) {
							int io_type = (Integer) rs.getObject(1);
							Date utilDate = new Date();
							java.sql.Date sqlDate = new java.sql.Date(utilDate
									.getTime());

							sql = "insert into inventory_detail (i_id, io_type, date, sender, organization, receiver) "
									+ "values (?, ?, ?, ?, ?, ?)";
							// System.out.println(sql + "");
							PreparedStatement statement = Start.connection
									.prepareStatement(sql);
							statement.setInt(1, id);
							statement.setInt(2, io_type);
							statement.setDate(3, sqlDate);
							statement.setString(4, textField.getText());
							statement.setString(5, textField_4.getText());
							statement.setString(6, textField_5.getText());
							statement.executeUpdate();
							message.setText("Item with sn id "
									+ sn
									+ " added into inventory and inventory_details tables!");
						}
					} else if (sn.length() != 11) {
						message.setText("S/N must be 11 characters!");
					} else if (exists) {
						System.out.println(sql_exists);
						message.setText("An item with the same S/N already exists in the database!");
					} else
						message.setText("There was an unknown problem.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(172, 225, 266, 44);
		contentPane.add(btnNewButton);

		textField_1 = new JTextField();
		textField_1.setBounds(111, 45, 210, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textArea = new JTextArea();
		textArea.setBounds(113, 119, 325, 44);
		contentPane.add(textArea);

		comboBox = new JComboBox();
		comboBox.setBounds(111, 20, 210, 20);
		contentPane.add(comboBox);

		JButton btnNewButton_1 = new JButton("Add PID");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainframe.fillComboBox(addp.getComboBox(),
						mainframe.getList("select * from category", 2));
				addp.setLocationRelativeTo(null);
				addp.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(321, 20, 117, 23);
		contentPane.add(btnNewButton_1);

		JLabel lblNewLabel_2 = new JLabel("Reason:");
		lblNewLabel_2.setBounds(10, 173, 64, 14);
		contentPane.add(lblNewLabel_2);

		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(111, 170, 123, 21);
		contentPane.add(comboBox_1);

		JLabel lblSender = new JLabel("Sender:");
		lblSender.setBounds(10, 198, 64, 14);
		contentPane.add(lblSender);

		textField = new JTextField();
		textField.setBounds(111, 195, 123, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblReceiver = new JLabel("Organization:");
		lblReceiver.setBounds(246, 175, 91, 14);
		contentPane.add(lblReceiver);

		textField_4 = new JTextField();
		textField_4.setBounds(333, 170, 105, 20);
		contentPane.add(textField_4);
		textField_4.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Receiver:");
		lblNewLabel_4.setBounds(246, 197, 75, 16);
		contentPane.add(lblNewLabel_4);

		textField_5 = new JTextField();
		textField_5.setBounds(333, 194, 105, 20);
		contentPane.add(textField_5);
		textField_5.setColumns(10);

		message = new JLabel(
				"Please fill necessary fields and click ADD button.");
		message.setForeground(Color.RED);
		message.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		message.setBounds(10, 281, 428, 51);
		contentPane.add(message);

		btnCancel = new JButton("CLOSE");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(10, 226, 129, 44);
		contentPane.add(btnCancel);

		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(10, 74, 91, 16);
		contentPane.add(lblLocation);

		comboBox_2 = new JComboBox();
		comboBox_2.setBounds(111, 70, 210, 27);
		contentPane.add(comboBox_2);

		JButton btnNewButton_2 = new JButton("Add Location");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addl.setLocationRelativeTo(null);
				addl.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(321, 69, 117, 29);
		contentPane.add(btnNewButton_2);

		chckbxNoSn = new JCheckBox("No S/N");
		chckbxNoSn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxNoSn.isSelected()) {
					textField_1.setText("");
					textField_1.setEnabled(false);
				} else {
					textField_1.setEnabled(true);
				}
			}
		});
		chckbxNoSn.setBounds(331, 43, 96, 23);
		contentPane.add(chckbxNoSn);
	}

	public JComboBox getComboBox() {
		return comboBox;
	}

	public JComboBox getComboBox_1() {
		return comboBox_1;
	}

	public JComboBox getComboBox_2() {
		return comboBox_2;
	}
}
