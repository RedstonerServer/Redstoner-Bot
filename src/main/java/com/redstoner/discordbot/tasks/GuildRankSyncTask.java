package com.redstoner.discordbot.tasks;

import com.redstoner.discordbot.utils.RankUtil;
import dev.logal.logalbot.LogalBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GuildRankSyncTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(GuildRankSyncTask.class);

	private static final String redstonerGuild = "470229570310766593";

	@Override
	public void run() {
		Guild guild = LogalBot.getJDA().getGuildById(redstonerGuild);

		for (Member member : guild.getMembers()) {
			try {
				RankUtil.syncRanks(member);
			} catch (Throwable exception) {
				logger.error(
						"An error occurred while trying to sync the ranks for " + member.getUser().getName() + " (" + member.getUser().getId() + ")!",
						exception
				);
			}
		}
	}
}