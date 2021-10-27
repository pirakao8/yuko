package util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public enum Level {
        DISCORD,
        SYSTEM,
        WARNING,
        ERROR
    }

    private File logFile;
    private static Logger logger;

    private Logger() {
        logFile = null;
    }

    public static Logger getLogger() {
        if(logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public final Logger setLogFile(String logFileUrl) {
        if(logFileUrl == null || logFileUrl.isEmpty()) {
            logFile = null;
            return logger;
        }

        try {
            logFile = new File(logFileUrl);
            if (logFileUrl.endsWith("/")) {
                logFileUrl = logFileUrl + "/yuko.log";
            }

            if(!logFile.exists()) {
                Files.createDirectories(Paths.get(logFile.getParent()));
                Files.createFile(Paths.get(logFileUrl));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }

    public final void log(@NotNull final Level level, final String content) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        final Date date = new Date();

        final String logString = formatter.format(date) + "-" + level.name() + "|" + content;

        System.out.println(logString);

        if(logFile != null) {
            try {
                if(logFile.exists() && logFile.canWrite()) {
                    Files.write(logFile.toPath(), (logString + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final void log(final String content) {
        log(Level.SYSTEM, content);
    }

    public final void logDiscord(@NotNull final Guild guild, @NotNull final Member member, @NotNull final TextChannel textChannel, final String content) {
        final String logDiscordString = "Guild:'" + guild.getName() + "'(id:" + guild.getId() + ")|" +
                "Member:'" + member.getEffectiveName() + "'(id:" + member.getId() + ")|" +
                "Channel:'" + textChannel.getName() + "'(id:" + textChannel.getId() + ")|" +
                content;
        log(Level.DISCORD, logDiscordString);
    }

    public final void logDiscord(@NotNull final Guild guild, final String content) {
        final String logDiscordString = "Guild:'" + guild.getName() + "'(id:" + guild.getId() + ")|" + content;
        log(Level.DISCORD, logDiscordString);
    }

    public final void logDiscordPermission(@NotNull final Guild guild, @NotNull final Member member, @NotNull final TextChannel textChannel, @NotNull final Permission @NotNull ... permissions) {
        final StringBuilder logDiscordBuilder = new StringBuilder();
        logDiscordBuilder.append("PermissionRefused:");
        for(Permission permission : permissions) {
            logDiscordBuilder.append("'" + permission.getName() +  "'(value:" + permission.getRawValue() + "):");
        }
        logDiscord(guild, member, textChannel, logDiscordBuilder.toString());
    }
}
