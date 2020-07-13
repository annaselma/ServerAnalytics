package fr.open.parser.thread;

import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.open.parser.AnalyticsParser;
import fr.open.parser.decoder.pattern.PatternLineDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LineParserPool implements ParserFinishNotifier {

	private final Logger log = LoggerFactory.getLogger(LineParserPool.class);
	
	final static int THREADS = Runtime.getRuntime().availableProcessors();
	
	private final BlockingQueue<LineParser> iddle = new ArrayBlockingQueue<LineParser>(
			THREADS);

	final Charset cSet;

	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(THREADS,
			THREADS, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(
					THREADS));

	public LineParserPool(PatternLineDecoder lineDecoder,
						  AnalyticsParser analyticsParser, Charset cSet) {
		this.cSet = cSet;
		for (int i = 0; i < THREADS; i++) {
			iddle.add(new LineParser(cSet, this, lineDecoder, analyticsParser));
		}
	}

	public void stop() throws InterruptedException {
			for (int i = 0; i < THREADS; i++) {
				iddle.take().stop();
		}
	}

	public void execute(LineParser parser) {
			executor.execute(parser);
	}

	public void finished(LineParser p) {
			iddle.add(p);
	}

	public LineParser getParser() {
		try {
			LineParser p;
			p = iddle.take();
			p.clear();
			return p;
		} catch (InterruptedException e) {
			log.info("Thread pool is interrupted application is probably stopping",e);
			return null;
		}
	}

}
