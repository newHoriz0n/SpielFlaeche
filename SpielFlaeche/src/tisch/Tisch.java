package tisch;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ctrl.TischControl;
import ctrl.TischControlAction;
import ctrl.TischControlStatus;
import ctrl.TischController;
import exe.PSpielFlaecheView;
import io.PBFileReadWriter;
import lib.SpielSet;
import math.Vektor2D;
import tisch.objekte.SpielObjekt;
import tisch.objekte.SpielSymbol;
import tisch.objekte.wuerfel.SpielWuerfel;

/**
 * 
 * @author PC Paul I
 *
 *
 *         Enthält alles, was auf einem Spieltisch liegt...
 */

// TODO: Nur Clip neu zeichnen
// Funktion: Reihenfolgen von mehreren Objekten umdrehen

public class Tisch implements TischController {

	private List<SpielBrett> spielbretter;
	private List<SpielObjekt> spielObjekte;
	private List<SpielSichtSchutz> spielSichtSchutze;
	// private ListIterator<SpielObjekt> spielObjekteIterator;

	private int[] auswahlrahmen; // xmin xmax ymin ymax
	// private SpielObjekt ausgewahltesObjekt;
	private List<SpielObjekt> ausgewaehlteObjekte;
	private boolean verschoben;
	private boolean mouseOn;

	private PSpielFlaecheView view;

	private TischControlStatus status = TischControlStatus.AUSWAHL_EINZEL;
	private HashMap<TischControlStatus, List<TischControl>> tischCtrls = loadTischControls();

	// private boolean ignoreMouseRelease;
	/**
	 * Erstelle leeren Tisch
	 */
	public Tisch() {

		this.spielbretter = new ArrayList<>();
		this.spielObjekte = new ArrayList<>();
		this.spielSichtSchutze = new ArrayList<>();
		this.ausgewaehlteObjekte = new ArrayList<>();
		
		// List<Vektor2D> ecken = new ArrayList<>();
		// ecken.add(new Vektor2D(100, 100));
		// ecken.add(new Vektor2D(500, 100));
		// ecken.add(new Vektor2D(500, 300));
		// ecken.add(new Vektor2D(100, 300));
		// SpielSichtSchutz s = new SpielSichtSchutz("test", ecken, this);
		// platziereSpielSichtSchutz(s);

	}

	/**
	 * Lade Tisch aus Datei
	 * 
	 * @param tischInfos
	 */
	public Tisch(List<String[]> tischInfos) {

		System.out.println("Lade Tisch aus Datei...");

		this.spielbretter = new ArrayList<>();
		this.spielObjekte = new ArrayList<>();
		this.spielSichtSchutze = new ArrayList<>();
		this.ausgewaehlteObjekte = new ArrayList<>();

		for (String[] info : tischInfos) {

			if (info.length > 1) {

				if (info[1].equals("b")) {
					SpielBrett b = new SpielBrett(info[2], PBFileReadWriter.createAbsPfad(info[3]),
							new Vektor2D(Integer.parseInt(info[4]), Integer.parseInt(info[5])));
					platziereSpielBrett(b);
				} else {
					SpielObjekt o = SpielObjekt.getInstanceFromString(info);
					platziereSpielObjekt(o);
				}

			}
		}

		System.out.println("Laden aus Datei abgeschlossen!");

	}

