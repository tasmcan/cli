import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CartFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	DefaultTableModel dtm = new DefaultTableModel();
	MainFrame mainframe;
	public ArrayList<JCheckBox> checkboxList = new ArrayList<JCheckBox>();
	SendDemoForm senddemo = new SendDemoForm(this);
	JPopupMenu popup;
	int rowNumber;
	int selectedId = -1;

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
		table.setComponentPopupMenu(popup);
		scrollPane.setViewportView(table);
		
		// Right Click PopUp Menu-- Start
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopUp(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopUp(e);
			}
		});

		/*JButton btnDeleteSelected = new JButton("Delete Selected");
		
		 * btnDeleteSelected.addActionListener(new ActionListener() { public
		 * void actionPerformed(ActionEvent e) { System.out.println("chartsize"
		 * + chartSize); ArrayList <Integer>todelete = new ArrayList<Integer>();
		 * int count = 0; for(int i=0; i<chartSize; i++){
		 * if(checkboxList.get(i).isSelected()){
		 * mainframe.demoChartArList.remove(i); todelete.add(i); count++; } }
		 * 
		 * for(int i=0; i<todelete.size(); i++){ System.out.println(i +
		 * "deleted");
		 * 
		 * contentPane.remove(checkboxList.get(todelete.get(i)));
		 * 
		 * checkboxList.remove(i);
		 * 
		 * dtm.removeRow(todelete.get(i));
		 * 
		 * contentPane.validate(); contentPane.repaint();
		 * 
		 * //refresh chartSize = mainframe.demoChartArList.size();
		 * refreshCheckboxes(); } } });
		 

		btnDeleteSelected.setBounds(334, 411, 119, 38);
		contentPane.add(btnDeleteSelected);*/

		JButton btnNewButton = new JButton("Checkout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < chartSize; i++) {
					int tmp = mainframe.demoChartArList.get(i);
					System.out.println("send to demo:" + tmp);
				}

				if(chartSize == 0){
					Start.msgbox("Demo chart is empty!");
					dispose();
				}else{
					senddemo.setLocationRelativeTo(null);
					senddemo.setVisible(true);
				}
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

		//refreshCheckboxes();

	}

	public void showPopUp(MouseEvent e) {

		// Right mouse click
		if (e.isPopupTrigger()) {
			// get the coordinates of the mouse click
			Point p = e.getPoint();

			// get the row index that contains that coordinate
			rowNumber = table.rowAtPoint(p);

			// Get the ListSelectionModel of the JTable
			ListSelectionModel model = table.getSelectionModel();

			// set the selected interval of rows. Using the "rowNumber"
			// variable for the beginning and end selects only that one
			// row.
			model.setSelectionInterval(rowNumber, rowNumber);

			selectedId = (int) table.getModel().getValueAt(rowNumber, 0);
			System.out.println("" + selectedId);

			popup = new JPopupMenu();
			JMenuItem deleteMenu = new JMenuItem("Delete From Cart");

			deleteMenu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(((JMenuItem) e.getSource()).getText()
							.toString());
					dtm.removeRow(rowNumber);
					mainframe.demoChartArList.remove(rowNumber);
					chartSize = mainframe.demoChartArList.size();
					System.out.println("" + chartSize);
					mainframe.lblChartSize.setText(chartSize + "");
				}
			});

			popup.add(deleteMenu);

			popup.show(e.getComponent(), e.getX(), e.getY());

		}

	}

	public void refreshCheckboxes() {
		int y = 10;
		for (int i = 0; i < chartSize; i++) {
			y = y + 17;
			checkboxList.add(new JCheckBox());
			checkboxList.get(i).setBounds(456, y, 20, 15);
			contentPane.add(checkboxList.get(i));
			System.out.println("checkbox added");
		}
	}
}
