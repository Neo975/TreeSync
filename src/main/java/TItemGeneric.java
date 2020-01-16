import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.nio.file.Path;

public abstract class TItemGeneric extends File {
	private static final int BUFFER_SIZE = 32768;
	private Path root;
	protected Path relative;
	private long crcValue;

	public TItemGeneric(File file, File root) {
		super(file.getPath());
		this.root = root.toPath();
		this.relative = this.root.relativize(file.toPath());
        this.crcValue = calculateCRC32();
	}
	
	public String getRelative() {
		return relative.toString();
	}
	
	public String getRoot() {
		return root.toString();
	}

	public long getCrcValue() {
    	return crcValue;
	}

	private long calculateCRC32() {
		FileInputStream fis = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		CRC32 crc = new CRC32();
		int readBytes;

		try {
			fis = new FileInputStream(this);
			while((readBytes = fis.read(buffer)) != -1) {
				crc.update(buffer, 0, readBytes);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found exception: " + this.getPath());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Input/output exception accessing file: " + this.getPath());
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				System.err.println("An input/output exception was thrown when closing the stream");
				e.printStackTrace();
			}
		}
		return crc.getValue();
	}
}
