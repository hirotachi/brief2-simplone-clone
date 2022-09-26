import java.util.ArrayList;

public interface Commander {
    ArrayList<Command> commands = new ArrayList<Command>();

    public void assignCommands(Command... commands);

    ArrayList<Command> getCommands();
}
