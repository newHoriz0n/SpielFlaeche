package tisch.create;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.PBFileReadWriter;
import math.Vektor2D;
import tisch.Tisch;
import tisch.objekte.karten.KartenTextAbschnitt;
import tisch.objekte.karten.SpielKarteGen;

public class FCreateKarte extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int breiteKarte;
	private int hoeheKarte;

	private Color cBGVorne = Color.GRAY;
	private Color cBGHinten = Color.GRAY;

	private JTextField tBezeichnung = new JTextField("Titel");

	// Set-Auswahl
	private JComboBox<String> sets = new JComboBox<>();

	// TextAbschnitte
	private List<TextAbschnitt> textAbschnitteVorne = new ArrayList<>();
	private JPanel pInhaltVorne = new JPanel();

	private List<TextAbschnitt> textAbschnitteHinten = new ArrayList<>();
	private JPanel pInhaltHinten = new JPanel();

	// Vorschau Panel

	private PKartenPanel pkp = new PKartenPanel();

	public FCreateKarte(Tisch t) {

		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);

		createGUI(t);

	}

	public void createGUI(Tisch t) {

		setLayout(new FlowLayout());

		// SETTINGS /////////////////////

		JPanel pSettings = new JPanel();
		pSettings.setLayout(new BoxLayout(pSettings, BoxLayout.Y_AXIS));
		// pSettings.setPreferredSize(new Dimension(300,1000));

		JPanel pBezeichnung = new JPanel();
		pBezeichnung.setBorder(BorderFactory.createTitledBorder("Bezeichnung"));
		tBezeichnung.setPreferredSize(new Dimension(200, 30));
		pBezeichnung.add(tBezeichnung);
		pSettings.add(pBezeichnung);

		JPanel pGrundSettings = new JPanel();
		pGrundSettings.setBorder(BorderFactory.createTitledBorder("Eigenschaften"));
		pGrundSettings.setLayout(new GridBagLayout());
		// pGrundSettings.setPreferredSize(new Dimension(300, 100));
		GridBagConstraints gbcGrundsettings = new GridBagConstraints();

		// Breite
		gbcGrundsettings.gridx = 0;
		gbcGrundsettings.gridy = 0;
		JLabel lbreite = new JLabel("Breite: ");
		pGrundSettings.add(lbreite, gbcGrundsettings);
		gbcGrundsettings.gridx = 1;
		JSpinner sbreite = new JSpinner(new SpinnerNumberModel(150, 50, 500, 1));
		sbreite.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				breiteKarte = (int) sbreite.getValue();
			}
		});
		breiteKarte = (int) sbreite.getValue();
		pGrundSettings.add(sbreite, gbcGrundsettings);

		// Hoehe
		gbcGrundsettings.gridy = 1;
		gbcGrundsettings.gridx = 0;
		JLabel lHoehe = new JLabel("Hoehe: ");
		pGrundSettings.add(lHoehe, gbcGrundsettings);
		gbcGrundsettings.gridx = 1;
		JSpinner sHoehe = new JSpinner(new SpinnerNumberModel(200, 50, 500, 1));
		sHoehe.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				hoeheKarte = (int) sHoehe.getValue();
			}
		});
		hoeheKarte = (int) sHoehe.getValue();
		pGrundSettings.add(sHoehe, gbcGrundsettings);

		// Hintergrundfarben
		// Vorne
		gbcGrundsettings.gridy = 2;
		gbcGrundsettings.gridx = 0;
		JLabel lBGFarbeVorne = new JLabel("Hintergrundfarbe Vorne: ");
		pGrundSettings.add(lBGFarbeVorne, gbcGrundsettings);
		gbcGrundsettings.gridx = 1;
		JButton bBGFarbeVorne = new JButton();
		bBGFarbeVorne.setBackground(cBGVorne);
		bBGFarbeVorne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog f = new JDialog();
				cBGVorne = JColorChooser.showDialog(f, "Select a color", cBGVorne);
				bBGFarbeVorne.setBackground(cBGVorne);
				pkp.repaint();
				f.requestFocus();
			}
		});
		pGrundSettings.add(bBGFarbeVorne, gbcGrundsettings);

		// Hinten
		gbcGrundsettings.gridy = 3;
		gbcGrundsettings.gridx = 0;
		JLabel lBGFarbeHinten = new JLabel("Hintergrundfarbe Vorne: ");
		pGrundSettings.add(lBGFarbeHinten, gbcGrundsettings);
		gbcGrundsettings.gridx = 1;
		JButton bBGFarbeHinten = new JButton();
		bBGFarbeHinten.setBackground(cBGHinten);
		bBGFarbeHinten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog f = new JDialog();
				cBGHinten = JColorChooser.showDialog(f, "Select a color", cBGHinten);
				bBGFarbeHinten.setBackground(cBGHinten);
				pkp.repaint();
				f.requestFocus();
			}
		});
		pGrundSettings.add(bBGFarbeHinten, gbcGrundsettings);

		// Füge GrundSettingsPanel zu Hauptpanel hinzu
		pSettings.add(pGrundSettings);

		// INHALT VORNE
		pInhaltVorne.setBorder(BorderFactory.createTitledBorder("Inhalte Vorderseite"));
		pInhaltVorne.setLayout(new BoxLayout(pInhaltVorne, BoxLayout.Y_AXIS));
		// pInhaltVorne.setPreferredSize(new Dimension(300, 300));

		for (TextAbschnitt ta : textAbschnitteVorne) {
			pInhaltVorne.add(ta.createTextAbschnittGUI());
		}

		// Neuer Textabschnitt
		JButton bNeuerTextVorne = new JButton("Textabschnitt hinzufügen");
		// bNeuerTextVorne.setPreferredSize(new Dimension(200, 30));
		bNeuerTextVorne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TextAbschnitt ta = new TextAbschnitt("Text einfügen");
				textAbschnitteVorne.add(ta);
				pInhaltVorne.add(ta.createTextAbschnittGUI());
				revalidate();
				pack();
			}
		});
		pInhaltVorne.add(bNeuerTextVorne);

		// Füge InhaltVornePanel zu Hauptpanel hinzu
		pSettings.add(pInhaltVorne);

		// INHALT HINTEN
		pInhaltHinten.setBorder(BorderFactory.createTitledBorder("Inhalte Rückseite"));
		pInhaltHinten.setLayout(new BoxLayout(pInhaltHinten, BoxLayout.Y_AXIS));

		for (TextAbschnitt ta : textAbschnitteHinten) {
			pInhaltHinten.add(ta.createTextAbschnittGUI());
		}

		// Neuer Textabschnitt
		JButton bNeuerTextHinten = new JButton("Textabschnitt hinzufügen");
		// bNeuerTextHinten.setPreferredSize(new Dimension(200, 30));
		bNeuerTextHinten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TextAbschnitt ta = new TextAbschnitt("Text einfügen");
				textAbschnitteHinten.add(ta);
				pInhaltHinten.add(ta.createTextAbschnittGUI());
				revalidate();
				pack();
			}
		});
		pInhaltHinten.add(bNeuerTextHinten);

		// Füge InhaltVornePanel zu Hauptpanel hinzu
		pSettings.add(pInhaltHinten);

		add(pSettings);

		// KARTENVORSCHAU /////////////////////

		add(pkp);

		// ABSCHLIESSEN

		JPanel pAbschliessen = new JPanel();
		pAbschliessen.setBorder(BorderFactory.createTitledBorder("Abschließen"));
		pAbschliessen.setLayout(new BoxLayout(pAbschliessen, BoxLayout.Y_AXIS));

		// Wähle Set

		JPanel pSelectSet = new JPanel();
		pSelectSet.add(new JLabel("Set:"));
		File pfad = new File("lib/Usersets/");
		sets.addItem("Default");
		for (File f : pfad.listFiles()) {
			// System.out.println(f.getName());
			sets.addItem(f.getName());
		}

		pSelectSet.add(sets);
		pAbschliessen.add(pSelectSet);

		// Platzieren

		JPanel pPlatzieren = new JPanel();
		JButton bPlatzieren = new JButton("Platzieren");
		bPlatzieren.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Namen auslesen
				String kartenName = tBezeichnung.getText();

				// Überprüfen, ob Name schon vergeben (indem Userset ausgelesen wird)

				ImageIcon saveIcon = new ImageIcon("save.png");

				String pfad = "lib/Usersets/" + sets.getSelectedItem().toString();

				File fv;
				fv = new File(pfad + "/" + kartenName + "/Vorne.png");
				File fh;
				fh = new File(pfad + "/" + kartenName + "/Hinten.png");

				if (!fv.exists() && !fh.exists()) {
					try {

						// Lege Ordner für Karte an
						File dir = new File("");
						dir = new File(pfad + "/" + kartenName);
						dir.mkdirs();

						// Lege Bilder an
						BufferedImage buffGraphVorne = new BufferedImage(breiteKarte, hoeheKarte, BufferedImage.TYPE_INT_RGB);
						BufferedImage buffGraphHinten = new BufferedImage(breiteKarte, hoeheKarte, BufferedImage.TYPE_INT_RGB);

						// Bemale Bilder
						pkp.drawKartenVorderseite(buffGraphVorne.getGraphics(), 0, 0);
						pkp.drawKartenRueckseite(buffGraphHinten.getGraphics(), 0, 0);

						// Beschreibe Bilderdateien
						ImageIO.write(buffGraphVorne, "png", fv);
						ImageIO.write(buffGraphHinten, "png", fh);

						// Bestägigungsdialog zum Anlegen der Dateien
						JOptionPane.showMessageDialog(null, kartenName + "-Karte has been created and saved to your directory...", "File Saved",
								JOptionPane.INFORMATION_MESSAGE, saveIcon);

						// TextAbschnitte auslesen
						List<KartenTextAbschnitt> tasVorne = new ArrayList<>();
						for (TextAbschnitt t : textAbschnitteVorne) {
							tasVorne.add(new KartenTextAbschnitt(t.font, t.text, t.color));
						}
						List<KartenTextAbschnitt> tasHinten = new ArrayList<>();
						for (TextAbschnitt t : textAbschnitteHinten) {
							tasHinten.add(new KartenTextAbschnitt(t.font, t.text, t.color));
						}

						// Kartenobjekt anlegen und platzieren
						SpielKarteGen skg = new SpielKarteGen(tBezeichnung.getText(), new Vektor2D(0, 0), new Vektor2D(breiteKarte, hoeheKarte), 0.0,
								true, cBGVorne, "", tasVorne, pfad + "/" + kartenName + "/Vorne.png", cBGHinten, "", tasHinten,
								pfad + "/" + kartenName + "/Hinten.png");
						t.platziereSpielObjekt(skg);

						saveDetails(skg, pfad + "/" + kartenName + "/");

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, kartenName + "-Karte already exist please use a different name...", "File Exists",
							JOptionPane.INFORMATION_MESSAGE, saveIcon);
				}

				// File dir = new File("");
				// dir = new File("lib/User/Objects/userkarte.");
				// dir.mkdirs();

				// t.platziereSpielObjekt(new SpielBilderKarte("User_Karte", new Vektor2D(0, 0),
				// new Vektor2D(breiteKarte, hoeheKarte), bildURLvorne,
				// bildURLhinten, offen));

			}

		});

		pPlatzieren.add(bPlatzieren);

		pAbschliessen.add(pPlatzieren);

		add(pAbschliessen);

		pack();

	}

	private void saveDetails(SpielKarteGen skg, String pfad) {

		try {
			PBFileReadWriter.writeStringToFile(skg.toSendString(), pfad + "details.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class TextAbschnitt {

		Font font = new Font("Arial", Font.BOLD, 16);
		String text = "";
		Color color = Color.BLACK;

		public TextAbschnitt(String text) {
			this.text = text;
		}

		public JPanel createTextAbschnittGUI() {

			JPanel p = new JPanel();
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = 0;

			// Text
			gbc.gridheight = 2;
			gbc.gridwidth = 2;
			JTextArea taText = new JTextArea(text);
			taText.setLineWrap(true);
			taText.addCaretListener(new CaretListener() {

				@Override
				public void caretUpdate(CaretEvent e) {
					text = taText.getText();
					pkp.repaint();
				}
			});
			taText.setForeground(color);
			taText.setColumns(25);
			p.add(taText, gbc);

			// Size
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 2;
			gbc.gridy = 0;
			p.add(new JLabel("Size: "), gbc);
			gbc.gridx = 3;
			JSpinner sFontSize = new JSpinner(new SpinnerNumberModel(font.getSize(), 8, 200, 1));
			sFontSize.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					float fsize = Long.parseLong("" + sFontSize.getValue());
					font = font.deriveFont(fsize);
					pkp.repaint();
				}
			});
			p.add(sFontSize, gbc);

			// Color
			gbc.gridx = 2;
			gbc.gridy = 1;
			p.add(new JLabel("Color: "), gbc);
			JButton bColor = new JButton();
			bColor.setBackground(color);
			bColor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JDialog f = new JDialog();
					color = JColorChooser.showDialog(f, "Select a color", color);
					bColor.setBackground(color);
					taText.setForeground(color);
					f.requestFocus();
				}
			});
			gbc.gridx = 3;
			p.add(bColor, gbc);

			return p;
		}

	}

	private class PKartenPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PKartenPanel() {
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			setPreferredSize(new Dimension(1030, 520));
			setVisible(true);
			setBackground(Color.WHITE);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			// VORNE

			int offX = 4;
			int offY = 17;
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.drawString("Vorderseite", offX, offY - 2);
			g.drawRect(offX, offY, 500, 500);

			drawKartenVorderseite(g, offX, offY);

			// HINTEN

			offX = 508;
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.drawString("Rückseite", offX, offY - 2);
			g.drawRect(offX, offY, 500, 500);

			drawKartenRueckseite(g, offX, offY);

		}

		private void drawKartenVorderseite(Graphics g, int offX, int offY) {
			g.setColor(cBGVorne);
			g.fillRect(offX, offY, breiteKarte, hoeheKarte);
			drawKartenText(g, offX, offY, textAbschnitteVorne);
		}

		private void drawKartenRueckseite(Graphics g, int offX, int offY) {
			g.setColor(cBGHinten);
			g.fillRect(offX, offY, breiteKarte, hoeheKarte);
			drawKartenText(g, offX, offY, textAbschnitteHinten);
		}

		private void drawKartenText(Graphics g, int offX, int offY, List<TextAbschnitt> abschnitte) {

			int textY = 0;
			for (TextAbschnitt ta : abschnitte) {
				g.setColor(ta.color);
				g.setFont(ta.font);
				textY += ta.font.getSize2D() + 10;

				String[] lines = ta.text.split("\n");
				for (String s : lines) {
					g.drawString(s, offX, offY + textY);
					textY += ta.font.getSize2D() + 10;
				}
				textY -= ta.font.getSize2D() + 10;

			}
		}

	}

}
