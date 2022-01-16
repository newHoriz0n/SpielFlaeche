package ctrl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Spezieller KeyListener für Spielflaeche
 * @author paulb
 *
 */
public class TastaturHoerer implements KeyListener {

	// 17: 	Strg
	// 112: F1
	// 127: Entf
	private boolean[] tasteGedruekt;
	private CtrlView v;

	public TastaturHoerer(CtrlView view) {
		this.v = view;
		tasteGedruekt = new boolean[256];
	}

	public boolean isTasteGedrueckt(int index) {
		return tasteGedruekt[index];
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		tasteGedruekt[arg0.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		v.handleTastenRelease(arg0.getKeyCode());
		tasteGedruekt[arg0.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
