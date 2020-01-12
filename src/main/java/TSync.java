import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Set;

public class TSync {
	private static final String PROGRAM_VERSION = "1.2.4";
    private static File FOLDER_ONE;
    private static File FOLDER_TWO;
    private static Set<TItemGeneric> setOne;
    private static Set<TItemGeneric> setTwo;
    private static Type type;
    private static Comparator<TItemGeneric> comparator;

    public static void main(String[] args) {
		checkArgs(args);
        setOne = scanFolder(FOLDER_ONE, FOLDER_ONE, 1);
        long startTime = System.nanoTime();
        setTwo = scanFolder(FOLDER_TWO, FOLDER_TWO, 2);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000_000;
        System.out.println("Execution time: " + duration + " seconds");   //in seconds
		printSymmetricDifference();
    }

    private static void checkArgs(String[] args) {
        if(args.length != 4) {
			printHelp();
			System.exit(1);
        }
        if(!args[0].equalsIgnoreCase("-c")) {
			printHelp();
			System.exit(1);
		}
        if(!args[1].equalsIgnoreCase("crc") && !args[1].equalsIgnoreCase("filename")) {
        	printHelp();
			System.exit(1);
		}
        if(args[1].equalsIgnoreCase("crc")) {
        	type = Type.CRC;
		}
        if(args[1].equalsIgnoreCase("filename")) {
        	type = Type.FILENAME;
		}
        FOLDER_ONE = new File(args[2]);
        FOLDER_TWO = new File(args[3]);
        if (!FOLDER_ONE.exists() || !FOLDER_ONE.isDirectory()) {
            System.out.println("First checking file object is not exists or it is not a folder");
            System.exit(1);
        }
        if (!FOLDER_TWO.exists() || !FOLDER_TWO.isDirectory()) {
            System.out.println("Second checking file object is not exists or it is not a folder");
            System.exit(1);
        }
		if(type == Type.CRC) {
			comparator = new TComparatorItemCRC();
		}
		if(type == Type.FILENAME) {
			comparator = new TComparatorItemFilename();
		}
    }

    private static Set<TItemGeneric> scanFolder(File folder, File root, int priority) {
        TreeSet<TItemGeneric> finalSet = new TreeSet<>(comparator);
		File[] childes = folder.listFiles();

		if(childes == null) {
			System.err.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return finalSet;
		}
		for (File obj : childes) {
			if (obj.isDirectory()) {
				finalSet.addAll(scanFolder(obj, root, priority));		//recursive call
			} else if (type == Type.CRC) {
				finalSet.add(new TItemCRC(obj, root, priority));		//recursive call
			} else if (type == Type.FILENAME) {
				finalSet.add(new TItemFilename(obj, root, priority));	//recursive call
			}
		}

        return finalSet;
    }
	
	private static Set<TItemGeneric> symmetricDifference() {
		TreeSet<TItemGeneric> symDiff = new TreeSet<>(comparator);
		symDiff.addAll(setOne);
		symDiff.addAll(setTwo);
		symDiff.removeAll(intersection());
		
		return symDiff;
	}
	
	private static Set<TItemGeneric> intersection() {
		TreeSet<TItemGeneric> intersection = new TreeSet<>(comparator);
		intersection.addAll(setOne);
		intersection.retainAll(setTwo);

		return intersection;
	}
	
	private static void printSymmetricDifference() {
		Set<TItemGeneric> symmetricDifference = symmetricDifference();
		if (symmetricDifference.size() == 0) {
			return;
		}
		TItemGeneric[] items = symmetricDifference.toArray(new TItemGeneric[1]);
		Arrays.sort(items, new TComparatorArray());
		for(TItemGeneric item : items) {
			if(item.getRoot().equals(FOLDER_ONE.getPath())) {
				System.out.println("(1) " + item.getRelative());
			} else {
				System.out.println("(2) " + item.getRelative());
			}
		}
	}

	private static void printHelp() {
		System.out.println("TreeSync, version " + PROGRAM_VERSION);
		System.out.println("Utility for compare the contents of two folders");
		System.out.println("1) Usage checking by CRC: java -jar ts.jar -c crc Path_to_first_checking_folder Path_to_second_checking_folder");
		System.out.println("2) Usage checking by file name: java -jar ts.jar -c filename Path_to_first_checking_folder Path_to_second_checking_folder");
		System.out.println("Example 1: java -jar ts.jar -c crc C:\\Temp D:\\Projects");
		System.out.println("Example 2: java -jar ts.jar -c filename C:\\Temp D:\\Projects");
	}

	private enum Type {
		CRC,
		FILENAME
	}
}
