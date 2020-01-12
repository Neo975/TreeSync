import java.io.File;
import java.nio.file.Path;

public abstract class TItemGeneric extends File {
	private Path root;
	private int priority;
	protected Path relative;

	public TItemGeneric(File file, File root, int priority) {
		super(file.getPath());
		this.root = root.toPath();
		this.priority = priority;
		this.relative = this.root.relativize(file.toPath());
	}
	
	public String getRelative() {
		return relative.toString();
	}
	
	public String getRoot() {
		return root.toString();
	}

	public int getPriority() {
		return priority;
	}

	public String getTemp() {
		if (priority == 1) {
			return "A";
		} else {
			return "B";
		}
	}
}
