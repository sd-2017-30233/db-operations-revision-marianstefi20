import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Controller implements ActionListener {
	private GUI gui;
	
	public Controller(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * String-ul command contine cuvinte trimise din TabbedPane de catre ascultatori 
		 */
		String command = e.getActionCommand();
		switch(command) {
			case "adaugaStudent":
				adaugaStudent();
				break;
			case "selectStudent":
				selectStudent();
				break;
			case "stergeStudent":
				stergeStudent();
				break;
			case "updateStudent":
				updateStudent();
				break;
			case "enrollStudent":
				enrollStudent();
				break;
			default:
				break;
		}
	}
	
	private void adaugaStudent() {
		JPanel panel1 = new JPanel(new GridLayout(4,1));
		JLabel l1 = new JLabel("Nume: ");
		JTextField tf1 = new JTextField();
		JLabel l2 = new JLabel("An univ.: ");
		JTextField tf2 = new JTextField();
		JLabel l3 = new JLabel("Adresa: ");
		JTextField tf3 = new JTextField();
		JLabel l4 = new JLabel("Facultate");
		JTextField tf4 = new JTextField();
		panel1.add(l1);
		panel1.add(tf1);
		panel1.add(l2);
		panel1.add(tf2);
		panel1.add(l3);
		panel1.add(tf3);
		panel1.add(l4);
		panel1.add(tf4);
		
		int result = JOptionPane.showConfirmDialog(null, panel1, "Adauga un student",
				 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
        	ArrayList<Object> data = new ArrayList<Object>();
        	data.add(tf1.getText());                   //nume
        	data.add(Integer.parseInt(tf2.getText())); //an univ
        	data.add(tf3.getText());                   //adresa
        	data.add(tf4.getText());                   //faculta
        	
        	try {
				DBManager.insert("students", "name, year, faculty, adress", data);
			} catch (SQLException e) {
				System.err.println(e);
			}
        	// Trebuie sa actualizam aici GUI
        	gui.mainPanel.revalidate();
        	gui.mainPanel.repaint();
		} else {
		    System.out.println("Cancelled");
		}

	}
	
	private void selectStudent(){
		JPanel panel = new JPanel(new GridLayout(1,1));
		JLabel l1 = new JLabel("Nume: ");  // selectia o facem dupa nume
		JTextField tf1 = new JTextField();
		panel.add(l1);
		panel.add(tf1);
		int result = JOptionPane.showConfirmDialog(null, panel, "Selecteaza un student",
				 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
			JTable table2 = null;
			try {
				table2 = new JTable(GUI.buildTableModel(DBManager.getRow("students", tf1.getText())));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JScrollPane pane2 = new JScrollPane(table2);
			pane2.setPreferredSize(new Dimension(500,100));
			panel.add(pane2);
		} else {
		    System.out.println("Cancelled");
		}
		
	}
	
	private void stergeStudent() {
		JPanel panel = new JPanel(new GridLayout(1,1));
		JLabel l1 = new JLabel("Nume: ");  // selectia o facem dupa nume
		JTextField tf1 = new JTextField();
		panel.add(l1);
		panel.add(tf1);
		int result = JOptionPane.showConfirmDialog(null, panel, "Sterge un student",
				 JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
			try {
				DBManager.delete("students", "id", Integer.parseInt(tf1.getText()));
			} catch (NumberFormatException e) {
				System.err.println(e);
			} catch (SQLException e) {
				System.err.println(e);
			}
		} else {
		    System.out.println("Cancelled");
		}
	}
	
	private void updateStudent() {
		
	}
	
	private void enrollStudent() {
		
	}

}
