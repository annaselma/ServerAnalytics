package fr.open.util;

public class CountElement implements Comparable<CountElement> {
	private String key;
	private long value;
	
	public CountElement(String key) {
		this.setKey(key);
		value = 1;
	}
	
	
	public void increment() {
		++value;
	}

	public long getCount() {
		return value;
	}


	@Override
	public int compareTo(CountElement o) {
		return Long.compare(value, o.getCount());
	}

	@Override
	public String toString() {
		return "CountElement{key:"+key+",value:"+value+"}";
	}


	public String getKey() {
		return key;
	}




	public void setKey(String key) {
		this.key = key;
	}

}
