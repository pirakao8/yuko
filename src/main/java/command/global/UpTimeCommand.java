package command.global;

import bot.Bot;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UpTimeCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "uptime";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get the time since I'm up";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.GLOBAL;
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        final Date startDate = Bot.getReleaseDate();
        final Date endDate = new Date();

        long different = endDate.getTime() - startDate.getTime();

        final long secondsInMilli = 1000;
        final long minutesInMilli = secondsInMilli * 60;
        final long hoursInMilli = minutesInMilli * 60;
        final long daysInMilli = hoursInMilli * 24;

        final long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        final long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        final long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        final long elapsedSeconds = different / secondsInMilli;

        final StringBuilder responseUptime = new StringBuilder();

        if (elapsedDays > 0) {
            if (elapsedDays > 1) {
                responseUptime.append(String.format("%d Days", elapsedDays));
            } else {
                responseUptime.append(String.format("%d Day", elapsedDays));
            }
        }
        if (elapsedHours > 0) {
            if (!responseUptime.toString().isEmpty()) {
                responseUptime.append(", ");
            }
            if (elapsedHours > 1) {
                responseUptime.append(String.format("%d Hours", elapsedHours));
            } else {
                responseUptime.append(String.format("%d Hour", elapsedHours));
            }
        }
        if (elapsedMinutes > 0) {
            if (!responseUptime.toString().isEmpty()) {
                responseUptime.append(", ");
            }

            if (elapsedMinutes > 1) {
                responseUptime.append(String.format("%d Minutes", elapsedMinutes));
            } else {
                responseUptime.append(String.format("%d Minute", elapsedMinutes));
            }
        }
        if (elapsedSeconds > 0) {
            if (!responseUptime.toString().isEmpty()) {
                responseUptime.append(", ");
            }
            if (elapsedSeconds > 1) {
                responseUptime.append(String.format("%d Seconds", elapsedSeconds));
            } else {
                responseUptime.append(String.format("%d Second", elapsedSeconds));
            }
        }
        event.reply(responseUptime.toString()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
    }
}
