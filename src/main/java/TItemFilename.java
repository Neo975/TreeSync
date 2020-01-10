import java.io.File;

public class TItemFilename extends TItemGeneric {

    public TItemFilename(File file, File root) {
        super(file, root);
        System.out.println("TItemFilename constructor executing");
    }

    @Override
    public boolean equals(Object other) {
        System.out.println("TItemFilename equals() method executing");

        if(other == null) {
            return false;
        }
        if(!(other instanceof TItemGeneric)) {
            return false;
        }

        return (this.getRelative()).equals(((TItemGeneric)other).getRelative());
    }

    public int hashCode() {
        System.out.println("TItemFilename hashCode() method executing");

        return relative.hashCode();
    }

}
