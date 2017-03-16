import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSetMetaData;

public class GUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static final int LATIME  = 540;
	public static final int INALTIME = 600;
	
	private Controller controller;
	public JPanel mainPanel;
	
	public GUI(String titluSimulare) throws SQLException {
		super(titluSimulare);		
		this.prepareGUI();
		setVisible(true); 
		this.controller = new Controller(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public void prepareGUI() {
		this.setSize(LATIME,INALTIME);
		this.setLayout(new GridLayout(1,1));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
		try {
			this.modelPanel(mainPanel);
		} catch (SQLException e) {
			System.err.println(e);
		}
		this.add(mainPanel);
	}
	
	private void modelPanel(JPanel panel) throws SQLException {
		Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);        
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        JLabel label1 = new JLabel();
        label1.setSize(LATIME, 100);
        label1.setBorder(empty);
        label1.setText("<html><p style='font-size:24px;margin-left:-10px;text-align:center;font-family:CMU Serif;font-weight:500'>" + this.getTitle() + "</p></html>");        
        panel.add(label1, gbc);
        
		/*
		 * The first half of the GUI - the one with add student*/
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		JPanel panel1 = new JPanel(false);
		panel1.setBackground(Color.white);
			JLabel stud = new JLabel();
			stud.setSize(LATIME, 30);
			stud.setText("<html><p style='font-size:14px;margin-left:-10px;text-align:left;font-family:CMU Serif;font-weight:500'>Lista studenti</p></html>");        
	        panel1.add(stud);
	        
			JTable table1 = new JTable(buildTableModel(DBManager.getAll("students")));
			table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
			table1.setFillsViewportHeight(true);
			JScrollPane pane1 = new JScrollPane(table1);
			pane1.setPreferredSize(new Dimension(500,100));
			//JOptionPane.showMessageDialog(null, new JScrollPane(table1));
			panel1.add(pane1);
			
			// Adaugarea de butoane pentru operatii CRUD
			// 1. Adaugare student
			JButton adaugaStudent = new JButton("Add");
			adaugaStudent.setActionCommand("adaugaStudent");
			adaugaStudent.addActionListener(this.controller);
			panel1.add(adaugaStudent);
			// 2. Selectare student
			JButton selectareStudent = new JButton("Select");
			selectareStudent.setActionCommand("selectStudent");
			selectareStudent.addActionListener(this.controller);
			panel1.add(selectareStudent);
			// 3. Stergere student
			JButton stergeStudent = new JButton("Delete");
			stergeStudent.setActionCommand("stergeStudent");
			stergeStudent.addActionListener(this.controller);
			panel1.add(stergeStudent);
			// 4. Vizualizare cursuri la care este inrolat studentul
			JButton enrollStudent = new JButton("Enrollment");
			enrollStudent.setActionCommand("enrollStudent");
			enrollStudent.addActionListener(this.controller);
			panel1.add(enrollStudent);
		panel.add(panel1, gbc);
		
		
		/*
		 * The second half of the UI - the one with the course*/
		gbc.fill = GridBagConstraints.BOTH;
	    gbc.ipady = 10;
	    gbc.ipadx = 20;
	    gbc.gridx = 0;
	    gbc.gridy = 2;
		gbc.weighty = 1.0;
		JPanel panel2 = new JPanel(false);
		panel2.setBackground(Color.LIGHT_GRAY);
			JLabel curs = new JLabel();
			curs.setSize(LATIME, 30);
			curs.setText("<html><p style='font-size:14px;margin-left:-10px;text-align:left;font-family:CMU Serif;font-weight:500'>Lista cursuri</p></html>");        
	        panel2.add(curs);
	        
			JTable table2 = new JTable(buildTableModel(DBManager.getAll("course")));
			table2.setPreferredScrollableViewportSize(table2.getPreferredSize());
			table2.setFillsViewportHeight(true);
			JScrollPane pane2 = new JScrollPane(table2);
			pane2.setPreferredSize(new Dimension(500,100));
			//JOptionPane.showMessageDialog(null, new JScrollPane(table2));
			panel2.add(pane2);
			
			
		panel.add(panel2,gbc);
		
		GridBagConstraints c = new GridBagConstraints();
 		c.gridx = 0;
 		c.gridy = 10;
 		c.fill = GridBagConstraints.NONE;
 		c.weightx=0;
 		c.weighty=0;
 		c.gridwidth = 2;
 		panel.add(new JLabel(" "),c);
	}
	
	/**
	 * Awesome method found on stackOverflow
	 * http://stackoverflow.com/questions/10620448/most-simple-code-to-populate-jtable-from-resultset
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
}
