package bot;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Region;
import dataBase.CommandDataBase;
import listener.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import util.Logger;

import javax.security.auth.login.LoginException;
import java.util.Date;

public class Bot implements Runnable {
    private static boolean apiLeagueEnable;
    private static Date releaseDate;
    private static final Logger logger  = Logger.getLogger();
    private static final String version = "v3.1";
    private static final String[] tabActivities = {
            "Boston Dynamics flipping",
            "Netflix",
            "Faker on the rift",
    };

    private JDA jda;

    public Bot() {
        logger.log(Logger.Level.SYSTEM, "Initializing Yuko");

        apiLeagueEnable = false;
        releaseDate     = new Date();
    }

    public final void initRiot(final String riotToken) throws ForbiddenException {
        logger.log(Logger.Level.SYSTEM, "Initializing Orianna Riot API");

        Orianna.setRiotAPIKey(riotToken);
        Orianna.setDefaultLocale("fr_FR");
        Orianna.setDefaultPlatform(Platform.EUROPE_WEST);
        Orianna.setDefaultRegion(Region.EUROPE_WEST);

        if (Orianna.championRotationsWithRegions(Region.EUROPE_WEST).get().isEmpty()) {
            throw new ForbiddenException("API key invalid or League is in big trouble");
        }

        apiLeagueEnable = true;

        logger.log(Logger.Level.SYSTEM, "Orianna Riot API ready");
    }

    public final void initDiscord(final String discordToken) throws InterruptedException, LoginException {
        logger.log(Logger.Level.SYSTEM, "Initializing JDA Discord API");

        jda = JDABuilder.createDefault(discordToken)
                .addEventListeners(new GuildJoinEventListener(), new CommandEventListener(), new ButtonEventListener(),
                        new VoiceLeaveEventListener(), new ChannelCreateEventListener())
                .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS,
                        CacheFlag.ONLINE_STATUS, CacheFlag.VOICE_STATE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.watching(tabActivities[(int) (Math.random() * tabActivities.length)]))
                .build();

        CommandListUpdateAction commandListUpdateAction = jda.updateCommands();
        commandListUpdateAction.addCommands(CommandDataBase.getCommandsFormatted()).queue();

        jda.awaitReady();
        logger.log(Logger.Level.SYSTEM, "Discord API ready");
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

    public static Date getReleaseDate() {
        return releaseDate;
    }

    public static String getVersion() {
        return version;
    }

    public static boolean isApiLeagueEnable() {
        return apiLeagueEnable;
    }
}
