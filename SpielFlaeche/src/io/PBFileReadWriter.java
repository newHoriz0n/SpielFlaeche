package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import exe.FSpielFlaeche;

/**
 * Klasse zum Vereinfachten Lesen und Schreiben von Dateien
 * @author paulb
 *
 */
public class PBFileReadWriter {

	/**
	 * Returs lines in file
	 * 
	 * @param url
	 * @return
	 */
	public static List<String> getLines(String url) {

		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(url));
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line.trim());
			}
			reader.close();
			return lines;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", url);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns files in folder
	 * 
	 * @param folderURL
	 * @return
	 */
	public static List<String> getFileURLsInFolder(String folderURL) {

		List<String> files = new ArrayList<String>();
		File test = new File(folderURL);

		File[] DIR = test.listFiles();
		for (int i = 0; i < DIR.length; i++) {
			if (!DIR[i].isDirectory()) {
				files.add(DIR[i].getName());
			}
		}

		return files;
	}

	public static void writeLinesToFile(List<String> lines, String fileURL) throws IOException {

		File f = new File(fileURL);
		f.createNewFile();
		FileWriter fw = new FileWriter(f, false);
		BufferedWriter bw = new BufferedWriter(fw);

		for (String s : lines) {
			bw.write(s);
			bw.write(System.getProperty("line.separator"));
		}

		bw.close();

	}

	public static List<String[]> getAttributeFromLine(String attributSeparator, String elementSeparator, String line) {

		List<String[]> attList = new ArrayList<>();
		String[] atts = line.split(attributSeparator);

		for (String s : atts) {
			String[] c = s.split(elementSeparator);
			attList.add(c);
		}

		return attList;

	}
	
	/**
	 * Erwartet 1,2,3
	 * 			1,2,3
	 * 			1,2,3 ...
	 * 
	 * @param seperator
	 * @param URL
	 * @return
	 */
	public static List<String[]> getContentFromFile(String seperator, String url) {
		
		List<String[]> lineContent = new ArrayList<String[]>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(url));
			String line;
			while ((line = reader.readLine()) != null) {			
				String[] out = (line.trim()).split(seperator);
				lineContent.add(out);
			}
			reader.close();
			return lineContent;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", url);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String createAbsPfad(String relPfad) {

		String userPfadString = "";

		try {
			File f = new File(FSpielFlaeche.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			userPfadString = f.getParentFile().toPath().toString() + "/";
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return userPfadString + relPfad;
	}
	
	public static String createRelPfad(String absPfad) {
		
		String userPfadString = "";

		try {
			File f = new File(FSpielFlaeche.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			userPfadString = f.getParentFile().toPath().toString() + "/";
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return absPfad.substring(userPfadString.length());
		
	}

}
