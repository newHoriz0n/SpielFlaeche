package tisch.objekte;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import io.PBFileReadWriter;
import math.Vektor2D;
import tisch.Tisch;

public class SpielSymbol extends SpielObjekt {

	public SpielSymbol(String bezeichnung, Vektor2D position, Vektor2D groesse, Vektor2D mitte, String bildURL, Tisch spielFlaeche) {
		super(bezeichnung, position, groesse, mitte, bildURL, spielFlaeche);
	}

	public SpielSymbol(SpielObjekt o) {
		super(o.bezeichnung, o.position, o.groesse, o.center, o.bildURL, o.spielFlaeche);
	}

	@Override
	public void drawSpielObjekt(Graphics2D g, double bildRotation) {

		AffineTransform at = new AffineTransform();
		at.translate(position.getPosXInt() - center.getPosXInt(), position.getPosYInt() - center.getPosYInt());
		at.rotate(-bildRotation, center.getPosX(), center.getPosY());
		g.drawImage(bild, at, null);
		
		if(mausOver) {
			g.setColor(Color.GREEN);
			g.drawRect((int) (position.getPosX() - center.getPosX()), (int) (position.getPosY() - center.getPosY()), groesse.getPosXInt(),
					groesse.getPosYInt());			
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
			g.drawRect((int) (position.getPosX() - center.getPosX()), (int) (position.getPosY() - center.getPosY()), groesse.getPosXInt(),
					groesse.getPosYInt());
		}

	}

	@Override
	public String toSendString() {
		return "" + "*,s," + bezeichnung + "," + position.getPosXInt() +"," + position.getPosYInt() + "," + groesse.getPosXInt() + "," + groesse.getPosYInt() + "," + center.getPosXInt() + "," + center.getPosYInt() + "," + PBFileReadWriter.createRelPfad(bildURL);
	}

	@Override
	public void handleCommand(String cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SpielObjekt getCopy() {
		return new SpielSymbol(bezeichnung, new Vektor2D(position.getPosX() + 5, position.getPosY() + 5), new Vektor2D(groesse), new Vektor2D(center), bildURL, spielFlaeche);
	}

}
