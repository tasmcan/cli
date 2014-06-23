import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.border.LineBorder;


public class SendDemo extends JFrame {

	private JPanel contentPane;
	SendDemoForm form = new SendDemoForm(this);
	JTextField textField;
	JTextArea message;
	JTextArea description;
	JTextArea category;
	JTextArea product;
	boolean found = false;
	public int inid = -1;
	JButton button;
	
	/**
	 * Create the frame.
	 */
	public SendDemo() {
		setTitle("Send Demo - Find");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 302, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("SN:");
		label.setBounds(10, 16, 21, 14);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.doClick();
			}
		});
		
		textField.setColumns(10);
		textField.setBounds(86, 13, 136, 20);
		contentPane.add(textField);
		
		button = new JButton("Find");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select c.name, p.p_code, i.notes, i.sn, i.id from inventory i, product p, category c where c.id = p.c_id AND p.id=i.p_id AND i.availability = 1 AND i.sn LIKE '%"+ textField.getText() +"%' ";
				//System.out.println(sql);
				try {
					ResultSet rs = Start.stmt.executeQuery(sql);
					int rowcount = 0;
					
					if(rs.last()){
						rowcount = rs.getRow();
						rs.beforeFirst();
					}
					
					if(rs.next() && rowcount == 1){
						found = true;
						category.setText((String) rs.getObject(1));
						product.setText((String) rs.getObject(2));
						description.setText((String) rs.getObject(3));
						textField.setText((String) rs.getObject(4));
						message.setText("Found! \n If you want to send this item to demo, click next button.");
						inid = (Integer) rs.getObject(5);
					}else if(rowcount >1 && textField.getText().compareTo("") != 0){
						message.setText("More than one result!\nPlease be more specific.");
					}else if(textField.getText().compareTo("") == 0){
						message.setText("Please enter an SN!");
					}else{
						message.setText("It does not exist or not in the inventory currently.");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(232, 13, 61, 23);
		contentPane.add(button);
		
		category = new JTextArea();
		category.setBackground(SystemColor.window);
		category.setEditable(false);
		category.setBounds(86, 43, 207, 20);
		contentPane.add(category);
		
		JLabel label_1 = new JLabel("Category:");
		label_1.setBounds(10, 44, 69, 14);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("Product:");
		label_2.setBounds(10, 69, 69, 14);
		contentPane.add(label_2);
		
		product = new JTextArea();
		product.setBackground(SystemColor.window);
		product.setEditable(false);
		product.setBounds(86, 68, 207, 20);
		contentPane.add(product);
		
		JLabel label_4 = new JLabel("Description:");
		label_4.setBounds(10, 96, 69, 23);
		contentPane.add(label_4);
		
		description = new JTextArea();
		description.setBackground(SystemColor.window);
		description.setEditable(false);
		description.setBounds(86, 99, 207, 52);
		contentPane.add(description);
		
		message = new JTextArea();
		message.setEditable(false);
		message.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		message.setBackground(SystemColor.window);
		message.setForeground(Color.RED);
		message.setWrapStyleWord(true);
		message.setText("Please enter an SN then click NEXT button to send a specific product for demo purposes.");
		message.setLineWrap(true);
		message.setBounds(10, 245, 273, 76);
		contentPane.add(message);
		
		JButton btnNewButton = new JButton("NEXT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(found){
					form.setVisible(true);
					form.setLocationRelativeTo(null);
					dispose();
				}else{
					message.setText("Please find the specific item first.");
				}
			}
		});
		btnNewButton.setBounds(99, 180, 184, 57);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("CLOSE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(10, 180, 77, 54);
		contentPane.add(btnNewButton_1);
	}
}
