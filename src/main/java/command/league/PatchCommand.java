package command.league;

import bot.GuildSettings;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.staticdata.Patch;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class PatchCommand extends AbstractLeagueCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "patch";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get the last patch note";
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        if (!GuildSettings.getInstance(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        super.execute(event);

        try {
            final Patch patch = Orianna.getPatch();
            if (patch.exists()) {
                final String patchVersion = Orianna.getPatch().getName();
                final SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");

                embedBuilder.setTitle("Patch **" + patchVersion + "** notes", "https://euw.leagueoflegends.com/en_us/news/game-updates/patch-" +
                        patchVersion.substring(0, 2) + "-" + patchVersion.substring(3) + "-notes/");
                embedBuilder.setDescription("The **" + formatter.format(patch.getStartTime().toDate()) + "**");
                embedBuilder.setImage("https://images.contentstack.io/v3/assets/blt731acb42bb3d1659/blt4d280f349eb96be1/5e44ba3132797c0d2ce79520/LOL_PROMOART_6.jpg");

                event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
                embedBuilder.clear();
            } else {
                throw new ForbiddenException("Patch doesn't exist");
            }
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Patch notes unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
