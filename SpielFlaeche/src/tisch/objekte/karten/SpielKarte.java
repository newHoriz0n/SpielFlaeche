package tisch.objekte.karten;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

/**
 * Spielkarte auf Basis von Bildern
 * @author paulb
 *
 */
public abstract class SpielKarte extends SpielObjekt {

	protected boolean offen;

	public SpielKarte(String bezeichnung, Vektor2D position, Vektor2D groesse, boolean offen) {

		super(bezeichnung, position, groesse, new Vektor2D(groesse.getPosX() / 2, groesse.getPosY() / 2));

		this.offen = offen;

	}

	public SpielKarte(SpielKarte s) {
		super(s.bezeichnung, new Vektor2D(s.position), new Vektor2D(s.groesse), new Vektor2D(s.center));

		this.offen = s.offen;

	}

	@Override
	public void aktionRechtsKlick() {

		umdrehen();

	}
	
	protected void umdrehen() {
		offen = !offen;
	}


	@Override
	public void handleCommand(String cmd) {
		// TODO Auto-generated method stub

	}

}
