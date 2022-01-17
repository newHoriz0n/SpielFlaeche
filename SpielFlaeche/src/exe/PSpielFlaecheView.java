package exe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ctrl.CtrlView;
import ctrl.MausHoerer;
import ctrl.TastaturHoerer;
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
public class PSpielFlaecheView extends JPanel implements CtrlView, ChangeListener {

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

	private transient MausHoerer mh;
	private transient TastaturHoerer th;

	public PSpielFlaecheView(SpielFlaecheModel m) {

		this.m = m;
		this.t = m.getTisch();

		this.zoom = 1;

		createMenu();

		this.mh = new MausHoerer(this);
		this.th = new TastaturHoerer(this);
		addMouseListener(mh);
		addMouseMotionListener(mh);
		addMouseWheelListener(mh);

	}

	private void createMenu() {

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
							sb.setPosition(getMaus2SpielKoords(getWidth() / 2, getHeight() / 2));
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
							so.setPosition(getMaus2SpielKoords(mh.getMausPos()));
							t.platziereSpielObjekt(so);
						}
					});
					l1.addMenuEintrag(objekt);
				}
			}
			menu.addMenuEintrag(l1);
		}

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
	public void verschiebeObjekt(double offX, double offY) {

		double newOffX = offX * Math.cos(rotation) + offY * Math.sin(rotation);
		double newOffY = offY * Math.cos(rotation) - offX * Math.sin(rotation);

		t.verschiebeObjekt(newOffX / zoom, newOffY / zoom);
		repaint();
	}

	@Override
	public void mausDruck(int button, double x, double y) {

		Vektor2D spielMausKoords = getMaus2SpielKoords(x, y);

		if (!menu.checkKlick((int) x, (int) y)) {
			if (button == 1) {
				// t.waehleObjekte(spielMausKoords.getPosX(), spielMausKoords.getPosY());
				t.waehleObjekt(spielMausKoords.getPosX(), spielMausKoords.getPosY());
			} else if (button == 3) {
				t.aktionRechtsKlick(spielMausKoords.getPosX(), spielMausKoords.getPosY());
				// t.aktionRechtsKlickMultiSelect(spielMausKoords.getPosX(),
				// spielMausKoords.getPosY());
			}
		}

		repaint();
	}

	@Override
	public void mausRadBewegung(int wheelRotation) {
		if (th.isTasteGedrueckt(17)) {
			zoom -= wheelRotation * 0.05;
			zoom = Math.max(0.1, zoom);
			zoom = Math.min(10, zoom);
		} else {
			if (t.getAusgewaehltesObjekt() != null || t.getAusgewaehlteObjekte().size() > 0) {
				t.rotiere(wheelRotation);
			} else {
				dreheAnsicht((double) wheelRotation * Math.PI / 16);
			}
		}
		repaint();
	}

	private Vektor2D getMaus2SpielKoords(double x, double y) {
		double drehpunktX = ((getWidth() / 2) - offset.getPosX());
		double drehpunktY = ((getHeight() / 2) - offset.getPosY());

		double dx = (x - (getWidth() / 2)) / zoom;
		double dy = (y - (getHeight() / 2)) / zoom;

		return new Vektor2D(drehpunktX + (dx * Math.cos(rotation) + dy * Math.sin(rotation)),
				drehpunktY + (dy * Math.cos(rotation) - dx * Math.sin(rotation)));
	}

	protected Vektor2D getMaus2SpielKoords(Vektor2D mausPos) {
		return getMaus2SpielKoords(mausPos.getPosX(), mausPos.getPosY());
	}

	public TastaturHoerer getTastaturHoerer() {
		return th;
	}

	@Override
	public void mausBewegung(int x, int y) {
		if (t.checkMouseOver(getMaus2SpielKoords(x, y))) {
			repaint();
		}
	}

	@Override
	public void handleTastenRelease(int keyCode) {

		System.out.println(keyCode);

		if (keyCode == 127) {
			t.entferneObjekt();
		} else if (keyCode == 33) {
			t.ObjektNachOben();
		} else if (keyCode == 34) {
			t.ObjektNachUnten();
		} else if (keyCode == 83) {
			t.ObjekteMischen();
		} else if (keyCode == 67) {
			t.kopiereObjekt();
		}

		repaint();
	}

	@Override
	public void handleMouseRelease(int firstMausX, int firstMausY, int lastMausX, int lastMausY, int aktButton) {
		// if (aktButton == 1) {
		// t.waehleObjekte(firstMausX, firstMausY, lastMausX, lastMausY);
		// }
	}

	
	
	@Override
	public void stateChanged(ChangeEvent ce) {
		repaint();
	}

}
