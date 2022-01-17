package util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
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
        final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
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

    public final void logDiscord(@NotNull final Guild guild, @NotNull final User user, @NotNull final GuildChannel guildChannel, final String content) {
        final String logDiscordString = "Guild:'" + guild.getName() + "'(id:" + guild.getId() + ")|" +
                "Member:'" + user.getName() + "'(id:" + user.getId() + ")|" +
                "Channel:'" + guildChannel.getName() + "'(id:" + guildChannel.getId() + ")|" +
                content;
        log(Level.DISCORD, logDiscordString);
    }

    public final void logDiscord(@NotNull final Guild guild, @NotNull final User user, final String content) {
        final String logDiscordString = "Guild:'" + guild.getName() + "'(id:" + guild.getId() + ")|" +
                "Member:'" + user.getName() + "'(id:" + user.getId() + ")|" +
                content;
        log(Level.DISCORD, logDiscordString);
    }

    public final void logDiscord(@NotNull final User user, final String content) {
        final String logDiscordString = "User:'" + user.getName() + "'(id:" + user.getId() + ")|" + content;
        log(Level.DISCORD, logDiscordString);
    }

    public final void logDiscordMemberPermission(@NotNull final Interaction interaction, @NotNull final Permission permission) {
        assert interaction.getGuild()  != null;

        logDiscord(interaction.getGuild(), interaction.getUser(), interaction.getTextChannel(), "PermissionRefused:'"
                + permission.getName() +  "'(value:" + permission.getRawValue() + ")");
    }

    public final void logDiscordBotPermission(@NotNull final Guild guild, @NotNull final InsufficientPermissionException insufficientPermissionException) {
        final String insufficientPermissionString = "PermissionRefused:" + insufficientPermissionException.getPermission().getName()
                + "'(value:" + insufficientPermissionException.getPermission().getRawValue();
        logDiscord(guild, guild.getSelfMember().getUser(), insufficientPermissionString);
    }

    public final void logDiscordHierarchyPermission(@NotNull final Guild guild, @NotNull final HierarchyException hierarchyException) {
        logDiscord(guild, guild.getSelfMember().getUser(), "HierarchyPermission:" + hierarchyException.getMessage());
    }
}
