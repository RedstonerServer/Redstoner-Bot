package com.redstoner.discordbot.utils;

import dev.logal.logalbot.utils.DataManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class RankUtil {
	private static final Logger logger = LoggerFactory.getLogger(RankUtil.class);

	private static final String databaseUsername = System.getenv("MYSQL_USERNAME");
	private static final String databasePassword = System.getenv("MYSQL_PASSWORD");
	private static final String pexDatabaseName  = System.getenv("MYSQL_PEX_DATABASE_NAME");

	private static final HashMap<String, String> roleDictionary = new HashMap<>();

	public static void registerRank(String rankName, String roleID) {
		roleDictionary.put(rankName, roleID);
	}

	public static void syncRanks(Member member) throws SQLException, ClassNotFoundException {
		String uuid = DataManager.getUserValue(member, "minecraftUUID");
		if (uuid == null) {
			return;
		}

		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + pexDatabaseName, databaseUsername, databasePassword);

		Statement statement = connection.createStatement();
		ResultSet results   = statement.executeQuery("SELECT parent FROM permissions_inheritance WHERE child='" + uuid + "'");

		Guild           guild      = member.getGuild();
		ArrayList<Role> rolesToAdd = new ArrayList<>();
		while (results.next()) {
			String parent = results.getString(1);
			Role   role   = null;
			if (roleDictionary.containsKey(parent)) {
				role = guild.getRoleById(roleDictionary.get(parent));
			}

			if (role != null) {
				rolesToAdd.add(role);
			}
		}

		String nickname = DataManager.getUserValue(member, "nickname");
		if (nickname != null && !nickname.equals("")) {
			guild.getController().setNickname(member, nickname).reason("Redstoner Name Synchronization").queue();
		} else {
			statement = connection.createStatement();
			results = statement.executeQuery("SELECT value FROM permissions WHERE type=1 AND permission='name' AND name='" + uuid + "'");

			if (!results.next()) {
				logger.warn(member.getUser().getName() + " (" + member
						.getUser()
						.getId() + ") has a linked account, but has no name according to PermissionsEx. Leaving Discord nickname as is.");
			} else {
				String name = results.getString(1);
				if (!member.getEffectiveName().equals(name)) {
					guild.getController().setNickname(member, name).reason("Redstoner Name Synchronization").queue();
				}
			}
		}

		connection.close();

		if (rolesToAdd.size() == 0) {
			logger.warn(member.getUser().getName() + " (" + member
					.getUser()
					.getId() + ") has a linked account, but has no ranks according to PermissionsEx. Assuming they are visitor rank.");
			rolesToAdd.add(guild.getRoleById(roleDictionary.get("visitor")));
		}

		if (!CollectionUtils.isEqualCollection(member.getRoles(), rolesToAdd)) {
			logger.info("Updating roles for " + member.getUser().getName() + " (" + member.getUser().getId() + ").");
			guild.getController().modifyMemberRoles(member, rolesToAdd).reason("Redstoner Rank Synchronization").queue();
		}
	}
}