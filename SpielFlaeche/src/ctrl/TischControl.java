package ctrl;

public class TischControl {

	private String taste;
	private int keyCode;
	private String beschreibung;
	private TischControlAction action;
	
	public TischControl(String taste, int keyCode, String beschreibung) {
		this.taste = taste;
		this.keyCode = keyCode;
		this.beschreibung = beschreibung;
	}
	
	public void setControlAction(TischControlAction a) {
		this.action = a;
	}
	
	public String getBeschreibung() {
		return beschreibung;
	}
	
	public int getKeyCode() {
		return keyCode;
	}
	
	public String getTaste() {
		return taste;
	}
	
	public void performAction() {
		action.performAction();
	}

}