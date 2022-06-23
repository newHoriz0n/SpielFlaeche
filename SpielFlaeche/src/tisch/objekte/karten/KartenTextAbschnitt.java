package tisch.objekte.karten;

import java.awt.Color;
import java.awt.Font;

public class KartenTextAbschnitt {

	Font font = new Font("Arial", Font.BOLD, 16);
	String text = "";
	Color color = Color.BLACK;

	public KartenTextAbschnitt(Font font, String text, Color color) {
		this.font = font;
		this.text = text;
		this.color = color;
	}

	public KartenTextAbschnitt(String sendString) {
		String[] infos = sendString.split("~");
		this.font = new Font(infos[0], Integer.parseInt(infos[1]), Integer.parseInt(infos[2]));
		this.color = new Color (Integer.parseInt(infos[3]));
		this.text = infos[4];
	}

	public String toSendString() {
		String result = "";
		result += font.getFamily() + "~" + font.getStyle() + "~" + font.getSize() + "~";
		result += color.getRGB() + "~";
		result += text;
		return result;
	}

}