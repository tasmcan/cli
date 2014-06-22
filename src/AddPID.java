import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import java.awt.SystemColor;

public class AddPID extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JComboBox comboBox;
	Add addf;

	JTextArea textArea;
	JTextArea message;

	/**
	 * Create the frame.
	 */
	public AddPID(Add a) {
		setTitle("Add Product");
		addf = a;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 348, 278);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		comboBox = new JComboBox();
		comboBox.setBounds(96, 33, 238, 20);
		contentPane.add(comboBox);

		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setForeground(Color.RED);
		lblCategory.setBounds(10, 36, 76, 14);
		contentPane.add(lblCategory);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(10, 69, 76, 14);
		contentPane.add(lblDescription);

		JLabel lblNewLabel = new JLabel("Product Code:");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 83, 14);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(96, 8, 238, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnAddProductCode = new JButton("Add Product Code");
		btnAddProductCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String p_code = textField.getText().trim();
				String sql2 = "select * from product where p_code LIKE '%" + p_code + "%'";
				ResultSet rs_exists;
				try {
					rs_exists = Start.stmt.executeQuery(sql2);

					if (p_code.length() != 0 && rs_exists.next() != true) {

						String sql = "insert into product (p_code, c_id, description) values (?, ?, ?)";
						try {
							PreparedStatement statement = Start.connection
									.prepareStatement(sql);
							statement.setString(1, p_code);
							statement.setInt(2, comboBox.getSelectedIndex() + 1);
							statement.setString(3, textArea.getText().trim());
							statement.executeUpdate();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						addf.mainframe.fillComboBox(addf.getComboBox(),
								addf.mainframe.getList("select * from product order by p_code",
										2));
						addf.mainframe.fillComboBox(addf.mainframe.edit.getComboBoxProduct(),
								addf.mainframe.getList("select * from product order by p_code",
										2));
						message.setText("Product ID added to the database!");
					} else if (p_code.length() == 0) {
						message.setText("You should enter a product code!");
					} else{
						rs_exists.beforeFirst();
						
						if(rs_exists.next()){
							message.setText("This product already exists in the database or you should be more specific. Please check product list first to see if it exists.");
						}else
							message.setText("There was an unknown problem.");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAddProductCode.setBounds(135, 144, 190, 34);
		contentPane.add(btnAddProductCode);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(96, 64, 236, 71);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		
		message = new JTextArea();
		message.setText("Enter the product code & description and select the category then click Add Product Code button.");
		message.setBounds(10, 190, 320, 60);
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
		btnCancel.setBounds(10, 144, 100, 34);
		contentPane.add(btnCancel);

	}

	public JComboBox getComboBox() {
		return comboBox;
	}
}
