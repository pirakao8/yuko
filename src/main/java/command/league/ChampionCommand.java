package command.league;

import bot.Bot;
import bot.setting.EmojiList;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionSpell;
import com.merakianalytics.orianna.types.core.staticdata.ChampionStats;
import com.merakianalytics.orianna.types.core.staticdata.Skin;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.List;

public class ChampionCommand extends AbstractLeagueCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getOptions().get(0) != null;

        if(!bot.getGuildSetting(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        try {
            final Champion champion = Orianna.championNamed(event.getOptions().get(0).getAsString()).get();

            if (!champion.exists()) {
                event.reply("Sorry, this champion doesn't exist, or you misspelled it").setEphemeral(true).queue();
                return;
            }

            final List<Skin> skinList = champion.getSkins();

            embedBuilder.setTitle(champion.getName() + ", " + champion.getTitle(),
                    "https://universe.leagueoflegends.com/en_us/champion/" + champion.getName().toLowerCase() + "/");
            embedBuilder.setDescription(champion.getLore());
            embedBuilder.setImage(skinList.get(skinList.size() - 1).getSplashImageURL());
            embedBuilder.setThumbnail(champion.getImage().getURL());

            final ChampionStats championStats = champion.getStats();
            embedBuilder.addField(EmojiList.HP.getTag(), (int) championStats.getHealth() + " +" + (int) championStats.getHealthPerLevel(), true);
            embedBuilder.addField(EmojiList.ARMOR.getTag(), (int)championStats.getArmor() + " +" + (int) championStats.getArmorPerLevel(), true);
            embedBuilder.addField(EmojiList.RM.getTag(), (int) championStats.getMagicResist() + " +" + (int) championStats.getMagicResistPerLevel(), true);

            embedBuilder.addField(EmojiList.AD.getTag(), (int) championStats.getAttackDamage() + " +" + (int) championStats.getAttackDamagePerLevel(), true);
            embedBuilder.addField(EmojiList.MS.getTag(), String.valueOf((int) championStats.getMovespeed()), true);
            embedBuilder.addField(EmojiList.ATTACK_RANGE.getTag(), String.valueOf((int) championStats.getAttackRange()), true);

            if (champion.getResource().equalsIgnoreCase("mana")) {
                embedBuilder.addField(EmojiList.MANA.getTag(), (int) championStats.getMana() + " +" + (int) championStats.getManaPerLevel(), true);
            } else {
                embedBuilder.addField(EmojiList.MANA.getTag(), "N/A", true);
            }
            embedBuilder.addField(EmojiList.AS.getTag(), "+" + championStats.getAttackSpeedPerLevel()  + "%", true);
            embedBuilder.addBlankField(true);

            final List<ChampionSpell> championSpells = champion.getSpells();
            final char[] keySpells = "QWER".toCharArray();
            for (int i = 0; i < championSpells.size(); i++) {
                embedBuilder.addField(keySpells[i] + ": " + championSpells.get(i).getName(), championSpells.get(i).getDescription().replace("<br>", "\n"), false);
            }

            embedBuilder.addField("Ally tips", champion.getAllyTips().toString().replace("[", "").replace("]", ""), false);
            embedBuilder.addField("Enemy tips", champion.getEnemyTips().toString().replace("[", "").replace("]", ""), false);

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Champion unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
