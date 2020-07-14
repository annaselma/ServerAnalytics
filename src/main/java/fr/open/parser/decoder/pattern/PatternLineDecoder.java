package fr.open.parser.decoder.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class PatternLineDecoder {

        private final static Logger log = LoggerFactory.getLogger(PatternLineDecoder.class);

        private final String pattern;
        private final char separator; // = ' ';

        private final int lineLength;

        private final List<PatternUtil.PatternEnum> patterns;
        private char c;

        static final private HashMap<String, PatternUtil.PatternEnum> patternMap;

        static {
            patternMap = new HashMap<String, PatternUtil.PatternEnum>();
            addPattern(PatternUtil.PatternEnum.HOST);
            addPattern(PatternUtil.PatternEnum.TIMESTAMP);
        }

        static PatternUtil.PatternEnum create(String string) {
             if (patternMap.get(string) == null) {
                log.error("This pattern " + string + " doesn't exit. Use H for Hostname or T for Timestamp. ");
                throw new RuntimeException();
            }
            return patternMap.get(string);
        }

        static private void addPattern(PatternUtil.PatternEnum patternEnum) {
            patternMap.put(patternEnum.getValue(), patternEnum);
        }

        public PatternLineDecoder(final String pattern, final char separator) {

            this.pattern = pattern;
            this.separator = separator;

            String[] patternsSplited = pattern.split(String.valueOf(separator));

            lineLength = patternsSplited.length;

            patterns = new ArrayList<PatternUtil.PatternEnum>(lineLength);

            Arrays.stream(patternsSplited).forEach(s -> patterns.add(create(s)));

        }


        synchronized public Object[] decode(final CharBuffer buffer) {

            int start = 0;
            int position = 0;
            int currentPattern = 0;

            Object[] ret = new Object[lineLength];

            while (buffer.hasRemaining() && currentPattern < lineLength) {
                c = buffer.get();
                // check for separator if not quoted
                if (c == '\n'  || !buffer.hasRemaining()
                        || c == separator) {

                    position = buffer.position();

                    // get pattern for current item
                    Object decode = null;
                    try {
                        if (patterns.get(currentPattern) == PatternUtil.PatternEnum.HOST) {

                            decode =  PatternUtil.decodeHost (buffer,  start, c == separator || c == '\n' ? position - 1 : position);

                        } else if (patterns.get(currentPattern) == PatternUtil.PatternEnum.TIMESTAMP) {

                            decode = PatternUtil.decodeTimesTamp(buffer, start,  c == separator || c == '\n' ? position - 1 : position);

                        }
                    } catch(Exception e){
                        System.out.println("if "+(c == '\n' || !buffer.hasRemaining() || c == separator)+"Error in char "+c+" position "+position+" hasremaining "+buffer.hasRemaining()+" "+buffer);
                    }
                    ret[currentPattern] = decode;
                    start = position;

                    // just in case set last known position if pattern had to change
                    // buffer position and did not go to
                    buffer.position(position);
                    currentPattern++;
                }

            }
            return ret;
        }

    }





