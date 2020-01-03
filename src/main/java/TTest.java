import java.io.File;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TTest {
	public static void main(String[] args) throws IOException {
		FileInputStream fis;
		FileOutputStream fos;
		byte[] buffer = new byte[32768];
		int readBytes;
		
		fis = new FileInputStream("C:\\temp2\\mike\\project\\UTA1\\Rcp.chm");
		fos = new FileOutputStream("C:\\temp2\\mike\\project\\UTA1\\Rcp2.chm");
		
		while((readBytes = fis.read(buffer)) != -1) {
			fos.write(buffer, 0, readBytes);
			System.out.println(readBytes + " read");
		}
		
		fis.close();
		fos.close();
	}
}
