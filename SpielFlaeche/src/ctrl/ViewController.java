package ctrl;

import math.Vektor2D;

/**
 * Controller für View der Spielfläche
 * @author paulb
 *
 */

public interface ViewController {
	
	public void repaintView();

	public void verschiebeAnsicht(double offX, double offY);
	
	boolean checkViewControllerPress(int button, int mausX, int mausY);

	////////////
	// SETTER
	////////////7
	
	public void setZoom(double zoom);
	
	public void dreheAnsicht(double d);

	/////////////
	// GETTER
	//////////////////////
	
	public double getViewRotation();

	public double getViewZoom();
	
	public Vektor2D getSpielKoordsVonMaus(int mausX, int mausY);


	
}
