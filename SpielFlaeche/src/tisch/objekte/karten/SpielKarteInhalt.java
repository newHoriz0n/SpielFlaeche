package tisch.objekte.karten;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import math.Vektor2D;

public class SpielKarteInhalt {

	private Font f;
	private Color c;
	private String t;
	private Vektor2D pos;
	
	public SpielKarteInhalt(String t, Vektor2D pos) {
		this.f = new Font("Arial", Font.BOLD, 16);
		this.c = Color.BLACK;
		this.t = t;
		this.pos = new Vektor2D(pos);
	}

	public Font getF() {
		return f;
	}

	public Color getC() {
		return c;
	}

	public String getT() {
		return t;
	}
	
	public Vektor2D getPos() {
		return pos;
	}
	
	public void drawSpielKartenInhalt(Graphics2D g) {
		g.setFont(f);
		g.setColor(c);
		g.drawString(t, pos.getPosXInt(), pos.getPosYInt());
	}
	
	
	
}
