package fr.open.parser.processor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import fr.open.util.CountElement;
import fr.open.util.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The Class CountProcessor == times given hosts are contacted.
 * Data is printed to logfile only if there is a data beeing processed
 * </p>
 */
public class CountProcessor implements Processor {
	private final Logger logout = LoggerFactory
			.getLogger("CountProcessorOutput");

	private static final int COUNTER_LIMIT = 100000;

	/** The fields HOST & TIMESTEMP counter */
	int[] fields = { 1, 2 };

	private Counter counter;

	private long lastClear;
	private final long clearPeriod = TimeUnit.HOURS.toMillis(1);

	public CountProcessor() {
		counter = new Counter(COUNTER_LIMIT, COUNTER_LIMIT / 2);
		lastClear = System.currentTimeMillis();
	}

	synchronized public void process(final Object[] dataLine) {
		checkTime();
		Arrays.stream(fields).forEach(field -> getCounter().count((String) dataLine[field]));
	}

	/**
	 * Check time is executed every time data is processed to check if we need
	 * to print out statistics. it uses Systems current time to do the check.
	 *
	 */
	public void checkTime() {
		checkTime(System.currentTimeMillis());
	}

	private void logData() {
		CountElement count = getCounter().getCount();
		if (count == null)
			return;
		logout.info("le serveur qui a généré le plus de connections : " + count.getKey()
				+ " , connections faites : " + count.getCount());
	}

	/**
	 *
	 * @param now
	 *            the time to be checked
	 */
	public void checkTime(long now) {
		if (clearPeriod < (now - lastClear)) {
			lastClear = System.currentTimeMillis();
			logData();
			getCounter().clear();
		}

	}

	@Override
	public void flush() {
		logData();
	}

	public Counter getCounter() {
		return counter;
	}

}
