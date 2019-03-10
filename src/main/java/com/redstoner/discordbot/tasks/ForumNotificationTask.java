package com.redstoner.discordbot.tasks;

import dev.logal.logalbot.LogalBot;
import dev.logal.logalbot.utils.DataManager;
import dev.logal.logalbot.utils.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

public final class ForumNotificationTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ForumNotificationTask.class);

	private static final Guild redstonerGuild = LogalBot.getJDA().getGuildById("470229570310766593");

	private static final String announcementsChannel    = "489957437978181635";
	private static final String staffGeneralChannel     = "472452678367313941";
	private static final String staffDevelopmentChannel = "491050574092042241";

	@Override
	public void run() {
		try {
			// Blog posts
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			InputStream stream = new URL("https://redstoner.com/blog.atom").openStream();
			Document    doc    = docBuilder.parse(stream);

			Element topEntry = (Element) doc.getDocumentElement().getElementsByTagName("entry").item(0);

			String title = topEntry.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

			Element authorBlock = (Element) topEntry.getElementsByTagName("author").item(0);
			String  author      = authorBlock.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String id   = topEntry.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			String link = topEntry.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();

			if (DataManager.getGuildValue(redstonerGuild, "knownAnnouncement:" + id) == null) {
				LogalBot
						.getJDA()
						.getTextChannelById(announcementsChannel)
						.sendMessage(
								":loudspeaker: New blog post **" + StringUtil.sanitize(title) + "** authored by *" + StringUtil.sanitize(author) + "*\n" + link)
						.queue();
				DataManager.setGuildValue(redstonerGuild, "knownAnnouncement:" + id, "true");
			}
		} catch (Throwable exception) {
			logger.error("An error occurred while posting new blog posts to Discord!", exception);
		}

		try {
			// Blames
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			InputStream stream = new URL("https://redstoner.com/forums/21.atom").openStream();
			Document    doc    = docBuilder.parse(stream);

			Element topEntry = (Element) doc.getDocumentElement().getElementsByTagName("entry").item(0);

			String title = topEntry.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

			Element authorBlock = (Element) topEntry.getElementsByTagName("author").item(0);
			String  author      = authorBlock.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String id   = topEntry.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			String link = topEntry.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();

			if (DataManager.getGuildValue(redstonerGuild, "knownBlame:" + id) == null) {
				LogalBot
						.getJDA()
						.getTextChannelById(staffGeneralChannel)
						.sendMessage(
								":loudspeaker: New blame **" + StringUtil.sanitize(title) + "** authored by *" + StringUtil.sanitize(author) + "*\n" + link)
						.queue();
				DataManager.setGuildValue(redstonerGuild, "knownBlame:" + id, "true");
			}
		} catch (Throwable exception) {
			logger.error("An error occurred while posting new blames to Discord!", exception);
		}

		try {
			// Appeals
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			InputStream stream = new URL("https://redstoner.com/forums/5.atom").openStream();
			Document    doc    = docBuilder.parse(stream);

			Element topEntry = (Element) doc.getDocumentElement().getElementsByTagName("entry").item(0);

			String title = topEntry.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

			Element authorBlock = (Element) topEntry.getElementsByTagName("author").item(0);
			String  author      = authorBlock.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String id   = topEntry.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			String link = topEntry.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();

			if (DataManager.getGuildValue(redstonerGuild, "knownAppeal:" + id) == null) {
				LogalBot
						.getJDA()
						.getTextChannelById(staffGeneralChannel)
						.sendMessage(
								":loudspeaker: New appeal **" + StringUtil.sanitize(title) + "** authored by *" + StringUtil.sanitize(author) + "*\n" + link)
						.queue();
				DataManager.setGuildValue(redstonerGuild, "knownAppeal:" + id, "true");
			}
		} catch (Throwable exception) {
			logger.error("An error occurred while posting new appeals to Discord!", exception);
		}

		try {
			// Problems & Bugs
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			InputStream stream = new URL("https://redstoner.com/forums/11.atom").openStream();
			Document    doc    = docBuilder.parse(stream);

			Element topEntry = (Element) doc.getDocumentElement().getElementsByTagName("entry").item(0);

			String title = topEntry.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

			Element authorBlock = (Element) topEntry.getElementsByTagName("author").item(0);
			String  author      = authorBlock.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String id   = topEntry.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			String link = topEntry.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();

			if (DataManager.getGuildValue(redstonerGuild, "knownBugReport:" + id) == null) {
				LogalBot
						.getJDA()
						.getTextChannelById(staffDevelopmentChannel)
						.sendMessage(":loudspeaker: New bug report **" + StringUtil.sanitize(title) + "** authored by *" + StringUtil.sanitize(
								author) + "*\n" + link)
						.queue();
				DataManager.setGuildValue(redstonerGuild, "knownBugReport:" + id, "true");
			}
		} catch (Throwable exception) {
			logger.error("An error occurred while posting new bug reports to Discord!", exception);
		}

		try {
			// Website Problems & Bugs
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			InputStream stream = new URL("https://redstoner.com/forums/4.atom").openStream();
			Document    doc    = docBuilder.parse(stream);

			Element topEntry = (Element) doc.getDocumentElement().getElementsByTagName("entry").item(0);

			String title = topEntry.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();

			Element authorBlock = (Element) topEntry.getElementsByTagName("author").item(0);
			String  author      = authorBlock.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

			String id   = topEntry.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
			String link = topEntry.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();

			if (DataManager.getGuildValue(redstonerGuild, "knownWebsiteBugReport:" + id) == null) {
				LogalBot
						.getJDA()
						.getTextChannelById(staffDevelopmentChannel)
						.sendMessage(":loudspeaker: New website bug report **" + StringUtil.sanitize(title) + "** authored by *" + StringUtil.sanitize(
								author) + "*\n" + link)
						.queue();
				DataManager.setGuildValue(redstonerGuild, "knownWebsiteBugReport:" + id, "true");
			}
		} catch (Throwable exception) {
			logger.error("An error occurred while posting new website bug reports to Discord!", exception);
		}
	}
}