	private HashMap<TischControlStatus, List<TischControl>> loadTischControls() {
		HashMap<TischControlStatus, List<TischControl>> ctrls = new HashMap<>();

		// FREE
		List<TischControl> tcsFree = new ArrayList<>();

		ctrls.put(TischControlStatus.FREE, tcsFree);

		// AUSWAHL_EINZEL
		List<TischControl> tcsAuswahlEinzel = new ArrayList<>();
		// Löschen
		TischControl delO = new TischControl("Entf", 127, "Entferne Objekt");
		delO.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				entferneObjekte(ausgewaehlteObjekte.get(0));
			}
		});
		tcsAuswahlEinzel.add(delO);
		// Kopieren
		TischControl copyO = new TischControl("C", 67, "Kopiere Objekt");
		copyO.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				SpielObjekt o = kopiereObjekt(ausgewaehlteObjekte.get(0));
				ausgewaehlteObjekte.get(0).setAusgewaehlt(false);
				ausgewaehlteObjekte.clear();
				o.setAusgewaehlt(true);
				ausgewaehlteObjekte.add(o);
			}
		});
		tcsAuswahlEinzel.add(copyO);
		// Nach Unten
		TischControl runterO = new TischControl("Bild v", 34, "Objekt nach unten");
		runterO.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				spielObjekte.remove(ausgewaehlteObjekte.get(0));
				spielObjekte.add(0, ausgewaehlteObjekte.get(0));
			}
		});
		tcsAuswahlEinzel.add(runterO);

		/*
		 * else if (arg0.getKeyCode() == 33) { tc.ObjektNachOben(); } else if
		 * (arg0.getKeyCode() == 34) { tc.ObjektNachUnten(); } else if
		 * (arg0.getKeyCode() == 83) { tc.ObjekteMischen();
		 * 
		 */

		ctrls.put(TischControlStatus.AUSWAHL_EINZEL, tcsAuswahlEinzel);

		// AUSWAHL MEHRERE
		List<TischControl> tcsAuswahlMehrere = new ArrayList<>();
		// Bündeln
		TischControl stackOs = new TischControl("B", 66, "Objekte stapeln");
		stackOs.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				objekteStapeln();
			}
		});
		tcsAuswahlMehrere.add(stackOs);

		// Mischen
		TischControl shuffleOs = new TischControl("S", 83, "Objekte mischen");
		shuffleOs.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				objekteMischen();
			}
		});
		tcsAuswahlMehrere.add(shuffleOs);

		// Rechtsklick AKtion auf alle
		TischControl rightOnOs = new TischControl("R", 82, "Rechtsklick auf Objekte");
		rightOnOs.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				for (SpielObjekt o : ausgewaehlteObjekte) {
					o.aktionRechtsKlick();
				}
			}
		});
		tcsAuswahlMehrere.add(rightOnOs);

		// Copy All
		TischControl copyOs = new TischControl("C", 67, "Kopiere Objekte");
		copyOs.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				List<SpielObjekt> neue = new ArrayList<>();
				for (SpielObjekt o : ausgewaehlteObjekte) {
					neue.add(kopiereObjekt(o));
					o.setAusgewaehlt(false);
				}
				ausgewaehlteObjekte.clear();
				ausgewaehlteObjekte.addAll(neue);
				for (SpielObjekt o : ausgewaehlteObjekte) {
					o.setAusgewaehlt(true);
				}
			}
		});
		tcsAuswahlMehrere.add(copyOs);

		// Delete All
		TischControl delOs = new TischControl("Entf", 127, "Lösche Objekte");
		delOs.setControlAction(new TischControlAction() {
			@Override
			public void performAction() {
				for (int i = ausgewaehlteObjekte.size() - 1; i >= 0; i--) {
					entferneObjekte(ausgewaehlteObjekte.get(i));
				}
			}
		});
		tcsAuswahlMehrere.add(delOs);

		ctrls.put(TischControlStatus.AUSWAHL_MEHRERE, tcsAuswahlMehrere);

		return ctrls;

	}

	/**
	 * Platziert alle Spielobjekte und Spielbretter
	 * 
	 * @param set
	 * @param position
	 */
	public void platziereSpielSet(SpielSet set) {

		for (SpielObjekt o : set.getObjekte()) {
			platziereSpielObjekt(o);
		}

		for (SpielBrett b : set.getBretter()) {
			platziereSpielBrett(b);
		}

	}

	public void platziereSpielBrett(SpielBrett b) {
		spielbretter.add(b);
	}

	public void platziereSpielObjekt(SpielObjekt o) {
		spielObjekte.add(o);
		if (view != null) {
			if (o instanceof SpielWuerfel) {
				o.addChangeLister(view);
			}
		}
	}

	public void platziereSpielSichtSchutz(SpielSichtSchutz s) {
		spielSichtSchutze.add(s);
		for (SpielSymbol symb : s.getEcken()) {
			spielObjekte.add(symb);
		}
	}

	public void drawTisch(Graphics2D g, double bildRotation, boolean light) {
		for (SpielBrett s : spielbretter) {
			s.paint(g);
		}

		for (SpielObjekt o : spielObjekte) {
			if (!light) {
				o.drawSpielObjekt(g, bildRotation);
			} else {
				o.drawSpielObjektLight(g, bildRotation);
			}
		}

		for (SpielSichtSchutz s : spielSichtSchutze) {
			s.drawSpielObjekt(g, bildRotation);
		}

		// Auswahlrahmen
		if (auswahlrahmen != null) {
			g.setColor(Color.GRAY);
			g.drawRect(auswahlrahmen[0], auswahlrahmen[2], auswahlrahmen[1] - auswahlrahmen[0], auswahlrahmen[3] - auswahlrahmen[2]);
		}
	}

	public void drawObjekt(Graphics2D graphics, double bildRotation, SpielObjekt o) {
		o.drawSpielObjekt(graphics, bildRotation);
	}

	public void waehleObjekte(int firstMausX, int firstMausY, int lastMausX, int lastMausY) {

		for (SpielObjekt o : ausgewaehlteObjekte) {
			o.setAusgewaehlt(false);
		}
		ausgewaehlteObjekte.clear();

		if (auswahlrahmen != null) {

			for (int i = 0; i < spielObjekte.size(); i++) {

				if (spielObjekte.get(i).checkRahmenAuswahl(auswahlrahmen[0], auswahlrahmen[2], auswahlrahmen[1], auswahlrahmen[3])) {
					spielObjekte.get(i).setAusgewaehlt(true);
					ausgewaehlteObjekte.add(spielObjekte.get(i));
				} else {
					spielObjekte.get(i).setAusgewaehlt(false);
				}
			}

			if (ausgewaehlteObjekte.size() == 1) {
				status = TischControlStatus.AUSWAHL_EINZEL;
			} else if (ausgewaehlteObjekte.size() >= 1) {
				status = TischControlStatus.AUSWAHL_MEHRERE;
			} else {
				status = TischControlStatus.FREE;
			}

		}

	}

	public void waehleObjekt(double x, double y) {

		for (SpielObjekt o : ausgewaehlteObjekte) {
			o.setAusgewaehlt(false);
		}
		ausgewaehlteObjekte.clear();

		int ausgewaeltIndex = -1;
		for (int i = spielObjekte.size() - 1; i >= 0; i--) {
			if (spielObjekte.get(i).checkKlick(x, y)) {
				spielObjekte.get(i).setAusgewaehlt(true);
				ausgewaehlteObjekte.add(spielObjekte.get(i));
				ausgewaeltIndex = i;
				break;
			}
		}

		// Bringe Ausgewähltes Objekt nach Oben.
		if (ausgewaeltIndex != -1) {
			spielObjekte.add(spielObjekte.size() - 1, spielObjekte.remove(ausgewaeltIndex));
			status = TischControlStatus.AUSWAHL_EINZEL;
			mouseOn = true;
		} else {
			status = TischControlStatus.FREE;
		}

	}

	public void rotiere(double winkel) {
		double rotSpeed = Math.PI / 8.0;
		for (SpielObjekt o : ausgewaehlteObjekte) {
			o.rotiere(winkel * rotSpeed);
		}
	}

	public void verschiebeObjekte(double offX, double offY) {
		if (ausgewaehlteObjekte.size() > 0) {
			for (SpielObjekt s : ausgewaehlteObjekte) {
				s.verschiebe(offX, offY);
			}
		}
	}

	public List<SpielObjekt> getAusgewaehltesObjekte() {
		return ausgewaehlteObjekte;
	}

	public List<SpielObjekt> getAusgewaehlteObjekte() {
		return ausgewaehlteObjekte;
	}

	/**
	 * 
	 * @param maus
	 * @return true wenn sich etwas ändert.
	 */
	public boolean calcMouseOver(Vektor2D maus) {
		boolean aenderung = false;
		for (SpielObjekt o : spielObjekte) {
			if (o.calcMouseOver(maus)) {
				aenderung = true;
			}
		}
		for (SpielSichtSchutz s : spielSichtSchutze) {
			if (s.checkMouseOver(maus)) {
				aenderung = true;
			}
		}
		return aenderung;
	}

	/*
	 * public void aktionRechtsKlickMultiSelect(double x, double y) {
	 * 
	 * ausgewaehlteObjekte.clear();
	 * 
	 * for (SpielObjekt o : spielObjekte) { if (o.checkKlick(x, y)) {
	 * ausgewahltesObjekt = o; ausgewaehlteObjekte.add(o); o.setAusgewaehlt(true); }
	 * else { o.setAusgewaehlt(false); } }
	 * 
	 * if (ausgewaehlteObjekte.size() != 0) { for (SpielObjekt s :
	 * ausgewaehlteObjekte) { s.aktionRechtsKlick(); } }
	 * 
	 * }
	 */

	public void aktionRechtsKlick(double x, double y) {

		waehleObjekt(x, y);

		if (ausgewaehlteObjekte.size() > 0) {
			for (SpielObjekt o : ausgewaehlteObjekte) {
				o.aktionRechtsKlick();
			}
		} else {
			status = TischControlStatus.FREE;
		}

	}

	public void updateView() {
		if (view != null) {
			view.repaint();
		}
	}

	public void setView(PSpielFlaecheView v) {
		this.view = v;
		for (SpielObjekt o : spielObjekte) {
			if (o instanceof SpielWuerfel) {
				o.addChangeLister(view);
			}
		}
	}

	/**
	 * 
	 * @param o:
	 *            Objekt, das gelöscht werden soll. Wenn null, werden alle
	 *            ausgewählten Objekte gelöscht.
	 */
	public void entferneObjekte(SpielObjekt o) {
		if (o != null) {
			spielObjekte.remove(o);
			ausgewaehlteObjekte.remove(o);
		} else {
			spielObjekte.removeAll(ausgewaehlteObjekte);
			ausgewaehlteObjekte.clear();
		}

		status = TischControlStatus.FREE;

		// for (SpielObjekt s : ausgewaehlteObjekte) {
		// spielObjekte.remove(s);
		// }
		// ausgewaehlteObjekte.clear();
	}

	public void erzeugeSendeDatei() {

		List<String> lines = new ArrayList<>();
		for (SpielBrett b : spielbretter) {
			lines.add(b.toSendString());
		}

		for (SpielObjekt spielObjekt : spielObjekte) {
			lines.add(spielObjekt.toSendString());
		}

		try {
			PBFileReadWriter.writeLinesToFile(lines, "sav/tisch" + System.currentTimeMillis() + ".sfd");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Serialisierung abgeschlossen!");

	}

	/*
	 * public void objektAuswaehlen(SpielObjekt o) { for (SpielObjekt o2 :
	 * ausgewaehlteObjekte) { o2.setAusgewaehlt(false); } if (ausgewahltesObjekt !=
	 * null) { ausgewahltesObjekt.setAusgewaehlt(false); }
	 * 
	 * this.ausgewahltesObjekt = o; o.setAusgewaehlt(true); }
	 */

	/*
	 * public void ObjektNachOben() { if (ausgewahltesObjekt != null) { int index =
	 * spielObjekte.indexOf(ausgewahltesObjekt); SpielObjekt o =
	 * spielObjekte.remove(index); spielObjekte.add(o); ausgewahltesObjekt = o; } }
	 * 
	 * public void ObjektNachUnten() { if (ausgewahltesObjekt != null) { int index =
	 * spielObjekte.indexOf(ausgewahltesObjekt); SpielObjekt o =
	 * spielObjekte.remove(index); spielObjekte.add(0, o); ausgewahltesObjekt = o; }
	 * }
	 */

	public void objekteMischen() {

		spielObjekte.removeAll(ausgewaehlteObjekte);
		Collections.shuffle(ausgewaehlteObjekte);
		spielObjekte.addAll(ausgewaehlteObjekte);
		objekteStapeln();
	}

	/**
	 * Bringt die ausgewählten Objekte zu einem gebündelten Stapel zusammen. Ändert
	 * dazu deren Position und Rotation auf die des ersten Elements des Stapels.
	 */
	public void objekteStapeln() {
		if (ausgewaehlteObjekte.size() > 0) {
			Vektor2D startPos = new Vektor2D(ausgewaehlteObjekte.get(0).getPosition());
			for (SpielObjekt o : ausgewaehlteObjekte) {
				o.setPosition(startPos.add(1, 0));
				o.setAusrichtung(ausgewaehlteObjekte.get(0).getAusrichtung());
			}
		}
	}

	/**
	 * 
	 * @param o
	 * @return das neu erzeuge Objekt. Null, wenn null übergeben wird.
	 */
	public SpielObjekt kopiereObjekt(SpielObjekt o) {
		if (o != null) {
			SpielObjekt neu = o.getCopy();
			if (neu != null) {
				platziereSpielObjekt(neu);
				return neu;
			}
		}
		return null;
	}

	//////////////////////////
	// Controller Functions
	//////////////////////////

	@Override
	public void handleLeftMouseDrag(double offX, double offY, Vektor2D firstMaus, Vektor2D lastMaus) {

		if (mouseOn) {
			if (ausgewaehlteObjekte.size() > 0) {
				verschiebeObjekte(offX, offY);
				verschoben = true;
			}
		} else {
			verschoben = false;

			// Auswahlrahmen

			int minXRahmen = firstMaus.getPosXInt();
			int minYRahmen = firstMaus.getPosYInt();
			int maxXRahmen = lastMaus.getPosXInt();
			int maxYRahmen = lastMaus.getPosYInt();

			if (firstMaus.getPosXInt() > lastMaus.getPosXInt()) {
				minXRahmen = lastMaus.getPosXInt();
				maxXRahmen = firstMaus.getPosXInt();
			}
			if (firstMaus.getPosYInt() > lastMaus.getPosYInt()) {
				minYRahmen = lastMaus.getPosYInt();
				maxYRahmen = firstMaus.getPosYInt();
			}

			auswahlrahmen = new int[4];
			auswahlrahmen[0] = minXRahmen;
			auswahlrahmen[1] = maxXRahmen;
			auswahlrahmen[2] = minYRahmen;
			auswahlrahmen[3] = maxYRahmen;
		}

	}

	@Override
	public void handleMausPress(int button, Vektor2D spielMausKoords) {

		verschoben = false;

		if (button == 1) {
			if (ausgewaehlteObjekte.size() == 0) {
				waehleObjekt(spielMausKoords.getPosX(), spielMausKoords.getPosY());
			} else {

				mouseOn = false;
				for (SpielObjekt o : ausgewaehlteObjekte) {
					if (o.checkKlick(spielMausKoords.getPosX(), spielMausKoords.getPosY())) {
						mouseOn = true;
						break;
					}
				}
				if (!mouseOn) {
					// Ausgewählte Objekte abwählen
					for (SpielObjekt o : ausgewaehlteObjekte) {
						o.setAusgewaehlt(false);
					}
					ausgewaehlteObjekte.clear();
					// Neues Objekt auswählen
					for (int i = spielObjekte.size() - 1; i >= 0; i--) {
						if (spielObjekte.get(i).checkKlick(spielMausKoords.getPosX(), spielMausKoords.getPosY())) {
							mouseOn = true;
							ausgewaehlteObjekte.add(spielObjekte.get(i));
							spielObjekte.get(i).setAusgewaehlt(true);
							break;
						}
					}
				}

			}
		} else if (button == 3) {

			// Ausgewählte Objekte abwählen
			for (SpielObjekt o : ausgewaehlteObjekte) {
				o.setAusgewaehlt(false);
			}
			ausgewaehlteObjekte.clear();
			// Neues Objekt auswählen
			for (int i = spielObjekte.size() - 1; i >= 0; i--) {
				if (spielObjekte.get(i).checkKlick(spielMausKoords.getPosX(), spielMausKoords.getPosY())) {
					mouseOn = true;
					ausgewaehlteObjekte.add(spielObjekte.get(i));
					spielObjekte.get(i).setAusgewaehlt(true);
					break;
				}
			}

			if (ausgewaehlteObjekte.size() == 1) {
				aktionRechtsKlick(spielMausKoords.getPosX(), spielMausKoords.getPosY());
			}
		}

	}

	@Override
	public void handleMausRad(int wheelRotation) {
		rotiere(wheelRotation);
	}

	@Override
	public boolean hatAusgewaehltesObjekt() {
		return (getAusgewaehlteObjekte().size() > 0);
	}

	@Override
	public void handleMausRelease(int aktButton, Vektor2D firstMaus, Vektor2D lastMaus) {

		if (!verschoben) {
			if (Math.abs(firstMaus.getPosX() - lastMaus.getPosX()) > 10 && Math.abs(firstMaus.getPosY() - lastMaus.getPosY()) > 10) {
				waehleObjekte(firstMaus.getPosXInt(), firstMaus.getPosYInt(), lastMaus.getPosXInt(), lastMaus.getPosYInt());
			} else {
				waehleObjekt(lastMaus.getPosXInt(), lastMaus.getPosY());
			}
		}
		auswahlrahmen = null;

	}

	@Override
	public void handleTischKeyControls(KeyEvent arg0) {
		for (TischControl c : tischCtrls.get(status)) {
			if (c.getKeyCode() == arg0.getKeyCode()) {
				c.performAction();
			}
		}
	}

	public List<TischControl> getCurrentTischControls() {
		return tischCtrls.get(status);
	}

}
