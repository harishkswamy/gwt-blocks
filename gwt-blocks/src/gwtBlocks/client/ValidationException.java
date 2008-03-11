package gwtBlocks.client;

import gwtBlocks.client.models.FeedbackModel;

public class ValidationException extends RuntimeException
{
    private static final long serialVersionUID = -3454227820042890585L;

    private FeedbackModel[]   _models;

    public ValidationException(String msg)
    {
        super(msg);
    }

    public ValidationException(FeedbackModel model, String msg)
    {
        this(new FeedbackModel[] { model }, msg);
    }

    public ValidationException(FeedbackModel model1, FeedbackModel model2, String msg)
    {
        this(new FeedbackModel[] { model1, model2 }, msg);
    }

    public ValidationException(FeedbackModel model1, FeedbackModel model2, FeedbackModel model3, String msg)
    {
        this(new FeedbackModel[] { model1, model2, model3 }, msg);
    }

    public ValidationException(FeedbackModel[] models, String msg)
    {
        super(msg);
        _models = models;
    }

    public FeedbackModel[] getMessageModels()
    {
        return _models;
    }
}
