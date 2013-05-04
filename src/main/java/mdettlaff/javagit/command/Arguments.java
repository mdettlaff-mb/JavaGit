package mdettlaff.javagit.command;

import java.util.ArrayList;
import java.util.List;

public class Arguments {

	private List<String> parameters;
	private List<String> options;

	public Arguments(String[] args) {
		parameters = new ArrayList<>();
		options = new ArrayList<>();
		for (String arg : args) {
			if (arg.matches("--..+")) {
				options.add(arg.substring(2));
			} else if (arg.matches("-.")) {
				options.add(arg.substring(1));
			} else {
				parameters.add(arg);
			}
		}
	}

	public boolean isOptionSet(String option) {
		return options.contains(option);
	}

	public List<String> getParameters() {
		return parameters;
	}
}
