package main;

import bot.Bot;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import javax.security.auth.login.LoginException;

public class Main {
    private static final Logger logger = Logger.getLogger().setLogFile("/yuko.log");

    public static void main(final String @NotNull [] argv) {
        String discordToken = "";
        String riotToken = "";

        logger.log("Main thread started");

        if (argv.length != 0) {
            for (String arg : argv) {
                if (arg.startsWith("--discord-token=") || arg.startsWith("--discord-token:")) {
                    discordToken = arg.substring("--discord-token=".length());
                } else if (arg.startsWith("--riot-token=") || arg.startsWith("--riot-token:")) {
                    riotToken = arg.substring("--riot-token=".length());
                }
            }

            if (!discordToken.equals("")) {
                Bot bot = new Bot();
                if (!riotToken.equals("")) {
                    try {
                        bot.initRiot(riotToken);
                    } catch (ForbiddenException e) {
                        logger.log(Logger.Level.ERROR, "Can't connect to Riot (or League got big trouble), check API key and Riot API status");
                    }
                } else {
                    logger.log(Logger.Level.WARNING, "No Riot token specified, League of Legends commands are disabled");
                }
                try {
                    bot.initCommands();
                    bot.initDiscord(discordToken);
                    new Thread(bot, "bot").start();
                } catch (LoginException | InterruptedException e) {
                    logger.log(Logger.Level.ERROR, "Can't connect to Discord, check API key and JDA status");
                }
            } else {
                logger.log(Logger.Level.ERROR, "No discord token specified");
            }
        } else {
            logger.log(Logger.Level.ERROR, "No argument specified");
            logger.log(Logger.Level.SYSTEM, "Aborting");
            System.exit(0);
        }
    }
}
