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

package quest.poeta;

import org.typezero.gameserver.model.EmotionId;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;
import org.typezero.gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _1004NeutralizingOdium extends QuestHandler {

	private final static int questId = 1004;

	public _1004NeutralizingOdium() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203082).addOnTalkEvent(questId);
		qe.registerQuestNpc(700030).addOnTalkEvent(questId);
		qe.registerQuestNpc(790001).addOnTalkEvent(questId);
		qe.registerQuestNpc(203067).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203082) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0)
							return sendQuestDialog(env, 1011);
						else if (var == 5)
							return sendQuestDialog(env, 2034);
					case SELECT_ACTION_1013:
						if (var == 0)
							playQuestMovie(env, 19);
						return false;
					case SETPRO1:
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						sendQuestSelectionDialog(env);
						return true;
					case SETPRO3:
						if (var == 5) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							sendQuestSelectionDialog(env);
							return true;
						}
				}
			}
			else if (targetId == 700030 && var == 1 || var == 4) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						if (qs.getQuestVarById(0) == 1) {
							if (giveQuestItem(env, 182200005, 1))
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						}
						else if (qs.getQuestVarById(0) == 4) {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							removeQuestItem(env, 182200005, 1);
						}
						updateQuestStatus(env);
						sendEmotion(env, player, EmotionId.STAND, true);
						return false;
				}
			}
			else if (targetId == 790001) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2)
							return sendQuestDialog(env, 1352);
						else if (var == 3)
							return sendQuestDialog(env, 1693);
						else if (var == 11)
							return sendQuestDialog(env, 1694);
					case SETPRO2:
						if (var == 2) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							sendQuestSelectionDialog(env);
							return true;
						}
					case SETPRO3:
						if (var == 11) {
							if (!giveQuestItem(env, 182200006, 1))
								return true;
							qs.setQuestVarById(0, 4);
							updateQuestStatus(env);
							removeQuestItem(env, 182200006, 1);
							sendQuestSelectionDialog(env);
							return true;
						}
					case CHECK_USER_HAS_QUEST_ITEM: {
						if (QuestService.collectItemCheck(env, true)) {
							qs.setQuestVarById(0, 11);
							updateQuestStatus(env);
							return sendQuestDialog(env, 1694);
						}
						else
							return sendQuestDialog(env, 1779);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203067)
				return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1100, true);
	}
}
