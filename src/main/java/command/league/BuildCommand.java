package command.league;

import bot.GuildSettings;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import com.merakianalytics.orianna.types.core.staticdata.RecommendedItems;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.concurrent.TimeUnit;

public class BuildCommand extends AbstractLeagueCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "build";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get the recommended build for a league champion";
    }

    @Contract(" -> new")
    @Override
    public final OptionData @NotNull [] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "champion", "Champion's name", true)
        };
    }

    @Override
    public final boolean isEnable() {
        return false;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        if (!GuildSettings.getInstance(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        super.execute(event);

        try {
            final String championName = event.getOptions().get(0).getAsString();
            final Champion champion   = Orianna.championNamed(championName).get();

            if (!champion.exists()) {
                event.reply("This champion doesn't exist, or you misspelled it").setEphemeral(true).queue();
                return;
            }

            final SearchableList<RecommendedItems> recommendedItemsList = champion.getRecommendedItems();

            if (recommendedItemsList.isEmpty()) {
                event.reply("Sorry, command currently not working, will be fixed soon").setEphemeral(true).queue();
                logger.log(Logger.Level.ERROR, "No recommended items");
            }

            embedBuilder.setTitle(champion.getName() + ", " + champion.getTitle());
            embedBuilder.setThumbnail(champion.getImage().getURL());

            for (final Item item : recommendedItemsList.get(0).get(0).keySet()) {
                embedBuilder.addField(item.getName(), item.getDescription(), false);
            }

            event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
            embedBuilder.clear();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Champion's builds unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
