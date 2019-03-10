package com.redstoner.discordbot;

import com.redstoner.discordbot.commands.LinkAccount;
import com.redstoner.discordbot.commands.LinkStatus;
import com.redstoner.discordbot.commands.Nickname;
import com.redstoner.discordbot.tasks.ForumNotificationTask;
import com.redstoner.discordbot.tasks.GuildRankSyncTask;
import com.redstoner.discordbot.utils.RankUtil;
import dev.logal.logalbot.LogalBot;
import dev.logal.logalbot.commands.CommandManager;
import dev.logal.logalbot.utils.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] arguments) {
		logger.info("Beginning startup of Redstoner-Bot...");
		RankUtil.registerRank("visitor", "470250026925293568");
		RankUtil.registerRank("member", "470233551284076555");
		RankUtil.registerRank("builder", "470233521919885317");
		RankUtil.registerRank("trusted", "470233420501352469");
		RankUtil.registerRank("helper", "518547428690100227");
		RankUtil.registerRank("trainingmod", "470233311541985320");
		RankUtil.registerRank("mod", "470233311541985320");
		RankUtil.registerRank("admin", "470233150514003969");

		RankUtil.registerRank("+lead", "516775853976453140");
		RankUtil.registerRank("+donor", "470237589052325888");
		RankUtil.registerRank("+donorplus", "470258077648683008");
		RankUtil.registerRank("+retired", "537816902584631301");
		RankUtil.registerRank("+dev", "508329092110614529");

		logger.info("Starting LogalBot...");
		LogalBot.start(false);

		logger.info("Unregistering unnecessary commands...");
		CommandManager.unregisterCommand("help");
		CommandManager.unregisterCommand("about");

		logger.info("Registering Redstoner commands...");
		CommandManager.registerCommand("linkaccount", new LinkAccount(), false);
		CommandManager.registerCommandAlias("link", "linkaccount");
		CommandManager.registerCommandAlias("lnk", "linkaccount");
		CommandManager.registerCommand("nickname", new Nickname(), true);
		CommandManager.registerCommandAlias("nick", "nickname");
		CommandManager.registerCommand("linkstatus", new LinkStatus(), false);
		CommandManager.registerCommandAlias("ls", "linkstatus");

		logger.info("Starting tasks...");
		Scheduler.scheduleAtFixedRate(new ForumNotificationTask(), 1, 1, TimeUnit.MINUTES);
		Scheduler.scheduleAtFixedRate(new GuildRankSyncTask(), 1, 5, TimeUnit.MINUTES);

		logger.info("Redstoner-Bot setup complete.");
	}
}