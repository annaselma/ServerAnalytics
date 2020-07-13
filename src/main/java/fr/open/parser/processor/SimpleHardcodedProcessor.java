package fr.open.parser.processor;

import java.util.ArrayList;
import java.util.List;

public class SimpleHardcodedProcessor implements Processor{
	private List<Processor> processors  = new ArrayList<Processor>();
	
	public SimpleHardcodedProcessor(final String hostFrom , final String hostTo) {

		// create logging processor which will log out all connections from certain host
		getProcessors().add(new LoggingProcessor("ConnectionsFrom", data -> ((String)data[1]).equalsIgnoreCase(hostFrom)));

		//create processor which will
		getProcessors().add(new LoggingProcessor("ConnectionsTo", data -> ((String)data[2]).equalsIgnoreCase(hostTo)));

		//create counting processor and add it in to the list of processors
		getProcessors().add(new CountProcessor());
	}

	@Override
	synchronized public void process(Object[] d) {
		getProcessors().forEach(p -> p.process(d));
	}

	@Override
	public void flush() {
		getProcessors().forEach(p -> p.flush());
	}

	public List<Processor> getProcessors() {
		return processors;
	}

}
