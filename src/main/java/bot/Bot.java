package bot;

import bot.listener.*;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import command.CommandEnum;
import command.AbstractSlashCommand;
import command.game.FlipCoinCommand;
import command.game.Want2PlayCommand;
import command.global.*;
import command.league.BuildCommand;
import command.league.ChampionCommand;
import command.league.PatchCommand;
import command.league.SummonerCommand;
import command.music.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import util.Logger;
import util.music.GuildMusicPlayer;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Bot implements Runnable {
    private static final Logger logger  = Logger.getLogger();
    private static final String version = "v2.2";
    private static final String[] tabActivities = {
            "Boston Dynamics flipping",
            "Netflix",
            "Faker on the rift"
    };

    private JDA jda;
    private boolean apiLeagueEnable;

    private final Date releaseDate;
    private final ArrayList<GuildSettings> guildSettingsList;
    private final GuildMusicPlayer guildMusicPlayer;
    private final HashMap<String, AbstractSlashCommand> slashCommandMap;

    public Bot() {
        logger.log(Logger.Level.SYSTEM, "Initializing Yuko");

        apiLeagueEnable   = false;
        releaseDate       = new Date();
        guildSettingsList = new ArrayList<>();
        slashCommandMap   = new HashMap<>();
        guildMusicPlayer  = GuildMusicPlayer.getInstance();
    }

    public final void initRiot(final String riotToken) throws ForbiddenException {
        logger.log(Logger.Level.SYSTEM, "Initializing Orianna Riot API");

        Orianna.setRiotAPIKey(riotToken);
        Orianna.setDefaultLocale("en_US");
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        Orianna.setDefaultRegion(Region.EUROPE_WEST);

        if(!Orianna.challengerLeagueInQueue(Queue.RANKED_SOLO).get().exists()) {
            throw new ForbiddenException("API key invalid or League got big trouble");
        }

        apiLeagueEnable = true;

        logger.log(Logger.Level.SYSTEM, "Orianna Riot API ready");
    }

    public final void initDiscord(final String discordToken) throws InterruptedException, LoginException {
        logger.log(Logger.Level.SYSTEM, "Initializing JDA Discord API");

        jda = JDABuilder.createDefault(discordToken)
                .addEventListeners(new GuildJoinEventListener(this), new SlashCommandEventListener(this),
                        new ButtonClickEventListener(this), new VoiceLeaveEventListener(this), new ChannelCreateEventListener(this))
                .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS,
                        CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.watching(tabActivities[(int) (Math.random() * tabActivities.length)]))
                .build();

        CommandListUpdateAction commandListUpdateAction = jda.updateCommands();
        commandListUpdateAction.addCommands(slashCommandMap.values()).queue();

        jda.awaitReady();

        if (!jda.getGuilds().isEmpty()) {
            for (Guild guild : jda.getGuilds()) {
                guildSettingsList.add(new GuildSettings(guild));
            }
        }

        logger.log(Logger.Level.SYSTEM, "Discord API ready");
    }

    public final void initCommands() {
        slashCommandMap.putIfAbsent(CommandEnum.FLIP_A_COIN.getName(), new FlipCoinCommand());
        slashCommandMap.putIfAbsent(CommandEnum.W2P.getName(), new Want2PlayCommand());

        slashCommandMap.putIfAbsent(CommandEnum.DELETE_RULES.getName(), new DeleteRulesCommand());
        slashCommandMap.putIfAbsent(CommandEnum.HELP.getName(),     new HelpCommand());
        slashCommandMap.putIfAbsent(CommandEnum.INFO.getName(),     new InfoCommand());
        slashCommandMap.putIfAbsent(CommandEnum.PING.getName(),     new PingCommand());
        slashCommandMap.putIfAbsent(CommandEnum.SETTINGS.getName(), new SettingsCommand());
        slashCommandMap.putIfAbsent(CommandEnum.TIME_OUT.getName(), new TimeOutCommand());
        slashCommandMap.putIfAbsent(CommandEnum.TOPIC.getName(),    new TopicCommand());
        slashCommandMap.putIfAbsent(CommandEnum.UP_TIME.getName(),  new UpTimeCommand());
        slashCommandMap.putIfAbsent(CommandEnum.RULES.getName(),    new RulesCommand());

        slashCommandMap.putIfAbsent(CommandEnum.BUILD.getName(),    new BuildCommand());
        slashCommandMap.putIfAbsent(CommandEnum.CHAMP.getName(),    new ChampionCommand());
        slashCommandMap.putIfAbsent(CommandEnum.PATCH.getName(),    new PatchCommand());
        slashCommandMap.putIfAbsent(CommandEnum.SUMMONER.getName(), new SummonerCommand());

        slashCommandMap.putIfAbsent(CommandEnum.CLEAR.getName(),   new ClearCommand());
        slashCommandMap.putIfAbsent(CommandEnum.PAUSE.getName(),   new PauseCommand());
        slashCommandMap.putIfAbsent(CommandEnum.PLAY.getName(),    new PlayCommand());
        slashCommandMap.putIfAbsent(CommandEnum.PLAYER.getName(),  new PlayerCommand());
        slashCommandMap.putIfAbsent(CommandEnum.QUEUE.getName(),   new QueueCommand());
        slashCommandMap.putIfAbsent(CommandEnum.SHUFFLE.getName(), new ShuffleCommand());
        slashCommandMap.putIfAbsent(CommandEnum.SKIP.getName(),    new SkipCommand());
        slashCommandMap.putIfAbsent(CommandEnum.STOP.getName(),    new StopCommand());
        slashCommandMap.putIfAbsent(CommandEnum.VOLUME.getName(),  new VolumeCommand());
    }

    @Override
    public final void run() {
        boolean running = true;

        logger.log(Logger.Level.SYSTEM, "Yuko ready and running");

        while(running);

        logger.log(Logger.Level.SYSTEM, "Bot stopped");
        jda.shutdown();
        System.exit(0);
    }

    public final void addSetting(final GuildSettings guildSettings) {
        guildSettingsList.add(guildSettings);
    }

    public final @NotNull GuildSettings getGuildSetting(final Guild guild) {
        for (GuildSettings guildSettings : guildSettingsList) {
            if (guildSettings.getGuild().equals(guild)) {
                return guildSettings;
            }
        }
        GuildSettings guildSettings = new GuildSettings(guild);
        addSetting(guildSettings);
        return guildSettings;
    }

    public final GuildMusicPlayer getGuildMusicPlayer() {
        return guildMusicPlayer;
    }

    public final AbstractSlashCommand getCommandByName(String name) {
        return slashCommandMap.get(name);
    }

    public final HashMap<String, AbstractSlashCommand> getSlashCommandMap() {
        return slashCommandMap;
    }

    public final Date getReleaseDate() {
        return releaseDate;
    }

    public final String getVersion() {
        return version;
    }

    public final boolean isApiLeagueEnable() {
        return apiLeagueEnable;
    }
}
