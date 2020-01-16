import java.io.File;

public class TItemCRC extends TItemGeneric {

    public TItemCRC(File file, File root) {
        super(file, root);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof TItemCRC)) {
			return false;
		}
		return this.getCrcValue() == ((TItemCRC)other).getCrcValue();
    }

    @Override
	public int hashCode() {
		return relative.hashCode() + (int)(this.getCrcValue() ^ (this.getCrcValue() >>> 32));
	}
}
