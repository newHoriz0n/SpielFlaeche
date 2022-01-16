package tisch;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import math.Vektor2D;
import tisch.objekte.SpielSymbol;

public class SpielSichtSchutz {

	private List<SpielSymbol> ecken;
	private boolean mausOver;
	private float opacity; // 0: transparent, 1: voll

	private Tisch spielFlaeche;

	public SpielSichtSchutz(String bezeichnung, List<Vektor2D> ecken, Tisch spielFlaeche) {

		this.ecken = new ArrayList<>();
		for (Vektor2D v : ecken) {
			this.ecken.add(new SpielSymbol("Ecke", v, new Vektor2D(30, 30), new Vektor2D(15, 15), ""));
		}
		this.opacity = 1.0f;
		this.spielFlaeche = spielFlaeche;
	}

	public void drawSpielObjekt(Graphics2D g, double rotation) {

		Polygon p = getPolygon();

		g.setColor(new Color(0.3f, 0.3f, 0.3f, opacity));
		g.fillPolygon(p);

		for (SpielSymbol s : ecken) {
			s.drawSpielObjekt(g, rotation);
		}

	}

	public Polygon getPolygon() {
		int[] xs = new int[ecken.size()];
		int[] ys = new int[ecken.size()];

		for (int i = 0; i < ecken.size(); i++) {
			xs[i] = ecken.get(i).getPosition().getPosXInt();
			ys[i] = ecken.get(i).getPosition().getPosYInt();
		}

		return new Polygon(xs, ys, ecken.size());
	}

	public boolean checkInnerhalbSichtschutz(Vektor2D pos) {
		return getPolygon().contains(new Point(pos.getPosXInt(), pos.getPosYInt()));

	}

	public List<SpielSymbol> getEcken() {
		return ecken;
	}

	/**
	 * 
	 * @param maus
	 * @return true wenn sich etwas ändert.
	 */
	public boolean checkMouseOver(Vektor2D maus) {
		boolean aenderung = false;
		if (getPolygon().contains(maus.getPosX(), maus.getPosY())) {
			if (mausOver == false) {
				SichtOeffnenThread s = new SichtOeffnenThread();
				s.start();
				aenderung = true;
			}
			mausOver = true;
		} else {
			if (mausOver == true) {
				SichtSchliessThread s = new SichtSchliessThread();
				s.start();
				aenderung = true;
			}
			mausOver = false;
		}
		return aenderung;
	}

	class SichtSchliessThread extends Thread {

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {

				opacity += 0.04;
				spielFlaeche.updateView();
				if (opacity > 1) {
					opacity = 1.0f;
					break;
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class SichtOeffnenThread extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				opacity -= 0.04;
				spielFlaeche.updateView();
				if (opacity < 0) {
					opacity = 0;
					break;
				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
