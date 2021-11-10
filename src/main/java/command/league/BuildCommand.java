package command.league;

import bot.Bot;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.searchable.SearchableList;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Item;
import com.merakianalytics.orianna.types.core.staticdata.RecommendedItems;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.List;

public class BuildCommand extends AbstractLeagueCommand {
    public BuildCommand() {
        super(CommandEnum.BUILD,
                new OptionData(OptionType.STRING, "champion", "Champion's name", true)
        );
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final @NotNull List<OptionMapping> options) {
        assert !options.isEmpty();

        if (!bot.getGuildSetting(interaction.getGuild()).isLeagueEnable()) {
            interaction.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        try {
            final Champion champion = Orianna.championNamed(options.get(0).getAsString()).get();

            if (!champion.exists()) {
                interaction.reply("Sorry, this champion doesn't exist, or you misspelled it").setEphemeral(true).queue();
                return;
            }

            final SearchableList<RecommendedItems> recommendedItemsList = champion.getRecommendedItems();

            if (recommendedItemsList.isEmpty()) {
                interaction.reply("Sorry, command currently not working, will be fixed soon").setEphemeral(true).queue();
                logger.log(Logger.Level.ERROR, "No recommended items");
            }

            embedBuilder.setTitle(champion.getName() + ", " + champion.getTitle());
            embedBuilder.setThumbnail(champion.getImage().getURL());

            for (final Item item : recommendedItemsList.get(0).get(0).keySet()) {
                embedBuilder.addField(item.getName(), item.getDescription(), false);
            }

            interaction.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            interaction.reply("Champion unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
