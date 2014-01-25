import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Component;


public class About extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
    
    //this is a commit test
	public About() {
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 277, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSafatinventory = new JLabel("CLI (Cisco Lab Inventory)");
		lblSafatinventory.setHorizontalAlignment(SwingConstants.CENTER);
		lblSafatinventory.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblSafatinventory.setBounds(10, 11, 241, 14);
		contentPane.add(lblSafatinventory);
		
		JLabel lblDeveloper = new JLabel("Authors");
		lblDeveloper.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeveloper.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDeveloper.setBounds(10, 93, 241, 14);
		contentPane.add(lblDeveloper);
		
		JLabel lblDeveloperSafaTopal = new JLabel("Software Developer: Safa TOPAL");
		lblDeveloperSafaTopal.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblDeveloperSafaTopal.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeveloperSafaTopal.setBounds(10, 118, 241, 14);
		contentPane.add(lblDeveloperSafaTopal);
		
		JLabel lblProjectManagerCafer = new JLabel("Project Manager: Cafer DURSUN");
		lblProjectManagerCafer.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblProjectManagerCafer.setHorizontalAlignment(SwingConstants.CENTER);
		lblProjectManagerCafer.setBounds(10, 143, 241, 14);
		contentPane.add(lblProjectManagerCafer);
		
		JLabel lblVersion = new JLabel("Version: 0.9.3");
		lblVersion.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setBounds(10, 237, 241, 14);
		contentPane.add(lblVersion);
		
		JLabel lblForFeedbacksContact = new JLabel("For feedbacks, contact via e-mail.");
		lblForFeedbacksContact.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblForFeedbacksContact.setHorizontalAlignment(SwingConstants.CENTER);
		lblForFeedbacksContact.setBounds(10, 212, 241, 14);
		contentPane.add(lblForFeedbacksContact);
		
		JTextArea txtrInventoryControlSoftware = new JTextArea();
		txtrInventoryControlSoftware.setWrapStyleWord(true);
		txtrInventoryControlSoftware.setBackground(SystemColor.window);
		txtrInventoryControlSoftware.setEditable(false);
		txtrInventoryControlSoftware.setLineWrap(true);
		txtrInventoryControlSoftware.setText("Inventory Control System Software \t         for Cisco Demo Labs");
		txtrInventoryControlSoftware.setBounds(20, 37, 241, 44);
		contentPane.add(txtrInventoryControlSoftware);
	}
}
