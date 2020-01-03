import java.io.File;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.util.zip.CRC32;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TItem extends File {
	private static final int BUFFER_SIZE = 32768;
	private Path root;
	private Path relative;
	private long crcValue;
	
	public TItem(File file, File root) {
		super(file.getPath());
		this.root = root.toPath();
		this.relative = this.root.relativize(file.toPath());
		this.crcValue = getCRC32();
	}
	
	public String getRelative() {
		return relative.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(!(other instanceof TItem)) {
			return false;
		}

		return (this.getRelative()).equals(((TItem)other).getRelative()) && (this.crcValue == ((TItem)other).crcValue);
	}
	
	@Override
	public int hashCode() {
		return relative.hashCode() + (int)(crcValue ^ (crcValue >>> 32));
	}
	
	private long getCRC32() {
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
			System.out.println("File not found exception: " + this.getPath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Input/output exception accessing file: " + this.getPath());
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				System.out.println("An input/output exception was thrown when closing the stream");
				e.printStackTrace();
			}
		}
		
		return crc.getValue();
	}
}
