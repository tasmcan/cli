import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SendDemoForm extends JFrame {

	private JPanel contentPane;
	SendDemo senddemo;
	CartFrame cartF;
	private JTextField senderF;
	private JTextField organizationF;
	private JTextField receiverF;
	private JTextField email1F;
	private JTextField email2F;
	JDateChooser expdateF;
	JDateChooser senddateF;
	JTextArea notesF;
	JFrame dialog;
	public boolean fromCart = false;

	/**
	 * Create the frame.
	 */
	public SendDemoForm(JFrame snddm) {
		
		if(snddm instanceof SendDemo){
			senddemo = (SendDemo) snddm;
		}else if(snddm instanceof CartFrame){
			cartF = (CartFrame) snddm;
		}
		setTitle("Send Demo - Finish - ID: ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 477, 300);
		dialog = new JFrame("Dialog");
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("Send date:");
		label.setBounds(10, 11, 91, 14);
		contentPane.add(label);

		JLabel lblExpireDate = new JLabel("Expire date:");
		lblExpireDate.setBounds(10, 36, 91, 14);
		contentPane.add(lblExpireDate);

		JLabel label_2 = new JLabel("Sender:");
		label_2.setBounds(10, 59, 89, 14);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("Organization:");
		label_3.setBounds(235, 36, 74, 14);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("Receiver:");
		label_4.setBounds(235, 11, 74, 14);
		contentPane.add(label_4);
		expdateF = new JDateChooser();
		expdateF.setBounds(111, 30, 114, 20);
		contentPane.add(expdateF);

		senddateF = new JDateChooser();
		senddateF.setBounds(111, 5, 114, 20);
		contentPane.add(senddateF);

		JLabel label_5 = new JLabel("E-mail 2:");
		label_5.setBounds(235, 82, 56, 14);
		contentPane.add(label_5);

		JLabel label_6 = new JLabel("E-mail 1:");
		label_6.setBounds(235, 59, 68, 14);
		contentPane.add(label_6);

		JButton btnSendFinish = new JButton("Send & Finish");
		btnSendFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fromCart){
					java.util.Date utildatesend = senddateF.getDate();
					java.util.Date utildateexp = expdateF.getDate();
					java.sql.Date datesend = new java.sql.Date(utildatesend
							.getTime());
					java.sql.Date dateexp = new java.sql.Date(utildateexp
							.getTime());
					String sender = senderF.getText().trim();
					String receiver = receiverF.getText().trim();
					String organization = organizationF.getText().trim();
					String email1 = email1F.getText().trim();
					String email2 = email2F.getText().trim();
					String notes = notesF.getText().trim();

					if (dateexp.compareTo(datesend) <= 0) {
						JOptionPane
								.showMessageDialog(
										null,
										"Expiration date cannot be before than or equal to send date!",
										"Date error!",
										JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String sql1 = "insert into movement (i_id, io, senddate, promiseddate, email1, email2, sender, receiver, organization, notes) "
							+ "values (?, 0, ?, ?, ?, ?, ?, ?, ?, ?)";
					String sql2 = "update inventory set availability = 0 where id = ?";

					try {
						PreparedStatement statement1 = Start.connection
								.prepareStatement(sql1);
						PreparedStatement statement2 = Start.connection
								.prepareStatement(sql2);
						for(int i=0; i<MainFrame.demoChartArList.size(); i++){

							statement1.setInt(1, MainFrame.demoChartArList.get(i));
							statement1.setDate(2, datesend);
							statement1.setDate(3, dateexp);
							statement1.setString(4, email1);
							statement1.setString(5, email2);
							statement1.setString(6, sender);
							statement1.setString(7, receiver);
							statement1.setString(8, organization);
							statement1.setString(9, notes);
							
							statement1.addBatch();
							
							statement2.setInt(1, MainFrame.demoChartArList.get(i));
							
							statement2.addBatch();
							
							System.out.println("demo is sent: " + i);
						}

						statement1.executeBatch();
						statement2.executeBatch();
						
						Start.msgbox("Demo is sent successfully!");
						MainFrame.demoChartArList.clear();
						MainFrame.lblChartSize.setText("0");
						cartF.checkboxList.clear();
						
						dispose();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
				}else{
					java.util.Date utildatesend = senddateF.getDate();
					java.util.Date utildateexp = expdateF.getDate();
					java.sql.Date datesend = new java.sql.Date(utildatesend
							.getTime());
					java.sql.Date dateexp = new java.sql.Date(utildateexp
							.getTime());
					String sender = senderF.getText().trim();
					String receiver = receiverF.getText().trim();
					String organization = organizationF.getText().trim();
					String email1 = email1F.getText().trim();
					String email2 = email2F.getText().trim();
					String notes = notesF.getText().trim();

					if (dateexp.compareTo(datesend) <= 0) {
						JOptionPane
								.showMessageDialog(
										null,
										"Expiration date cannot be before than or equal to send date!",
										"Date error!",
										JOptionPane.ERROR_MESSAGE);
						return;
					}

					String sql1 = "insert into movement (i_id, io, senddate, promiseddate, email1, email2, sender, receiver, organization, notes) "
							+ "values (?, 0, ?, ?, ?, ?, ?, ?, ?, ?)";
					String sql2 = "update inventory set availability = 0 where id = ?";
					try {
						PreparedStatement statement1 = Start.connection
								.prepareStatement(sql1);
						PreparedStatement statement2 = Start.connection
								.prepareStatement(sql2);
						statement1.setInt(1, senddemo.inid);
						statement1.setDate(2, datesend);
						statement1.setDate(3, dateexp);
						statement1.setString(4, email1);
						statement1.setString(5, email2);
						statement1.setString(6, sender);
						statement1.setString(7, receiver);
						statement1.setString(8, organization);
						statement1.setString(9, notes);

						statement2.setInt(1, senddemo.inid);

						statement1.executeUpdate();
						statement2.executeUpdate();
						
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					int n = JOptionPane
							.showConfirmDialog(
									dialog,
									"Your demo entry is saved! Do you want to send another item for demo?",
									"One more item?",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);

					if (n == 0) {
						senddateF.setDate(null);
						expdateF.setDate(null);
						senderF.setText("");
						receiverF.setText("");
						organizationF.setText("");
						email1F.setText("");
						email2F.setText("");
						notesF.setText("");
						dispose();

						senddemo.category.setText("");
						senddemo.description.setText("");
						senddemo.product.setText("");
						senddemo.textField.setText("");
						senddemo.setVisible(true);
					} else {
						dispose();
					}
				}
			}
		});
		btnSendFinish.setBounds(10, 132, 181, 58);
		contentPane.add(btnSendFinish);

		senderF = new JTextField();
		senderF.setBounds(111, 56, 114, 20);
		contentPane.add(senderF);
		senderF.setColumns(10);

		organizationF = new JTextField();
		organizationF.setBounds(316, 33, 135, 20);
		contentPane.add(organizationF);
		organizationF.setColumns(10);

		receiverF = new JTextField();
		receiverF.setBounds(316, 8, 135, 20);
		contentPane.add(receiverF);
		receiverF.setColumns(10);

		email1F = new JTextField();
		email1F.setBounds(316, 56, 135, 20);
		contentPane.add(email1F);
		email1F.setColumns(10);

		email2F = new JTextField();
		email2F.setBounds(316, 79, 135, 20);
		contentPane.add(email2F);
		email2F.setColumns(10);

		notesF = new JTextArea();
		notesF.setLineWrap(true);
		notesF.setBounds(201, 132, 250, 119);
		contentPane.add(notesF);

		JLabel lblNewLabel = new JLabel("Notes:");
		lblNewLabel.setBounds(201, 107, 46, 14);
		contentPane.add(lblNewLabel);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				senddemo.setVisible(true);
			}
		});
		btnBack.setBounds(12, 228, 89, 23);
		contentPane.add(btnBack);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(102, 228, 89, 23);
		contentPane.add(btnCancel);

		java.util.Date utilDate = new java.util.Date();
		senddateF.setDate(utilDate);

		expdateF.setDate(new Date(utilDate.getTime()
				+ (14 * 24 * 60 * 60 * 1000)));
	}
}
