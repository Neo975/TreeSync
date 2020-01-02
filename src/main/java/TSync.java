import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.nio.file.Path;

public class TSync {
    private static File FOLDER_ONE;
    private static File FOLDER_TWO;
    private static HashSet<TItem> setOne;
    private static HashSet<TItem> setTwo;

    public static void main(String[] args) {
//		checkArgs(args);
//        checkArgs(new String[] {"C:\\temp2\\mike\\project\\UTA1", "C:\\temp2\\mike\\project\\UTA2"});
        checkArgs(new String[] {"C:\\Program Files", "C:\\Program Files (x86)"});
        setOne = scanFolder(FOLDER_ONE, FOLDER_ONE);
        long startTime = System.nanoTime();
        setTwo = scanFolder(FOLDER_TWO, FOLDER_TWO);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000_000;
        System.out.println("duration: " + duration);   //in seconds
		
		testMethod();
		HashSet<TItem> symmetricDifference = symmetricDifference();
		HashSet<TItem> intersection = intersection();
/*
		for(TItem item : symmetricDifference) {
			System.out.println("Symmetric difference: " + item.getPath());
		}
*/
		for(TItem item : intersection) {
			System.out.println("Intersection: " + item.getPath());
		}
    }

    private static void checkArgs(String[] args) {
        if(args.length != 2) {
            System.out.println("TreeSync, version 1.0");
            System.out.println("Usage: java -jar TreeSync.jar Path_to_first_checking_folder Path_to_second_checking_folder");
            System.out.println("Example: java -jar TreeSync.jar C:\\Temp D:\\Projects");
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

    private static HashSet<TItem> scanFolder(File folder, File root) {
        HashSet<TItem> finalSet = new HashSet<>();
		File[] childs = folder.listFiles();
		
		if(childs == null) {
			System.out.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return finalSet;
		}
		for (File obj : childs) {
			if (obj.isDirectory()) {
				//recursive call
				finalSet.addAll(scanFolder(obj, root));
			} else {
				finalSet.add(new TItem(obj, root));
			}
		}

        return finalSet;
    }
	
	private static HashSet<TItem> symmetricDifference() {
		HashSet<TItem> symDiff = new HashSet<>();
		symDiff.addAll(setOne);
		symDiff.addAll(setTwo);
		symDiff.removeAll(intersection());
		
		return symDiff;
	}
	
	private static HashSet<TItem> intersection() {
		HashSet<TItem> intersection = new HashSet<>();
		intersection.addAll(setOne);
		intersection.retainAll(setTwo);
		
		return intersection;
	}
	
	private static void testMethod() {
/*		
		File file1 = new File("C:\\temp2\\mike\\project\\UTA1\\CEUpdate.exe");
		File file2 = new File("C:\\temp2\\mike\\project\\UTA2\\CEUpdate.exe");
		TItem item1 = new TItem(file1, FOLDER_ONE);
		TItem item2 = new TItem(file2, FOLDER_TWO);
		HashSet<TItem> set1 = new HashSet<>();
		HashSet<TItem> set2 = new HashSet<>();
		set1.add(item1);
		set2.add(item2);
		boolean b = item1.equals(item2);
		System.out.println(b);
		System.out.println("item1.getRelative(): " + item1.getRelative());
		System.out.println("item2.getRelative(): " + item2.getRelative());
		
		for(TItem item : set1) {
			if(set2.contains(item)) {
				System.out.println("Matched! " + item.getRelative());
			}
		}
*/

/*		
        System.out.println("setOne's size: " + setOne.size());
        System.out.println("setTwo's size: " + setTwo.size());
		setOne.removeAll(setTwo);
		System.out.println("Intersection");
        System.out.println("setOne's size: " + setOne.size());
        System.out.println("setTwo's size: " + setTwo.size());
*/
		
/*		
		System.out.println("setOne content:");
		for(TItem item : setOne) {
			System.out.println(item.getRelative());
		}

		System.out.println("setTwo content:");
		for(TItem item : setTwo) {
			System.out.println(item.getRelative());
		}

		boolean b = setOne.retainAll(setTwo);
		System.out.println("b = " + b);
        System.out.println("setOne's size: " + setOne.size());
        System.out.println("setTwo's size: " + setTwo.size());
*/
	}
}
