import java.io.File;
import java.util.*;

public class TSync {
	private static final String PROGRAM_VERSION = "1.3.0";
    private static File FOLDER_ONE;
    private static File FOLDER_TWO;
    private static Set<TItemGeneric> setOne;
    private static Set<TItemGeneric> setTwo;
	private static Map<Long, List<TItemGeneric>> mapOne;
	private static Map<Long, List<TItemGeneric>> mapTwo;
    private static CompareType type;
//    private static Comparator<TItemGeneric> comparator;
    private static int countSubItems = 0;
    private static int currentStage = 0;
	
	private File folderOne;
	private File folderTwo;
	private CompareType compareType;
	private Comparator<TItemGeneric> comparator;

    public static void main(String[] args) {
/*		
		checkArgs(args);
		long startTime = System.nanoTime();
		countSubItems += countSubItems(FOLDER_ONE);
		countSubItems += countSubItems(FOLDER_TWO);
		mapOne = new TreeMap<>();
		mapTwo = new TreeMap<>();
        setOne = scanFolder(FOLDER_ONE, FOLDER_ONE, mapOne);
        setTwo = scanFolder(FOLDER_TWO, FOLDER_TWO, mapTwo);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000_000;
        System.out.println("Execution time: " + duration + " seconds");   //in seconds
		printSymmetricDifference();
		System.out.println("Printout duplicates for " + FOLDER_ONE);
		printDuplicates(mapOne);
		System.out.println("Printout duplicates for " + FOLDER_TWO);
		printDuplicates(mapTwo);
*/
		File folderOne = parseArgsForFolderOne(args);
		File folderTwo = parseArgsForFolderTwo(args);
		CompareType compareType = parseArgsForType(args);
		if (folderOne == null || folderTwo == null || compareType == null) {
			printHelp();
			System.exit(1);
		}
		TSync tsyncInstance = new TSync(folderOne, folderTwo, compareType);
    }
	
	public TSync(File folderOne, File folderTwo, CompareType compareType) {
		if (folderOne == null) {
			throw new IllegalArgumentException("Argument folderOne can't be null");
		}
		if (folderTwo == null) {
			throw new IllegalArgumentException("Argument folderTwo can't be null");
		}
        if (folderOne.exists() || !folderOne.isDirectory()) {
			throw new IllegalArgumentException("Object folderOne is not exists or it is not a folder");
        }
        if (!FOLDER_TWO.exists() || !FOLDER_TWO.isDirectory()) {
			throw new IllegalArgumentException("Object folderTwo is not exists or it is not a folder");
        }
		if (compareType == null) {
			throw new IllegalArgumentException("Argument compareType can't be null");
		}
		this.folderOne = folderOne;
		this.folderTwo = folderTwo;
		this.compareType = compareType;
	}
	
	public 
	
	private static File parseArgsForFolderOne(String[] args) {
		if (args.length != 4) {
			return null;
		}

		return new File(args[2]);
	}
	
	private static File parseArgsForFolderTwo(String[] args) {
		if (args.length != 4) {
			return null;
		}

		return new File(args[3]);
	}
	
	private static CompareType parseArgsForType(String[] args) {
        if(args.length != 4) {
			return null;
        }
        if(!args[0].equalsIgnoreCase("-c")) {
			return null;
		}
        if(!args[1].equalsIgnoreCase("crc") && !args[1].equalsIgnoreCase("filename")) {
			return null;
		}
        if(args[1].equalsIgnoreCase("crc")) {
			comparator = new TComparatorItemCRC();

        	return CompareType.CRC;
		}
        if(args[1].equalsIgnoreCase("filename")) {
			comparator = new TComparatorItemFilename();

        	return CompareType.FILENAME;
		}
		
		return null;
	}

/*
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
*/

    private static Set<TItemGeneric> scanFolder(File folder, File root, Map<Long, List<TItemGeneric>> map) {
        TreeSet<TItemGeneric> finalSet = new TreeSet<>(comparator);
		File[] childes = folder.listFiles();

		if(childes == null) {
			System.err.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return finalSet;
		}
		for (File obj : childes) {
			if (obj.isDirectory()) {
				finalSet.addAll(scanFolder(obj, root, map));		//recursive call
			} else if (type == CompareType.CRC) {
				TItemGeneric item = new TItemCRC(obj, root);
				finalSet.add(item);		//recursive call
				if (map.get(item.getCrcValue()) != null) {	//уже имеется список, состоящий минимум из одного файла
					map.get(item.getCrcValue()).add(item);	//Найден дубликат файла
				} else {
					List<TItemGeneric> list = new ArrayList<>();
					list.add(item);
					map.put(item.getCrcValue(), list);
				}
				currentStage++;
				printProgressBar(currentStage, countSubItems);
			} else if (type == CompareType.FILENAME) {
				TItemGeneric item = new TItemFilename(obj, root);
				finalSet.add(item);	//recursive call
				if (map.get(item.getCrcValue()) != null) {	//уже имеется список, состоящий минимум из одного файла
					map.get(item.getCrcValue()).add(item);	//Найден дубликат файла
				} else {
					List<TItemGeneric> list = new ArrayList<>();
					list.add(item);
					map.put(item.getCrcValue(), list);
				}
				currentStage++;
				printProgressBar(currentStage, countSubItems);
			}
		}

        return finalSet;
    }

	private static int countSubItems(File folder) {
		int count = 0;
		File[] childes = folder.listFiles();

		if(childes == null) {
			System.err.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return 0;
		}
		for (File obj : childes) {
			if (obj.isDirectory()) {
				count += countSubItems(obj);	//recursive call
			} else {
				count += 1;						//recursive call
			}
		}

		return count;
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
		System.out.println("Printout of differences between folder contents:");
		for(TItemGeneric item : items) {
			if(item.getRoot().equals(FOLDER_ONE.getPath())) {
				System.out.println("(1) " + item.getRelative());
			} else {
				System.out.println("(2) " + item.getRelative());
			}
		}
	}
	
	private static void printDuplicates(Map<Long, List<TItemGeneric>> map) {
		System.out.println("Display duplicate file information:");
		Iterator<List<TItemGeneric>> iterator = map.values().iterator();
		int step = 1;
		while (iterator.hasNext()) {
			List<TItemGeneric> list = iterator.next();
			if (list.size() > 1) {	//Найдены дубликаты
				System.out.println(step + ") Duplicates found:");
				for (TItemGeneric item : list) {
					System.out.println(step + ") " + item.getAbsolutePath());
				}
			}
//			System.out.println("-----------------------------------------------------------------");
			step++;
		}
	}

	private static void printHelp() {
		System.out.println("TreeSync, version " + PROGRAM_VERSION);
		System.out.println("Utility for compare the contents of two folders and duplicates search");
		System.out.println("1) Usage checking by CRC: java -jar ts.jar -c crc Path_to_first_checking_folder Path_to_second_checking_folder");
		System.out.println("2) Usage checking by file name: java -jar ts.jar -c filename Path_to_first_checking_folder Path_to_second_checking_folder");
		System.out.println("Example 1: java -jar ts.jar -c crc C:\\Temp D:\\Projects");
		System.out.println("Example 2: java -jar ts.jar -c filename C:\\Temp D:\\Projects");
	}

	private static void printProgressBar(int currentStage, int maxStage) {
    	int percent = currentStage * 100 / maxStage;
    	System.out.print("Processing: " + percent + "%\r");
	}

	private enum CompareType {
		CRC,
		FILENAME
	}
}
