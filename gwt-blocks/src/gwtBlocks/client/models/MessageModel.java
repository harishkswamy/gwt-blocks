package gwtBlocks.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hkrishna
 */
public class MessageModel extends ValueModel
{
    public void addMessage(FeedbackModel model, String msg)
    {
        addMessage(model, msg, null);
    }

    public void addMessage(FeedbackModel valModel, String msg, FeedbackModel[] msgModels)
    {
        msgModels = msgModels == null || msgModels.length == 0 ? new FeedbackModel[] {valModel} : msgModels;
        List msgModelList = concat(new ArrayList(), msgModels);
        Map messageMap = getMessageMap();
        Map modelMessages = getModelMessages(messageMap, valModel);
        List oldMsgModels = (List) modelMessages.get(msg);

        if (oldMsgModels != null)
            msgModelList.addAll(oldMsgModels);

        modelMessages.put(msg, msgModelList);
        setValue(messageMap); // This needs to be called to notify change listeners
    }

    private List concat(List msgModelList, FeedbackModel[] msgModels)
    {
        if (msgModels == null)
            return msgModelList;
        
        for (int i = 0; i < msgModels.length; i++)
            msgModelList.add(msgModels[i]);

        return msgModelList;
    }

    public boolean hasErrors()
    {
        return !getMessageMap().isEmpty();
    }

    public void clear(FeedbackModel model)
    {
        Map messageMap = getMessageMap();
        if (messageMap.remove(model) != null)
            setValue(messageMap); // This needs to be called to notify change listeners
    }

    public void clear()
    {
        Map messageMap = getMessageMap();
        messageMap.clear();
        setValue(messageMap); // This needs to be called to notify change listeners
    }

    public Map getMessages()
    {
        return (Map) getValue();
    }

    private Map getMessageMap()
    {
        Map messageMap = (Map) getValue();
        return messageMap == null ? new HashMap() : messageMap;
    }

    private Map getModelMessages(final Map messageMap, FeedbackModel model)
    {
        Map messages = (Map) messageMap.get(model);

        if (messages == null)
        {
            messages = new HashMap();
            messageMap.put(model, messages);
        }

        return messages;
    }
}
