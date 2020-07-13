package fr.open.parser.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import fr.open.parser.thread.LineParser;
import fr.open.parser.thread.LineParserPool;

/**
 * The Class LogReader is responsible for reading file and passing line by line
 * to {@link LineParserPool}.
 */
public class LogReader {


	private static final int BUFFER_SIZE = 1024 * 4;

	private static final int EOF = -1;

	private byte[] inbuf;


	private LineParserPool pool;


	public LogReader(final LineParserPool pool) {
		this.pool = pool;
		inbuf = new byte[BUFFER_SIZE];
	}

	/**
	 * Read lines from the file passed as a prameter and pass them to parsing  pool
	 *
	 * @param reader
	 *            the reader
	 * @throws IOException
	 *             the IO exception
	 */
	public void read(final RandomAccessFile reader)
			throws IOException {
		LineParser parser = pool.getParser();
		int num;
		while ((num = reader.read(inbuf)) >= 0) {
			for (int i = 0; i < num; i++) {
				final byte ch = inbuf[i];
				switch (ch) {
				case '\r':
				case '\n':
					pool.execute(parser);
					parser = pool.getParser();
					break;
				default:
					parser.getBuffer().put(ch);
				}
			}
		}

	}

}
