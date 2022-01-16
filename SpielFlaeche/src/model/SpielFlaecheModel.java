package model;

import lib.SpieleSchrank;
import tisch.Tisch;

public class SpielFlaecheModel {

	private Tisch t;
	private SpieleSchrank s;
	
	public SpielFlaecheModel(Tisch t) {
		this.t = t;
		this.s = new SpieleSchrank();
	}
	
	public Tisch getTisch() {
		return t;
	}
	
	public SpieleSchrank getSpieleSchrank() {
		return s;
	}
	
}
