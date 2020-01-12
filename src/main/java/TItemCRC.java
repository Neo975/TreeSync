import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;

public class TItemCRC extends TItemGeneric {
	private static final int BUFFER_SIZE = 32768;
	private long crcValue;

    public TItemCRC(File file, File root, int priority) {
        super(file, root, priority);
        crcValue = getCRC32();
	}

	public long getCrcValue() {
    	return crcValue;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(!(other instanceof TItemCRC)) {
			return false;
		}
		return this.crcValue == ((TItemCRC)other).crcValue;
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
