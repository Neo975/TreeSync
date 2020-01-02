import java.io.File;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.util.zip.CRC32;
import java.io.IOException;
import java.io.FileNotFoundException;

public class TItem extends File{
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
		FileInputStream fis;
		byte[] buffer = new byte[32768];
		CRC32 crc = new CRC32();

		try {
			fis = new FileInputStream(this);
			while(fis.read(buffer) != -1) {
				crc.update(buffer);
			}
		} catch (FileNotFoundException e) {
			
		} catch(IOException e) {
			
		}
		
		return crc.getValue();
	}
}
