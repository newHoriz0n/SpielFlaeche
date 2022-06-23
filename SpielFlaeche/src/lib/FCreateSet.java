package lib;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FCreateSet extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FCreateSet(SpieleSchrank ss) {
		
		setVisible(true);
		setTitle("Create Set");

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(gbl);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		// SETTINGS
		// Name
		JLabel lName = new JLabel("Name");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(lName, gbc);
		JTextField tfName = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(tfName, gbc);
		
		// Anlegen
		JButton bFertig = new JButton("Set anlegen");
		bFertig.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				// Ordner anlegen
				File dir = new File("");
				dir = new File("lib/Usersets/" + tfName.getText());
				dir.mkdirs();
				
				dispose();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(bFertig, gbc);
		
		// Abbrechen
		JButton bAbbrechen = new JButton("Abbrechen");
		bAbbrechen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(bAbbrechen, gbc);
		
		pack();

		// Setze Fokus auf TextFeld
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.delay(100);
			robot.keyRelease(KeyEvent.VK_TAB);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
}
