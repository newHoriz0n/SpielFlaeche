package ctrl;

import math.Vektor2D;

public interface TischController {

	public void verschiebeAusgewaelteObjekte(double offX, double offY);

	public boolean checkMouseOver(Vektor2D spielkoords);

	public void handleMausPress(int button, Vektor2D spielMausKoords);

	public void handleMausRad(int wheelRotation);

	public boolean hatAusgewaehltesObjekt();

	public void entferneObjekt();

	public void ObjektNachOben();

	public void ObjektNachUnten();

	public void ObjekteMischen();

	public void kopiereObjekt();

	public void handleAuswahlRahmen(int aktButton, Vektor2D firstMaus, Vektor2D lastMaus );

}
