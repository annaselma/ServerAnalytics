package fr.open.parser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import fr.open.parser.decoder.pattern.PatternLineDecoder;
import fr.open.parser.io.LogReader;
import fr.open.parser.processor.Processor;
import fr.open.parser.thread.LineParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.open.parser.processor.SimpleHardcodedProcessor;

public class AnalyticsParser {
	private static final String CHARSET = "UTF-8";
	
	private final Logger log = LoggerFactory.getLogger(AnalyticsParser.class);
	

	private LogReader logReader;
	private PatternLineDecoder lineDecoder;
	private LineParserPool pool;
	private Processor processor;
	private RandomAccessFile file;

	public AnalyticsParser(RandomAccessFile file, String hostFrom, String hostTo ) {
		this.file = file;
		lineDecoder = new PatternLineDecoder("t h h", ' ');
		pool = new LineParserPool(lineDecoder, this, Charset.forName(CHARSET));
		logReader = new LogReader(pool);
		processor = new SimpleHardcodedProcessor(hostFrom,hostTo);

	}

	public void parse() {
		try {
			logReader.read(file);
		} catch (IOException e) {
			log.error("exception while parsing file",e);
		}
		processor.flush();
	}

	/**
	 * Decoded is executed when line is decoded and is executed in one of the
	 * pools threads
	 *
	 * @param line
	 *            the line
	 */
	public void decoded(Object[] line) {
		// after data is decoded use processor to process this data this code is
		// executed on the time of one of the threads in the thread pool 
		processor.process(line);
	}

	
	public Processor getProcessor(){
		return processor;
	}
	
	
	/**
	 * The main method.  takes as an input 4 arguments
	 * <pre>
	 * LogParser filename follow hostFrom hostTo
	 * </pre>
	 * <ul>
	 * <li> filename - name of the file to process</li>
	 * <li> follow - boolean if follow is true LogParser will wait for data to be appended to file</li>
	 * <li> hostFrom - source host from which data will be logged </li>
	 * <li> hostTo - destination host from which data will be logged </li>
	 * </ul>
	 *
	 * @param args the args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 4 ){
			AnalyticsParser analyticsParser = new AnalyticsParser(new RandomAccessFile(args[0], "r"),  args[1], args[2]);
			analyticsParser.parse();
		}else{
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println(" Usage : LogParser filename follow hostFrom hostTo\n See JavaDoc for more info");
	}
}
