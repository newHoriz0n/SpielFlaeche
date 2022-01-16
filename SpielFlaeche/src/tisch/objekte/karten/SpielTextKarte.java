package tisch.objekte.karten;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

public class SpielTextKarte extends SpielKarte {

	private String textVorne;
	private String textHinten;

	private List<SpielKarteInhalt> inhalteVorne;
	private List<SpielKarteInhalt> inhalteHinten;

	public SpielTextKarte(String bezeichnung, Vektor2D position, Vektor2D groesse, String textVorne, String textHinten, boolean offen) {

		super(bezeichnung, position, groesse, offen);
		getKartenInhalte(textVorne, textHinten);

	}

	public SpielTextKarte(SpielTextKarte k) {
		super(k);

		getKartenInhalte(k.textVorne, k.textHinten);

	}

	private void getKartenInhalte(String infoVorne, String infoHinten) {

		this.textVorne = infoVorne;
		this.textHinten = infoHinten;

		this.inhalteVorne = new ArrayList<>();
		this.inhalteHinten = new ArrayList<>();

		String separator = ";";
		String[] vorneInhalte = textVorne.split(separator);
		String[] hintenInhalte = textHinten.split(separator);

		int offYStandard = 30;

		int offY = offYStandard;

		for (String s : vorneInhalte) {
			SpielKarteInhalt ski = new SpielKarteInhalt(s, new Vektor2D(20, offY));
			inhalteVorne.add(ski);
			offY += 30;
		}

		offY = offYStandard;
		for (String s : hintenInhalte) {
			SpielKarteInhalt ski = new SpielKarteInhalt(s, new Vektor2D(20, offY));
			inhalteHinten.add(ski);
			offY += 30;
		}

	}

	@Override
	public void drawSpielObjekt(Graphics2D g, double rotation) {

		AffineTransform at = new AffineTransform();
		at.translate(position.getPosXInt() - center.getPosXInt(), position.getPosYInt() - center.getPosYInt());
		at.rotate(-winkel, center.getPosX(), center.getPosY());

		g.transform(at);

		if (offen) {
			for (SpielKarteInhalt ski : inhalteVorne) {
				ski.drawSpielKartenInhalt(g);
			}
		} else {
			for (SpielKarteInhalt ski : inhalteHinten) {
				ski.drawSpielKartenInhalt(g);
			}
		}

		try {
			g.transform(at.createInverse());
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int[][] polygon = calcPolygon();

		g.setColor(Color.BLACK);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		g.drawPolygon(polygon[0], polygon[1], 4);

	}

	@Override
	public String toSendString() {
		return "*,kt," + bezeichnung + "," + position.getPosXInt() + "," + position.getPosYInt() + "," + groesse.getPosXInt() + ","
				+ groesse.getPosYInt() + "," + textVorne + "," + textHinten + "," + offen;
	}

	@Override
	public SpielObjekt getCopy() {
		return new SpielTextKarte(bezeichnung, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), new Vektor2D(groesse), textVorne,
				textHinten, offen);
	}
}
