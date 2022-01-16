package lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tisch.SpielBrett;
import tisch.objekte.SpielObjekt;
import tisch.objekte.SpielSymbol;

/**
 * Objekt zur Verwaltung von Spielsets mit Brettern und Figuren..
 * @author paulb
 *
 */
public class SpielSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String titel;
	private List<SpielBrett> bretter;
	private List<SpielObjekt> objekte;
	private List<SpielSymbol> symbole;
	
	public SpielSet(String titel) {
		this.titel = titel;
		this.bretter = new ArrayList<>();
		this.objekte = new ArrayList<>();
		this.symbole = new ArrayList<>();
	}
	
	public String getTitel() {
		return titel;
	}
	
	public void addBrett(SpielBrett sb) {
		bretter.add(sb);
	}

	public void addObjekt(SpielObjekt so) {
		objekte.add(so);
	}
	
	public void addSymbol(SpielSymbol sy) {
		symbole.add(sy);
	}
	
	public List<SpielObjekt> getObjekte() {
		return objekte;
	}
	
	public List<SpielBrett> getBretter() {
		return bretter;
	}
	
	public List<SpielSymbol> getSymbole() {
		return symbole;
	}
	
}
