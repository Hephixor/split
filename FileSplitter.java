import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSplitter {
	private static File destDir;

	public static File[] splitFile(String filePath, long maxSize, String toDir)
			throws IOException {
		handleToDir(toDir);
		File[] splittedFiles;
		List<File> files = new ArrayList<File>();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String fileName = new File(filePath).getName();
		StringBuffer fileContent = new StringBuffer();
		String line;
		File currentFile = FileSplitter.createSplittedFile(fileName, files
				.size());
		while ((line = reader.readLine()) != null) {
			fileContent.append(line);
			if (fileContent.length() >= maxSize) {
				FileSplitter.writeFile(currentFile, fileContent.toString());
				files.add(currentFile);
				fileContent = new StringBuffer();
				currentFile = FileSplitter.createSplittedFile(fileName, files
						.size());
			}
		}
		FileSplitter.writeFile(currentFile, fileContent.toString());
		files.add(currentFile);

		splittedFiles = new File[files.size()];
		int c=0;
		for (File file : files) {
			splittedFiles[c] = files.get(c);
			c++;
		}
		return splittedFiles;
	}

	private static void handleToDir(String toDir) {
		destDir = new File(toDir);
		if (destDir.exists())
			destDir.delete();
		destDir.mkdir();
	}

	private static File createSplittedFile(String fileName, int index)
			throws IOException {
		return File.createTempFile(new File(fileName).getName() + "_" + index,
				".spf", destDir);
	}

	private static void writeFile(File destFile, String content)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
		writer.write(content);
		writer.flush();
		writer.close();
		writer = null;
	}

	public static void main(String[] args) {
		try {
			// 1048576 = 1Mo
			System.out.println(FileSplitter.splitFile("D:/huge.txt", 1048576,
					"D:/testSplit").length
					+ " files generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}