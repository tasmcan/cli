import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class CartFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	DefaultTableModel dtm = new DefaultTableModel();
	MainFrame mainframe;
	public ArrayList<JCheckBox> checkboxList = new ArrayList<JCheckBox>();
	SendDemoForm senddemo = new SendDemoForm(this);
	
	public int chartSize = 0;
	/**
	 * Create the frame.
	 */
	public CartFrame(MainFrame mf) {
		mainframe = mf;
		senddemo.fromCart = true;
		chartSize = mainframe.demoChartArList.size();
		System.out.println("" + chartSize);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 501, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 443, 389);
		contentPane.add(scrollPane);
		
		table = new JTable(dtm);
		scrollPane.setViewportView(table);
		
		JButton btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<chartSize; i++){
					if(checkboxList.get(i).isSelected()){
						System.out.println(i + "deleted");
						
						
						
						contentPane.remove(checkboxList.get(i));
						contentPane.validate();
						contentPane.repaint();
						
						//remove deleted items for good from demoChart
						int blabla = mainframe.demoChartArList.remove(i);
						System.out.println("demochart silinen" + blabla);
						checkboxList.remove(i);
						dtm.removeRow(i);
						
						//refresh
						chartSize = mainframe.demoChartArList.size();
						refreshCheckboxes();
					}
				}
			}
		});
		btnDeleteSelected.setBounds(334, 411, 119, 38);
		contentPane.add(btnDeleteSelected);
		
		JButton btnNewButton = new JButton("Checkout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int i=0; i<chartSize; i++){
					int tmp = mainframe.demoChartArList.get(i);
					System.out.println("send to demo:" + tmp);
				}
				
				senddemo.setLocationRelativeTo(null);
				senddemo.setVisible(true);
			}
		});
		btnNewButton.setBounds(10, 411, 128, 38);
		contentPane.add(btnNewButton);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(148, 411, 73, 38);
		contentPane.add(btnClose);
		
		refreshCheckboxes();

	}
	
	public void refreshCheckboxes(){
		int y = 10;
		for(int i=0; i< chartSize; i++){
			y=y+17;
			checkboxList.add(new JCheckBox());
			checkboxList.get(i).setBounds(456, y, 20, 15);
			contentPane.add(checkboxList.get(i));
			System.out.println("checkbox added");
		}
	}
}

