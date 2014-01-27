import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;


public class Delete extends JFrame {

	JFrame dialog;

	private JPanel contentPane;
	private JTextField textField;
	MainFrame mainframe;
	private JComboBox comboBox;
	JTextArea message;
	JTextArea category;
	JTextArea description;
	JTextArea product;
	JButton btnFind;

	public int id;

	/**
	 * Create the frame.
	 */
	public Delete(MainFrame mf) {
		setTitle("Delete Item");
		mainframe = mf;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog = new JFrame("Dialog");
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 309, 352);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		comboBox = new JComboBox();
		comboBox.setBounds(89, 173, 149, 20);
		contentPane.add(comboBox);

		JLabel lblReason = new JLabel("Reason:");
		lblReason.setBounds(10, 176, 69, 14);
		contentPane.add(lblReason);

		btnFind = new JButton("Find");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String sql = "select c.name, p.p_code, i.notes, i.sn, i.id from inventory i, product p, category c where c.id = p.c_id AND p.id=i.p_id AND i.sn LIKE '%"+ textField.getText() +"%' AND i.availability = 1";
				
				
				try {
					
					ResultSet rs = mainframe.login.stmt.executeQuery(sql);
					int rowcount = 0;

					if(rs.last()){
						rowcount = rs.getRow();
						rs.beforeFirst();
					}

					if(rs.next() && rowcount == 1){
						category.setText((String) rs.getObject(1));
						product.setText((String) rs.getObject(2));
						description.setText((String) rs.getObject(3));
						textField.setText((String) rs.getObject(4));
						id = (int) rs.getObject(5);
						message.setText("Found! \n If you want to remove this item, click delete button.");
					}else if(rowcount >1 && textField.getText().compareTo("") != 0){
						message.setText("More than one result!\nPlease be more specific.");
					}else if(textField.getText().compareTo("") == 0){
						message.setText("Please enter an SN!");
					}else{
						message.setText("Not found!");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFind.setBounds(231, 11, 61, 23);
		contentPane.add(btnFind);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ENTER){
					btnFind.doClick();
				}
			}
		});
		textField.setBounds(89, 12, 136, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblSn = new JLabel("SN:");
		lblSn.setBounds(10, 15, 21, 14);
		contentPane.add(lblSn);

		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setBounds(10, 43, 69, 14);
		contentPane.add(lblCategory);

		JLabel lblNewLabel = new JLabel("Product:");
		lblNewLabel.setBounds(10, 68, 69, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Description:");
		lblNewLabel_1.setBounds(10, 100, 69, 14);
		contentPane.add(lblNewLabel_1);

		JButton btnDeleteNow = new JButton("DELETE");
		btnDeleteNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs = null;
				int n = JOptionPane.showConfirmDialog(
						dialog,
						"Are you sure to delete this?",
						"Confirm Delete Operation",
						JOptionPane.YES_NO_OPTION);

				if(n == 0){
					String sql1 = "select id from io_type where name LIKE '%" + comboBox.getSelectedItem() + "%'";
					try {
						rs = mainframe.login.stmt.executeQuery(sql1);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if(rs.next()){
							int io_type = (int) rs.getObject(1);

							String sql2 = "insert into inventory_detail (i_id, io_type, date) " +
									"values ('"+id+"',"+io_type+", NOW())";
							String sql3 = "update inventory set availability = null where id=" + id;
							System.out.println(sql2);
							System.out.println(sql3);
							mainframe.login.stmt.execute(sql2);
							mainframe.login.stmt.execute(sql3);
							message.setText("Deleted from inventory and added inventory_details!");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					textField.setText("");
					category.setText("");
					product.setText("");
					description.setText("");
				}else
					message.setText("Not deleted!");
			}
		});
		btnDeleteNow.setBounds(143, 204, 149, 42);
		contentPane.add(btnDeleteNow);

		description = new JTextArea();
		description.setEditable(false);
		description.setBounds(89, 99, 207, 52);
		contentPane.add(description);

		category = new JTextArea();
		category.setEditable(false);
		category.setBounds(89, 38, 207, 20);
		contentPane.add(category);

		product = new JTextArea();
		product.setEditable(false);
		product.setBounds(89, 63, 207, 20);
		contentPane.add(product);

		message = new JTextArea();
		message.setEditable(false);
		message.setForeground(Color.RED);
		message.setBackground(SystemColor.window);
		message.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		message.setWrapStyleWord(true);
		message.setLineWrap(true);
		message.setColumns(5);
		message.setText("Please enter an SN then click Find button to delete a specific product.");
		message.setRows(5);
		message.setBounds(10, 248, 286, 76);
		contentPane.add(message);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setText("");
				category.setText("");
				product.setText("");
				description.setText("");
				dispose();
			}
		});
		btnCancel.setBounds(10, 204, 101, 42);
		contentPane.add(btnCancel);
	}
	public JComboBox getComboBox() {
		return comboBox;
	}
}
