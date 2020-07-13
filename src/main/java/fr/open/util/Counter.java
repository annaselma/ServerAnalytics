package fr.open.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * The Class CountProcessor is responsible for calculating maximum number of
 * occurrences of certain string to reduce memory usage there can be a limit set
 * for maximum number of elements stored in the counter , if the limit is > 0
 * than count will be truncated every time its size is reaching limit and
 * reduced to reduceSize size,
 */
public class Counter {

	/** The limit. */
	private int limit;

	/** The reduce size. */
	private int reduceSize;

	/** The working set. */
	HashMap<String, CountElement> workingSet;

	/**
	 * The Constructor.
	 *
	 * @param limit
	 *            the limit of the counter if 0 there is
	 * @param reduceSize
	 *            the reduce size if limit is > 0 then counter will be reduced
	 *            to this size after truncate
	 */
	public Counter(int limit, int reduceSize) {
		this.limit = limit;
		this.reduceSize = reduceSize;
		clear();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return workingSet.size();
	}

	/**
	 * Count number of occurrences of this word
	 *
	 * @param word
	 *            the word
	 */
	synchronized public void count(String word) {
		CountElement countElement = workingSet.get(word);
		if (countElement == null) {
			countElement = new CountElement(word);
			workingSet.put(countElement.getKey(), countElement);
		} else {
			countElement.increment();
		}
		truncate();
	}

	/**
	 * Gets the count this function is sorting all counters and returns the
	 * highest one.
	 *
	 * @return the count
	 */
	synchronized public CountElement getCount() {
		TreeSet<CountElement> tree = new TreeSet<CountElement>();
		tree.addAll(workingSet.values());
		if (tree.size() <=0) return null;
		return tree.last();
	}

	/**
	 * Truncate function is used to reduce the size of working set if limit is >
	 * 0.
	 */
	private void truncate() {
		if (limit == 0) {
			return;
		}
		if (workingSet.size() < limit) {
			return;
		}
		ArrayList<CountElement> list = new ArrayList<CountElement>(
				workingSet.values());
		Collections.sort(list);

		clear();

		for (int i = list.size() - 1; i >= reduceSize; i--) {
			workingSet.put(list.get(i).getKey(), list.get(i));
		}

	}

	/**
	 * Clear working set
	 */
	synchronized public void clear() {
		workingSet = new HashMap<String, CountElement>();
	}

}
