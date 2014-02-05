import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;


public class ReceiveDemo extends JFrame {

	private JPanel contentPane;
	private JTextField sn;
	private JTextField organization;
	JTextArea message;
	boolean found = false;
	int inid;
	int mid;
	
	JLabel sender;
	JLabel receiver;
	JLabel senddate;
	
	JTextArea notes;
	JLabel lblExpectedDate;
	JLabel expecteddate;
	
	JFrame dialog;
	
	JButton btnFind;

	/**
	 * Create the frame.
	 */
	public ReceiveDemo() {
		setTitle("Receive Demo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 327, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		dialog = new JFrame("Dialog");
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JButton btnCancel = new JButton("CLOSE");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(10, 259, 89, 57);
		contentPane.add(btnCancel);

		JLabel lblSn = new JLabel("SN:");
		lblSn.setBounds(10, 11, 69, 14);
		contentPane.add(lblSn);

		sn = new JTextField();
		sn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFind.doClick();
			}
		});
		sn.setBounds(99, 8, 151, 20);
		contentPane.add(sn);
		sn.setColumns(10);

		btnFind = new JButton("FIND");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "";
				if(sn.getText().compareTo("") != 0 && organization.getText().compareTo("") == 0){
					sql = "select i.id, i.sn, m.organization, m.sender, m.receiver, m.senddate, m.promiseddate, m.id, m.notes from inventory i, movement m where i.id = m.i_id AND i.sn LIKE '%"+ sn.getText().trim() +"%' AND m.io=0";
				}else if(sn.getText().compareTo("") == 0 && organization.getText().compareTo("") != 0){
					sql = "select i.id, i.sn, m.organization, m.sender, m.receiver, m.senddate, m.promiseddate, m.id, m.notes from inventory i, movement m where i.id = m.i_id AND m.organization LIKE '%"+ organization.getText().trim() +"%' AND m.io=0";
				}else if(sn.getText().compareTo("") != 0 && organization.getText().compareTo("") !=0){
					sql = "select i.id, i.sn, m.organization, m.sender, m.receiver, m.senddate, m.promiseddate, m.id, m.notes from inventory i, movement m where i.id = m.i_id AND i.sn LIKE '%"+ sn.getText().trim() +"%' AND m.organization LIKE '%"+ organization.getText().trim() +"%' AND m.io=0";
				}else if(sn.getText().compareTo("") == 0 && organization.getText().compareTo("") == 0){
					message.setText("Please enter something!");
					return;
				}
				
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
						sn.setText((String)rs.getObject(2));
						organization.setText((String) rs.getObject(3));
						sender.setText((String) rs.getObject(4));
						receiver.setText((String) rs.getObject(5));
						senddate.setText(((Date) rs.getObject(6)).toString());
						expecteddate.setText(((Date) rs.getObject(7)).toString());
						message.setText("Found! \n If this item is received, click RECEIVED button.");
						inid = (Integer) rs.getObject(1);
						mid = (Integer) rs.getObject(8);
						notes.setText((String)rs.getObject(9));
					}else if(rowcount >1){
						message.setText("More than one result!\nPlease be more specific.");
					}else{
						message.setText("Not found!");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFind.setBounds(252, 7, 69, 49);
		contentPane.add(btnFind);

		JLabel lblOrganization = new JLabel("Organization:");
		lblOrganization.setBounds(10, 42, 89, 14);
		contentPane.add(lblOrganization);

		organization = new JTextField();
		organization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFind.doClick();
			}
		});
		organization.setBounds(99, 36, 151, 20);
		contentPane.add(organization);
		organization.setColumns(10);

		message = new JTextArea();
		message.setText("Please find the item first then click returned button.");
		message.setEditable(false);
		message.setWrapStyleWord(true);
		message.setBackground(SystemColor.window);
		message.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		message.setForeground(Color.RED);
		message.setLineWrap(true);
		message.setBounds(10, 327, 298, 62);
		contentPane.add(message);

		JLabel lblSender = new JLabel("Sender:");
		lblSender.setBounds(10, 67, 69, 14);
		contentPane.add(lblSender);

		JLabel lblReceiver = new JLabel("Receiver:");
		lblReceiver.setBounds(10, 92, 69, 14);
		contentPane.add(lblReceiver);

		sender = new JLabel("");
		sender.setBounds(99, 68, 151, 14);
		contentPane.add(sender);

		receiver = new JLabel("");
		receiver.setBounds(99, 92, 151, 14);
		contentPane.add(receiver);

		JLabel lblSendDate = new JLabel("Send date:");
		lblSendDate.setBounds(10, 117, 74, 14);
		contentPane.add(lblSendDate);

		lblExpectedDate = new JLabel("Expire date:");
		lblExpectedDate.setBounds(10, 142, 74, 14);
		contentPane.add(lblExpectedDate);

		senddate = new JLabel("");
		senddate.setBounds(99, 117, 151, 14);
		contentPane.add(senddate);

		JButton btnReturned = new JButton("Returned!");
		btnReturned.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!found){
					message.setText("First search and find the item you receive...");
					return;
				}
				String sql1 = "update movement set io = 1, receivedate = NOW(), notes = ? where id = ?";
				String sql2 = "update inventory set availability = 1 where id = ?";
				
				try {
					PreparedStatement statement1 = Start.connection.prepareStatement(sql1);
					statement1.setString(1, notes.getText());
					statement1.setInt(2, mid);
					
					PreparedStatement statement2 = Start.connection.prepareStatement(sql2);
					statement2.setInt(1, inid);
					
					statement1.executeUpdate();
					statement2.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println(sql1);
				System.out.println(sql2);
				
				int n = JOptionPane.showConfirmDialog(
					    dialog,
					    "Your demo entry is saved! Do you want to receive another item from demo?",
					    "One more item?",
					    JOptionPane.YES_NO_OPTION);
				
				if(n == 0){
					sender.setText("");
					receiver.setText("");
					organization.setText("");
					senddate.setText("");
					expecteddate.setText("");
					notes.setText("");
					message.setText("Please search for the item to mark it returned.");
					
				}else{
					dispose();
				}
			}
		});
		btnReturned.setBounds(123, 259, 185, 57);
		contentPane.add(btnReturned);
		
		notes = new JTextArea();
		notes.setBounds(89, 167, 219, 83);
		contentPane.add(notes);
		
		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setBounds(10, 167, 69, 14);
		contentPane.add(lblNotes);
		
		expecteddate = new JLabel("");
		expecteddate.setBounds(99, 142, 151, 14);
		contentPane.add(expecteddate);
	}
}
