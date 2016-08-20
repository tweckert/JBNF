package javax.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Thomas Weckert
 */
public class FileIO {

	public static CharSequence readFile(String filename, String encoding) throws IOException {
		
		StringBuffer buf = new StringBuffer();
		InputStreamReader reader = null;

		try {
			
			buf = new StringBuffer();
			reader = new InputStreamReader(FileIO.class.getResourceAsStream(filename), encoding);

			BufferedReader br = new BufferedReader(reader);
			for (int c = br.read(); c != -1; c = br.read()) {
				buf.append((char) c);
			}
		} finally {
			closeQuietly(reader);
		}

		return buf;
	}
	
	public static void closeQuietly(Reader reader) {
		
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Throwable t) {
			// intentionally left blank
		} finally {
			reader = null;
		}
	}

}
