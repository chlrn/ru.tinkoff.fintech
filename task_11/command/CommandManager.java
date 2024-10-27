package com.example.rutinkofffintech.task_11.command;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void executeAll() {
        commands.forEach(Command::execute);
    }
}
