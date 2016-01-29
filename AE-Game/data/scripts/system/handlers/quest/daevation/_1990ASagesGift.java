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

package quest.daevation;

import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;

/**
 * @author kecimis,Tiger
 * @reworked vlog
 */
public class _1990ASagesGift extends QuestHandler {

	private final static int questId = 1990;

	// mob counter
	// it's binary. Don't change!
	private int ALL = 0;
	private int A = 0;
	private int B = 0;
	private int C = 0;

	public _1990ASagesGift() {
		super(questId);
	}

	@Override
	public void register() {
		int[] mobs = { 256617, 253721, 253720, 254514, 254513 };
		qe.registerQuestNpc(203771).addOnQuestStart(questId);
		qe.registerQuestNpc(203771).addOnTalkEvent(questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203771) { // Fermina
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					if (isDaevanionArmorEquipped(player)) {
						return sendQuestDialog(env, 4762);
					}
					else {
						return sendQuestDialog(env, 4848);
					}
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			if (targetId == 203771) { // Fermina
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2 && var1 == 60) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 0, 1, false, 10000, 10001); // 1
					}
					case SELECT_ACTION_2035: {
						int currentDp = player.getCommonData().getDp();
						int maxDp = player.getGameStats().getMaxDp().getCurrent();
						long burner = player.getInventory().getItemCountByItemId(186000040); // Divine Incense Burner
						if (currentDp == maxDp && burner >= 1) {
							removeQuestItem(env, 186000040, 1);
							player.getCommonData().setDp(0);
							changeQuestStep(env, 3, 3, true); // reward
							return sendQuestDialog(env, 5);
						}
						else {
							return sendQuestDialog(env, 2120);
						}
					}
					case SETPRO2: {
						return defaultCloseDialog(env, 1, 2); // 2
					}
					case SETPRO3: {
						qs.setQuestVar(3); // 3
						updateQuestStatus(env);
						return sendQuestSelectionDialog(env);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203771) { // Fermina
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				switch (env.getTargetId()) {
					case 256617: { // Strange Lake Spirit
						if (A >= 0 && A < 30) {
							++A;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;// var0
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
					case 253721:
					case 253720: { // Lava Hoverstone
						if (B >= 0 && B < 30) {
							++B;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;// var0
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
					case 254514:
					case 254513: { // Disturbed Resident
						if (C >= 0 && C < 30) {
							++C;
							ALL = C;
							ALL = ALL << 7;
							ALL += B;
							ALL = ALL << 7;
							ALL += A;
							ALL = ALL << 7;
							ALL += 2;// var0
							qs.setQuestVar(ALL);
							updateQuestStatus(env);
						}
						break;
					}
				}
				if (qs.getQuestVarById(0) == 2 && A == 30 && B == 30 && C == 30) {
					qs.setQuestVarById(1, 60);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}

	private boolean isDaevanionArmorEquipped(Player player) {
		int plate = player.getEquipment().itemSetPartsEquipped(9);
		int chain = player.getEquipment().itemSetPartsEquipped(8);
		int leather = player.getEquipment().itemSetPartsEquipped(7);
		int cloth = player.getEquipment().itemSetPartsEquipped(6);
		int gunner = player.getEquipment().itemSetPartsEquipped(378);
		if (plate == 5 || chain == 5 || leather == 5 || cloth == 5 || gunner == 5) {
			return true;
		}
		return false;
	}
}