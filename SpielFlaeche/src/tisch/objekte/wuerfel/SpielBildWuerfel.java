package tisch.objekte.wuerfel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

/**
 * Würfel wo die Seiten aus Bildern generiert werden
 * @author paulb
 *
 */
public class SpielBildWuerfel extends SpielWuerfel {

	private List<String> bildURLs;
	private List<BufferedImage> bilder;

	public SpielBildWuerfel(String bezeichnung, Vektor2D position, int groesse, int flaechenzahl, List<String> bildURLs,
			int aktFlaeche) {
		super(bezeichnung, position, groesse, flaechenzahl, bildURLs.get(0), aktFlaeche);

		this.bildURLs = bildURLs;
		this.bilder = new ArrayList<BufferedImage>();

		for (String s : bildURLs) {
			try {
				bilder.add(ImageIO.read(new File(s)));
			} catch (IOException e) {
				System.out.println("Cant read Bildwuerfelbild.");
				e.printStackTrace();
			}
		}

	}

	public SpielBildWuerfel(SpielBildWuerfel w) {
		super(w.bezeichnung, w.position, w.groesse.getPosXInt(), w.flaechenZahl, w.getBildURL(), w.aktFlaeche);
		this.bildURLs = w.bildURLs;
		this.bilder = w.bilder;
	}

	@Override
	public void drawSpielObjekt(Graphics2D g, double rotation) {

		g.drawImage(bilder.get(aktFlaeche - 1), position.getPosXInt() - (groesse.getPosXInt() / 2),
				position.getPosYInt() - (groesse.getPosXInt() / 2), null);

		int[][] polygon = calcPolygon();

		g.setColor(Color.WHITE);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		g.drawPolygon(polygon[0], polygon[1], 4);

	}

	public String getBildURLStrings() {

		String s = "";

		for (String u : bildURLs) {
			s += u + ",";
		}
		s.substring(0, s.length() - 1);

		return s;

	}

	@Override
	public String toSendString() {
		return "*,bw," + bezeichnung + "," + flaechenZahl + "," + aktFlaeche + "," + position.getPosXInt() + "," + position.getPosYInt() + ","
				+ groesse.getPosXInt() + "," + getBildURLStrings();
	}

	@Override
	public SpielObjekt getCopy() {
		return new SpielBildWuerfel(bezeichnung, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), groesse.getPosXInt(),
				flaechenZahl, bildURLs, aktFlaeche);
	}

}
