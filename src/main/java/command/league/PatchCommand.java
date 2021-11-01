package command.league;

import bot.Bot;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class PatchCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        if(!bot.getGuildSetting(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        try {
            String lastPatchVersion = Orianna.getPatch().getName();
            String linkLastPatch = "https://euw.leagueoflegends.com/en-us/news/game-updates/patch-" +
                    lastPatchVersion.substring(0, 2) + "-" + lastPatchVersion.substring(3) + "-notes/";

            event.reply(linkLastPatch).queue();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Patch info unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
