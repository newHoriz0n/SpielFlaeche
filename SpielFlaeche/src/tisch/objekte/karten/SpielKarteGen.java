package tisch.objekte.karten;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

// TODO: Karten wieder laden

public class SpielKarteGen extends SpielKarte {

	protected Color farbeVorne;
	protected String bildVorneURL;
	protected transient BufferedImage bildVorne;
	private List<KartenTextAbschnitt> texteVorne;
	protected Color farbeHinten;
	protected String bildHintenURL;
	protected transient BufferedImage bildHinten;
	private List<KartenTextAbschnitt> texteHinten;

	protected BufferedImage renderedVorne;
	protected BufferedImage renderedHinten;
	protected String renderedVorneURL;
	protected String renderedHintenURL;

	public SpielKarteGen(String bezeichnung, Vektor2D position, Vektor2D groesse, double rotation, boolean offen, Color farbeVorne,
			String bildVorneURL, List<KartenTextAbschnitt> texteVorne, String renderedVorneURL, Color farbeHinten, String bildHintenURL,
			List<KartenTextAbschnitt> texteHinten, String renderedHintenURL) {

		super(bezeichnung, position, groesse, offen);

		winkel = rotation;

		try {
			this.bildVorne = ImageIO.read(new File(bildVorneURL));
			this.bildHinten = ImageIO.read(new File(bildHintenURL));
		} catch (IOException e) {
			System.out.println("Bildladen fehlgeschlagen: " + bildVorneURL + " / " + bildHintenURL);
		}

		try {
			this.renderedVorne = ImageIO.read(new File(renderedVorneURL));
			this.renderedHinten = ImageIO.read(new File(renderedHintenURL));
		} catch (IOException e) {
			System.out.println("Bildladen fehlgeschlagen: " + renderedVorneURL + " / " + renderedHintenURL);
		}

		if (offen) {
			bild = renderedVorne;
			bildURL = renderedVorneURL;
		} else {
			bild = renderedHinten;
			bildURL = renderedHintenURL;
		}

		this.farbeVorne = farbeVorne;
		this.texteVorne = texteVorne;
		this.bildVorneURL = bildVorneURL;
		this.farbeHinten = farbeHinten;
		this.texteHinten = texteHinten;
		this.bildHintenURL = bildHintenURL;
		this.renderedVorneURL = renderedVorneURL;
		this.renderedHintenURL = renderedHintenURL;

	}

	public SpielKarteGen(SpielKarteGen s) {
		super(s);

		this.farbeVorne = s.farbeVorne;
		this.farbeHinten = s.farbeHinten;
		this.texteVorne = s.texteVorne;
		this.texteHinten = s.texteHinten;
		this.bildVorneURL = s.bildVorneURL;
		this.bildHintenURL = s.bildHintenURL;
		this.renderedVorneURL = s.renderedVorneURL;
		this.renderedHintenURL = s.renderedHintenURL;

		try {
			bildVorne = ImageIO.read(new File(bildVorneURL));
			bildHinten = ImageIO.read(new File(bildHintenURL));
		} catch (IOException e) {
		}

		// Karte auf die richtige Seite drehen

		if (offen) {
			bild = renderedVorne;
			bildURL = renderedVorneURL;
		} else {
			bild = renderedHinten;
			bildURL = renderedHintenURL;
		}

	}

	protected void umdrehen() {

		super.umdrehen();

		if (renderedVorne == null) {
			if (renderedVorneURL != "") {
				try {
					renderedVorne = ImageIO.read(new File(renderedVorneURL));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (renderedHinten == null) {
			if (renderedHintenURL != "") {
				try {
					renderedHinten = ImageIO.read(new File(renderedHintenURL));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (offen) {
			bild = renderedVorne;
			bildURL = renderedVorneURL;
		} else {
			bild = renderedHinten;
			bildURL = renderedHintenURL;
		}
	}

	@Override
	public SpielObjekt getCopy() {
		return new SpielKarteGen(bezeichnung, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), new Vektor2D(groesse), winkel, offen,
				farbeVorne, bildVorneURL, texteVorne, renderedVorneURL, farbeHinten, bildHintenURL, texteHinten, renderedHintenURL);
	}

	@Override
	public String toSendString() {
		String out = "*,kg," + bezeichnung + "," + position.getPosXInt() + "," + position.getPosYInt() + "," + groesse.getPosXInt() + ","
				+ groesse.getPosYInt() + "," + winkel + "," + offen + "," + farbeVorne.getRGB() + "," + bildVorneURL + ",";

		// Texte Vorne
		String texteVorneString = "";
		for (KartenTextAbschnitt t : texteVorne) {
			texteVorneString += t.toSendString() + ";";
		}
		if (texteVorneString.length() > 0) {
			texteVorneString = texteVorneString.substring(0, texteVorneString.length() - 1);
		}
		out += texteVorneString + ",";

		out += renderedVorneURL + "," + farbeHinten.getRGB() + "," + bildHintenURL + ",";

		// Texte Hinten
		String texteHintenString = "";
		for (KartenTextAbschnitt t : texteHinten) {
			texteHintenString += t.toSendString() + ";";
		}
		if (texteHintenString.length() > 0) {
			texteHintenString = texteHintenString.substring(0, texteHintenString.length() - 1);
		}
		out += texteHintenString + ",";

		out += renderedHintenURL;

		return out;

	}

	public static List<KartenTextAbschnitt> getTextAbschnitteVonSendString(String string) {

		List<KartenTextAbschnitt> texteAbschnitte = new ArrayList<>();

		String[] texte = string.split(";");
		for (String s : texte) {
			if (s.length() != 0) {
				texteAbschnitte.add(new KartenTextAbschnitt(s));
			}
		}

		return texteAbschnitte;
	}

	@Override
	public void drawSpielObjekt(Graphics2D g, double rotation) {

		AffineTransform at = new AffineTransform();
		at.translate(position.getPosXInt() - center.getPosXInt(), position.getPosYInt() - center.getPosYInt());
		at.rotate(-winkel, center.getPosX(), center.getPosY());

		g.drawImage(bild, at, null);

		int[][] polygon = calcPolygon();

		// g.setColor(Color.BLACK);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		g.drawPolygon(polygon[0], polygon[1], 4);

	}

}
