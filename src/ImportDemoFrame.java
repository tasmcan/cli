import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;


public class ImportDemoFrame extends JFrame {

	private JPanel contentPane;
	MainFrame mainframe;
	public JTable table;
	DefaultTableModel dtm = new DefaultTableModel();
	 private File file;
	 Vector header = new Vector();
	 Vector data = new Vector();

	
	public ImportDemoFrame(MainFrame mf) {
		mainframe = mf;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 240);
		contentPane.add(scrollPane);
		
		table = new JTable(dtm);
		scrollPane.setViewportView(table);
	}
	 
}
