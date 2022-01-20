package math;

import java.io.Serializable;

/**
 * 2D Vektor mit einfachen Operatoren
 * @author paulb
 *
 */
public class Vektor2D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double posX;
	private double posY;

	public Vektor2D(double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public Vektor2D(Vektor2D copy) {
		this.posX = copy.getPosX();
		this.posY = copy.getPosY();
	}

	public Vektor2D() {
		this.posX = 0;
		this.posY = 0;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public int getPosXInt() {
		return (int) posX;
	}

	public int getPosYInt() {
		return (int) posY;
	}

	public Vektor2D add(double x, double y) {
		posX += x;
		posY += y;
		return this;
	}

	public Vektor2D add(Vektor2D add) {
		posX += add.posX;
		posY += add.posY;
		return this;
	}

	public void scale(double d) {
		posX *= d;
		posY *= d;
	}

	public void set(double newX, double newY) {
		posX = newX;
		posY = newY;
	}

	public void set(Vektor2D position2) {
		posX = position2.posX;
		posY = position2.posY;
	}

	public String toString() {
		return "" + posX + "," + posY;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(posX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(posY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Vektor2D other = (Vektor2D) obj;
		if (Double.doubleToLongBits(posX) != Double.doubleToLongBits(other.posX))
			return false;
		if (Double.doubleToLongBits(posY) != Double.doubleToLongBits(other.posY))
			return false;
		return true;
	}

}
