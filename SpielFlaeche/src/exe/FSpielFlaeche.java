package exe;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.PBFileReadWriter;
import tisch.Tisch;

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
	private Tisch t = new Tisch();

	public FSpielFlaeche() {

		try {
			this.t = loadTisch();
		} catch (FileNotFoundException e1) {
		}

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		PSpielFlaecheView psfv = new PSpielFlaecheView(this.t);
		this.t.setView(psfv);

		add(psfv);

		addKeyListener(psfv.getTastaturHoerer());

		pack();
		setExtendedState(MAXIMIZED_BOTH);

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
				t.erzeugeSendeDatei();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
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

			List<String[]> tischInfos = PBFileReadWriter.getContentFromFile(",", chooser.getSelectedFile().getPath());
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
