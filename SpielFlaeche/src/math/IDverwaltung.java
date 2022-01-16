package math;

/** Klasse zum Anlegen von Singletons für Objekte
 * 
 * @author paulb
 *
 */
public class IDverwaltung {

	private static IDverwaltung idv = null;
	private static int currentID;

	private IDverwaltung() {
		currentID = 0;
	}

	public static int getNextID() {
		if (idv == null) {
			synchronized (IDverwaltung.class) {
				if (idv == null) {
					idv = new IDverwaltung();
				}
			}
		}
		currentID++;
		return currentID;
	}

}
