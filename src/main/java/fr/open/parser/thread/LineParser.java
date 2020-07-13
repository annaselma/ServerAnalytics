package fr.open.parser.thread;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import fr.open.parser.AnalyticsParser;
import fr.open.parser.decoder.pattern.PatternLineDecoder;


public class LineParser implements Runnable{
	
	private static final int BUFFER_SIZE = 1024;
	
	private final CharsetDecoder cDecoder;
	private final ByteBuffer bBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private final CharBuffer cBuffer = CharBuffer.allocate(BUFFER_SIZE);
	
	private final PatternLineDecoder lineDecoder;
	
	private final AnalyticsParser analyticsParser;
	
	private ParserFinishNotifier listener;
	
	
	LineParser(Charset cSet, ParserFinishNotifier listener, PatternLineDecoder lineDecoder, AnalyticsParser analyticsParser) {
		this.listener = listener;
		this.lineDecoder = lineDecoder;
		this.analyticsParser = analyticsParser;
		cDecoder = cSet.newDecoder();
	}
	
	synchronized public ByteBuffer getBuffer(){
		return bBuffer;
	}
	
	public void run() {
		bBuffer.flip();
		cBuffer.clear();
		cDecoder.reset();
		cDecoder.decode(bBuffer, cBuffer, false);
		cBuffer.flip();
		analyticsParser.decoded(lineDecoder.decode(cBuffer));
		bBuffer.clear();
		listener.finished(this);
	}

	public void stop() {
		//release reference to the pool
		listener = null;
	}

	public void clear() {
		bBuffer.clear();
		cBuffer.clear();
	}

}
