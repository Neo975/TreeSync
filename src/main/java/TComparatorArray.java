import java.util.Comparator;

public class TComparatorArray implements Comparator<TItemGeneric> {
    @Override
    public int compare(TItemGeneric item1, TItemGeneric item2) {
        return item1.getAbsolutePath().compareTo(item2.getAbsolutePath());
    }
}
