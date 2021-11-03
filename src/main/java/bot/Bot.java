package bot;

import bot.listener.*;
import bot.setting.Commands;
import bot.setting.GuildSettings;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Region;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Date;

public class Bot implements Runnable {
    private static final Logger logger  = Logger.getLogger();
    private static final String version = "v2.1";
    private static final String[] tabActivities = {
            "Boston Dynamics flipping",
            "Netflix",
            "Faker on the rift"
    };

    private JDA jda;
    private boolean apiLeagueEnable;

    private final Date releaseDate;
    private final ArrayList<GuildSettings> guildSettingsList;
    private final ArrayList<CommandData> commandList;

    public Bot() {
        logger.log(Logger.Level.SYSTEM, "Initializing Yuko");

        apiLeagueEnable   = false;
        releaseDate       = new Date();
        guildSettingsList = new ArrayList<>();
        commandList       = new ArrayList<>();
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

        commandList.addAll(new Commands().getAllCommands(apiLeagueEnable));

        jda = JDABuilder.createDefault(discordToken)
                .addEventListeners(new ReadyListener(this), new GuildJoinListener(this), new NewMessageListener(this),
                        new SlashCommandListener(this), new ButtonClickListener(this), new VoiceLeaveListener(this),
                        new ChannelCreateListener(this))
                .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS,
                        CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.watching(tabActivities[(int) (Math.random() * tabActivities.length)]))
                .build();

        CommandListUpdateAction commandListUpdateAction = jda.updateCommands();
        commandListUpdateAction.addCommands(commandList).queue();

        jda.awaitReady();

        if(!jda.getGuilds().isEmpty()) {
            for (Guild guild : jda.getGuilds()) {
                guildSettingsList.add(new GuildSettings(guild));
            }
        }
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
