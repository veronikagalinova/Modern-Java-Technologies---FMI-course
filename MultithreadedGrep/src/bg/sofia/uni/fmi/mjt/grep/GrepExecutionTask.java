package bg.sofia.uni.fmi.mjt.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GrepExecutionTask implements Callable<String> {
	private File file;
	private String stringToFind;
	private boolean isCaseSensitivityEnabled = false;
	private boolean isWholeWordsOptionsEnabled = false;

	public GrepExecutionTask(File file, String stringToFind, boolean isCaseSensitivityEnabled,
			boolean isWholeWordsOptionsEnabled) {
		this.file = file;
		this.stringToFind = stringToFind;
		this.isCaseSensitivityEnabled = isCaseSensitivityEnabled;
		this.isWholeWordsOptionsEnabled = isWholeWordsOptionsEnabled;
	}

	@Override
	public String call() throws Exception {
		StringBuilder sBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file)))) {
			String line;
			int index = 0;
			while ((line = br.readLine()) != null) {
				index++;

				if (isWholeWordsOptionsEnabled) {
					stringToFind = "\\b" + stringToFind + "\\b";
				}
				Pattern pattern = Pattern.compile(stringToFind);
				if (isCaseSensitivityEnabled) {
					pattern = Pattern.compile(stringToFind, Pattern.CASE_INSENSITIVE);
				}

				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					sBuilder.append(file.getAbsolutePath()).append(":" + index + ":").append(line)
							.append(System.lineSeparator());
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("Unable to open file " + file + ": " + e.getMessage());
			System.exit(1);
		}
		return sBuilder.toString();
	}

}
