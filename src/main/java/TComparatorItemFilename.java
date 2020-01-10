import java.util.Comparator;

public class TComparatorItemFilename implements Comparator<TItemGeneric> {
    @Override
    public int compare(TItemGeneric item1, TItemGeneric item2) {
        return (item1.getRelative()).compareTo(item2.getRelative());
    }
}
