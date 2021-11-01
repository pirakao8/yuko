package command.league;

import bot.Bot;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class ChampionCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getOptions().get(0) != null;

        if(!bot.getGuildSetting(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        char[] arr = event.getOptions().get(0).getAsString().toLowerCase().toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);

        final Champion champion = Orianna.championNamed(new String(arr)).get();

        try {
            if(champion.exists()) {
                final String championInfo = champion.getName() + ", " + champion.getTitle() + " :\n\n" +
                        champion.getLore() + "\n\nAlly tips :\n" + champion.getAllyTips().toString().replace("[", "").replace("]", "") +
                        "\nEnemy tips :\n" + champion.getEnemyTips().toString().replace("[", "").replace("]", "");

                event.reply(championInfo).queue();
            } else {
                event.reply("Sorry, this champion doesn't exist (not yet!)").setEphemeral(true).queue();
            }
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Champion info unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
