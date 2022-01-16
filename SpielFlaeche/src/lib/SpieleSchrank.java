package lib;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.PBFileReadWriter;
import math.Vektor2D;
import tisch.SpielBrett;
import tisch.objekte.SpielSymbol;
import tisch.objekte.karten.SpielBilderKarte;
import tisch.objekte.karten.SpielTextKarte;
import tisch.objekte.wuerfel.SpielBildWuerfel;
import tisch.objekte.wuerfel.SpielZahlenWuerfel;

/**
 * Lädt und Verwaltet SpieleSets
 * 
 * @author paulb
 *
 */
public class SpieleSchrank {

	private HashMap<String, SpielSet> sets;

	public SpieleSchrank() {

		this.sets = new HashMap<>();

		loadObjekte();

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
									new Vektor2D(Integer.parseInt(s[8]), Integer.parseInt(s[9])), PBFileReadWriter.createAbsPfad(s[10])));
				} else if (s[2].equals("zw")) {
					sets.get(s[1]).addObjekt(
							new SpielZahlenWuerfel(s[3], Integer.parseInt(s[4]), new Vektor2D(Integer.parseInt(s[5]), Integer.parseInt(s[6])), 50,
									new Color(Integer.parseInt(s[7]), Integer.parseInt(s[8]), Integer.parseInt(s[9])),
									new Color(Integer.parseInt(s[10]), Integer.parseInt(s[11]), Integer.parseInt(s[12])), s[13], 1));
				} else if (s[2].equals("bw")) {
					List<String> bildURLs = new ArrayList<>();
					for (int i = 6; i < 6 + Integer.parseInt(s[4]); i++) {
						bildURLs.add(s[i]);
					}
					sets.get(s[1]).addObjekt(new SpielBildWuerfel(s[3], new Vektor2D(), Integer.parseInt(s[5]), Integer.parseInt(s[4]), bildURLs, 1));
				} else if (s[2].equals("c")) {
					sets.get(s[1]).addObjekt(new SpielSymbol(s[3], new Vektor2D(0, 0), new Vektor2D(50, 50), new Vektor2D(25, 25),
							PBFileReadWriter.createAbsPfad(s[4])));
				} else if (s[2].equals("k")) {
					sets.get(s[1])
							.addObjekt(new SpielBilderKarte(s[3], new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5])),
									new Vektor2D(Integer.parseInt(s[6]), Integer.parseInt(s[7])), PBFileReadWriter.createAbsPfad(s[8]),
									PBFileReadWriter.createAbsPfad(s[9]), true));
				} else if (s[2].equals("kt")) {
					sets.get(s[1]).addObjekt(new SpielTextKarte(s[3], new Vektor2D(Integer.parseInt(s[4]), Integer.parseInt(s[5])),
							new Vektor2D(Integer.parseInt(s[6]), Integer.parseInt(s[7])), s[8], s[9], Boolean.parseBoolean(s[10])));
				}

			}
		}
	}

	public HashMap<String, SpielSet> getSpielSets() {
		return sets;
	}

}
