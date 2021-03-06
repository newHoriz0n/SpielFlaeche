package tisch.objekte;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeListener;

import io.PBFileReadWriter;
import io.Sendbares;
import math.IDverwaltung;
import math.Vektor2D;
import tisch.objekte.karten.SpielBilderKarte;
import tisch.objekte.karten.SpielKarteGen;
import tisch.objekte.karten.SpielTextKarte;
import tisch.objekte.wuerfel.SpielBildWuerfel;
import tisch.objekte.wuerfel.SpielZahlenWuerfel;

public abstract class SpielObjekt implements Sendbares {

	private int objektID;

	protected String bezeichnung;

	protected String beschreibung;

	protected Vektor2D position;
	protected Vektor2D groesse;
	protected Vektor2D center;
	protected double winkel; // Ausrichtung

	protected boolean ausgewaehlt;
	protected boolean mausOver;

	protected BufferedImage bild;
	protected String bildURL;

	// ChangeListener
	protected LinkedList<ChangeListener> listeners;

	public SpielObjekt(String bezeichnung, Vektor2D position, Vektor2D groesse, Vektor2D mitte) {
		this.bezeichnung = bezeichnung;
		this.position = new Vektor2D(position);
		this.groesse = new Vektor2D(groesse);
		this.center = new Vektor2D(mitte);
		this.bildURL = "";
		this.objektID = IDverwaltung.getNextID();
		this.listeners = new LinkedList<>();
	}

	public SpielObjekt(String bezeichnung, Vektor2D position, Vektor2D groesse, Vektor2D mitte, String bildURL) {
		this.bezeichnung = bezeichnung;
		this.position = new Vektor2D(position);
		this.groesse = new Vektor2D(groesse);
		this.center = new Vektor2D(mitte);

		this.bildURL = bildURL;
		this.bild = null;
		if (bildURL != "") {
			try {
				bild = ImageIO.read(new File(bildURL));
			} catch (IOException e) {
				System.out.println("failed to load bild in SpielObjekt");
			}
		}

		this.objektID = IDverwaltung.getNextID();
		this.listeners = new LinkedList<>();
	}

	/**
	 * Copy - Constructor
	 * 
	 * @param o
	 */
	// public SpielObjekt(SpielObjekt o) {
	// this.bezeichnung = o.bezeichnung;
	// this.beschreibung = o.beschreibung;
	// this.position = new Vektor2D(o.position);
	// this.groesse = new Vektor2D(o.groesse);
	// this.center = new Vektor2D(o.center);
	// this.bildURL = o.bildURL;
	// if (!bildURL.equals("")) {
	// this.bild = null;
	// try {
	// bild = ImageIO.read(new File(bildURL));
	// } catch (IOException e) {
	// System.out.println("failed");
	// }
	// }
	//
	// this.objektID = IDverwaltung.getNextID();
	// }

	public void drawSpielObjekt(Graphics2D g, double rotation) {

		g.setColor(Color.BLACK);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		int[][] polygon = calcPolygon();
		g.drawPolygon(polygon[0], polygon[1], 4);

	}

	public void drawSpielObjektLight(Graphics2D g, double rotation) {

		g.setColor(Color.BLACK);
		if (mausOver) {
			g.setColor(Color.GREEN);
		}
		if (ausgewaehlt) {
			g.setColor(Color.RED);
		}
		int[][] polygon = calcPolygon();
		g.drawPolygon(polygon[0], polygon[1], 4);

	}

	/**
	 * 
	 * @return polygon des Objektes, [0]: xs, [1]: ys
	 */
	protected int[][] calcPolygon() {

		int[] xs = new int[4];
		xs[0] = (int) (position.getPosX() - center.getPosX() * Math.cos(winkel) - center.getPosY() * Math.sin(winkel));
		xs[1] = (int) (position.getPosX() + (groesse.getPosX() - center.getPosX()) * Math.cos(winkel) - center.getPosY() * Math.sin(winkel));
		xs[2] = (int) (position.getPosX() + (groesse.getPosX() - center.getPosX()) * Math.cos(winkel)
				+ (groesse.getPosY() - center.getPosY()) * Math.sin(winkel));
		xs[3] = (int) (position.getPosX() - center.getPosX() * Math.cos(winkel) + (groesse.getPosY() - center.getPosY()) * Math.sin(winkel));

		int[] ys = new int[4];
		// ys[0] = 500;
		// ys[1] = 600;
		// ys[2] = 700;
		// ys[3] = 800;
		ys[0] = (int) (position.getPosY() - center.getPosY() * Math.cos(winkel) + center.getPosX() * Math.sin(winkel));
		ys[1] = (int) (position.getPosY() - (groesse.getPosX() - center.getPosX()) * Math.sin(winkel) - center.getPosY() * Math.cos(winkel));
		ys[2] = (int) (position.getPosY() + (groesse.getPosY() - center.getPosY()) * Math.cos(winkel)
				- (groesse.getPosX() - center.getPosX()) * Math.sin(winkel));
		ys[3] = (int) (position.getPosY() + center.getPosX() * Math.sin(winkel) + (groesse.getPosY() - center.getPosY()) * Math.cos(winkel));

		return new int[][] { xs, ys };

	}

	public boolean checkKlick(double x, double y) {
		int[][] poly = calcPolygon();
		Polygon p = new Polygon(poly[0], poly[1], 4);
		return p.contains(x, y);
	}

