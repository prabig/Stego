import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Linecounter {
	public int lineCount = 0;
	
	public static void main(String[] args) throws FileNotFoundException {
		Linecounter lc = new Linecounter();
		lc.directoryCount(new File("C:\\Users\\prabi_000\\workspaceSE\\SE\\src"));
		System.out.println(lc.lineCount);
	}
	
	public void directoryCount(File directory) throws FileNotFoundException {
		for(File file : directory.listFiles()) {
			if(file.isDirectory()) {
				directoryCount(file);
			}
			else 
				countLines(file);
		}
	}
	
	public void countLines(File file) throws FileNotFoundException {
		FileInputStream fio = new FileInputStream(file);
		Scanner scanner = new Scanner(fio);
		try {
			String line;
			while(scanner.hasNext()) {
				line = scanner.nextLine();
				if(line.startsWith("//") || line.equals("{") || line.equals("}") || line.isEmpty()) {
					continue;
				}
				else if(line.startsWith("/**") || line.startsWith("/*")) {
					while(!(line.contains("*/") || line.contains("**/"))) {
						line = scanner.nextLine();
					}
				}
				else {
					lineCount++;
				}
			}
			scanner.close();
			return;
			
		} catch (NoSuchElementException e) {
			scanner.close();
			return;
		}
	}
	
}
