/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 * <p/>
 * Aion-Lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * <p/>
 * Aion-Lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. *
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning.
 * If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * <p/>
 * Credits goes to all Open Source Core Developer Groups listed below
 * Please do not change here something, ragarding the developer credits, except the "developed by XXXX".
 * Even if you edit a lot of files in this source, you still have no rights to call it as "your Core".
 * Everybody knows that this Emulator Core was developed by Aion Lightning
 *
 * @-Aion-Unique-
 * @-Aion-Lightning
 * @Aion-Engine
 * @Aion-Extreme
 * @Aion-NextGen
 * @Aion-Core Dev.
 */
package org.typezero.chatserver.network.aion.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typezero.chatserver.configs.Config;
import org.typezero.chatserver.model.ChatClient;
import org.typezero.chatserver.model.channel.Channel;
import org.typezero.chatserver.network.aion.AbstractClientPacket;
import org.typezero.chatserver.network.aion.serverpackets.SM_CHANNEL_RESPONSE;
import org.typezero.chatserver.network.netty.handler.ClientChannelHandler;
import org.typezero.chatserver.service.ChatService;

import java.io.UnsupportedEncodingException;

/**
 * @author ATracer
 */
public class CM_CHANNEL_REQUEST extends AbstractClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_CHANNEL_REQUEST.class);
	private int channelIndex;
	private byte[] channelIdentifier;
	private ChatService chatService;

	/**
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_CHANNEL_REQUEST(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler, ChatService chatService) {
		super(channelBuffer, gameChannelHandler, 0x10);
		this.chatService = chatService;
	}

	@Override
	protected void readImpl() {
		readC(); // 0x40
		readH(); // 0x00
		channelIndex = readH();
		readB(18); //?
		int length = (readH() * 2);
		channelIdentifier = readB(length);
		readD(); // ?
	}

	@Override
	protected void runImpl() {
		try {
			if (Config.LOG_CHANNEL_REQUEST) {
				log.info("Channel requested " + new String(channelIdentifier, "UTF-16le"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ChatClient chatClient = clientChannelHandler.getChatClient();
		Channel channel = chatService.registerPlayerWithChannel(chatClient, channelIndex, channelIdentifier);
		if (channel != null) {
			clientChannelHandler.sendPacket(new SM_CHANNEL_RESPONSE(channel, channelIndex));
		}
	}

	@Override
	public String toString() {
		return "CM_CHANNEL_REQUEST [channelIndex=" + channelIndex + ", channelIdentifier=" + new String(channelIdentifier)
				+ "]";
	}
}
