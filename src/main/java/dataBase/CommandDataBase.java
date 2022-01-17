package dataBase;

import bot.Bot;
import command.Command;
import command.admin.DeleteRulesCommand;
import command.admin.RulesCommand;
import command.admin.SettingsCommand;
import command.game.FlipCoinCommand;
import command.game.Want2PlayCommand;
import command.global.*;
import command.league.*;
import command.music.*;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CommandDataBase {
    public static @Nullable Command getCommandByName(@NotNull final String name) {
        for (Command command : getCommands()) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    public static @NotNull ArrayList<Command> getCommands() {
        final ArrayList<Command> commandArrayList = new ArrayList<>();

        commandArrayList.add(new DeleteRulesCommand());
        commandArrayList.add(new RulesCommand());
        commandArrayList.add(new SettingsCommand());

        commandArrayList.add(new FlipCoinCommand());
        commandArrayList.add(new Want2PlayCommand());

        commandArrayList.add(new HelpCommand());
        commandArrayList.add(new InfoCommand());
        commandArrayList.add(new TopicCommand());
        commandArrayList.add(new UpTimeCommand());

        if (Bot.isApiLeagueEnable()) {
            commandArrayList.add(new BuildCommand());
            commandArrayList.add(new ChampionCommand());
            commandArrayList.add(new PatchCommand());
            commandArrayList.add(new SummonerCommand());
        }

        commandArrayList.add(new ClearCommand());
        commandArrayList.add(new PauseCommand());
        commandArrayList.add(new PlayCommand());
        commandArrayList.add(new QueueCommand());
        commandArrayList.add(new ShuffleCommand());
        commandArrayList.add(new SkipCommand());
        commandArrayList.add(new StopCommand());
        commandArrayList.add(new VolumeCommand());

        return commandArrayList;
    }

    public static @NotNull ArrayList<CommandData> getCommandsFormatted() {
        final ArrayList<CommandData> commandDataArrayList = new ArrayList<>();
        for (Command command : getCommands()) {
            final CommandData commandData = new CommandData(command.getName(), command.getDescription());
            if (command.getOptions() != null) {
                commandData.addOptions(command.getOptions());
            }
            commandDataArrayList.add(commandData);
        }
        return commandDataArrayList;
    }
}
