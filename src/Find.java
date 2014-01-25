import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Find extends JFrame {

	private JPanel contentPane;
	MainFrame mainframe;
	private JTextField sn;
	private JTextField category;
	private JTextField product;

	/**
	 * Create the frame.
	 */
	public Find(MainFrame mf) {
		mainframe = mf;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 225, 170);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("S/N:");
		lblNewLabel.setBounds(10, 11, 55, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setBounds(10, 36, 55, 14);
		contentPane.add(lblCategory);
		
		JLabel lblProduct = new JLabel("Product:");
		lblProduct.setBounds(10, 61, 55, 14);
		contentPane.add(lblProduct);
		
		sn = new JTextField();
		sn.setBounds(75, 8, 117, 20);
		contentPane.add(sn);
		sn.setColumns(10);
		
		category = new JTextField();
		category.setBounds(75, 33, 117, 20);
		contentPane.add(category);
		category.setColumns(10);
		
		product = new JTextField();
		product.setBounds(75, 58, 117, 20);
		contentPane.add(product);
		product.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select * from inventory where";
				Start.showTable(sql, mainframe.dtm);
			}
		});
		btnSearch.setBounds(75, 89, 117, 32);
		contentPane.add(btnSearch);
	}
}
