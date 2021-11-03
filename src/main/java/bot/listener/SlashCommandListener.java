package bot.listener;

import bot.Bot;
import command.bot.*;
import command.game.FlipCoinCommand;
import command.game.Want2PlayCommand;
import command.league.ChampionCommand;
import command.league.PatchCommand;
import command.league.SummonerCommand;
import command.music.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class SlashCommandListener extends AbstractListener {
    private final GuildMusicPlayer guildMusicPlayer;
    public SlashCommandListener(final Bot bot) {
        super(bot);
        guildMusicPlayer = GuildMusicPlayer.getInstance();
    }

    public final void onSlashCommand(@NotNull final SlashCommandEvent event) {
        super.onSlashCommand(event);

        assert event.getMember() != null;
        assert event.getGuild() != null;

        logger.logDiscord(event.getGuild(), event.getMember(), event.getTextChannel(), "Slash command detected:" + event.getName());

        switch (event.getName()) {
            case "to":
                new TimeOutCommand().execute(event, bot);
                return;

            case "w2p":
                new Want2PlayCommand().execute(event, bot);
                return;

            case "ping":
                new PingCommand().execute(event);
                return;

            case "settings":
                new SettingsCommand().execute(event, bot);
                return;

            case "uptime":
                new UpTimeCommand().execute(event, bot);
                return;

            case "flipacoin":
                new FlipCoinCommand().execute(event);
                return;

            case "help":
                new HelpCommand().execute(event, bot);
                return;

            case "topic":
                new TopicCommand().execute(event);
                return;

            case "info":
                new InfoCommand().execute(event, bot);
                return;

            case "rules":
                new RulesCommand().execute(event, bot);
                return;

            case "deleterules":
                new DeleteRulesCommand().execute(event, bot);
                return;


//****************************************************
//****************************************************
//****************************************************
            case "patch":
                new PatchCommand().execute(event, bot);
                return;

            case "summoner":
                new SummonerCommand().execute(event, bot);
                return;

            case "champ":
                new ChampionCommand().execute(event, bot);
                return;

//****************************************************
//****************************************************
//****************************************************
            case "play":
                new PlayCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "stop":
                new StopCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "pause":
                new PauseCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "resume":
                new ResumeCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "skip":
                new SkipCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "queue":
                new QueueCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "volume":
                new VolumeCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "clear":
                new ClearCommand(guildMusicPlayer).execute(event, bot);
                return;

            case "shuffle":
                new ShuffleCommand(guildMusicPlayer).execute(event, bot);
                return;

            default:
                break;
        }
    }
}
