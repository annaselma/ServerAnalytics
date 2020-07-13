package fr.open.parser.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LoggingProcessor implements Processor{
	final Logger logout ;
	final private LoggingStrategy strategy;
	
	public LoggingProcessor(final String loggerName,final LoggingStrategy strategy) {
		this.strategy = strategy;
		logout = LoggerFactory.getLogger(loggerName);
	}

	@Override
	synchronized public void process(final Object[] data) {
		if (strategy.filter(data) ){
			logout.info("Connection made at "+data[0]+" from " +data[1] + " to "+data[2]);
		}
		
	}

	@Override
	public void flush() {
	}

}
