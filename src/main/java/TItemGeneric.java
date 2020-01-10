import java.io.File;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.util.Comparator;
import java.util.zip.CRC32;
import java.io.IOException;
import java.io.FileNotFoundException;

public abstract class TItemGeneric extends File {
	private static final int BUFFER_SIZE = 32768;
	private Path root;
	protected Path relative;
	protected long crcValue;

	public TItemGeneric(File file, File root) {
		super(file.getPath());
		this.root = root.toPath();
		this.relative = this.root.relativize(file.toPath());
		this.crcValue = getCRC32();
	}
	
	public String getRelative() {
		return relative.toString();
	}
	
	public String getRoot() {
		return root.toString();
	}

	public long getCRC() {
		return crcValue;
	}

/*
	@Override
	public boolean equals(Object other) {
		System.out.println("---");
		if (this instanceof TItemCRC) {
			return ((TItemCRC)this).equals(other);
		}
		if (this instanceof TItemFilename) {
			return ((TItemFilename)this).equals(other);
		}
		return false;
	}
*/
	/*
	public int hashCode() {
		System.out.println("===");
		if (this instanceof TItemCRC) {
			return ((TItemCRC)this).hashCode();
		}
		if (this instanceof TItemFilename) {
			return ((TItemFilename)this).hashCode();
		}
		return 42;
	}
*/
/*
	@Override
	public int compare(Object o1, Object o2) {
		System.out.println("+++");
		if(!(o1 instanceof TItemGeneric) || !(o2 instanceof TItemGeneric)) {
			return 0;
		}
		return ((TItemGeneric) o1).compareTo((TItemGeneric) o2);
	}

	@Override
	public int compare(TItemGeneric o1, TItemGeneric o2) {
		System.out.println("+++");
		if(!(o1 instanceof TItemGeneric) || !(o2 instanceof TItemGeneric)) {
			return 0;
		}
		return ((TItemGeneric) o1).compareTo((TItemGeneric) o2);
	}

*/
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
