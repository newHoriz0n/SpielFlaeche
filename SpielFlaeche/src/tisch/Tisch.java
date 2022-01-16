package tisch;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import io.PBFileReadWriter;
import lib.SpielSet;
import math.Vektor2D;
import tisch.objekte.SpielObjekt;
import tisch.objekte.SpielSymbol;
import tisch.objekte.karten.SpielBilderKarte;
import tisch.objekte.karten.SpielTextKarte;
import tisch.objekte.wuerfel.SpielBildWuerfel;
import tisch.objekte.wuerfel.SpielZahlenWuerfel;

/**
 * 
 * @author PC Paul I
 *
 *
 *         Enthält alles, was auf einem Spieltisch liegt...
 */
public class Tisch {

	private HashMap<String, SpielSet> sets;

	private List<SpielBrett> spielbretter;
	private List<SpielObjekt> spielObjekte;
	private List<SpielSichtSchutz> spielSichtSchutze;
	// private ListIterator<SpielObjekt> spielObjekteIterator;

	private SpielObjekt ausgewahltesObjekt;
	private List<SpielObjekt> ausgewaehlteObjekte;

	private JPanel view;

	public Tisch() {

		this.spielbretter = new ArrayList<>();
		this.spielObjekte = new ArrayList<>();
		this.spielSichtSchutze = new ArrayList<>();
		this.ausgewaehlteObjekte = new ArrayList<>();

		this.sets = new HashMap<>();
		loadObjekte();

		// List<Vektor2D> ecken = new ArrayList<>();
		// ecken.add(new Vektor2D(100, 100));
		// ecken.add(new Vektor2D(500, 100));
		// ecken.add(new Vektor2D(500, 300));
		// ecken.add(new Vektor2D(100, 300));
		// SpielSichtSchutz s = new SpielSichtSchutz("test", ecken, this);
		// platziereSpielSichtSchutz(s);

	}

