package tisch.objekte.karten;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.PBFileReadWriter;
import math.Vektor2D;
import tisch.Tisch;
import tisch.objekte.SpielObjekt;

public class SpielBilderKarte extends SpielKarte{


	protected String bildURLvorne;
	protected transient BufferedImage bildVorne;
	protected String bildURLhinten;
	protected transient BufferedImage bildHinten;
	
	public SpielBilderKarte(String bezeichnung, Vektor2D position, Vektor2D groesse, String bildURLvorne, String bildURLhinten, boolean offen,
			Tisch spielFlaeche) {
		
		super(bezeichnung, position, groesse, offen, spielFlaeche);
		
		try {
			bildVorne = ImageIO.read(new File(bildURLvorne));
			bildHinten = ImageIO.read(new File(bildURLhinten));
		} catch (IOException e) {
			System.out.println("Bildladen fehlgeschlagen");
		}

		if (offen) {
			bild = bildVorne;
			bildURL = bildURLvorne;
		} else {
			bild = bildHinten;
			bildURL = bildURLhinten;
		}

		this.bildURLvorne = bildURLvorne;
		this.bildURLhinten = bildURLhinten;
		
	}
	
	public SpielBilderKarte(SpielBilderKarte s) {
		
		super(s);
		
		this.bildURLvorne = s.bildURLvorne;
		this.bildURLhinten = s.bildURLhinten;
		

		try {
			bildVorne = ImageIO.read(new File(bildURLvorne));
			bildHinten = ImageIO.read(new File(bildURLhinten));
		} catch (IOException e) {
		}

		if (offen) {
			bild = bildVorne;
			bildURL = bildURLvorne;
		} else {
			bild = bildHinten;
			bildURL = bildURLhinten;
		}
		
	}
	
	protected void umdrehen() {
		
		super.umdrehen();
		
		if (bildVorne == null) {
			if (bildURLvorne != "") {
				try {
					bildVorne = ImageIO.read(new File(bildURLvorne));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (bildHinten == null) {
			if (bildURLhinten != "") {
				try {
					bildHinten = ImageIO.read(new File(bildURLhinten));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (offen) {
			bild = bildVorne;
			bildURL = bildURLvorne;
		} else {
			bild = bildHinten;
			bildURL = bildURLhinten;
		}
	}
	
	@Override
	public void drawSpielObjekt(Graphics2D g, double rotation) {

		AffineTransform at = new AffineTransform();
		at.translate(position.getPosXInt() - center.getPosXInt(), position.getPosYInt() - center.getPosYInt());
		at.rotate(-winkel, center.getPosX(), center.getPosY());
		
		g.drawImage(bild, at, null);

		// int[][] polygon = calcPolygon();
		//
		// g.setColor(Color.BLACK);
		// if (mausOver) {
		// g.setColor(Color.GREEN);
		// }
		// if (ausgewaehlt) {
		// g.setColor(Color.RED);
		// }
		// g.drawPolygon(polygon[0], polygon[1], 4);

	}
	
	@Override
	public String toSendString() {
		return "*,k," + bezeichnung + "," + position.getPosXInt() + "," + position.getPosYInt() + "," + groesse.getPosXInt() + ","
				+ groesse.getPosYInt() + "," + PBFileReadWriter.createRelPfad(bildURLvorne) + "," + PBFileReadWriter.createRelPfad(bildURLhinten)
				+ "," + offen;
	}


	@Override
	public SpielObjekt getCopy() {
		return new SpielBilderKarte(bezeichnung, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), new Vektor2D(groesse), bildURLvorne, bildURLhinten, offen, spielFlaeche);
	}
	
}
