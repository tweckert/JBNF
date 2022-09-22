package javax.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Thomas Weckert
 */
public class FileIO {

	public static CharSequence readFile(final String filename, final String encoding) throws IOException {

		final StringBuffer buf = new StringBuffer();
		InputStreamReader reader = null;

		try {

			reader = new InputStreamReader(FileIO.class.getResourceAsStream(filename), encoding);

			final BufferedReader br = new BufferedReader(reader);
			for (int c = br.read(); c != -1; c = br.read()) {
				buf.append((char) c);
			}
		} finally {
			closeQuietly(reader);
		}

		return buf;
	}
	
	public static void closeQuietly(final Reader reader) {
		
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Throwable t) {
			// intentionally left blank
		}
	}

}
