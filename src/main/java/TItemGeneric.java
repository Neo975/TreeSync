import java.io.File;
import java.nio.file.Path;

public abstract class TItemGeneric extends File {
	private Path root;
	protected Path relative;

	public TItemGeneric(File file, File root) {
		super(file.getPath());
		this.root = root.toPath();
		this.relative = this.root.relativize(file.toPath());
	}
	
	public String getRelative() {
		return relative.toString();
	}
	
	public String getRoot() {
		return root.toString();
	}

}
