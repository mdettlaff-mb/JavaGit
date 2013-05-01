package mdettlaff.javagit.command;

import java.io.IOException;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;

public class CatFile {

	public static void main(String[] args) throws IOException {
		InflaterInputStream input = new InflaterInputStream(System.in);
		byte[] result = IOUtils.toByteArray(input);
		System.out.println(new String(result));
	}
}
