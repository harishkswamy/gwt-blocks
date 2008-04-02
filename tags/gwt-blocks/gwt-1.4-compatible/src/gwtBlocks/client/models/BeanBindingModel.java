package gwtBlocks.client.models;

import gwtBlocks.client.ValidationException;

import java.util.Iterator;

/**
 * This model is a collection of {@link PropertyBindingModel}s each of which binds a single form widget to a property
 * in the domain object wrapped by this model. The binding enables automatic convertion, validation and transfer of user
 * input to the domain object.
 * <p>
 * This is a buffered model, meaning input values from the bound widgets are not transferred to the domain object until
 * {@link #commit()} is called.
 * <p>
 * This model is automatically generated at runtime and there are some conventions to be followed for the model to be
 * generated correctly.
 * <ul>
 * <li>The model class must declare a no args constructor.</li>
 * <li>The model class must declare the binding domain object type via the javadoc tag
 * <code>-@cvg.domain.class-name</code>
 * <li>The binding domain class must declare a no args constructor.</li>
 * <li>The model class must declare the properties to be bound as abstract getter methods with "Model" appended to the
 * getter name and must return {@link PropertyBindingModel}. For example <code>public abstract PropertyBindingModel 
 * getTitleModel();</code>
 * where <code>title</code> is the domain property.</li>
 * <li>The {@link #setValue(Object)} method must be called before {@link #reset()}, {@link #commit()} or
 * {@link #save()} is called.</li>
 * </ul>
 * <p>
 * Validators can be registered to the model in the {@link #setup()} method.
 * 
 * @author hkrishna
 */
public abstract class BeanBindingModel extends CompositeModel implements BindingModel
{
    private interface Task
    {
        void execute(PropertyBindingModel model);
    }

    private boolean _autoCommit;

    public BeanBindingModel()
    {
        setName("");
    }

    public void setAutoCommit(boolean autoCommit)
    {
        _autoCommit = autoCommit;

        doForEachChild(new Task()
        {
            public void execute(PropertyBindingModel model)
            {
                model.setAutoCommit(_autoCommit);
            }
        });
    }

    /**
     * This method will transfer the model's buferred bound property values onto the domain object.
     */
    public void commit()
    {
        if (_autoCommit)
            return;

        doForEachChild(new Task()
        {
            public void execute(PropertyBindingModel model)
            {
                model.commit();
            }
        });
    }

    private void doForEachChild(Task task)
    {
        Iterator itr = getChildIterator();

        if (itr == null)
            return;

        while (itr.hasNext())
        {
            ComposableModel model = (ComposableModel) itr.next();

            if (model instanceof PropertyBindingModel)
                task.execute((PropertyBindingModel) model);
        }
    }

    /**
     * This method will {@link #commit()} if and only if there are no validation errors.
     * 
     * @throws ValidationException
     *             When there are validation errors.
     */
    public void save() throws ValidationException
    {
        if (getMessageModel().hasErrors())
            throw new ValidationException("");

        commit();
    }
}
