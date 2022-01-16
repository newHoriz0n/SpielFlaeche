package tisch;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exe.PSpielFlaecheView;
import io.PBFileReadWriter;
import lib.SpielSet;
import math.Vektor2D;
import tisch.objekte.SpielObjekt;
import tisch.objekte.SpielSymbol;

/**
 * 
 * @author PC Paul I
 *
 *
 *         Enthält alles, was auf einem Spieltisch liegt...
 */
public class Tisch {

	private List<SpielBrett> spielbretter;
	private List<SpielObjekt> spielObjekte;
	private List<SpielSichtSchutz> spielSichtSchutze;
	// private ListIterator<SpielObjekt> spielObjekteIterator;

	private SpielObjekt ausgewahltesObjekt;
	private List<SpielObjekt> ausgewaehlteObjekte;

	private PSpielFlaecheView view;

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
	}

	public void platziereSpielSichtSchutz(SpielSichtSchutz s) {
		spielSichtSchutze.add(s);
		for (SpielSymbol symb : s.getEcken()) {
			spielObjekte.add(symb);
		}
	}

	public void drawTisch(Graphics2D g, double bildRotation) {
		for (SpielBrett s : spielbretter) {
			s.paint(g);
		}

		for (SpielObjekt o : spielObjekte) {
			o.drawSpielObjekt(g, bildRotation);
		}

		for (SpielSichtSchutz s : spielSichtSchutze) {
			s.drawSpielObjekt(g, bildRotation);
		}
	}

	public void waehleObjekte(int firstMausX, int firstMausY, int lastMausX, int lastMausY) {

		ausgewaehlteObjekte.clear();
		for (int i = 0; i < spielObjekte.size(); i++) {

			if (spielObjekte.get(i).checkRahmenAuswahl(firstMausX, firstMausY, lastMausX, lastMausY)) {
				spielObjekte.get(i).setAusgewaehlt(true);
				ausgewaehlteObjekte.add(spielObjekte.get(i));
			} else {
				spielObjekte.get(i).setAusgewaehlt(false);
			}
		}

	}

	public void waehleObjekt(double d, double e) {

		ausgewahltesObjekt = null;

		// while(spielObjekteIterator.hasPrevious()) {
		// spielObjekteIterator.previous();
		// }
		// while (spielObjekteIterator.hasNext()) {
		// System.out.println(spielObjekteIterator.next().groesse);
		// }
		//
		// for (SpielObjekt o : spielObjekte) {
		// if (o.checkKlick(d, e)) {
		// ausgewahltesObjekt = o;
		// o.setAusgewaehlt(true);
		//// spielObjekte.remove(o);
		//// spielObjekte.add(o);
		// } else {
		// o.setAusgewaehlt(false);
		// }
		// }
		//

		int ausgewaeltIndex = -1;
		for (int i = 0; i < spielObjekte.size(); i++) {
			if (spielObjekte.get(i).checkKlick(d, e)) {
				if (ausgewahltesObjekt != null) {
					ausgewahltesObjekt.setAusgewaehlt(false);
				}
				ausgewahltesObjekt = spielObjekte.get(i);
				ausgewahltesObjekt.setAusgewaehlt(true);
				ausgewaeltIndex = i;
			} else {
				spielObjekte.get(i).setAusgewaehlt(false);
			}
		}
		if (ausgewaeltIndex != -1) {
			spielObjekte.add(spielObjekte.size() - 1, spielObjekte.remove(ausgewaeltIndex));
		}

	}

	public void rotiere(double winkel) {
		if (ausgewahltesObjekt != null) {
			ausgewahltesObjekt.rotiere(winkel * Math.PI / 8.0);
		}
		// for (SpielObjekt s : ausgewaehlteObjekte) {
		// s.rotiere(winkel * Math.PI / 8.0);
		// }
	}

	public void verschiebeObjekt(double offX, double offY) {
		if (ausgewahltesObjekt != null) {
			ausgewahltesObjekt.verschiebe(offX, offY);
		}
		// for (SpielObjekt s : ausgewaehlteObjekte) {
		// s.verschiebe(offX, offY);
		// }

	}

	public Object getAusgewaehltesObjekt() {
		return ausgewahltesObjekt;
	}

	public List<SpielObjekt> getAusgewaehlteObjekte() {
		return ausgewaehlteObjekte;
	}

	/**
	 * 
	 * @param maus
	 * @return true wenn sich etwas ändert.
	 */
	public boolean checkMouseOver(Vektor2D maus) {
		boolean aenderung = false;
		for (SpielObjekt o : spielObjekte) {
			if (o.checkMouseOver(maus)) {
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

	public void aktionRechtsKlickMultiSelect(double x, double y) {

		ausgewaehlteObjekte.clear();

		for (SpielObjekt o : spielObjekte) {
			if (o.checkKlick(x, y)) {
				ausgewahltesObjekt = o;
				ausgewaehlteObjekte.add(o);
				o.setAusgewaehlt(true);
			} else {
				o.setAusgewaehlt(false);
			}
		}

		if (ausgewaehlteObjekte.size() != 0) {
			for (SpielObjekt s : ausgewaehlteObjekte) {
				s.aktionRechtsKlick();
			}
		}

	}

	public void aktionRechtsKlick(double x, double y) {

		ausgewahltesObjekt = null;

		for (SpielObjekt o : spielObjekte) {
			if (o.checkKlick(x, y)) {
				ausgewahltesObjekt = o;
				o.setAusgewaehlt(true);
			} else {
				o.setAusgewaehlt(false);
			}
		}

		if (ausgewahltesObjekt != null) {
			ausgewahltesObjekt.aktionRechtsKlick();
		}

	}

	public void updateView() {
		if (view != null) {
			view.repaint();
		}
	}

	public void setView(PSpielFlaecheView v) {
		this.view = v;
	}

	public void entferneObjekt() {
		if (ausgewahltesObjekt != null) {
			spielObjekte.remove(ausgewahltesObjekt);
			ausgewahltesObjekt = null;
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Serialisierung abgeschlossen!");

	}

	public void objektAuswaehlen(SpielObjekt o) {
		ausgewahltesObjekt.setAusgewaehlt(false);
		this.ausgewahltesObjekt = o;
		o.setAusgewaehlt(true);
	}

	public void ObjektNachOben() {
		if (ausgewahltesObjekt != null) {
			int index = spielObjekte.indexOf(ausgewahltesObjekt);
			SpielObjekt o = spielObjekte.remove(index);
			spielObjekte.add(o);
			ausgewahltesObjekt = o;
		}
	}

	public void ObjektNachUnten() {
		if (ausgewahltesObjekt != null) {
			int index = spielObjekte.indexOf(ausgewahltesObjekt);
			SpielObjekt o = spielObjekte.remove(index);
			spielObjekte.add(0, o);
			ausgewahltesObjekt = o;
		}
	}

	public void ObjekteMischen() {
		Collections.shuffle(spielObjekte);
	}

	public void kopiereObjekt() {
		if (ausgewahltesObjekt != null) {
			SpielObjekt neu = ausgewahltesObjekt.getCopy();
			if (neu != null) {
				spielObjekte.add(neu);
			}
			objektAuswaehlen(neu);
		}
	}

}
