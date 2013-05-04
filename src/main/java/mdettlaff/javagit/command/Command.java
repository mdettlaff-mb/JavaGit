package mdettlaff.javagit.command;

public interface Command {

	public void execute(Arguments args) throws Exception;
}
