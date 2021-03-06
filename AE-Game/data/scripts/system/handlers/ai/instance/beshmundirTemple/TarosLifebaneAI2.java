/**
 * This file is part of Aion Eternity Core <Ver:4.5>.
 *
 * Aion Eternity Core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion Eternity Core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Aion Eternity Core. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ai.instance.beshmundirTemple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import ai.AggressiveNpcAI2;

import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.model.gameobjects.Creature;
import org.typezero.gameserver.services.NpcShoutsService;

/**
 * @author Antraxx
 */
@AIName("taroslifebane")
public class TarosLifebaneAI2 extends AggressiveNpcAI2 {

    private AtomicBoolean isHome = new AtomicBoolean(true);
    protected List<Integer> percents = new ArrayList<Integer>();

    private void addPercent() {
        percents.clear();
        Collections.addAll(percents, new Integer[]{75, 50, 25});
    }

    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        addPercent();
    }

    private synchronized void checkPercentage(int hpPercentage) {
        for (Integer percent : percents) {
            if (hpPercentage <= percent) {
                switch (percent) {
                    case 75:
                        NpcShoutsService.getInstance().sendMsg(getOwner(), 1500074, getObjectId(), 0, 0);
                        break;
                    case 50:
                        NpcShoutsService.getInstance().sendMsg(getOwner(), 1500074, getObjectId(), 0, 0);
                        break;
                    case 25:
                        NpcShoutsService.getInstance().sendMsg(getOwner(), 1500074, getObjectId(), 0, 0);
                        break;
                }
                percents.remove(percent);
                break;
            }
        }
    }

    @Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
        if (isHome.compareAndSet(true, false)) {
            NpcShoutsService.getInstance().sendMsg(getOwner(), 1500073, getObjectId(), 0, 0);
        }
        checkPercentage(getLifeStats().getHpPercentage());
    }

    @Override
    protected void handleDied() {
        super.handleDied();
        percents.clear();
        NpcShoutsService.getInstance().sendMsg(getOwner(), 1500075, getObjectId(), 0, 0);
    }

    @Override
    protected void handleBackHome() {
        super.handleBackHome();
        isHome.set(true);
    }
}
