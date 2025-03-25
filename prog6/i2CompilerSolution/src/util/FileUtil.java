package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Helper class for file operations.
 */
public class FileUtil {

	/**
	 * Convert file to string.
	 * 
	 * @param filename
	 *            File
	 * @return String with content of file.
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be read.
	 */
	public static String fileToString(String filename) throws IOException {
		// Try to open file
		FileReader in = new FileReader(filename);

		// Read file into string
		StringBuilder str = new StringBuilder();
		int countBytes = 0;
		char[] bytesRead = new char[512];
		while ((countBytes = in.read(bytesRead)) > 0) {
			str.append(bytesRead, 0, countBytes);
		}

		// Close stream
		in.close();
		return str.toString();
	}

	/**
	 * Convert file to list of lines.
	 * 
	 * @param filename
	 *            File
	 * @return List of string corresponding to lines in file.
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be read.
	 */
	public static ArrayList<String> fileToStringLines(String filename) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();

		// Try to open file
		FileReader in = new FileReader(filename);
		BufferedReader reader = new BufferedReader(in);

		// Read lines from file
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				lines.add(line);				
			}
		}
		reader.close();
		in.close();
		return lines;
	}

	/**
	 * Write string to file.
	 * 
	 * @param filename
	 *            Name of file.
	 * @param s
	 *            String to write.
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be written to.
	 */
	public static void stringToFile(String filename, String s) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write(s);
		writer.close();
	}

	/**
	 * Return filename without extension
	 * 
	 * @param file
	 *            Path to file
	 * @return Name of the file
	 */
	public static String getFileName(String file) {
		String filename = new File(file).getName();
		int pos = filename.lastIndexOf(".");
		if (pos > 0) {
			return filename.substring(0, pos);
		} else {
			return filename;
		}
	}
}
