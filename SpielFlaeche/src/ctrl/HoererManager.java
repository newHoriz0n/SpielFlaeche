package ctrl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import math.Vektor2D;

/**
 * Spezieller MouseListener für Spielflaeche
 * 
 * @author paulb
 *
 */
public class HoererManager implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	private int aktButton; // -1 wenn nicht gedrückt, 1 links, 3 rechts

	private ViewController vc;
	private TischController tc;

	private int firstMausX;
	private int firstMausY;
	private int lastMausX;
	private int lastMausY;

	// 17: Strg
	// 112: F1
	// 127: Entf
	private boolean[] tasteGedruekt;

	public HoererManager(ViewController v, TischController t) {
		this.vc = v;
		this.tc = t;

		tasteGedruekt = new boolean[256];
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (aktButton == 1) {
			double newOffX = (e.getX() - lastMausX) * Math.cos(vc.getViewRotation()) + (e.getY() - lastMausY) * Math.sin(vc.getViewRotation());
			double newOffY = (e.getY() - lastMausY) * Math.cos(vc.getViewRotation()) - (e.getX() - lastMausX) * Math.sin(vc.getViewRotation());
			tc.handleLeftMouseDrag(newOffX / vc.getViewZoom(), newOffY / vc.getViewZoom(), vc.getSpielKoordsVonMaus(firstMausX, firstMausY), vc.getSpielKoordsVonMaus(lastMausX, lastMausY));
			vc.repaintView();
		}
		if (aktButton == 3) {
			vc.verschiebeAnsicht(e.getX() - lastMausX, e.getY() - lastMausY);
		}
		lastMausX = e.getX();
		lastMausY = e.getY();

		vc.setLightDrawing(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		lastMausX = e.getX();
		lastMausY = e.getY();

		if (tc.calcMouseOver(vc.getSpielKoordsVonMaus(e.getX(), e.getY()))) {		
			vc.repaintView();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (aktButton == -1) {
			firstMausX = e.getX();
			firstMausY = e.getY();
		}
		aktButton = e.getButton();
		lastMausX = e.getX();
		lastMausY = e.getY();

		Vektor2D spielMausKoords = vc.getSpielKoordsVonMaus(e.getX(), e.getY());

		if (!vc.checkViewControllerPress(e.getButton(), e.getX(), e.getY())) {
			tc.handleMausPress(e.getButton(), spielMausKoords);
		}

		vc.repaintView();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		aktButton = -1;
		tc.handleMausRelease(aktButton, vc.getSpielKoordsVonMaus(firstMausX, firstMausY), vc.getSpielKoordsVonMaus(lastMausX, lastMausY));

		vc.setLightDrawing(false);
		vc.repaintView();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {

		if (isTasteGedrueckt(17)) {
			vc.setZoom(vc.getViewZoom() - arg0.getWheelRotation() * 0.05);
		} else {
			if (tc.hatAusgewaehltesObjekt()) {
				tc.handleMausRad(arg0.getWheelRotation());
			} else {
				vc.dreheAnsicht((double) arg0.getWheelRotation() * Math.PI / 16);
			}
		}

		vc.repaintView();

	}

	public Vektor2D getMausPos() {
		return new Vektor2D(lastMausX, lastMausY);
	}

	///////////////////////////
	// TASTATUR
	//////////////

	public boolean isTasteGedrueckt(int index) {
		return tasteGedruekt[index];
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		tasteGedruekt[arg0.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		System.out.println(arg0.getKeyCode());

		tc.handleTischKeyControls(arg0);
		
		tasteGedruekt[arg0.getKeyCode()] = false;
		vc.repaintView();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
