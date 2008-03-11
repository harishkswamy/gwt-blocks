package gwtBlocks.client.models;

/**
 * This is a {@link ValueModel} that wraps the {@link MessageModel} and has a name. This is a convenience model that can
 * be used to provide user feedback.
 * 
 * @author hkrishna
 */
public class FeedbackModel extends ValueModel
{
    private String       _name;
    private MessageModel _messageModel;

    public void setName(String name)
    {
        _name = name;
    }

    public String getName()
    {
        return _name;
    }

    public MessageModel getMessageModel()
    {
        return getParent() == null ? _messageModel : getParent().getMessageModel();
    }

    public void setMessageModel(MessageModel model)
    {
        _messageModel = model;
    }

    public void detach()
    {
        MessageModel msgModel = getMessageModel();

        if (msgModel == null)
            return;

        msgModel.clear(this);
    }
}