	public Tisch(List<String[]> tischInfos) {

		System.out.println("Lade Tisch aus Datei...");

		this.spielbretter = new ArrayList<>();
		this.spielObjekte = new ArrayList<>();
		this.spielSichtSchutze = new ArrayList<>();
		this.ausgewaehlteObjekte = new ArrayList<>();

		this.sets = new HashMap<>();
		loadObjekte();

		for (String[] info : tischInfos) {

			if (info.length > 1) {
				if (info[1].equals("b")) {
					SpielBrett b = new SpielBrett(info[2], PBFileReadWriter.createAbsPfad(info[3]),
							new Vektor2D(Integer.parseInt(info[4]), Integer.parseInt(info[5])));
					platziere(b, new Vektor2D());
				} else if (info[1].equals("s")) {
					SpielSymbol s = new SpielSymbol(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
							new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])),
							new Vektor2D(Integer.parseInt(info[7]), Integer.parseInt(info[8])), PBFileReadWriter.createAbsPfad(info[9]), this);
					platziere(s, new Vektor2D(s.getPosition()));
				} else if (info[1].equals("zw")) {
					SpielZahlenWuerfel w = new SpielZahlenWuerfel(info[2], Integer.parseInt(info[3]),
							new Vektor2D(Integer.parseInt(info[4]), Integer.parseInt(info[5])), Integer.parseInt(info[6]),
							new Color(Integer.parseInt(info[7]), Integer.parseInt(info[8]), Integer.parseInt(info[9])),
							new Color(Integer.parseInt(info[10]), Integer.parseInt(info[11]), Integer.parseInt(info[12])), this, info[14], 1);
					w.setAktFlaeche(Integer.parseInt(info[13]));
					platziere(w, new Vektor2D(w.getPosition()));
				} else if (info[1].equals("bw")) {
					List<String> bildURLs = new ArrayList<>();
					for (int i = 8; i < 8 + Integer.parseInt(info[3]); i++) {
						bildURLs.add(info[i]);
					}
					SpielBildWuerfel w = new SpielBildWuerfel(info[2], new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])),
							Integer.parseInt(info[7]), this, Integer.parseInt(info[3]), bildURLs, 1);
					w.setAktFlaeche(Integer.parseInt(info[4]));
					platziere(w, new Vektor2D(w.getPosition()));
				} else if (info[1].equals("k")) {
					SpielBilderKarte k = new SpielBilderKarte(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
							new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), PBFileReadWriter.createAbsPfad(info[7]),
							PBFileReadWriter.createAbsPfad(info[8]), Boolean.parseBoolean(info[9]), this);
					platziere(k, new Vektor2D(k.getPosition()));
				} else if (info[1].equals("kt")) {
					SpielTextKarte k = new SpielTextKarte(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
							new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), info[7],
							info[8], Boolean.parseBoolean(info[9]), this);
					platziere(k, new Vektor2D(k.getPosition()));
				}
			}
		}

		System.out.println("Laden aus Datei abgeschlossen!");

	}

	/**
	 * Lade Library aus objekte.txt
	 */
	private void loadObjekte() {

		List<String[]> lines = PBFileReadWriter.getContentFromFile(",", PBFileReadWriter.createAbsPfad("lib/objekte.txt"));
		for (String[] s : lines) {
			if (s[0].equals("#")) { // Spielsetzubehör
				if (!sets.containsKey(s[1])) { // Neues Set anlegen falls nicht vorhanden
					sets.put(s[1], new SpielSet(s[1]));
				}
				if (s[2].equals("b")) { // Brett oder Figur in Set anlegen
					sets.get(s[1]).addBrett(
							new SpielBrett(s[3], PBFileReadWriter.createAbsPfad(s[6]), new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5]))));
				} else if (s[2].equals("s")) {
					sets.get(s[1])
							.addObjekt(new SpielSymbol(s[3], new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5])),
									new Vektor2D(Integer.parseInt(s[6]), Integer.parseInt(s[7])),
									new Vektor2D(Integer.parseInt(s[8]), Integer.parseInt(s[9])), PBFileReadWriter.createAbsPfad(s[10]), this));
				} else if (s[2].equals("zw")) {
					sets.get(s[1]).addObjekt(
							new SpielZahlenWuerfel(s[3], Integer.parseInt(s[4]), new Vektor2D(Integer.parseInt(s[5]), Integer.parseInt(s[6])), 50,
									new Color(Integer.parseInt(s[7]), Integer.parseInt(s[8]), Integer.parseInt(s[9])),
									new Color(Integer.parseInt(s[10]), Integer.parseInt(s[11]), Integer.parseInt(s[12])), this, s[13], 1));
				} else if (s[2].equals("bw")) {
					List<String> bildURLs = new ArrayList<>();
					for (int i = 6; i < 6 + Integer.parseInt(s[4]); i++) {
						bildURLs.add(s[i]);
					}
					sets.get(s[1])
							.addObjekt(new SpielBildWuerfel(s[3], new Vektor2D(), Integer.parseInt(s[5]), this, Integer.parseInt(s[4]), bildURLs, 1));
				} else if (s[2].equals("c")) {
					sets.get(s[1]).addObjekt(new SpielSymbol(s[3], new Vektor2D(0, 0), new Vektor2D(50, 50), new Vektor2D(25, 25),
							PBFileReadWriter.createAbsPfad(s[4]), this));
				} else if (s[2].equals("k")) {
					sets.get(s[1])
							.addObjekt(new SpielBilderKarte(s[3], new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5])),
									new Vektor2D(Integer.parseInt(s[6]), Integer.parseInt(s[7])), PBFileReadWriter.createAbsPfad(s[8]),
									PBFileReadWriter.createAbsPfad(s[9]), true, this));
				} else if (s[2].equals("kt")) {
					sets.get(s[1]).addObjekt(new SpielTextKarte(s[3], new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5])),
							new Vektor2D(Integer.parseInt(s[6]), Integer.parseInt(s[7])), s[8], s[9], Boolean.parseBoolean(s[10]), this));
				}

			}
		}

		Collections.shuffle(spielObjekte);

	}

	public void platziere(SpielSet set, Vektor2D position) {
		for (SpielObjekt o : set.getObjekte()) {
			Vektor2D neuePos = new Vektor2D(o.getPosition());
			neuePos.add(position);
			if (o.getClass().equals(SpielSymbol.class)) {
				SpielSymbol s = new SpielSymbol(o);
				s.setPosition(neuePos);
				spielObjekte.add(s);
			} else if (o.getClass().equals(SpielZahlenWuerfel.class)) {
				SpielObjekt ob = new SpielZahlenWuerfel((SpielZahlenWuerfel) o);
				ob.setPosition(neuePos);
				spielObjekte.add(ob);
			} else if (o.getClass().equals(SpielBildWuerfel.class)) {
				SpielObjekt ob = new SpielBildWuerfel((SpielBildWuerfel) o);
				ob.setPosition(neuePos);
				spielObjekte.add(ob);
			} else if (o.getClass().equals(SpielBilderKarte.class)) {
				SpielObjekt ob = new SpielBilderKarte((SpielBilderKarte) o);
				ob.setPosition(neuePos);
				spielObjekte.add(ob);
			} else if (o.getClass().equals(SpielTextKarte.class)) {
				SpielObjekt ob = new SpielTextKarte((SpielTextKarte) o);
				ob.setPosition(neuePos);
				spielObjekte.add(ob);
			}
		}
		for (SpielBrett b : set.getBretter()) {
			SpielBrett neu = new SpielBrett(b);
			Vektor2D neuePos = new Vektor2D(b.getPosition());
			neuePos.add(position);
			neu.setPosition(neuePos);
			spielbretter.add(neu);
		}
	}

	public void platziere(SpielBrett b, Vektor2D position) {
		SpielBrett neu = new SpielBrett(b);
		Vektor2D neuePos = new Vektor2D(b.getPosition());
		neuePos.add(position);
		neu.setPosition(neuePos);
		spielbretter.add(neu);
	}

	public void platziere(SpielObjekt o, Vektor2D position) {
		if (o.getClass().equals(SpielSymbol.class)) {
			SpielSymbol s = new SpielSymbol(o);
			s.setPosition(position);
			spielObjekte.add(s);
		} else if (o.getClass().equals(SpielZahlenWuerfel.class)) {
			SpielObjekt ob = new SpielZahlenWuerfel((SpielZahlenWuerfel) o);
			ob.setPosition(position);
			spielObjekte.add(ob);
		} else if (o.getClass().equals(SpielBildWuerfel.class)) {
			SpielObjekt ob = new SpielBildWuerfel((SpielBildWuerfel) o);
			ob.setPosition(position);
			spielObjekte.add(ob);
		} else if (o.getClass().equals(SpielBilderKarte.class)) {
			SpielObjekt ob = new SpielBilderKarte((SpielBilderKarte) o);
			ob.setPosition(position);
			spielObjekte.add(ob);
		} else if (o.getClass().equals(SpielTextKarte.class)) {
			SpielObjekt ob = new SpielTextKarte((SpielTextKarte) o);
			ob.setPosition(position);
			spielObjekte.add(ob);
		}
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

	public HashMap<String, SpielSet> getSpielSets() {
		return sets;
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

	public void setView(JPanel v) {
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
