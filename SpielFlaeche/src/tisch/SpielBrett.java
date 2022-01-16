package tisch;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.PBFileReadWriter;
import io.Sendbares;
import math.Vektor2D;

/**
 * Statisch positioniertes Spielbrett
 * @author paulb
 *
 */
public class SpielBrett implements Sendbares {

	private String name;
	private BufferedImage bild;
	private String bildURL;
	private Vektor2D position;

	public SpielBrett(String name, String bildSrc, Vektor2D position) {
		this.name = name;
		this.position = position;
		this.bildURL = bildSrc;

		if (!bildURL.equals("")) {
			this.bild = null;
			try {
				bild = ImageIO.read(new File(bildURL));
			} catch (IOException e) {
			}
		}
	}

	public SpielBrett(SpielBrett b) {
		this.name = b.name;
		this.position = new Vektor2D(b.getPosition());
		this.bildURL = b.bildURL;

		if (!bildURL.equals("")) {
			this.bild = null;
			try {
				bild = ImageIO.read(new File(bildURL));
			} catch (IOException e) {
			}
		}
	}

	public void paint(Graphics2D g) {
		g.drawImage(bild, position.getPosXInt(), position.getPosYInt(), null);
	}

	public String getName() {
		return name;
	}

	public String getBildURL() {
		return bildURL;
	}

	public void setPosition(Vektor2D position2) {
		this.position.set(position2);
	}

	public Vektor2D getPosition() {
		return position;
	}

	public void verschiebe(double offX, double offY) {
		position.add(offX, offY);
	}

	@Override
	public String toSendString() {
		return "*,b," + name + "," + PBFileReadWriter.createRelPfad(bildURL) + "," + position.getPosXInt() + "," + position.getPosYInt();
	}

	public SpielBrett getCopy() {
		return new SpielBrett(getName(), bildURL, position);
	}


}
