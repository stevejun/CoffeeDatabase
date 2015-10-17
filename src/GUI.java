import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI extends JFrame{
	
	static PrintStream out;
	
	/*
	public static void main(String[] args){
		createGUI();
	}
	*/
	
	/*********************************************************************/
	/*********************************************************************/
	

	public static void createGUI() {
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JButton create = new JButton("New Entry");
		JButton search = new JButton("Search Database");
		Font font = new Font("Courier", Font.BOLD, 16);
		create.setFont(font);
		search.setFont(font);
		panel.add(create);
		panel.add(search);
		create.setToolTipText("Click to enter new entry.");
		search.setToolTipText("Click to view entry.");
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ANTEIKU");
		frame.setSize(208, 108);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
	            try{
	            	newEntry();
	            }catch (Exception eCreate){
	                eCreate.printStackTrace();
	            }
                
            }
        });
		
		search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
	            try{
	            	;
	            }catch (Exception eSearch){
	                eSearch.printStackTrace();
	            }
                
            }
        });


		

		
	}

	
	public static void newEntry() {
		
		JFrame entryFrame = new JFrame();
		JPanel entryPanel = new JPanel();
		entryFrame.getContentPane().add(BorderLayout.CENTER, entryPanel);
		entryFrame.setTitle("Create new entry");
		entryFrame.setLocation(184, 360);
		entryFrame.setSize(920, 96);
		entryFrame.setResizable(false);
		entryFrame.setVisible(true);
		
		JLabel itemIdLabel = new JLabel("Item ID");
		JLabel coffeeBlendLabel = new JLabel("Coffee Blend");
		JLabel priceLabel = new JLabel("Price");
		JLabel quantityLabel = new JLabel("Quantity");

		JTextField itemIdTextField = new JTextField(8);
		JTextField coffeeBlendTextField = new JTextField(32);
		JTextField priceTextField = new JTextField(8);
		JTextField quantityTextField = new JTextField(8);

		JButton save = new JButton("Save");
		JButton clear = new JButton("Clear");
		JButton back = new JButton("Back");
		
		entryPanel.add(itemIdLabel);
		entryPanel.add(itemIdTextField);
		
		entryPanel.add(coffeeBlendLabel);
		entryPanel.add(coffeeBlendTextField);
		
		entryPanel.add(priceLabel);
		entryPanel.add(priceTextField);
		
		entryPanel.add(quantityLabel);
		entryPanel.add(quantityTextField);
		
		entryPanel.add(save);
		entryPanel.add(clear);
		entryPanel.add(back);
		save.setToolTipText("Click to save entry to database");
		clear.setToolTipText("Click to clear all fields.");
		back.setToolTipText("Click to go back to previous window.");
		
		save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	if (!itemIdTextField.getText().isEmpty() &&
					!coffeeBlendTextField.getText().isEmpty()  &&
					!priceTextField.getText().isEmpty()  &&
					!quantityTextField.getText().isEmpty() ){
		            try{
		            	String[] entry = {
		            						itemIdTextField.getText(),
		            						coffeeBlendTextField.getText(),
		            						priceTextField.getText(),
		            						quantityTextField.getText()		};
		            	Database.insert("COFFEES", entry, out);
		            	System.out.println(	"Entry: \t" + 
		            						itemIdTextField.getText()+" "+
		            						coffeeBlendTextField.getText()+" "+
		            						priceTextField.getText()+" "+
		            						quantityTextField.getText());
		            }catch (Exception eSave){
		                eSave.printStackTrace();
		            }
		            
            	}
            	else{
            		JOptionPane.showMessageDialog(null,"One of your fields are missing.", "Emtpy Field", 1 );
            	}
            }
        });
		
		clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
	            try{
	            	itemIdTextField.setText(null);
					coffeeBlendTextField.setText(null);
					priceTextField.setText(null);
					quantityTextField.setText(null);
	            }catch (Exception eClear){
	                eClear.printStackTrace();
	            }
            }
        });
		
		back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
	            try{
	            	entryFrame.dispose();
	            }catch (Exception eReturn){
	                eReturn.printStackTrace();
	            }
            }
        });
		

		
	}
	
	
	
	
	/*********************************************************************/
	/*********************************************************************/
	
}


