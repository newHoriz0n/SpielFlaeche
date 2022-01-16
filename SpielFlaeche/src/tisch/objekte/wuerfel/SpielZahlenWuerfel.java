package tisch.objekte.wuerfel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

/**
 * Würfel aus Zahlen
 * @author paulb
 *
 */
public class SpielZahlenWuerfel extends SpielWuerfel {

	private Color farbe;
	private Color augenfarbe;
	private Font zahlFont = new Font("Arial", Font.BOLD, 24);

	public SpielZahlenWuerfel(String bezeichnung, int flaechenZahl, Vektor2D position, int groesse, Color farbe, Color augenfarbe,
			String bildURL, int aktFlaeche) {
		super(bezeichnung, position, groesse, flaechenZahl, bildURL, aktFlaeche);

		this.farbe = farbe;
		this.augenfarbe = augenfarbe;
	}

	public SpielZahlenWuerfel(SpielZahlenWuerfel w) {
		super(w.bezeichnung, new Vektor2D(w.position), w.groesse.getPosXInt(), w.flaechenZahl, w.bildURL, w.aktFlaeche);

		this.farbe = w.farbe;
		this.augenfarbe = w.augenfarbe;
	}

	@Override
	public void drawSpielObjekt(Graphics2D g, double rotation) {

		int[][] polygon = calcPolygon();
		g.setColor(farbe);
		g.fillPolygon(polygon[0], polygon[1], 4);

		g.setColor(Color.WHITE);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		g.drawPolygon(polygon[0], polygon[1], 4);

		g.setColor(augenfarbe);
		g.setFont(zahlFont);
		if (aktFlaeche < 10) {
			g.drawString("" + aktFlaeche, position.getPosXInt() - 8, position.getPosYInt() + 8);
		} else {
			g.drawString("" + aktFlaeche, position.getPosXInt() - 13, position.getPosYInt() + 8);
		}
	}

	private String getColorToString(Color c) {
		String s = "";
		s += c.getRed() + "," + c.getGreen() + "," + c.getBlue();
		return s;
	}

	@Override
	public String toSendString() {
		return "*,zw," + bezeichnung + "," + flaechenZahl + "," + position.getPosXInt() + "," + position.getPosYInt() + "," + groesse.getPosXInt()
				+ "," + getColorToString(farbe) + "," + getColorToString(augenfarbe) + "," + aktFlaeche + "," + bildURL;
	}

	@Override
	public SpielObjekt getCopy() {
		return new SpielZahlenWuerfel(bezeichnung, flaechenZahl, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), groesse.getPosXInt(),
				farbe, augenfarbe, bildURL, aktFlaeche);
	}

}
