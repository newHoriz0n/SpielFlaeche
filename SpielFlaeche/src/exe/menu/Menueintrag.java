package exe.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Klasse für Menüeinträge
 * @author paulb
 *
 */
public class Menueintrag {

	private String titel;
	private BufferedImage icon;
	private List<Menueintrag> eintraege;
	private boolean geoeffnet; // => Kinder werden sichtbar

	private int posX;
	private int posY;
	private int breite;
	private int hoehe;

	private List<Aktion> aktionen; // Was passiert wenn Eintrag angeklickt.

	public Menueintrag(String titel, String imgURL) {
		
		this.titel = titel;
		this.geoeffnet = false;
		this.eintraege = new ArrayList<>();
		if (!imgURL.equals("")) {
			try {
				this.icon = ImageIO.read(new File(imgURL));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.aktionen = new ArrayList<>();
	}

	public void drawMenuEintrag(Graphics2D g2d, int offX, int offY, int breite, int hoehe, int spaltenabstand, Color rahmen, Color bg, Font font) {

		int startX = offX;
		int startY = offY;
		
		this.posX = offX;
		this.posY = offY;
		this.breite = breite;
		this.hoehe = hoehe;

		g2d.setColor(bg);
		g2d.fillRect(offX, offY, breite, hoehe);
		g2d.setColor(rahmen);
		g2d.drawRect(offX, offY, breite, hoehe);

		if (icon != null) {
			g2d.drawImage(icon, offX + 2, offY + 2, hoehe - 5, hoehe - 5, null);
		}

		g2d.setFont(font);
		g2d.drawString(titel, offX + hoehe, offY + hoehe / 2 + font.getSize2D() / 2);

		if (geoeffnet) {
			int internY = 0;
			for (Menueintrag m : eintraege) {
				m.drawMenuEintrag(g2d, offX + breite + spaltenabstand, internY, breite, hoehe, spaltenabstand, rahmen, bg, font);
				internY += hoehe;
				
				if(internY >= 950) {
					internY = startY;
					offX = startX + spaltenabstand + breite + 10;
				}
				
			}
		}

	}

	public boolean checkAndHandle(int mausX, int mausY) {
		if (mausX >= posX && mausX <= posX + breite && mausY >= posY && mausY <= posY + hoehe) {
			geoeffnet = true;
			if (eintraege.size() == 0) {
				performAktion();
				return false;
			}
			return true;
		} else {
			if (geoeffnet) {
				for (Menueintrag m : eintraege) {
					if (m.checkAndHandle(mausX, mausY)) {
						return true;
					}
				}
			}
			for (Menueintrag m : eintraege) {
				m.schiessen();
				geoeffnet = false;
				return false;
			}

		}

		return false;

	}

	private void performAktion() {
		for (Aktion a : aktionen) {
			a.performAktion();
		}
	}

	public void addAktion(Aktion a) {
		this.aktionen.add(a);
	}

	public void addMenuEintrag(Menueintrag m) {
		this.eintraege.add(m);
	}

	public void schiessen() {
		geoeffnet = false;
		for (Menueintrag m : eintraege) {
			m.schiessen();
		}
	}

	public boolean containsEintrag(String name) {
		for (Menueintrag m : eintraege) {
			if (m.titel.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
