package com.redstoner.discordbot.commands;

import dev.logal.logalbot.commands.Command;
import dev.logal.logalbot.commands.CommandResponse;
import dev.logal.logalbot.utils.DataManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public final class LinkStatus implements Command {
	@Override
	public CommandResponse execute(String[] arguments, Member executor, TextChannel channel) {
		if (DataManager.getUserValue(executor, "minecraftUUID") == null) {
			return new CommandResponse("negative_squared_cross_mark", executor.getAsMention() + ", your account is not linked.");
		} else {
			return new CommandResponse("ballot_box_with_check", executor.getAsMention() + ", your account is linked.");
		}
	}
}