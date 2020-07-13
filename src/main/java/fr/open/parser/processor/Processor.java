package fr.open.parser.processor;


import fr.open.parser.decoder.pattern.PatternLineDecoder;

/**
 * The Processor is responsible for working with line of data after it was
 * decoded by {@link PatternLineDecoder}.
 */
public interface Processor {

	/**
	 * Handle data function is executed when decoder decoded line from buffer
	 * and object is created , a decoder is passed as a parameter to help
	 * determine types of object array.
	 *
	 * @param d
	 *            the d
	 */
	void process(Object[] d);

	/**
	 * Flush all data .
	 */
	void flush();
}
