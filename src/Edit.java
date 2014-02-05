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
import javax.xml.transform.Result;

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
	MainFrame mainframe;
	JTextArea notes;
    public int id;
	public int dbID = -1;
	private JTextArea message;
    JButton btnFinish;

	/**
	 * Create the frame.
	 */
	public Edit(MainFrame mf) {
		mainframe = mf;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 355, 349);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSn = new JLabel("SN:");
		lblSn.setBounds(10, 11, 46, 14);
		contentPane.add(lblSn);
		
		JLabel lblProduct = new JLabel("Product:");
		lblProduct.setBounds(10, 43, 64, 14);
		contentPane.add(lblProduct);
		
		JLabel lblNewLabel = new JLabel("Notes:");
		lblNewLabel.setBounds(10, 97, 46, 14);
		contentPane.add(lblNewLabel);
		
		sn = new JTextField();
		sn.setBounds(86, 8, 155, 20);
		contentPane.add(sn);
		sn.setColumns(10);
		
		product = new JComboBox();
		product.setBounds(86, 41, 155, 20);
		contentPane.add(product);

		JButton btnNewButton = new JButton("Find");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sql = "select p.p_code, c.name, i.notes, i.sn, l.loc_code, i.id from inventory i, product p, category c, location l where c.id = p.c_id AND p.id=i.p_id AND i.location = l.id AND i.sn LIKE '%"+ sn.getText() +"%'";
				ResultSet rs = mainframe.find(sql);

				int rowcount = 0;

				try {
					if(rs.last()){
						rowcount = rs.getRow();
						rs.beforeFirst();
					}

				if(rs.next() && rowcount == 1){
					sn.setText((String) rs.getObject(4));
					product.setSelectedItem( (String) rs.getObject(1));
					location.setSelectedItem((String) rs.getObject(5));
					
					notes.setText((String) rs.getObject(3));
					id = (Integer) rs.getObject(6);
                    //Save Button Enabled
                     btnFinish.setEnabled(true);
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
		scrollPane.setBounds(86, 97, 255, 105);
		contentPane.add(scrollPane);
		
		notes = new JTextArea();
		scrollPane.setViewportView(notes);
		
		 btnFinish = new JButton("SAVE");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

                int productId=0;
                int locationId =0;

                String newSn = sn.getText();
                String newLocation = location.getSelectedItem().toString();
                String newProductId = product.getSelectedItem().toString();
                String newNotes = notes.getText();

                String getLocationIdSql ="select id from location where loc_code ='"+newLocation+"'";
                String getProductIdSql ="select id from product where p_code ='"+newProductId+"'";

                ResultSet rsLocationId = mainframe.find(getLocationIdSql);
                try {
                    if(rsLocationId.next())
                    {
                        locationId = (Integer)rsLocationId.getObject(1);

                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                ResultSet rsProductId = mainframe.find(getProductIdSql);


                try {
                    if(rsProductId.next())
                    {
                         productId = (Integer)rsProductId.getObject(1);

                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


                String editSql ="Update inventory " +
                                " set p_id= "+productId+", " +
                                 "location="+locationId+", " +
                                  "notes ='"+newNotes+"'"+"," +
                                  "sn='"+newSn+"'"+
                                    "where id ="+id;

                try {
                    Start.stmt.executeUpdate(editSql);
                    message.setText("Editted \n Your changes has been saved for SN:"+newSn);
                } catch (SQLException e1) {
                    message.setText("SQL Exception \n Your changes has not been saved for SN:"+newSn);
                    e1.printStackTrace();

                }
                System.out.println("Pd:"+productId + "\nLoc:"+locationId);

			}
		});
		btnFinish.setBounds(135, 214, 206, 52);
		contentPane.add(btnFinish);
		
		JButton btnCancel = new JButton("CLOSE");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(10, 214, 113, 52);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel_1 = new JLabel("Location:");
		lblNewLabel_1.setBounds(10, 69, 61, 16);
		contentPane.add(lblNewLabel_1);
		
		location = new JComboBox();
		location.setBounds(86, 65, 155, 27);
		contentPane.add(location);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(253, 37, 88, 29);
		contentPane.add(btnAdd);
		
		JButton btnNewButton_2 = new JButton("Add");
		btnNewButton_2.setBounds(253, 64, 88, 29);
		contentPane.add(btnNewButton_2);
		
		message = new JTextArea();
		message.setForeground(Color.RED);
		message.setBackground(SystemColor.window);
		message.setBounds(10, 269, 331, 52);
		contentPane.add(message);
	}
	public JComboBox getComboBoxProduct() {
		return product;
	}
	public JComboBox getComboBoxLocation() {
		return location;
	}
}
