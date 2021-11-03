package command.league;

import bot.Bot;
import bot.setting.EmojiList;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.common.GameMode;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMasteries;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.ParticipantStats;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.text.NumberFormat;
import java.util.Date;

public class SummonerCommand extends AbstractLeagueCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getOptions().get(0) != null;

        if(!bot.getGuildSetting(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        try {
            final Summoner summoner = Summoner.named(event.getOptions().get(0).getAsString()).get();
            if (!summoner.exists()) {
                event.reply("Summoner not found").setEphemeral(true).queue();
                return;
            }
            embedBuilder.setTitle("" + summoner.getName());
            embedBuilder.appendDescription("Level: " + summoner.getLevel());
            embedBuilder.setThumbnail(summoner.getProfileIcon().getImage().getURL());

            embedBuilder.addField("SoloQ", getInfoLeague(summoner.getLeaguePosition(Queue.RANKED_SOLO)), true);
            embedBuilder.addField("Flex", getInfoLeague(summoner.getLeaguePosition(Queue.RANKED_FLEX)), true);
            embedBuilder.addBlankField(true);

            final ChampionMasteries championMasteries = summoner.getChampionMasteries();
            embedBuilder.addField(EmojiList.FIRST.getTag(), getInfoMasteries(championMasteries, 0), true);
            embedBuilder.addField(EmojiList.SECOND.getTag(), getInfoMasteries(championMasteries, 1), true);
            embedBuilder.addField(EmojiList.THIRD.getTag(), getInfoMasteries(championMasteries, 2), true);
            try {
                embedBuilder.setImage(championMasteries.get(0).getChampion().getSkins().get(0).getSplashImageURL());
            } catch (ForbiddenException e) {
                logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about CHAMPION MASTERIES, check key permission or api status page");
            }

            final StringBuilder infoLastMatchBuilder = new StringBuilder();
            try {
                final Match lastMatch = summoner.matchHistory().get().get(0);
                if (lastMatch.exists()) {
                    for (Participant participant : lastMatch.getParticipants()) {
                        if (participant.getSummoner().equals(summoner)) {
                            final ParticipantStats stats = participant.getStats();
                            infoLastMatchBuilder.append("**" + participant.getChampion().getName() + "** in "
                                    + stats.getKills() + "/" + stats.getDeaths() + "/" + stats.getAssists());

                            if (lastMatch.getMode().equals(GameMode.CLASSIC)) {
                                infoLastMatchBuilder.append(" at **" + participant.getLane().name().toLowerCase() + "**");
                            } else {
                                infoLastMatchBuilder.append(" in " + lastMatch.getMode().name());
                            }
                        }
                    }
                }
            } catch (ForbiddenException e) {
                logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about GAME HISTORY, check key permission or api status page");
                infoLastMatchBuilder.append("Unavailable (Riot API Error)");
            }
            embedBuilder.addField("Last game", infoLastMatchBuilder.toString(), true);

            final StringBuilder infoCurrentMatchBuilder = new StringBuilder();
            try {
                if (summoner.isInGame()) {
                    final CurrentMatch currentMatch = summoner.getCurrentMatch();
                    final long gameDuration = (new Date().getTime() - currentMatch.getCreationTime().getMillis()) / (1000 * 60);
                    infoCurrentMatchBuilder.append(summoner.getName() + " currently in game since **" + gameDuration + "** minutes ");
                    if (currentMatch.getMode().equals(GameMode.CLASSIC)) {
                        infoCurrentMatchBuilder.append("on the rift");
                    }
                    else {
                        infoCurrentMatchBuilder.append("in " + currentMatch.getMode().name().toLowerCase());
                    }
                } else {
                    infoCurrentMatchBuilder.append("N/A");
                }
            } catch (ForbiddenException e) {
                logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about CURRENT GAME, check key permission or api status page");
                infoCurrentMatchBuilder.append("Unavailable (Riot API Error)");
            }
            embedBuilder.addField("Live game", infoCurrentMatchBuilder.toString(), true);

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();

        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about SUMMONER, check key permission or api status page");
            event.reply("Unavailable (Riot API Error)").setEphemeral(true).queue();
        }
    }

    private @NotNull String getInfoLeague(final LeagueEntry leagueEntry) {
        if (leagueEntry == null) {
            return "**UNRANKED**";
        }

        final StringBuilder leagueBuilder = new StringBuilder();
        try {
            final int wins = leagueEntry.getWins();
            final int losses = leagueEntry.getLosses();
            final double winrate = Math.round(((wins * 100.0) / (wins + losses)) * 100.0) / 100.0;

            leagueBuilder.append("**" + leagueEntry.getTier().name() + " " + leagueEntry.getDivision() + "**\n");
            leagueBuilder.append(leagueEntry.getLeaguePoints() + "LP\n");
            leagueBuilder.append("Wins: " + wins + "\n");
            leagueBuilder.append("Losses: " + losses + "\n");
            leagueBuilder.append("Total: " + (wins + losses) + "\n");
            leagueBuilder.append("Winrate: **" + winrate + "%**");
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about SUMMONER, check key permission or api status page");
            leagueBuilder.append("Unavailable (Riot API Error)");
        }
        return leagueBuilder.toString();
    }

    private @NotNull String getInfoMasteries(@NotNull final ChampionMasteries masteries, int position) {
        final StringBuilder infoChampionMasteries = new StringBuilder();
        try {
            final ChampionMastery championMastery = masteries.get(position);
            infoChampionMasteries.append("**" + championMastery.getChampion().getName() + "**\n");
            infoChampionMasteries.append("M" + championMastery.getLevel() + "\n");
            infoChampionMasteries.append("**" + NumberFormat.getInstance().format(championMastery.getPoints()) + "** points");
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about CHAMPION MASTERIES, check key permission or api status page");
            infoChampionMasteries.append("Unavailable (Riot API Error)");
        }
        return infoChampionMasteries.toString();
    }
}
