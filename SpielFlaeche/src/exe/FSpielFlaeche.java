package exe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ctrl.HoererManager;
import io.PBFileReadWriter;
import lib.FCreateSet;
import model.SpielFlaecheModel;
import tisch.Tisch;
import tisch.create.FCreateKarte;

/**
 * Mainklasse der Spielfläche
 * @author paulb
 *
 */
public class FSpielFlaeche extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpielFlaecheModel m;
	
	private JMenuBar menuBar = new JMenuBar(); // Window menu bar

	public FSpielFlaeche() {

		Tisch t = new Tisch();
		try {
			t = loadTisch();
		} catch (FileNotFoundException e1) {
		}
		
		this.m = new SpielFlaecheModel(t);

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		PSpielFlaecheView psfv = new PSpielFlaecheView(this.m);
		HoererManager hm = new HoererManager(psfv, t);
		psfv.setHoererManager(hm);
		
		t.setView(psfv);

		add(psfv);
		addKeyListener(hm);

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Serialisiere...");
				m.getTisch().erzeugeSendeDatei();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		// MENÜ
		// Create
	    JMenu createMenu = new JMenu("Create");
	    // Karte
	    JMenuItem createKarte = new JMenuItem("Karte");
	    createKarte.addActionListener(new ActionListener() {
	    	
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		FCreateKarte fck = new FCreateKarte(m.getTisch());
	    		fck.requestFocus();
	    	}
	    });
	    createMenu.add(createKarte);
	    // Kartendeck
	    
	    // View
	    JMenu viewMenu = new JMenu("View");
	    
	    // Sets
	    JMenu setMenu = new JMenu("Sets"); 
	    JMenuItem createSet = new JMenuItem("Create Set");
	    createSet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FCreateSet fcs = new FCreateSet(m.getSpieleSchrank());
				fcs.requestFocus();
			}
		});
	    setMenu.add(createSet);
	    
	    
	    
	    menuBar.add(setMenu);
	    menuBar.add(createMenu);
	    menuBar.add(viewMenu);
	    
	    setJMenuBar(menuBar);
	    
	    // PACK
	    
		pack();
		setExtendedState(MAXIMIZED_BOTH);

		
	}

	public static Tisch loadTisch() throws FileNotFoundException {

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(PBFileReadWriter.createAbsPfad("")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Spielflächen-Daten", "sfd");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(new JFrame("Lade Spielkonfiguration"));
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
		}

		File file = chooser.getSelectedFile();
		if (file != null && file.exists()) {

			List<String[]> tischInfos = PBFileReadWriter.getContentFromFileLF(",", chooser.getSelectedFile().getPath());
			return new Tisch(tischInfos);

		}

		return new Tisch();

	}

	public static void main(String[] args) {

//		System.out.println(System.currentTimeMillis());
		
//		long testTime = 700000000L; // 1 Woche
		long testTime = 32000000000L; // 1 Jahr		
		
		long startTime = 1642252226085L;

		if (System.currentTimeMillis() < startTime + testTime) {
			FSpielFlaeche fsf = new FSpielFlaeche();
			fsf.requestFocus();
		}

	}

}
