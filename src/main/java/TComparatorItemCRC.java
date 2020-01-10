import java.util.Comparator;

public class TComparatorItemCRC implements Comparator<TItemGeneric> {
    @Override
    public int compare(TItemGeneric item1, TItemGeneric item2) {
        System.out.println("TComparatorItemCRC.compare()");
        if(item1.getCRC() == item2.getCRC()) {
            return 0;
        }

        return item1.compareTo(item2);
    }
}
