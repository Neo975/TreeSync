package com.hubber.TreeSync;

import java.io.File;
import java.util.*;

public class TSync {
	private static final String PROGRAM_VERSION = "1.3.1";
    private static int countSubItems = 0;
    private static int currentStage = 0;
	private Set<TItemGeneric> itemSet;
	private Map<Long, List<TItemGeneric>> itemMap;

    public static void main(String[] args) {
		File folderOne = parseArgsForFolderOne(args);
		File folderTwo = parseArgsForFolderTwo(args);
		File folderDup = parseArgsForFolderDup(args);
		CompareType compareType = parseArgsForType(args);
		if (compareType != null && (folderOne == null || folderTwo == null)) {
			printHelp();
			System.exit(1);
		}
		if (compareType == null && folderDup == null) {
			printHelp();
			System.exit(1);
		}
		if (compareType == CompareType.CRC || compareType == CompareType.FILENAME) {
			TSync tSyncInstance1 = new TSync(folderOne, compareType);
			TSync tSyncInstance2 = new TSync(folderTwo, compareType);
			Set<TItemGeneric> complement1 = tSyncInstance1.getComplement(tSyncInstance2);
			Set<TItemGeneric> complement2 = tSyncInstance2.getComplement(tSyncInstance1);
			printComplement(folderOne + " differs from " + folderTwo + " in the following files:", "1) ", complement1);
			printComplement(folderTwo + " differs from " + folderOne + " in the following files:", "2) ", complement2);
		}
		if (folderDup != null) {
			TSync tSyncInstance3 = new TSync(folderDup);
			tSyncInstance3.printDuplicates(tSyncInstance3.getMap());
		}
    }
	
	public TSync(File rootFolder, CompareType compareType) {
		if (rootFolder == null) {
			throw new IllegalArgumentException("Argument rootFolder can't be null");
		}
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
			throw new IllegalArgumentException("Object rootFolder is not exists or it is not a folder");
        }
        this.itemMap = new TreeMap<>();
		itemSet = scanFolder(rootFolder, rootFolder, compareType, itemMap);
	}

	public TSync(File rootFolder) {
		if (rootFolder == null) {
			throw new IllegalArgumentException("Argument rootFolder can't be null");
		}
		if (!rootFolder.exists() || !rootFolder.isDirectory()) {
			throw new IllegalArgumentException("Object rootFolder is not exists or it is not a folder");
		}
		this.itemMap = new TreeMap<>();
		itemSet = scanFolder(rootFolder, rootFolder, CompareType.CRC, itemMap);
	}

	public Set<TItemGeneric> getComplement(TSync otherSync) {
		HashSet<TItemGeneric> setComplement = new HashSet<>();
		setComplement.addAll(this.getSet());
		setComplement.removeAll(otherSync.getSet());

		return setComplement;
	}

	public Set<TItemGeneric> getSet() {
		return itemSet;
	}

	public Map<Long, List<TItemGeneric>> getMap() {
    	return itemMap;
	}
	
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

	private static File parseArgsForFolderDup(String[] args) {
		if(!(args.length == 4 || args.length == 4 || args.length == 2)) {
			return null;
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-d")) {
				if(args[i + 1] != null) {
					return new File(args[i + 1]);
				} else {
					return null;
				}
			}
		}

		return null;
	}

	private static CompareType parseArgsForType(String[] args) {
        if(!(args.length == 4 || args.length == 4 || args.length == 2)) {
			return null;
        }
        for (int i = 0; i < args.length; i++) {
        	if (args[i].equals("-c")) {
				if(args[i + 1].equalsIgnoreCase("crc")) {
					return CompareType.CRC;
				}
				if(args[i + 1].equalsIgnoreCase("filename")) {
					return CompareType.FILENAME;
				}
			}
		}
        if(!args[0].equalsIgnoreCase("-c")) {
			return null;
		}
        if(!args[1].equalsIgnoreCase("crc") && !args[1].equalsIgnoreCase("filename")) {
			return null;
		}

		return null;
	}

    private static Set<TItemGeneric> scanFolder(File folder,
												File root,
												CompareType compareType,
												Map<Long, List<TItemGeneric>> map) {
    	Set<TItemGeneric> finalSet = new HashSet<>();
		File[] childes = folder.listFiles();

		if(childes == null) {
			System.err.println("Problem accessing folder. Check if you have access rights to this directory: \"" + folder.getPath() + "\"");
			return finalSet;
		}
		for (File obj : childes) {
			if (obj.isDirectory()) {
				finalSet.addAll(scanFolder(obj, root, compareType, map));		//recursive call
			} else if (compareType == CompareType.CRC) {
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
//				printProgressBar(currentStage, countSubItems);
			} else if (compareType == CompareType.FILENAME) {
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
//				printProgressBar(currentStage, countSubItems);
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

	private static void printComplement(String message, String prefix, Set<TItemGeneric> set) {
		if (set.size() == 0) {
			System.out.println("No differences found");
			return;
		}
		TItemGeneric[] items = set.toArray(new TItemGeneric[1]);
		Arrays.sort(items, new TComparatorArray());
		System.out.println(message);
		for(TItemGeneric item : items) {
			System.out.println(prefix + item.getRelative());
		}
	}
	
	public static void printDuplicates(Map<Long, List<TItemGeneric>> map) {
		System.out.println("Display duplicate files information:");
		Iterator<List<TItemGeneric>> iterator = map.values().iterator();
		int step = 1;
		while (iterator.hasNext()) {
			List<TItemGeneric> list = iterator.next();
			if (list.size() > 1) {	//Найдены дубликаты
				System.out.println(step + ") Duplicate files found:");
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
		System.out.println("Utility for compare the contents of two folders and for duplicate files search");
		System.out.println("Usage: java -jar ts.jar [-c (crc | filename) <Path1> <Path2>] [-d <Path3>]");
		System.out.println("-c				Use \"compare mode\". In this mode the utility compares ");
		System.out.println("				the contents of two directories that are specified in the Path1, Path2 arguments.");
		System.out.println("				Key \"crc\" is used when checksum comparison is used");
		System.out.println("				Key \"filename\" is used when file names comparison is used");
		System.out.println("-d				Use \"duplicate search mode\". In this mode the utility searches for duplicate files ");
		System.out.println("				in the directory that is specified in the Path3 argument");
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
