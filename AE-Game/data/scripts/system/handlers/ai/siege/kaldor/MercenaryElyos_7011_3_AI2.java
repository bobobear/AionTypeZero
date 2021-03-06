package ai.siege.kaldor;

import org.typezero.gameserver.ai2.AIName;
import org.typezero.gameserver.ai2.NpcAI2;
import org.typezero.gameserver.model.gameobjects.Npc;
import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.model.gameobjects.siege.SiegeNpc;
import org.typezero.gameserver.model.siege.SiegeLocation;
import org.typezero.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import org.typezero.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.typezero.gameserver.questEngine.QuestEngine;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;
import org.typezero.gameserver.services.QuestService;
import org.typezero.gameserver.services.SiegeService;
import org.typezero.gameserver.utils.PacketSendUtility;
import org.typezero.gameserver.world.WorldMapInstance;
import java.util.List;

/**
 * @author Romanz
 */
@AIName("7011_mercenary_ely3")
public class MercenaryElyos_7011_3_AI2 extends NpcAI2 {

    protected int rewardDialogId = 5;
    protected int startingDialogId = 10;
    protected int questDialogId = 10;

    @Override
    protected void handleDialogStart(Player player) {
        checkDialog(player);
        if(player.getLegion() != null) {
      int SiegeId = ((SiegeNpc) this.getOwner()).getSiegeId();
      SiegeLocation Location = SiegeService.getInstance().getSiegeLocation(SiegeId);
      if(Location != null) {
          if(Location.getLegionId()== player.getLegion().getLegionId()) {
             PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
             return;
              }
          }
        }
       PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
        }

