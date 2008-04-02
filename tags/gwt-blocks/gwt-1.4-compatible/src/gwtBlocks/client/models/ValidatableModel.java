package gwtBlocks.client.models;

import gwtBlocks.client.ValidationException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a {@link FeedbackModel} that maintains a validator chain.
 * 
 * @author hkrishna
 */
public class ValidatableModel extends FeedbackModel
{
    private Set _validators;

    public void addValidator(Validator validator)
    {
        if (_validators == null)
            _validators = new HashSet();
        
        _validators.add(validator);
        
        // Validate current value in the model
        if (getMessageModel() != null)
            validate(validator);
    }

    public void removeValidator(Validator validator)
    {
        if (_validators == null)
            return;

        _validators.remove(validator);
    }

    protected void validate()
    {
        if (getMessageModel() == null)
            return;

        getMessageModel().clear(this);

        if (_validators == null)
            return;

        // Validate
        for (Iterator itr = _validators.iterator(); itr.hasNext();)
            validate((Validator) itr.next());
    }

    private void validate(Validator validator)
    {
        try
        {
            validator.validate(this);
        }
        catch (ValidationException e)
        {
            getMessageModel().addMessage(this, e.getMessage(), e.getMessageModels());
        }
    }

    /**
     * Validation is plugged in after setting value but before notifying change listeners.
     */
    protected void beforeNotifyChangeListeners()
    {
        validate();
    }
}
