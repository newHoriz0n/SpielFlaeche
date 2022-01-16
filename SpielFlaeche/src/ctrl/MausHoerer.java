package ctrl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import math.Vektor2D;

/**
 * Spezieller MouseListener für Spielflaeche
 * @author paulb
 *
 */
public class MausHoerer implements MouseListener, MouseMotionListener, MouseWheelListener {

	private int aktButton; // -1 wenn nicht gedrückt, 1 links, 3 rechts

	private CtrlView v;

	private int firstMausX;
	private int firstMausY;
	private int lastMausX;
	private int lastMausY;

	public MausHoerer(CtrlView v) {
		this.v = v;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (aktButton == 1) {
			v.verschiebeObjekt(e.getX() - lastMausX, e.getY() - lastMausY);
		}
		if (aktButton == 3) {
			v.verschiebeAnsicht(e.getX() - lastMausX, e.getY() - lastMausY);
		}
		lastMausX = e.getX();
		lastMausY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		lastMausX = e.getX();
		lastMausY = e.getY();
		v.mausBewegung(e.getX(), e.getY());
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

		if(aktButton == -1) {
			firstMausX = e.getX();
			firstMausY = e.getY();
		}
		aktButton = e.getButton();
		lastMausX = e.getX();
		lastMausY = e.getY();

		v.mausDruck(e.getButton(), e.getX(), e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		v.handleMouseRelease(firstMausX, firstMausY, lastMausX, lastMausY, aktButton);
		aktButton = -1;
		
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		v.mausRadBewegung(arg0.getWheelRotation());
	}

	public Vektor2D getMausPos() {
		return new Vektor2D(lastMausX, lastMausY);
	}

}
