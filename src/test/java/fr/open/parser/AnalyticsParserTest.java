package fr.open.parser;

import static org.junit.Assert.*;
import java.io.RandomAccessFile;
import java.net.URL;

import org.junit.Test;

import fr.open.parser.processor.CountProcessor;
import fr.open.parser.processor.SimpleHardcodedProcessor;
import fr.open.util.CountElement;

public class AnalyticsParserTest {

	@Test
	public void test() throws Exception {
		
		URL resource = getClass().getResource("/largeData.txt");
		RandomAccessFile file = new RandomAccessFile(resource.getPath(), "r");
		AnalyticsParser analyticsParser = new AnalyticsParser(file,"z","a");
		analyticsParser.parse();
		
		SimpleHardcodedProcessor processor = (SimpleHardcodedProcessor) analyticsParser.getProcessor();
		
		CountProcessor p = (CountProcessor) processor.getProcessors().get(2);
		
		CountElement count = p.getCounter().getCount();
		assertEquals("z", count.getKey());
		assertEquals(235, count.getCount());
		
		
	}

}
