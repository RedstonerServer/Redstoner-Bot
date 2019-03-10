package com.redstoner.discordbot.commands;

import com.redstoner.discordbot.utils.RankUtil;
import dev.logal.logalbot.commands.Command;
import dev.logal.logalbot.commands.CommandResponse;
import dev.logal.logalbot.utils.DataManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public final class Nickname implements Command {
	@Override
	public CommandResponse execute(String[] arguments, Member executor, TextChannel channel) {
		Guild guild = channel.getGuild();
		if (arguments.length == 0) {
			if (DataManager.getUserValue(executor, "minecraftUUID") == null) {
				return new CommandResponse(
						"no_entry_sign",
						"Sorry " + executor.getAsMention() + ", but your nickname could not be reset because your account is not linked."
				).setDeletionDelay(10, TimeUnit.SECONDS);
			} else {
				DataManager.setUserValue(executor, "nickname", "");
				try {
					RankUtil.syncRanks(executor);
					return new CommandResponse("white_check_mark", executor.getAsMention() + ", your nickname has been reset.");
				} catch (Throwable exception) {
					return new CommandResponse(
							"sos",
							"Sorry " + executor.getAsMention() + ", but your nickname could not be reset because an error occurred while trying to look up your name."
					).setDeletionDelay(10, TimeUnit.SECONDS);
				}
			}
		}

		String userID = arguments[0].replaceFirst("<@[!]?([0-9]*)>", "$1");
		Member target;
		try {
			target = guild.getMemberById(userID);
		} catch (Throwable exception) {
			target = null;
		}

		if (target == null) {
			if (DataManager.getUserValue(executor, "minecraftUUID") == null) {
				return new CommandResponse(
						"no_entry_sign",
						"Sorry " + executor.getAsMention() + ", but your nickname could not be reset because your account is not linked."
				).setDeletionDelay(10, TimeUnit.SECONDS);
			} else {
				DataManager.setUserValue(executor, "nickname", arguments[0]);
				try {
					RankUtil.syncRanks(executor);
					return new CommandResponse("white_check_mark", executor.getAsMention() + ", your nickname has been set.");
				} catch (Throwable exception) {
					return new CommandResponse(
							"sos",
							"Sorry " + executor.getAsMention() + ", but your nickname could not be set because an error occurred while trying to look up your name."
					).setDeletionDelay(10, TimeUnit.SECONDS);
				}
			}
		} else {
			if (DataManager.getUserValue(target, "minecraftUUID") == null) {
				return new CommandResponse(
						"no_entry_sign",
						"Sorry " + executor.getAsMention() + ", but the nickname for that user could not be set because their account is not linked."
				).setDeletionDelay(10, TimeUnit.SECONDS);
			}

			if (arguments.length == 1) {
				try {
					DataManager.setUserValue(target, "nickname", "");
					RankUtil.syncRanks(target);
					return new CommandResponse("white_check_mark", executor.getAsMention() + " has reset the nickname for " + target.getAsMention() + ".");
				} catch (Throwable exception) {
					return new CommandResponse(
							"sos",
							"Sorry " + executor.getAsMention() + ", but the nickname for that user could not be reset because an error occurred while trying to look up their name."
					).setDeletionDelay(10, TimeUnit.SECONDS);
				}
			} else {
				try {
					DataManager.setUserValue(target, "nickname", arguments[1]);
					RankUtil.syncRanks(target);
					return new CommandResponse("white_check_mark", executor.getAsMention() + " has set the nickname for " + target.getAsMention() + ".");
				} catch (Throwable exception) {
					return new CommandResponse(
							"sos",
							"Sorry " + executor.getAsMention() + ", but the nickname for that user could not be set because an error occurred while trying to look up their name."
					).setDeletionDelay(10, TimeUnit.SECONDS);
				}
			}
		}
	}
}