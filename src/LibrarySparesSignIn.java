import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JComboBox;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.Panel;

import java.awt.Desktop;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.imageio.ImageIO;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JRadioButton;

public class LibrarySparesSignIn{

	private JFrame frame;
	private static ArrayList<Student> mainList;
	static FileInputStream fileIn;
	static ObjectInputStream objectIn;
	private static int day = 1;
	private int period = 1;
	/*
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LibrarySparesSignIn window = new LibrarySparesSignIn();
					window.frame.setVisible(true);
					window.frame.setResizable(false);	
				}
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "ERROR has occured. Contact customer support.");
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public LibrarySparesSignIn() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	//JComboBox<String> periodDrop;
	DefaultListModel<Student> listModel;
	JList<Student> list;
	private final JSplitPane searchPane = new JSplitPane();
	boolean[] spares = new boolean[8];
	JRadioButton[] spareButton = new JRadioButton[8];
	UpdateFrame updatePanel;
	
	private void initialize() {
		
		//Sets up JFrame
		frame = new JFrame();
		frame.setTitle("Student Spares Management System");
		frame.setMaximumSize(new Dimension(100, 2147483647));
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Left panel that displays information of student and images
		Panel panel = new Panel();
		panel.setBackground(new Color(198, 233, 255));
		frame.getContentPane().add(panel, BorderLayout.WEST);
		
		//Default font
		Font defaultFont = new Font("Microsoft Sans Serif", Font.PLAIN, 16);
		listModel = new DefaultListModel<Student>();
		
		//Layout manager for left panel
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 21, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]
		{0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		//Button to open the UpdateFrame
		JButton updateInfoButton = new JButton("Edit information");
		updateInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				updatePanel = new UpdateFrame();
				updatePanel.setVisible(true);
			}
		});
		GridBagConstraints gbc_updateInfoButton = new GridBagConstraints();
		gbc_updateInfoButton.insets = new Insets(0, 0, 5, 5);
		gbc_updateInfoButton.gridx = 1;
		gbc_updateInfoButton.gridy = 1;
		panel.add(updateInfoButton, gbc_updateInfoButton);
		
		//Chech log button, lets user open log while running the application
		JButton checkLogButton = new JButton("Open sign-in log");
		checkLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try{
				Desktop desktop = Desktop.getDesktop();
				File file = new File(StudentIO.getLogPath());
				if(file.exists()){desktop.open(file);}
				}
				catch(IOException exception)
				{
					JOptionPane.showMessageDialog(null, "File does not exist");
				}
			}
		});
		GridBagConstraints gbc_checkLogButton = new GridBagConstraints();
		gbc_checkLogButton.insets = new Insets(0, 0, 5, 5);
		gbc_checkLogButton.gridx = 2;
		gbc_checkLogButton.gridy = 1;
		panel.add(checkLogButton, gbc_checkLogButton);
		
		JSplitPane daySplitPane = new JSplitPane();
		GridBagConstraints gbc_daySplitPane = new GridBagConstraints();
		gbc_daySplitPane.insets = new Insets(0, 0, 5, 5);
		gbc_daySplitPane.fill = GridBagConstraints.BOTH;
		gbc_daySplitPane.gridx = 1;
		gbc_daySplitPane.gridy = 2;
		panel.add(daySplitPane, gbc_daySplitPane);
		
		JLabel dayLabel = new JLabel("Day");
		dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dayLabel.setFont(defaultFont);
		daySplitPane.setLeftComponent(dayLabel);
		
		//Sets up first drop down for selecting day
		JComboBox<String> dayDrop = new JComboBox<>();
		dayDrop.setMaximumRowCount(6);
		daySplitPane.setRightComponent(dayDrop);
		daySplitPane.setDividerLocation(70 + daySplitPane.getInsets().left);
		dayDrop.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4"}));
		dayDrop.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event) 
			{	
				day = Character.getNumericValue(((String) dayDrop.getSelectedItem()).charAt(0));
				listModel.removeAllElements();
				mainList = StudentIO.read(day,period);
					for(int i = 0; i < mainList.size(); i++){
						listModel.addElement(mainList.get(i));
					}
			}
		});
		
		JSplitPane periodSplitPane = new JSplitPane();
		GridBagConstraints gbc_periodSplitPane = new GridBagConstraints();
		gbc_periodSplitPane.insets = new Insets(0, 0, 5, 5);
		gbc_periodSplitPane.fill = GridBagConstraints.BOTH;
		gbc_periodSplitPane.gridx = 2;
		gbc_periodSplitPane.gridy = 2;
		panel.add(periodSplitPane, gbc_periodSplitPane);
		
		JLabel periodLabel = new JLabel("Period");
		periodLabel.setHorizontalAlignment(SwingConstants.CENTER);
		periodLabel.setFont(defaultFont);
		periodSplitPane.setLeftComponent(periodLabel);
		
		//Sets up second drop down for selecting period
		JComboBox<String> periodDrop = new JComboBox<String>();
		periodSplitPane.setRightComponent(periodDrop);
		periodSplitPane.setDividerLocation(70 + periodSplitPane.getInsets().left);
		periodDrop.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4"}));
		periodDrop.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event) 
			{	
				period = Character.getNumericValue(((String) periodDrop.getSelectedItem()).charAt(0));
				listModel.removeAllElements();
				mainList = StudentIO.read(day,period);
					for(int i = 0; i < mainList.size(); i++){
						listModel.addElement(mainList.get(i));
					}
			}
		});
		
		//Photo label instantiated with a default photo
		JLabel photo = new JLabel("");
		GridBagConstraints gbc_photo = new GridBagConstraints();
		gbc_photo.gridwidth = 5;
		gbc_photo.gridheight = 2;
		gbc_photo.insets = new Insets(0, 0, 5, 0);
		gbc_photo.gridx = 0;
		gbc_photo.gridy = 3;
		panel.add(photo, gbc_photo);
			try {photo.setIcon(new ImageIcon(ImageIO.read(new File("default_display_picture.png"))));} 
			catch (IOException defaultImageException) 
			{
				defaultImageException.printStackTrace();
				JOptionPane.showMessageDialog(null, "Defult picture could not load");
			}
		SimpleAttributeSet as = new SimpleAttributeSet();
		StyleConstants.setAlignment(as,StyleConstants.ALIGN_CENTER);
		
		//Builds student information text panel
		JTextPane studentInfo = new JTextPane();
		studentInfo.setPreferredSize(new Dimension(6, 30));
		studentInfo.setForeground(Color.BLACK);
		studentInfo.setBackground(new Color(198, 233, 255));
		studentInfo.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 22));
		GridBagConstraints gbc_studentInfo = new GridBagConstraints();
		gbc_studentInfo.gridwidth = 2;
		gbc_studentInfo.fill = GridBagConstraints.BOTH;
		gbc_studentInfo.gridheight = 4;
		gbc_studentInfo.insets = new Insets(0, 0, 5, 5);
		gbc_studentInfo.gridx = 1;
		gbc_studentInfo.gridy = 5;
		panel.add(studentInfo, gbc_studentInfo);
		studentInfo.setEditable(false);
		studentInfo.setParagraphAttributes(as,true);
		
		JLabel lblNewLabel = new JLabel("Student Spares");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 9;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		//The following 8 radio buttons are used to display and edit the spares a student had
		JRadioButton aButton = new JRadioButton("A");
		GridBagConstraints gbc_aButton = new GridBagConstraints();
		gbc_aButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_aButton.anchor = GridBagConstraints.NORTH;
		gbc_aButton.insets = new Insets(0, 0, 5, 5);
		gbc_aButton.gridx = 1;
		gbc_aButton.gridy = 10;
		panel.add(aButton, gbc_aButton);
		
		JRadioButton eButton = new JRadioButton("E");
		GridBagConstraints gbc_eButton = new GridBagConstraints();
		gbc_eButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_eButton.anchor = GridBagConstraints.NORTH;
		gbc_eButton.insets = new Insets(0, 0, 5, 5);
		gbc_eButton.gridx = 2;
		gbc_eButton.gridy = 10;
		panel.add(eButton, gbc_eButton);
		
		JRadioButton bButton = new JRadioButton("B");
		GridBagConstraints gbc_bButton = new GridBagConstraints();
		gbc_bButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_bButton.insets = new Insets(0, 0, 5, 5);
		gbc_bButton.gridx = 1;
		gbc_bButton.gridy = 11;
		panel.add(bButton, gbc_bButton);
		
		JRadioButton fButton = new JRadioButton("F");
		GridBagConstraints gbc_fButton = new GridBagConstraints();
		gbc_fButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_fButton.insets = new Insets(0, 0, 5, 5);
		gbc_fButton.gridx = 2;
		gbc_fButton.gridy = 11;
		panel.add(fButton, gbc_fButton);
		
		JRadioButton cButton = new JRadioButton("C");
		GridBagConstraints gbc_cButton = new GridBagConstraints();
		gbc_cButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_cButton.insets = new Insets(0, 0, 5, 5);
		gbc_cButton.gridx = 1;
		gbc_cButton.gridy = 12;
		panel.add(cButton, gbc_cButton);
		
		JRadioButton gButton = new JRadioButton("G");
		GridBagConstraints gbc_gButton = new GridBagConstraints();
		gbc_gButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_gButton.insets = new Insets(0, 0, 5, 5);
		gbc_gButton.gridx = 2;
		gbc_gButton.gridy = 12;
		panel.add(gButton, gbc_gButton);
		
		JRadioButton dButton = new JRadioButton("D");
		GridBagConstraints gbc_dButton = new GridBagConstraints();
		gbc_dButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_dButton.insets = new Insets(0, 0, 5, 5);
		gbc_dButton.gridx = 1;
		gbc_dButton.gridy = 13;
		panel.add(dButton, gbc_dButton);
		
		JRadioButton hButton = new JRadioButton("H");
		GridBagConstraints gbc_hButton = new GridBagConstraints();
		gbc_hButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_hButton.insets = new Insets(0, 0, 5, 5);
		gbc_hButton.gridx = 2;
		gbc_hButton.gridy = 13;
		panel.add(hButton, gbc_hButton);
		
		//Spare radio buttons added to an array
		spareButton[0] = aButton;
		spareButton[1] = bButton;
		spareButton[2] = cButton;
		spareButton[3] = dButton;
		spareButton[4] = eButton;
		spareButton[5] = fButton;
		spareButton[6] = gButton;
		spareButton[7] = hButton;
		
		SparesListener radioButtonListener = new SparesListener();
		for(int i = 0; i < spareButton.length; i++)
		{
			spareButton[i].addMouseListener(radioButtonListener);
		}
		
		//Button to add student check in time to log file
		JButton checkInButton = new JButton("Check In");
		GridBagConstraints gbc_checkInButton = new GridBagConstraints();
			gbc_checkInButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_checkInButton.gridwidth = 2;
			gbc_checkInButton.insets = new Insets(0, 0, 0, 5);
			gbc_checkInButton.gridx = 1;
			gbc_checkInButton.gridy = 14;
		panel.add(checkInButton, gbc_checkInButton);
		
		//Listener for check in button
		checkInButton.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent event){
				try{
					Student student = list.getSelectedValue();
					StudentIO.checkIn(student);
					studentInfo.setText(student.getFirst() + " " + student.getLast() + "\n" + String.valueOf(student.getNum()) + "\n" + "Last sign in: " + StudentIO.checkLog(student.getNum()));
				}
				catch(NullPointerException exception){
					JOptionPane.showMessageDialog(null, "Could not check in student, please make sure student has a spare");
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		
		//Reads in the list of students that have a spare during the current period
		mainList = StudentIO.read(day,period);
			for(int i = 0; i < mainList.size(); i++){
				listModel.addElement(mainList.get(i));
			}
		list = new JList<>();
		list.setModel(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setCellRenderer(new StripeRenderer());
		
		/*If a student is selected, the students information is returned to the left panel
		 * which displays the students image and information
		 */
		list.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting())
					return;
				if(!list.isSelectionEmpty()) {
					
					//Gets student that is selected on the list, and returns student information and image to window
					Student student = list.getSelectedValue();
					String time = StudentIO.checkLog(student.getNum());
					studentInfo.setText(student.getFirst() + " " + student.getLast() + "\n" + String.valueOf(student.getNum()) + "\n" + "Last sign in: " + time);
					for(int i = 0; i < spareButton.length; i++)
					{
							spareButton[i].setSelected(false);
					}
					spares = StudentIO.getSpares(student);
					for(int i = 0; i < spareButton.length; i++)
					{
						if(spares[i] == true){
							spareButton[i].setSelected(true);
						}
					}
					//Inputs the student image
					try {
						Image image = ImageIO.read(new File("images/" + student.getNum() + ".BMP"));
						photo.setIcon(new ImageIcon(image));
					}
					catch (IOException exception) {
						try {photo.setIcon(new ImageIcon(ImageIO.read(new File("default_display_picture.png"))));}
						catch (IOException defaultImageException) {
							defaultImageException.printStackTrace();
							JOptionPane.showMessageDialog(null, "Could not load image");
						}
					}
					repaint();
				}	
				else{repaint();
					for(int i = 0; i < spareButton.length; i++)
					{
							spareButton[i].setSelected(false);
					}
				}
			}
		});
		//Sets up ENTER as the hotkey to check in a student
		list.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent event) 
			{
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					Student student = list.getSelectedValue();
					StudentIO.checkIn(student);
					studentInfo.setText(student.getFirst() + " " + student.getLast() + "\n" + String.valueOf(student.getNum()) + "\n" + "Last sign in: " + StudentIO.checkLog(student.getNum()));
				}
			}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0) {}
			});
		//Makes list scrollable
		JScrollPane scrollPane = new JScrollPane(list);
		frame.getContentPane().add(scrollPane, BorderLayout.EAST);
		list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setColumnHeaderView(searchPane);
		
		//Adds search bar
		JFormattedTextField searchBar = new JFormattedTextField();
		searchBar.setColumns(35);
		searchPane.setLeftComponent(searchBar);
		
		/*Builds a search button that searches for any names in the list that match the search bar test
		 * or searches for any student numbers that are valid
		 * or shows the entire list if no text in entered
		 */
		JButton searchButton = new JButton("Search");
		searchPane.setRightComponent(searchButton);
		searchButton.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent event) {
				try{
					String find = searchBar.getText();
					listModel.removeAllElements();
					if(searchBar.getText().equals("")){
							for(int i = 0; i < mainList.size(); i++){
								listModel.addElement(mainList.get(i));
							}
					}
					else
					{	
						for(int i = 0; i < mainList.size(); i++){
							if(mainList.get(i).getFirst().equalsIgnoreCase(find)||mainList.get(i).getLast().equalsIgnoreCase(find)||(Integer.toString(mainList.get(i).getNum())).equals(find)){
								if(day == 3 || day == 4){
									switch(period){
										case 3: if(period == 3){period = 4;}else if(period == 4){period = 3;}
											break;
										case 4: if(period == 3){period = 4;}else if(period == 4){period = 3;}
											break;
										default:;
									}
								}
								if(mainList.get(i).getSpare() == period){
									listModel.addElement(mainList.get(i));
								}
							}
						}
					}
				}
				catch(NullPointerException exception){JOptionPane.showMessageDialog(null, "Could not search students, please make sure all files are present");}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
		});
		//Makes ENTER a search hot key, and executes searching code block
		//Same logic as mouse listener, could be a seperate method
		searchBar.addKeyListener(new KeyListener(){
		public void keyPressed(KeyEvent event) {
			try{
				String find = null;
				if(event.getKeyCode() == KeyEvent.VK_ENTER){
					listModel.removeAllElements();
					find = searchBar.getText();
					}
				if(searchBar.getText().equals("")){
					listModel.removeAllElements();
					for(int i = 0; i < mainList.size(); i++){
						listModel.addElement(mainList.get(i));
					}
				}
				else{
					//Searches for student first name/last name
					for(int i = 0; i < mainList.size(); i++){
						if(mainList.get(i).getFirst().equalsIgnoreCase(find)||mainList.get(i).getLast().equalsIgnoreCase(find))
						{
							if(day == 3 || day == 4){
								switch(period){
									case 3: if(period == 3){period = 4;}else if(period == 4){period = 3;}
										break;
									case 4: if(period == 3){period = 4;}else if(period == 4){period = 3;}
										break;
									default:;
								}
							}
							if(mainList.get(i).getSpare() == period){
								listModel.addElement(mainList.get(i));
							}
						}
					}
				}
			}catch(NullPointerException exception){JOptionPane.showMessageDialog(null, "Could not search students, please make sure all files are present");}
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}			
		});	
		

	}
	/**
	 * Updates the images and text displayed
	 */
	protected void repaint(){}

	private class SparesListener implements MouseListener{

		public void mouseClicked(MouseEvent event) {
			
			for(int i = 0; i < spares.length; i++){
			
				boolean match = ((JRadioButton)spareButton[i]).isSelected();
				if(match != spares[i])
				{
					if(match == true)
					{
						//Add spare
						int spareDay = 0;
						int sparePeriod = 0;
						Student student = list.getSelectedValue();
						
						if(i < 4){
							spareDay = 1;
							sparePeriod = i+1;
						}
						else{
							spareDay = 2;
							sparePeriod = i-3;
						}
						
						StudentIO.addStudent(spareDay,sparePeriod, student.getFirst(), student.getLast(), student.getNum());
					}
					else
					{
						//Remove spare
					}
					break;
				}

			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		}
}
