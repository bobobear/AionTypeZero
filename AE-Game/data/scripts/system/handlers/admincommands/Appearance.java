/*
 * Copyright (c) 2015, TypeZero Engine (game.developpers.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of TypeZero Engine nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package admincommands;

import org.typezero.gameserver.model.gameobjects.VisibleObject;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.gameobjects.player.PlayerAppearance;
import org.typezero.gameserver.services.teleport.TeleportService2;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Divinity
 */
public class Appearance extends AdminCommand {

	public Appearance() {
		super("appearance");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params == null || params.length < 1) {
			onFail(admin, null);
			return;
		}

		VisibleObject target = admin.getTarget();
		Player player;

		if (target == null)
			player = admin;
		else
			player = (Player) target;

		if (params[0].equals("reset")) {
			PlayerAppearance savedPlayerAppearance = player.getSavedPlayerAppearance();

			if (savedPlayerAppearance == null) {
				PacketSendUtility.sendMessage(admin, "The target has already the normal appearance.");
				return;
			}

			// Edit the current player's appearance with the saved player's appearance
			player.setPlayerAppearance(savedPlayerAppearance);

			// See line 44
			player.setSavedPlayerAppearance(null);

			// Warn the player
			PacketSendUtility.sendMessage(player, "An admin has resetted your appearance.");

			// Send update packets
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(),
				player.getZ(), player.getHeading());

			return;
		}

		if (params.length < 2) {
			onFail(player, null);
			return;
		}

		// Get the current player's appearance
		PlayerAppearance playerAppearance = player.getPlayerAppearance();

		// Save a clean player's appearance
		if (player.getSavedPlayerAppearance() == null)
			player.setSavedPlayerAppearance((PlayerAppearance) playerAppearance.clone());

		if (params[0].equals("size")) // Edit player's size. Min: 0, Max: 50 (prevent bug)
		{
			float height;

			try {
				height = Float.parseFloat(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (height < 0 || height > 50) {
				PacketSendUtility.sendMessage(admin, "Size: Min value : 0 - Max value : 50");
				return;
			}

			// Edit the height
			playerAppearance.setHeight(height);
		}
		else if (params[0].equals("voice")) // Min: 0, Max: 3
		{
			int voice;

			try {
				voice = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (voice < 0 || voice > 3) {
				PacketSendUtility.sendMessage(admin, "Voice: Min value : 0 - Max value : 3");
				return;
			}

			// Edit the voice
			playerAppearance.setVoice(voice);
		}
		else if (params[0].equals("hair")) // Min: 1, Max: 43
		{
			int hair;

			try {
				hair = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (hair < 1 || hair > 43) {
				PacketSendUtility.sendMessage(admin, "Hair: Min value : 1 - Max value : 43");
				return;
			}

			// Edit the hair
			playerAppearance.setHair(hair);
		}
		else if (params[0].equals("face")) // Min: 1, Max: 24
		{
			int face;

			try {
				face = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (face < 1 || face > 24) {
				PacketSendUtility.sendMessage(admin, "Face: Min value : 1 - Max value : 24");
				return;
			}

			// Edit the face
			playerAppearance.setFace(face);
		}
		else if (params[0].equals("deco")) // Min: 1, Max: 18
		{
			int deco;

			try {
				deco = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (deco < 1 || deco > 18) {
				PacketSendUtility.sendMessage(admin, "Deco: Min value : 1 - Max value : 18");
				return;
			}

			// Edit the deco
			playerAppearance.setDeco(deco);
		}
		else if (params[0].equals("head_size")) // Min: 0, Max: 100
		{
			int head;

			try {
				head = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (head < 0 || head > 100) {
				PacketSendUtility.sendMessage(admin, "Head Size: Min value : 0 - Max value : 100");
				return;
			}

			// Edit the head
			playerAppearance.setHeadSize(head + 200);
		}
		else if (params[0].equals("tattoo")) // Min: 1, Max: 13
		{
			int tattoo;

			try {
				tattoo = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "The value must be a number !");
				onFail(player, e.getMessage());
				return;
			}

			if (tattoo < 1 || tattoo > 13) {
				PacketSendUtility.sendMessage(admin, "Tattoo: Min value : 1 - Max value : 13");
				return;
			}

			// Edit the tattoo
			playerAppearance.setTattoo(tattoo);
		}
		else {
			onFail(player, null);
			return;
		}

		// Edit the current player's appearance with our modifications
		player.setPlayerAppearance(playerAppearance);

		// Warn the player
		PacketSendUtility.sendMessage(player, "An admin has changed your appearance.");

		// Send update packets
		TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(),
			player.getZ(), player.getHeading());
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //appearance <size | voice | hair | face | deco | head_size | tattoo | reset (to reset the appearance)> <value>";
		PacketSendUtility.sendMessage(player, syntax);
	}
}