import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

public class UpdateFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9065202392690693087L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateFrame frame = new UpdateFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	DefaultListModel<String> listModel;
	JList<String> fileList;
	String path;
	int pathIndex;
	private JTable table;
	int rows;
	
	public UpdateFrame() {
		
		//Sets up JFrame
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 689, 341);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Makes it scrollable
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setBounds(0, 0, 684, 310);
		contentPane.add(scrollPanel);
		
		//Layout of pane
		JPanel panel = new JPanel();
		scrollPanel.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{551, 0};
		gbl_panel.rowHeights = new int[]{16, 123, 0, 0, 25, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		//Header
		JLabel headerLabel = new JLabel("Enter new studnent information in fields below");
		headerLabel.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 18));
		GridBagConstraints gbc_headerLabel = new GridBagConstraints();
		gbc_headerLabel.insets = new Insets(0, 0, 5, 0);
		gbc_headerLabel.gridx = 0;
		gbc_headerLabel.gridy = 0;
		panel.add(headerLabel, gbc_headerLabel);
		
		//Pane to keep table and button together
		JSplitPane splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.anchor = GridBagConstraints.NORTH;
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		panel.add(splitPane, gbc_splitPane);
				
		//Header for JTable
		String[] collumnNames = {"Day of spare", "Period of spare", "First name", "Last name", "Student number"};
		rows = 1;
		Object [][] data = new Object [1][rows];
		DefaultTableModel model = new DefaultTableModel(data,collumnNames);
		
		//Makes tables scrollable
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		splitPane.setDividerLocation(500);
		Dimension splitSize = new Dimension(680,100);
		splitPane.setPreferredSize(splitSize);
		
		//Adds model and listeners to table
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(model);
		table.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER)
				{
					Object[] student = null;
					model.addRow(student);
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {
				//Hot key to add table row
			}});
		
		//Add and delete button appear here
		JSplitPane buttonPane = new JSplitPane();
		buttonPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(buttonPane);
		//Button to add student to data file
		JButton addButton = new JButton("Add student");
		buttonPane.setLeftComponent(addButton);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
				String[] temp = new String[4];
					for(int i = 0; i < temp.length; i++){
					temp[i] = (String) table.getModel().getValueAt(0,i);
					}
				StudentIO.addStudent(Integer.valueOf(temp[0]),Integer.valueOf(temp[1]),temp[2],temp[3], (int)Integer.valueOf((String) table.getModel().getValueAt(0, 4)));
				}catch (NullPointerException exc){
					System.out.println("Data for all cells was not entered");
				}catch(NumberFormatException exc){
					System.out.println("Invalid number formatting");
				}
				
			}
		});
		//Button to delete student from data file
		JButton deleteButton = new JButton("Delete student");
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try{
					String temp = (String) table.getModel().getValueAt(0, 4);
					delete((int)Integer.valueOf(temp));
				}catch(Exception e){}
			}
		});
		buttonPane.setRightComponent(deleteButton);
		buttonPane.setDividerLocation(42);
		
		//Second section header
		JLabel header = new JLabel("Select a file to change");
		header.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 18));
		GridBagConstraints gbc_header = new GridBagConstraints();
		gbc_header.anchor = GridBagConstraints.NORTH;
		gbc_header.insets = new Insets(0, 0, 5, 0);
		gbc_header.gridx = 0;
		gbc_header.gridy = 3;
		panel.add(header, gbc_header);
		
		//File list
		JList<String> list = new JList<>();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.gridx = 0;
		gbc_list.gridy = 4;
		panel.add(list, gbc_list);
		
		//Files
		listModel = new DefaultListModel<>();
		listModel.addElement(StudentIO.getLogPath());
		listModel.addElement(StudentIO.getOnePath());
		listModel.addElement(StudentIO.getTwoPath());
		
		//Listeners that allow user to swap a data file for a new one (Useful for updating files in the new semester)
				list.setModel(listModel);
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.addListSelectionListener(new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent e)
					{
						if(e.getValueIsAdjusting())
							return;
						try{
							if(!list.isSelectionEmpty()){
								FileDialog fd = new FileDialog(new JFrame());
								fd.setVisible(true);
								File[] f = fd.getFiles();
								if(f.length > 0)
								{
									String newPath = fd.getFiles()[0].getAbsolutePath();
								    StudentIO.setPath(list.getSelectedValue(), newPath);
								    listModel.remove(list.getSelectedIndex());
									listModel.addElement(newPath);
									repaint();
								}
							}
							else{repaint();}
						}
						catch(NullPointerException exception)
						{JOptionPane.showMessageDialog(null, "Please select a file to change first");
						exception.printStackTrace();}
					}
				});
				list.addMouseListener(new MouseListener(){
					@Override
					public void mouseClicked(MouseEvent arg0) {
						try{
							if(!list.isSelectionEmpty()){
								FileDialog fd = new FileDialog(new JFrame());
								fd.setVisible(true);
								File[] f = fd.getFiles();
								if(f.length > 0)
								{
									String newPath = fd.getFiles()[0].getAbsolutePath();
								    StudentIO.setPath(list.getSelectedValue(), newPath);
								    listModel.remove(list.getSelectedIndex());
									listModel.addElement(newPath);
									repaint();
								}
							}
							else{repaint();}
						}
						catch(NullPointerException exception)
						{JOptionPane.showMessageDialog(null, "Please select a file to change first");exception.printStackTrace();}						
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {}
					@Override
					public void mouseExited(MouseEvent arg0) {}
					@Override
					public void mousePressed(MouseEvent arg0) {}
					@Override
					public void mouseReleased(MouseEvent arg0) {}
					});
		
	}
	private void delete(int num)
	{
		
	}
}


