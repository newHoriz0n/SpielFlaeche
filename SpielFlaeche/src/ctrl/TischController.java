package ctrl;

import java.awt.event.KeyEvent;

import math.Vektor2D;

public interface TischController {


	public boolean checkMouseOver(Vektor2D spielkoords);

	public void handleMausPress(int button, Vektor2D spielMausKoords);

	public void handleMausRad(int wheelRotation);

	public void handleLeftMouseDrag(double offX, double offY, Vektor2D firstMaus, Vektor2D lastMaus);

	public boolean hatAusgewaehltesObjekt();

	public void handleMausRelease(int aktButton, Vektor2D firstMaus, Vektor2D lastMaus );

	public void handleTischKeyControls(KeyEvent arg0);

}
