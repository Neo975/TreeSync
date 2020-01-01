import java.io.File;
import java.util.HashSet;

//Commit 01.01.2020
public class TSync {
    private static File FOLDER_ONE;
    private static File FOLDER_TWO;
    private static HashSet<TItem> setOne;
    private static HashSet<TItem> setTwo;

    public static void main(String[] args) {

        checkArgs(args);
        setOne = scanFolder(FOLDER_ONE);
        long startTime = System.nanoTime();
        setTwo = scanFolder(FOLDER_TWO);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000_000;
        System.out.println("duration: " + duration);   //in seconds
        System.out.println("setOne's size: " + setOne.size());
        System.out.println("setTwo's size: " + setTwo.size());

//		tempMethod();
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

    private static HashSet<TItem> scanFolder(File folder) {
        HashSet<TItem> finalSet = new HashSet<>();

        try {
            for (File child : folder.listFiles()) {
                if (child.isDirectory()) {
                    //recursive call
                    finalSet.addAll(scanFolder(child));
                } else {
                    finalSet.add(new TItem(child));
                }
            }
        } catch (Exception e) {
            System.out.println("Problem file object: " + folder.getPath());
            e.printStackTrace();
        }

        return finalSet;
    }
	
	private static void tempMethod() {
		TItem item = new TItem("C:\\temp2\\mike\\project\\src\\main\\java");
		File[] files = item.listFiles();
		for(File f : files) {
			System.out.println(f.getPath());
		}
	}
}