    @Override
    public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
        QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
        env.setExtendedRewardIndex(extendedRewardIndex);
        checkEntryConditions(player, dialogId, questId);
        if (QuestEngine.getInstance().onDialog(env)) {
            return true;
        }
        return true;
    }

    private void checkDialog(Player player) {
        int npcId = getNpcId();
        List<Integer> relatedQuests = QuestEngine.getInstance().getQuestNpc(npcId).getOnTalkEvent();
        boolean playerHasQuest = false;
        boolean playerCanStartQuest = false;
        if (!relatedQuests.isEmpty()) {
            for (int questId : relatedQuests) {
                QuestState qs = player.getQuestStateList().getQuestState(questId);
                if (qs != null && (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD)) {
                    playerHasQuest = true;
                    break;
                }
                else if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
                    if (QuestService.checkStartConditions(new QuestEnv(getOwner(), player, questId, 0), true)) {
                        playerCanStartQuest = true;
                        continue;
                    }
                }
            }
        }

        if (playerHasQuest) {
            boolean isRewardStep = false;
            for (int questId : relatedQuests) {
                QuestState qs = player.getQuestStateList().getQuestState(questId);
                if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), rewardDialogId, questId));
                    isRewardStep = true;
                    break;
                }
            }
            if (!isRewardStep) {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), questDialogId));
            }
        }
        else if (playerCanStartQuest) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), startingDialogId));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 0));
        }
    }

    private void checkEntryConditions(Player player, int dialogId, int questId) {
        if (dialogId == 10000) {
            if (player.getInventory().getItemCountByItemId(186000236) >= 39) {
                if ((getPosition().getWorldMapInstance().getNpc(251842) == null &&
                getPosition().getWorldMapInstance().getNpc(252260) == null &&
                getPosition().getWorldMapInstance().getNpc(251847) == null &&
                getPosition().getWorldMapInstance().getNpc(251872)  == null) &&
                player.getInventory().decreaseByItemId(186000236, 39)) {
                spawn(251842, 554.04f, 679.67f, 153.24f, (byte) 35); // Kn
                spawn(252260, 550.03f, 681.05f, 153.55f, (byte) 35); // Fi
                spawn(252260, 563.4f, 684.85f, 154.28f, (byte) 35); // Fi
                spawn(252260, 547.16f, 682.6f, 153.5f, (byte) 35); // Fi
                spawn(252260, 560.4f, 683.3f, 153.73f, (byte) 35); // Fi
                spawn(251847, 566.51f, 678.26f, 159.77f, (byte) 35); // Ra
                spawn(251847, 547.54f, 675.07f, 159.6f, (byte) 35); // Ra
                spawn(251847, 566.33f, 670.91f, 159.74f, (byte) 35); // Ra
                spawn(251847, 549.51f, 667.93f, 159.57f, (byte) 35); // Ra
                spawn(251872, 558.76f, 680.18f, 153.24f, (byte) 35); // Wi
                spawn(252428, 556.75f, 677.01f, 152.87f, (byte) 35); // trap
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402489));
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402496));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 2375));
                } else {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401837));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
                }
            } else {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
            }
        }
        else if (dialogId == 10001) {
            if (player.getInventory().getItemCountByItemId(186000236) >= 39) {
                if ((getPosition().getWorldMapInstance().getNpc(251843) == null &&
                getPosition().getWorldMapInstance().getNpc(252261) == null &&
                getPosition().getWorldMapInstance().getNpc(251848) == null &&
                getPosition().getWorldMapInstance().getNpc(251873)  == null) &&
                player.getInventory().decreaseByItemId(186000236, 39)) {
                spawn(251843, 1075.68f, 713.93f, 185.12f, (byte) 35); // Kn
                spawn(252261, 1085.47f, 715.96f, 185.07f, (byte) 35); // Fi
                spawn(252261, 1083.713f, 713.96f, 185.12f, (byte) 35); // Fi
                spawn(252261, 1072.98f, 716.2f, 185.03f, (byte) 35); // Fi
                spawn(252261, 1071.47f, 717.96f, 185.05f, (byte) 35); // Fi
                spawn(251848, 1068.06f, 712.31f, 192.04f, (byte) 35); // Ra
                spawn(251848, 1086.85f, 708.88f, 192.21f, (byte) 35); // Ra
                spawn(251848, 1067.25f, 705.02f, 192.02f, (byte) 35); // Ra
                spawn(251848, 1084.34f, 701.86f, 192.18f, (byte) 35); // Ra
                spawn(251873, 1080.47f, 713.1f, 185.12f, (byte) 35); // Wi
                spawn(252428, 1077.70f, 711.23f, 185.125f, (byte) 35); // trap
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402490));
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402497));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 2375));
                } else {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401837));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
                }
            } else {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
            }
        }
        else if (dialogId == 10002) {
            if (player.getInventory().getItemCountByItemId(186000236) >= 20) {
                if ((getPosition().getWorldMapInstance().getNpc(251844) == null &&
                getPosition().getWorldMapInstance().getNpc(252262) == null &&
                getPosition().getWorldMapInstance().getNpc(251849) == null &&
                getPosition().getWorldMapInstance().getNpc(251874)  == null) &&
                player.getInventory().decreaseByItemId(186000236, 20)) {
                spawn(251844, 975.48f, 480.3f, 195.91f, (byte) 60); // Kn
                spawn(251844, 972.66f, 483.64f, 197.03f, (byte) 60); // Kn
                spawn(252262, 980.61f, 486.77f, 195.42f, (byte) 60); // Fi
                spawn(252262, 983.73f, 482.96f, 194.84f, (byte) 60); // Fi
                spawn(251849, 958.2f, 487.26f, 199.6f, (byte) 60); // Ra
                spawn(251849, 960.68f, 485.07f, 199.3f, (byte) 60); // Ra
                spawn(251874, 966.51f, 487.94f, 198.4f, (byte) 60); // Wi
                spawn(252428, 976.58f, 484.01f, 196.14f, (byte) 35); // trap
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402491));
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402498));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 2375));
                } else {
                PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401837));
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
                }
            } else {
                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
            }
        }
		else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
        }
    }
    public void deleteMobs(List<Npc> npcs) {
        for (Npc npc : npcs) {
            if (npc != null) {
                npc.getController().onDelete();
            }
        }
    }

    @Override
    protected void handleDespawned() {
        WorldMapInstance instance = getPosition().getWorldMapInstance();
        deleteMobs(instance.getNpcs(251842));
        deleteMobs(instance.getNpcs(252260));
        deleteMobs(instance.getNpcs(251847));
        deleteMobs(instance.getNpcs(251872));
		deleteMobs(instance.getNpcs(251843));
		deleteMobs(instance.getNpcs(251844));
		deleteMobs(instance.getNpcs(252261));
		deleteMobs(instance.getNpcs(252262));
		deleteMobs(instance.getNpcs(251848));
		deleteMobs(instance.getNpcs(251849));
		deleteMobs(instance.getNpcs(251873));
		deleteMobs(instance.getNpcs(251874));
		deleteMobs(instance.getNpcs(252428));
        super.handleDespawned();
    }
}
