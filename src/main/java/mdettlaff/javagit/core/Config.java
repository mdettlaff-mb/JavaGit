package mdettlaff.javagit.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

public class Config {

	private final FilesWrapper files;
	private final Path path;

	private Map<String, String> values;

	public Config(FilesWrapper files, Path path) {
		this.files = files;
		this.path = path;
	}

	public String get(String key) throws IOException {
		if (values == null) {
			init();
		}
		String value = values.get(key);
		Preconditions.checkNotNull(value, "Config value not found: " + key);
		return value;
	}

	private void init() throws IOException {
		Preconditions.checkState(files.exists(path), "Config file not found in: " + path);
		values = new HashMap<>();
		Reader reader = new InputStreamReader(files.newInputStream(path));
		try (BufferedReader bufferedReader = new BufferedReader(reader)) {
			String prefix = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				Matcher sectionMatcher = Pattern.compile("\\[(.+)]").matcher(line);
				if (sectionMatcher.matches()) {
					prefix = sectionMatcher.group(1);
					continue;
				}
				Matcher propertyMatcher = Pattern.compile("(.+)=(.+)").matcher(line);
				if (propertyMatcher.matches()) {
					String key = prefix + "." + propertyMatcher.group(1).trim();
					String value = propertyMatcher.group(2).trim();
					values.put(key, value);
				}
			}
		}
	}
}
