import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;


public class AddLoc extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	Add addf;

	JTextArea textArea;
	JTextArea message;

	/**
	 * Create the frame.
	 */
	public AddLoc(Add a) {
		setTitle("Add Product");
		addf = a;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 348, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 38, 76, 14);
		contentPane.add(lblDescription);

		JLabel lblNewLabel = new JLabel("Location:");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 83, 14);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(96, 8, 238, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnAddProductCode = new JButton("Add Location");
		btnAddProductCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String loc_code = textField.getText().trim();
				String sql2 = "select * from location where loc_code LIKE '%" + loc_code + "%'";
				ResultSet rs_exists;
				try {
					rs_exists = Start.stmt.executeQuery(sql2);

					if (loc_code.length() != 0 && rs_exists.next() != true) {

						String sql = "insert into location (loc_code, description) values (?, ?)";
						try {
							PreparedStatement statement = Start.connection
									.prepareStatement(sql);
							statement.setString(1, loc_code);
							statement.setString(2, textArea.getText().trim());
							statement.executeUpdate();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						addf.mainframe.fillComboBox(addf.getComboBox_2(),
								addf.mainframe.getList("select * from location order by loc_code",
										2));
						addf.mainframe.fillComboBox(addf.mainframe.edit.getComboBoxLocation(),
								addf.mainframe.getList("select * from location order by loc_code",
										2));
						message.setText("Location ID is added to the database!");
					} else if (loc_code.length() == 0) {
						message.setText("You should enter a location ID!");
					} else{
						rs_exists.beforeFirst();
						
						if(rs_exists.next()){
							message.setText("This location identifier is already exists in the database.");
						}else
							message.setText("There was an unknown problem.");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAddProductCode.setBounds(144, 116, 190, 34);
		contentPane.add(btnAddProductCode);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(96, 33, 236, 71);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		message = new JTextArea();
		message.setText("Enter the location identifier inside the Lab and a description if necessary.");
		message.setBounds(10, 162, 320, 60);
		contentPane.add(message);
		message.setWrapStyleWord(true);
		message.setBackground(SystemColor.window);
		message.setForeground(Color.RED);
		message.setEditable(false);
		message.setLineWrap(true);
		
		JButton btnCancel = new JButton("CLOSE");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(10, 116, 100, 34);
		contentPane.add(btnCancel);

	}

}
