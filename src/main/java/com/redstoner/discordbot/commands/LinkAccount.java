package com.redstoner.discordbot.commands;

import com.redstoner.discordbot.utils.RankUtil;
import dev.logal.logalbot.commands.Command;
import dev.logal.logalbot.commands.CommandResponse;
import dev.logal.logalbot.utils.DataManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class LinkAccount implements Command {
	private static final Logger logger = LoggerFactory.getLogger(LinkAccount.class);

	private final String databaseUsername  = System.getenv("MYSQL_USERNAME");
	private final String databasePassword  = System.getenv("MYSQL_PASSWORD");
	private final String tokenDatabaseName = System.getenv("MYSQL_TOKEN_DATABASE_NAME");

	@Override
	public CommandResponse execute(String[] arguments, Member executor, TextChannel channel) {
		if (DataManager.getUserValue(executor, "minecraftUUID") != null) {
			return new CommandResponse("no_entry_sign", "Sorry " + executor.getAsMention() + ", but your account is already linked.").setDeletionDelay(
					10, TimeUnit.SECONDS);
		}

		String token = arguments[0];

		if (token.length() != 8 || !token.matches("[A-Za-z0-9]+")) {
			return new CommandResponse("no_entry_sign", "Sorry " + executor.getAsMention() + ", but that doesn't appear to be a token.").setDeletionDelay(
					10, TimeUnit.SECONDS);
		}

		UUID uuid;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + tokenDatabaseName, databaseUsername, databasePassword);

			Statement statement = connection.createStatement();
			ResultSet results   = statement.executeQuery("SELECT uuid FROM discord WHERE token='" + token + "' AND used=0");

			if (!results.next()) {
				return new CommandResponse(
						"no_entry_sign", "Sorry " + executor.getAsMention() + ", but that token doesn't appear to be valid.").setDeletionDelay(
						10, TimeUnit.SECONDS);
			}

			uuid = UUID.fromString(UUID
					                       .fromString(results
							                                   .getString(1)
							                                   .replaceFirst(
									                                   "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
									                                   "$1-$2-$3-$4-$5"
							                                   ))
					                       .toString());

			statement = connection.createStatement();
			statement.execute("UPDATE discord SET used=1 WHERE token='" + token + "'");

			connection.close();
		} catch (Throwable exception) {
			logger.error(
					"An error occurred while trying to look up the token for " + executor.getEffectiveName() + " (" + executor.getUser().getId() + ")!",
					exception
			);
			return new CommandResponse(
					"sos", "Sorry " + executor.getAsMention() + ", but an error occurred while trying to look up your token.").setDeletionDelay(
					10, TimeUnit.SECONDS);
		}

		DataManager.setUserValue(executor, "minecraftUUID", uuid.toString());
		try {
			RankUtil.syncRanks(executor);
		} catch (Throwable exception) {
			logger.error(
					"An error occurred while trying to sync the ranks for " + executor.getEffectiveName() + " (" + executor.getUser().getId() + ")!",
					exception
			);
			return new CommandResponse(
					"exclamation",
					executor.getAsMention() + ", your account has been successfully linked, but your ranks could not be synchronized at this time."
			).setDeletionDelay(10, TimeUnit.SECONDS);
		}

		return new CommandResponse("link", executor.getAsMention() + ", your account has been successfully linked and your ranks have been synchronized.");
	}
}