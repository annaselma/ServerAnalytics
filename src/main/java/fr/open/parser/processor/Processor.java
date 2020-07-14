package fr.open.parser.processor;


import fr.open.parser.decoder.pattern.PatternLineDecoder;

/**
 * The Processor is responsible for working with line of data after it was
 * decoded by {@link PatternLineDecoder}.
 */
public interface Processor {

	/**
	 *
	 * @param d
	 *
	 */
	void process(Object[] d);

	/**
	 * Flush all data .
	 */
	void flush();
}
