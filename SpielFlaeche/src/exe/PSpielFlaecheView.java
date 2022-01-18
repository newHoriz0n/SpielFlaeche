package exe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ctrl.ViewController;
import ctrl.HoererManager;
import exe.menu.Aktion;
import exe.menu.Menueintrag;
import exe.menu.PPMenu;
import math.Vektor2D;
import model.SpielFlaecheModel;
import tisch.SpielBrett;
import tisch.Tisch;
import tisch.objekte.SpielObjekt;

/** Panel zur graphischen Darstellung der Spielfläche
 * 
 * @author paulb
 *
 */
public class PSpielFlaecheView extends JPanel implements ViewController, ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vektor2D offset; // Verschiebung der Ansicht auf den Tisch +x = nach rechts, +y = runter
	private double rotation;
	private double zoom;
	private SpielFlaecheModel m;
	private Tisch t;

	private transient PPMenu menu;

	private transient HoererManager hm;

	public PSpielFlaecheView(SpielFlaecheModel m) {

		this.m = m;
		this.t = m.getTisch();

		this.zoom = 1;

		createObjektMenu();

	}

	private void createObjektMenu() {

		this.menu = new PPMenu(10, 10);

		for (String key : m.getSpieleSchrank().getSpielSets().keySet()) {
			Menueintrag l1 = new Menueintrag(key, "");
			
			Menueintrag set = new Menueintrag(m.getSpieleSchrank().getSpielSets().get(key).getTitel() + "set", "");
			set.addAktion(new Aktion() {

				@Override
				public void performAktion() {
					t.platziereSpielSet(m.getSpieleSchrank().getSpielSets().get(key));
				}
			});
			
			l1.addMenuEintrag(set);
			
			for (SpielBrett b : m.getSpieleSchrank().getSpielSets().get(key).getBretter()) {
				if (!l1.containsEintrag(b.getName())) {
					Menueintrag brett = new Menueintrag(b.getName(), b.getBildURL());
					brett.addAktion(new Aktion() {

						@Override
						public void performAktion() {
							
							SpielBrett sb = b.getCopy();
							sb.setPosition(getSpielKoordsVonMaus(getWidth() / 2, getHeight() / 2));
							t.platziereSpielBrett(sb);
						}
					});
					l1.addMenuEintrag(brett);
				}
			}
			
			for (SpielObjekt o : m.getSpieleSchrank().getSpielSets().get(key).getObjekte()) {
				if (!l1.containsEintrag(o.getBezeichnung())) {
					Menueintrag objekt = new Menueintrag(o.getBezeichnung(), o.getBildURL());
					objekt.addAktion(new Aktion() {

						@Override
						public void performAktion() {
							SpielObjekt so = o.getCopy();
							so.setPosition(getSpielKoordsVonMaus(hm.getMausPos().getPosXInt(),hm.getMausPos().getPosYInt() ));
							t.platziereSpielObjekt(so);
						}
					});
					l1.addMenuEintrag(objekt);
				}
			}
			menu.addMenuEintrag(l1);
		}

	}
	
	public void setHoererManager(HoererManager hm) {
		this.hm = hm;
		
		addMouseListener(hm);
		addMouseMotionListener(hm);
		addMouseWheelListener(hm);
	}
	
	public void paint(Graphics g) {

		if (offset == null) {
			// offset = new Vektor2D(getWidth() / 2, getHeight() / 2);
			offset = new Vektor2D();
		}

		// HINTERGRUND
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		// VORDERGRUND
		Graphics2D g2d = (Graphics2D) g;
		g2d.rotate(rotation, getWidth() / 2, getHeight() / 2);
		g2d.translate(offset.getPosX() * zoom, offset.getPosY() * zoom);
		g2d.translate(getWidth() / 2, getHeight() / 2);
		g2d.scale(zoom, zoom);
		g2d.translate(-getWidth() / 2, -getHeight() / 2);
		t.drawTisch(g2d, rotation);

		// MENÜ
		g2d.translate(getWidth() / 2, getHeight() / 2);
		g2d.scale(1.0 / zoom, 1.0 / zoom);
		g2d.translate(-getWidth() / 2, -getHeight() / 2);
		g2d.translate(-offset.getPosX() * zoom, -offset.getPosY() * zoom);
		g2d.rotate(-rotation, getWidth() / 2, getHeight() / 2);
		menu.drawPPMenu(g2d);

	}

	@Override
	public void verschiebeAnsicht(double offX, double offY) {

		double newOffX = offX * Math.cos(rotation) + offY * Math.sin(rotation);
		double newOffY = offY * Math.cos(rotation) - offX * Math.sin(rotation);

		offset.add(newOffX / zoom, newOffY / zoom);

		repaint();
	}

	public void dreheAnsicht(double winkel) {
		rotation += winkel;
		repaint();
	}


	@Override
	public boolean checkViewControllerPress(int button, int mausX, int mausY) {
		return menu.checkKlick(mausX, mausY);
	}


	@Override
	public Vektor2D getSpielKoordsVonMaus(int mausX, int mausY) {
		double drehpunktX = ((getWidth() / 2) - offset.getPosX());
		double drehpunktY = ((getHeight() / 2) - offset.getPosY());

		double dx = (mausX - (getWidth() / 2)) / zoom;
		double dy = (mausY - (getHeight() / 2)) / zoom;

		return new Vektor2D(drehpunktX + (dx * Math.cos(rotation) + dy * Math.sin(rotation)),
				drehpunktY + (dy * Math.cos(rotation) - dx * Math.sin(rotation)));
	}
	
	@Override
	public void stateChanged(ChangeEvent ce) {
		repaint();
	}

	public double getViewRotation() {
		return rotation;
	}

	public double getViewZoom() {
		return zoom;
	}
	
	@Override
	public void repaintView() {
		repaint();
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		this.zoom = Math.max(0.1, zoom);
		this.zoom = Math.min(10, zoom);
	}

}
