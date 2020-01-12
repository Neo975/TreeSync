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



//        System.out.println("TComparatorItemCRC.compare()");



/*

        if(((TItemCRC)item1).getCrcValue() == ((TItemCRC)item2).getCrcValue()) {
            return 0;
        } else {
            return (item1.getRelative()).compareTo(item2.getRelative());
        }
*/

/*
        System.out.println("TComparatorItemCRC.compare()");
        if (((TItemCRC)item1).getCrcValue() == ((TItemCRC)item2).getCrcValue()) {
            return 0;
        } else {
            String s11 = item1.getTemp() + item1.getRelative();
            String s12 = item2.getTemp() + item2.getRelative();
            int k1 = s11.compareTo(s12);
            int ak1 = s12.compareTo(s11);
            String s21 = item1.getRelative();
            String s22 = item2.getRelative();
            int k2 = s21.compareTo(s22);
            int ak2 = s22.compareTo(s21);

            if (item1.getPriority() < item2.getPriority()) {
                return -1;
            } else if (item1.getPriority() > item2.getPriority()) {
                return 1;
            } else {
                return (item1.getRelative()).compareTo(item2.getRelative());
            }
        }


 */

    }
}
