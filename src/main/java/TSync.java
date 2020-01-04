import java.io.File;
import java.util.TreeSet;
import java.util.Set;

public class TSync {
    private static File FOLDER_ONE;
    private static File FOLDER_TWO;
    private static Set<TItem> setOne;
    private static Set<TItem> setTwo;

    public static void main(String[] args) {
		checkArgs(args);
        setOne = scanFolder(FOLDER_ONE, FOLDER_ONE);
        long startTime = System.nanoTime();
        setTwo = scanFolder(FOLDER_TWO, FOLDER_TWO);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000_000;
        System.out.println("Execution time: " + duration + " seconds");   //in seconds
		printSymmetricDifference();
    }

    private static void checkArgs(String[] args) {
        if(args.length != 2) {
            System.out.println("TreeSync, version 1.1");
            System.out.println("Utility for compare the contents of two folders");
            System.out.println("Usage: java -jar ts.jar Path_to_first_checking_folder Path_to_second_checking_folder");
            System.out.println("Example: java -jar ts.jar C:\\Temp D:\\Projects");
			System.exit(1);
        }
        FOLDER_ONE = new File(args[0]);
        FOLDER_TWO = new File(args[1]);
        if (!FOLDER_ONE.exists() || !FOLDER_ONE.isDirectory()) {
            System.out.println("First checking file object is not exists or it is not a folder");
            System.exit(1);
        }
        if (!FOLDER_TWO.exists() || !FOLDER_TWO.isDirectory()) {
            System.out.println("Second checking file object is not exists or it is not a folder");
            System.exit(1);
        }
    }

    private static Set<TItem> scanFolder(File folder, File root) {
        TreeSet<TItem> finalSet = new TreeSet<>();
		File[] childs = folder.listFiles();
		
		if(childs == null) {
			System.err.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return finalSet;
		}
		for (File obj : childs) {
			if (obj.isDirectory()) {
				finalSet.addAll(scanFolder(obj, root));	//recursive call
			} else {
				finalSet.add(new TItem(obj, root));
			}
		}

        return finalSet;
    }
	
	private static Set<TItem> symmetricDifference() {
		TreeSet<TItem> symDiff = new TreeSet<>();
		symDiff.addAll(setOne);
		symDiff.addAll(setTwo);
		symDiff.removeAll(intersection());
		
		return symDiff;
	}
	
	private static Set<TItem> intersection() {
		TreeSet<TItem> intersection = new TreeSet<>();
		intersection.addAll(setOne);
		intersection.retainAll(setTwo);
		
		return intersection;
	}
	
	private static void printSymmetricDifference() {
		Set<TItem> symmetricDifference = symmetricDifference();
		
		for(TItem item : symmetricDifference) {
			if(item.getRoot().equals(FOLDER_ONE.getPath())) {
				System.out.println("(1) " + item.getRelative());
			} else {
				System.out.println("(2) " + item.getRelative());
			}
		}
	}
}
