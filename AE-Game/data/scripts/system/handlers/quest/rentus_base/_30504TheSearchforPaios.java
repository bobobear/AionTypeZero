package quest.rentus_base;

import org.typezero.gameserver.model.gameobjects.player.Player;
import org.typezero.gameserver.questEngine.handlers.QuestHandler;
import org.typezero.gameserver.model.DialogAction;
import org.typezero.gameserver.questEngine.model.QuestEnv;
import org.typezero.gameserver.questEngine.model.QuestState;
import org.typezero.gameserver.questEngine.model.QuestStatus;


/**
 * @author Romanz
 *
 */
public class _30504TheSearchforPaios extends QuestHandler
{
	private final static int questId=30504;

	public _30504TheSearchforPaios()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.registerQuestNpc(804879).addOnQuestStart(questId);
		qe.registerQuestNpc(804879).addOnTalkEvent(questId);
		qe.registerQuestNpc(701098).addOnTalkEvent(questId);
		qe.registerQuestNpc(799536).addOnTalkEvent(questId);

	}
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		Player player=env.getPlayer();
		QuestState qs=player.getQuestStateList().getQuestState(questId);
		DialogAction dialog=env.getDialog();
		int targetId=env.getTargetId();
		if(qs==null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat())
		{
			switch(targetId)
			{
				case 804879:
				{
					switch(dialog)
					{
						case QUEST_SELECT:
							return sendQuestDialog(env,4762);
						default:
							return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if(qs != null && qs.getStatus() ==QuestStatus.START)
		{
			switch(targetId)
			{
				case 799536:
				{
					switch(dialog)
					{
						case QUEST_SELECT:
							return sendQuestDialog(env,1352);
						case SET_SUCCEED:
							return defaultCloseDialog(env, 1, 2, true, false);//reward
					}
				}
				break;
				case 701098:
				{
					switch(dialog)
					{
						case USE_OBJECT:
							return useQuestObject(env, 0, 1, false, true);
					}
				}
			}
		}
		else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 205438)
			{
				if(env.getDialog() == DialogAction.USE_OBJECT)
					return sendQuestDialog(env, 10002);
				else if(env.getDialog() == DialogAction.SELECT_QUEST_REWARD)
					return sendQuestDialog(env, 5);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
