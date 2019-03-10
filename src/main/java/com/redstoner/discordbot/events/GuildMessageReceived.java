package com.redstoner.discordbot.events;

import dev.logal.logalbot.commands.CommandManager;
import dev.logal.logalbot.commands.CommandResponse;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public final class GuildMessageReceived extends ListenerAdapter {
	private final String rankSyncChannel = "470263993458491392";

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Guild       guild      = event.getGuild();
		TextChannel channel    = event.getChannel();
		Message     message    = event.getMessage();
		String      rawMessage = message.getContentRaw();
		Member      member     = event.getMember();

		if (member.getUser().equals(event.getJDA().getSelfUser())) {
			return;
		}

		if (channel.getId().equals(rankSyncChannel)) {
			if (guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_MANAGE)) {
				message.delete().reason("Redstoner Channel Restriction").queue();
			}

			if (rawMessage.length() == 8 && rawMessage.matches("[A-Za-z0-9]+")) {
				CommandManager.executeCommand(new String[] { "linkaccount", rawMessage }, member, channel);
			} else {
				new CommandResponse(
						"no_entry_sign",
						"Sorry " + member.getAsMention() + ", but this channel can only be used for rank synchronization tokens. If you need help, contact staff in the `staff-help` channel."
				).setDeletionDelay(30, TimeUnit.SECONDS).sendResponse(channel);
			}
		}
	}
}