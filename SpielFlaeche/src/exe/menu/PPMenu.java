package exe.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Menüverwaltung
 * @author paulb
 *
 */
public class PPMenu {

	private int startX;
	private int startY;

	private int spaltenbreite = 200;
	private int spaltenabstand = 2;
	private int zeilenhoehe = 40;
	private Color cHintergrundfarbe = new Color(255, 255, 250);
	private Color cRahmenfarbe = new Color(50, 50, 50);
	private Font fTextFont = new Font("Arial", Font.PLAIN, 14);

	private List<Menueintrag> eintraege;

	public PPMenu(int posX, int posY) {
		this.startX = posX;
		this.startY = posY;
		this.eintraege = new ArrayList<>();
	}

	public void drawPPMenu(Graphics2D g2d) {

		int x = startX;
		int y = startY;

		for (Menueintrag m : eintraege) {
			m.drawMenuEintrag(g2d, x, y, spaltenbreite, zeilenhoehe, spaltenabstand, cRahmenfarbe, cHintergrundfarbe, fTextFont);
			y += zeilenhoehe;
			if(y >= 900) {
				y = startY;
				x = startX + spaltenbreite;
			}
		}

	}

	public boolean checkKlick(int mausX, int mausY) {
		boolean offen = false;
		for (Menueintrag m : eintraege) {
			if (m.checkAndHandle(mausX, mausY)) {
				offen = true;
			}
		}
		if (!offen) {
			for (Menueintrag m : eintraege) {
				m.schiessen();
			}
		}
		return offen;
	}

	public void addMenuEintrag(Menueintrag eintrag) {
		this.eintraege.add(eintrag);
	}

}
