package fr.open.prototype;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Random;

public class FileWriter {
	
	private final static char [] chars = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ".toCharArray();
	
	
	public static void main(String[] args) throws Exception {
		RandomAccessFile aFile = new RandomAccessFile("/Users/salma/Desktop/text.txt", "rws");
		FileChannel channel = aFile.getChannel();
		StringBuilder b = new StringBuilder();
		Random r = new Random();
		Charset enc = Charset.forName("UTF-8");
		
		
		
		
		int i ;
		while (true){
			b.setLength(0);
			b.append(System.currentTimeMillis());
			b.append(" ");
			
			for (i = r.nextInt(63) ; i >= 0 ;i-- ){
				b.append(chars[r.nextInt(chars.length)]);
			}
			b.append(" ");
			for (i = r.nextInt(63) ; i >= 0 ;i-- ){
				b.append(chars[r.nextInt(chars.length)]);
			}
			b.append('\n');
			
			channel.write(enc.encode(b.toString()));
			//Thread.sleep(400+r.nextInt(500));
		}
	}
	

}
