package bot.setting;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Commands {
    public static @NotNull ArrayList<CommandData> getCommands(final boolean APILeagueEnable) {
        final ArrayList<CommandData> commandList =  new ArrayList<>();

        if(APILeagueEnable) {
            CommandData commandSummoner = new CommandData("summoner", "Get some info about a summoner")
                    .addOption(OptionType.STRING, "summoner", "Summoner's name", true);
            commandList.add(commandSummoner);

            CommandData commandPatch = new CommandData("patch", "Get the last patch note");
            commandList.add(commandPatch);

            CommandData commandChamp = new CommandData("champ", "Get tips on a league champion")
                    .addOption(OptionType.STRING, "champion", "Champion's name", true);
            commandList.add(commandChamp);
        }

//****************************************************
//****************************************************
//****************************************************
        CommandData commandTo       = new CommandData("to", "Time out someone")
                .addOption(OptionType.USER, "member", "Member you want to kick temporarily", true)
                .addOption(OptionType.INTEGER, "time", "Time in minutes", false);
        commandList.add(commandTo);

        CommandData commandW2P      = new CommandData("w2p", "I ask to everybody connected if they want to play with you");
        commandList.add(commandW2P);

        CommandData commandPing     = new CommandData("ping", "Check my response time");
        commandList.add(commandPing);

        CommandData commandFlipCoin = new CommandData("flipacoin", "Flip a coin. Basic. Simple");
        commandList.add(commandFlipCoin);

        CommandData commandSettings = new CommandData("settings", "Enable/Disable settings");
        commandList.add(commandSettings);

        CommandData commandHelp     = new CommandData("help", "Check all the commands available");
        commandList.add(commandHelp);

        CommandData commandTopic    = new CommandData("topic", "Get the topic for the current channel");
        commandList.add(commandTopic);

        CommandData commandUptime   = new CommandData("uptime", "Get the time since I'm up");
        commandList.add(commandUptime);

        CommandData commandInfo     = new CommandData("info", "Get some info about Yuko's life");
        commandList.add(commandInfo);

//****************************************************
//****************************************************
//****************************************************
        CommandData commandPlay     = new CommandData("play", "Play a Youtube audio")
                .addOption(OptionType.STRING, "title", "Youtube link or title of the Youtube audio", true);
        commandList.add(commandPlay);

        CommandData commandVolume   = new CommandData("volume", "Set up the volume")
                .addOption(OptionType.INTEGER, "volume", "Volume between 0 and 100", true);
        commandList.add(commandVolume);

        CommandData commandStop     = new CommandData("stop", "Stop the track and clear the queue");
        commandList.add(commandStop);

        CommandData commandSkip     = new CommandData("skip", "Skip the track currently playing");
        commandList.add(commandSkip);

        CommandData commandPause    = new CommandData("pause", "Pause the track");
        commandList.add(commandPause);

        CommandData commandResume   = new CommandData("resume", "Resume the track");
        commandList.add(commandResume);

        CommandData commandQueue    = new CommandData("queue", "Display the current queue");
        commandList.add(commandQueue);

        CommandData commandClear    = new CommandData("clear", "Clear the current queue");
        commandList.add(commandClear);

        CommandData commandShuffle  = new CommandData("shuffle", "Shuffle the current queue");
        commandList.add(commandShuffle);

        return commandList;
    }
}
