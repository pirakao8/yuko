package command.global;

import bot.Bot;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class UpTimeCommand extends AbstractSlashCommand {
    public UpTimeCommand() {
        super(CommandEnum.UP_TIME);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        final Date startDate = bot.getReleaseDate();
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
        interaction.reply(responseUptime.toString()).setEphemeral(true).queue();
    }
}
