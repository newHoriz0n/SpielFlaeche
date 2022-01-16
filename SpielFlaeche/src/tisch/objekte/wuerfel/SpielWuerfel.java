package tisch.objekte.wuerfel;

import java.util.Random;

import javax.swing.event.ChangeListener;

import math.Vektor2D;
import tisch.objekte.SpielObjekt;

/**
 * Abstrakter Würfel mit Randomizer
 * @author paulb
 *
 */
public abstract class SpielWuerfel extends SpielObjekt {


	protected int flaechenZahl;
	protected int aktFlaeche;
	private Random r = new Random();

	public SpielWuerfel(String bezeichnung, Vektor2D position, int groesse, int flaechenzahl, String bildURL, int aktFlaeche) {
		super(bezeichnung, position, new Vektor2D(groesse, groesse), new Vektor2D(groesse / 2, groesse / 2), bildURL);
		
		this.flaechenZahl = flaechenzahl;
		this.aktFlaeche = aktFlaeche;
		
	}


	@Override
	public void rotiere(double d) {
		// super.rotiere(d);
		aktFlaeche -= Math.signum(d);
		aktFlaeche = Math.max(1, aktFlaeche);
		aktFlaeche = Math.min(flaechenZahl, aktFlaeche);
	}
	
	public void setAktFlaeche(int zahl) {
		this.aktFlaeche = zahl;
	}

	public void wuerfeln() {
		WuerfelThread wt = new WuerfelThread();
		wt.start();
	}

	class WuerfelThread extends Thread {

		@Override
		public void run() {
			for (int i = 0; i < 30; i++) {
				aktFlaeche = r.nextInt(flaechenZahl) + 1;
				for (ChangeListener cl : listeners) {
					cl.notify();
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void aktionRechtsKlick() {
		wuerfeln();
	}


	@Override
	public abstract String toSendString();
	

	@Override
	public void handleCommand(String cmd) {

		String[] infos = cmd.split(",");

		switch (infos[0]) {
		case "m":
			handleMoveCommand(infos);
			break;
			
		case "w":
			handleWuerfelCommand(infos);
			break;
			
		}

	}


	/**
	 * 
	 * @param infos
	 */
	private void handleWuerfelCommand(String[] infos) {
		setAktFlaeche(Integer.parseInt(infos[1]));
	}
	
	
	
	
}
