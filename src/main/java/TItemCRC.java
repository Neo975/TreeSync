import java.io.File;

public class TItemCRC extends TItemGeneric {

    public TItemCRC(File file, File root) {
        super(file, root);
		System.out.println("TItemCRC constructor executing");

	}

	public boolean equals(Object other) {
		System.out.println("TItemCRC equals() method executing");

		if(other == null) {
			return false;
		}
		if(!(other instanceof TItemGeneric)) {
			return false;
		}

		return (this.getRelative()).equals(((TItemGeneric)other).getRelative()) && (this.crcValue == ((TItemGeneric)other).crcValue);
    }

    public int hashCode() {
		System.out.println("TItemCRC hashCode() method executing");
		return relative.hashCode() + (int)(crcValue ^ (crcValue >>> 32));
	}

/*
	@Override
	public int compare(Object o1, Object o2) {
		System.out.println("TIC compareTo");
		if(!(o1 instanceof TItemGeneric) || !(o2 instanceof TItemGeneric)) {
			return 0;
		}
		return ((TItemGeneric) o1).compareTo((TItemGeneric) o2);
	}

 */
/*
	@Override
	public int compareTo(File pathname) {
		System.out.println("TItemCRC.compareTo()");
		return 1;
	}

 */
}
