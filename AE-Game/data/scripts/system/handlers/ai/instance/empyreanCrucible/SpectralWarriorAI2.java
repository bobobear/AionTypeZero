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

package ai.instance.empyreanCrucible;

import ai.AggressiveNpcAI2;
import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.model.actions.NpcActions;
import org.typezero.gameserver.model.gameobjects.Creature;
import org.typezero.gameserver.model.gameobjects.Npc;
import org.typezero.gameserver.model.gameobjects.VisibleObject;
import org.typezero.gameserver.model.instance.StageType;
import org.typezero.gameserver.utils.ThreadPoolManager;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Luzien
 */
@AIName("spectral_warrior")
public class SpectralWarriorAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isDone = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 50 && isDone.compareAndSet(false, true)) {
			getPosition().getWorldMapInstance().getInstanceHandler().onChangeStage(StageType.START_STAGE_6_ROUND_5);

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					resurrectAllies();
				}

			}, 2000);
		}
	}

	private void resurrectAllies() {
		for (VisibleObject obj : getKnownList().getKnownObjects().values()) {
			if (obj instanceof Npc) {
				Npc npc = (Npc) obj;

				if (npc == null || NpcActions.isAlreadyDead(npc))
					continue;

				switch (npc.getNpcId()) {
					case 205413:
						spawn(217576, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
						NpcActions.delete(npc);
						break;
					case 205414:
						spawn(217577, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
						NpcActions.delete(npc);
						break;
				}
			}
		}
	}
}
