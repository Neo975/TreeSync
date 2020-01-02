import java.io.File;
import java.nio.file.Path;

public class TItem extends File{
	private Path root;
	private Path relative;
	
	public TItem(File file, File root) {
		super(file.getPath());
		this.root = root.toPath();
		this.relative = this.root.relativize(file.toPath());
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

		return (this.getRelative()).equals(((TItem)other).getRelative());
	}
	
	@Override
	public int hashCode() {
		return relative.hashCode();
	}
}
