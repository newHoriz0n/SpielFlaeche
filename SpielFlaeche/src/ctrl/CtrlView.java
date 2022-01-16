package ctrl;

/**
 * Controller für View der Spielfläche
 * @author paulb
 *
 */

public interface CtrlView {

	public void verschiebeAnsicht(double offX, double offY);

	public void mausDruck(int button, double x, double y);

	public void mausRadBewegung(int wheelRotation);

	public void verschiebeObjekt(double offX, double offY);

	public void mausBewegung(int x, int y);

	public void handleTastenRelease(int keyCode);

	public void handleMouseRelease(int firstMausX, int firstMausY, int lastMausX, int lastMausY, int aktButton);
	
}
