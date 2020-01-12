import java.util.Comparator;

public class TComparatorItemCRC implements Comparator<TItemGeneric> {
    @Override
    public int compare(TItemGeneric item1, TItemGeneric item2) {
        if(((TItemCRC)item1).getCrcValue() == ((TItemCRC)item2).getCrcValue()) {
            return 0;
        } else if(((TItemCRC)item1).getCrcValue() > ((TItemCRC)item2).getCrcValue()) {
            return 1;
        } else {
            return -1;
        }
    }
}
