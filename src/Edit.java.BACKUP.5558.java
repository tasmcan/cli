import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.SystemColor;
import java.awt.Color;


public class Edit extends JFrame {

	private JPanel contentPane;
	private JTextField sn;
	private JComboBox product;
	private JComboBox location;
	private JComboBox category;
	MainFrame mainframe;
	JTextArea notes;
	
	public int dbID = -1;
	private JTextArea message;


	/**
	 * Create the frame.
	 */
	public Edit(MainFrame mf) {
		mainframe = mf;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 345, 385);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSn = new JLabel("SN:");
		lblSn.setBounds(10, 11, 46, 14);
		contentPane.add(lblSn);
		
		JLabel lblProduct = new JLabel("Product:");
		lblProduct.setBounds(10, 63, 64, 14);
		contentPane.add(lblProduct);
		
		JLabel lblNewLabel = new JLabel("Notes:");
		lblNewLabel.setBounds(10, 156, 46, 14);
		contentPane.add(lblNewLabel);
		
		sn = new JTextField();
		sn.setBounds(86, 8, 155, 20);
		contentPane.add(sn);
		sn.setColumns(10);
		
		product = new JComboBox();
		product.setBounds(86, 61, 155, 20);
		contentPane.add(product);
		
		JButton btnNewButton = new JButton("Find");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select p.p_code, c.name, i.notes, i.sn, l.loc_code, i.id from inventory i, product p, category c, location l where c.id = p.c_id AND p.id=i.p_id AND i.location = l.id AND i.sn LIKE '%"+ sn.getText() +"%' AND i.availability = 1";
				ResultSet rs = mainframe.find(sql);
				int id;
				
				int rowcount = 0;

				try {
					if(rs.last()){
						rowcount = rs.getRow();
						rs.beforeFirst();
					}


				if(rs.next() && rowcount == 1){
					sn.setText((String) rs.getObject(4));
					product.setSelectedItem( (String) rs.getObject(1));
					category.setSelectedItem( (String) rs.getObject(2));
					location.setSelectedItem((String) rs.getObject(5));
					notes.setText((String) rs.getObject(3));
<<<<<<< HEAD
					
					id = (int) rs.getObject(6);
=======
					id = (Integer) rs.getObject(6);

>>>>>>> f17c0c615a042be8c1f8f6fbd13637cac08e04fa
					message.setText("Found! \n If you want to remove this item, click delete button.");
				}else if(rowcount >1 && sn.getText().compareTo("") != 0){
					message.setText("More than one result!\nPlease be more specific.");
				}else if(sn.getText().compareTo("") == 0){
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
		btnNewButton.setBounds(253, 8, 88, 23);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(86, 153, 201, 87);
		contentPane.add(scrollPane);
		
		notes = new JTextArea();
		scrollPane.setViewportView(notes);
		
		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(253, 33, 88, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnFinish = new JButton("SAVE");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnFinish.setBounds(174, 250, 142, 44);
		contentPane.add(btnFinish);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(21, 247, 117, 47);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel_1 = new JLabel("Location:");
		lblNewLabel_1.setBounds(10, 89, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		location = new JComboBox();
		location.setBounds(86, 85, 155, 27);
		contentPane.add(location);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(253, 57, 88, 29);
		contentPane.add(btnAdd);
		
		JLabel lblLocation = new JLabel("Category:");
		lblLocation.setBounds(10, 37, 61, 16);
		contentPane.add(lblLocation);
		
		category = new JComboBox();
		category.setBounds(86, 31, 155, 27);
		contentPane.add(category);
		
		JButton btnNewButton_2 = new JButton("Add");
		btnNewButton_2.setBounds(253, 84, 88, 29);
		contentPane.add(btnNewButton_2);
		
		message = new JTextArea();
		message.setForeground(Color.RED);
		message.setBackground(SystemColor.window);
		message.setBounds(10, 305, 329, 52);
		contentPane.add(message);
	}
	public JComboBox getComboBox() {
		return product;
	}
	public JComboBox getComboBox2() {
		return location;
	}
}