	/**
	 * ?berpr?ft, ob Spielobjekt vollst?ndig innerhalb eines Rechtecks mit
	 * Diagonalpunkten firstMaus / lastMaus liegt.
	 * 
	 * @param firstMausX
	 * @param firstMausY
	 * @param lastMausX
	 * @param lastMausY
	 * @return
	 */
	public boolean checkRahmenAuswahl(int minXRahmen, int minYRahmen, int maxXRahmen, int maxYRahmen) {

		int[][] polygon = calcPolygon();

		// Umrahmendes Rechteck des Objektes

		int minXObjekt = Integer.MAX_VALUE;
		int minYObjekt = Integer.MAX_VALUE;
		int maxXObjekt = Integer.MIN_VALUE;
		int maxYObjekt = Integer.MIN_VALUE;

		for (Integer x : polygon[0]) {
			if (x < minXObjekt) {
				minXObjekt = x;
			}
			if (x > maxXObjekt) {
				maxXObjekt = x;
			}
		}

		for (Integer y : polygon[1]) {
			if (y < minYObjekt) {
				minYObjekt = y;
			}
			if (y > maxYObjekt) {
				maxYObjekt = y;
			}
		}

		if (minXRahmen < minXObjekt && maxXRahmen > maxXObjekt && minYRahmen < minYObjekt && maxYRahmen > maxYObjekt) {
			return true;
		}

		return false;

	}

	public void setAusgewaehlt(boolean b) {
		this.ausgewaehlt = b;
	}

	public void rotiere(double d) {
		this.winkel += d;
	}

	public void setAusrichtung(double d) {
		this.winkel = d;
	}

	public double getAusrichtung() {
		return winkel;
	}

	public void verschiebe(double offX, double offY) {
		position.add(offX, offY);
	}

	public void setPosition(Vektor2D position2) {
		position.set(position2);
	}

	public void setPosition(double x, double y) {
		position.set(x, y);
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public String getBildURL() {
		return bildURL;
	}

	public Vektor2D getPosition() {
		return position;
	}

	public int getBreite() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + objektID;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpielObjekt other = (SpielObjekt) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (objektID != other.objektID)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	/**
	 * 
	 * @param maus
	 * @return true wenn sich etwas ?ndert.
	 */
	public boolean calcMouseOver(Vektor2D maus) {
		boolean aenderung = false;
		if (checkKlick(maus.getPosX(), maus.getPosY())) {
			if (mausOver == false) {
				aenderung = true;
			}
			mausOver = true;
		} else {
			if (mausOver == true) {
				aenderung = true;
			}
			mausOver = false;
		}
		return aenderung;
	}

	public abstract SpielObjekt getCopy();

	public int getObjektID() {
		return objektID;
	}

	public void aktionRechtsKlick() {
	}

	@Override
	public abstract String toSendString();

	/**
	 * 
	 * @param kompletter
	 *            empfangener Befehl.
	 */
	public void handleMoveCommand(String[] infos) {
		this.position.set(Integer.parseInt(infos[1]), Integer.parseInt(infos[2]));
	}

	/**
	 * 
	 * @param infos
	 */
	public void handleRotationCommand(String[] infos) {
		this.winkel = Double.parseDouble(infos[1]);
	}

	public abstract void handleCommand(String cmd);

	public void addChangeLister(ChangeListener cl) {
		this.listeners.add(cl);
	}

	/**
	 * SpielObjektFabrik
	 * 
	 * @param info
	 * @return
	 */
	public static SpielObjekt getInstanceFromString(String[] info) {

		SpielObjekt o = null;

		if (info[1].equals("s")) {
			o = new SpielSymbol(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
					new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])),
					new Vektor2D(Integer.parseInt(info[7]), Integer.parseInt(info[8])), PBFileReadWriter.createAbsPfad(info[9]));

		} else if (info[1].equals("zw")) {
			o = new SpielZahlenWuerfel(info[2], Integer.parseInt(info[3]), new Vektor2D(Integer.parseInt(info[4]), Integer.parseInt(info[5])),
					Integer.parseInt(info[6]), new Color(Integer.parseInt(info[7]), Integer.parseInt(info[8]), Integer.parseInt(info[9])),
					new Color(Integer.parseInt(info[10]), Integer.parseInt(info[11]), Integer.parseInt(info[12])), info[14], 1);
			((SpielZahlenWuerfel) o).setAktFlaeche(Integer.parseInt(info[13]));

		} else if (info[1].equals("bw")) {
			List<String> bildURLs = new ArrayList<>();
			for (int i = 8; i < 8 + Integer.parseInt(info[3]); i++) {
				bildURLs.add(info[i]);
			}
			o = new SpielBildWuerfel(info[2], new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), Integer.parseInt(info[7]),
					Integer.parseInt(info[3]), bildURLs, 1);
			((SpielBildWuerfel) o).setAktFlaeche(Integer.parseInt(info[4]));

		} else if (info[1].equals("k")) {
			o = new SpielBilderKarte(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
					new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), PBFileReadWriter.createAbsPfad(info[7]),
					PBFileReadWriter.createAbsPfad(info[8]), Boolean.parseBoolean(info[9]));

		} else if (info[1].equals("kt")) {
			o = new SpielTextKarte(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
					new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), info[7], info[8], Boolean.parseBoolean(info[9]));

		} else if (info[1].equals("kg")) {
			o = new SpielKarteGen(info[2], new Vektor2D(Integer.parseInt(info[3]), Integer.parseInt(info[4])),
					new Vektor2D(Integer.parseInt(info[5]), Integer.parseInt(info[6])), Double.parseDouble(info[7]), Boolean.parseBoolean(info[8]),
					new Color(Integer.parseInt(info[9])), info[10], SpielKarteGen.getTextAbschnitteVonSendString(info[11]), info[12],
					new Color(Integer.parseInt(info[13])), info[14], SpielKarteGen.getTextAbschnitteVonSendString(info[15]), info[16]);
		}

		return o;

	}

}
