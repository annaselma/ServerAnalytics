package fr.open.parser.decoder.pattern;

import java.nio.CharBuffer;

public class PatternUtil {
	
	public static enum PatternEnum {
		HOST("h"),
		TIMESTAMP("t");

		private String value;
		private PatternEnum (String value){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static String decodeHost(final CharBuffer buffer, final int start,final int stop) {
		buffer.position(start);
		String hostname = buffer.subSequence(0, stop - start).toString();
		buffer.position(stop);
		return hostname;
	}

	public static Long decodeTimesTamp(CharBuffer buffer, int start, int stop) {
		buffer.position(start);
		String timeStamp = buffer.subSequence(0, stop - start).toString();
		buffer.position(stop);
		return Long.valueOf(timeStamp.trim()) ;
	}

	public static PatternEnum getPatternEnum(String value) {
		return  PatternEnum.valueOf(value);
	}

}
