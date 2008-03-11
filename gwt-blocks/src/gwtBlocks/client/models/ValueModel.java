package gwtBlocks.client.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is the base view model class of the MVC framework. This model wraps a single domain value and provides data
 * services to the view. This model registers {@link ValueChangeListener}s and notifes them when this model's value
 * changes.
 * <p>
 * This model can start a batch and temporarily collect the changes to the model value and not propagate the changes to
 * listeners until {@link #commitBatch()} is called.
 * <p>
 * Views typically register with their models to update their state whenever the model changes.
 * 
 * @author hkrishna
 */
public class ValueModel implements ComposableModel
{
    private Object         _value;
    private Set            _changeListeners;
    private boolean        _inBatchMode;
    private CompositeModel _parent;

    public void setParent(String key, CompositeModel parent)
    {
        if (parent == _parent)
            return;

        if (_parent != null)
            _parent.removeChild(key);

        _parent = parent;

        if (_parent != null)
            _parent.addChild(key, this);
    }

    public CompositeModel getParent()
    {
        return _parent;
    }

    public void parentValueChanged()
    {
    }

    /**
     * Sets this model's value and notifies registered change listeners.
     * <p>
     * If in batch mode this method will not notify listeners until {@link #commitBatch()} is called.
     * 
     * @param value
     *            The new value of the model.
     * @see #commitBatch()
     */
    public void setValue(Object value)
    {
        beforeSetValue(value);

        _value = value;

        if (_parent != null)
            _parent.childValueChanged(this);

        if (!_inBatchMode)
            fireValueChanged();

        afterSetValue(value);
    }

    public Object getValue()
    {
        return _value;
    }

    /**
     * Starting a batch stops the changes to this model value being propagated to listeners.
     * 
     * @see #commitBatch()
     */
    public void startBatch()
    {
        _inBatchMode = true;
    }

    /**
     * If in batch mode, this method makes the value in this model permanent, notifies all listeners and ends the batch.
     */
    public void commitBatch()
    {
        if (!_inBatchMode)
            return;

        fireValueChanged();
        _inBatchMode = false;
    }

    /**
     * This method could be used to initialize unbound models. This is done before setting the bean model's value so the
     * property models could use these values to initialize their state.
     */
    protected void beforeSetValue(Object value)
    {
    }

    /**
     * This method could be used to perform one time initialization of bound property models. This method is called
     * after the value is set and the listeners are notified.
     */
    protected void afterSetValue(Object value)
    {
    }

    private void fireValueChanged()
    {
        beforeNotifyChangeListeners();

        if (_changeListeners != null)
            notifyChangeListeners();

        afterNotifyChangeListeners();
    }

    private void notifyChangeListeners()
    {
        // Notify model change listeners
        for (Iterator itr = _changeListeners.iterator(); itr.hasNext();)
            ((ValueChangeListener) itr.next()).valueChanged(this);
    }

    /**
     * Hook method that will be called immediately after this model's value is set but before notifying any registered
     * change listeners.
     * <p>
     * For example, see {@link InputModel}.
     */
    protected void beforeNotifyChangeListeners()
    {
    }

    /**
     * Hook method that will be called immediately after notifying all registered change listeners.
     */
    protected void afterNotifyChangeListeners()
    {
    }

    public void registerChangeListener(ValueChangeListener listener)
    {
        if (_changeListeners == null)
            _changeListeners = new HashSet();

        _changeListeners.add(listener);
    }

    public void removeChangeListener(ValueChangeListener listener)
    {
        if (_changeListeners == null)
            return;

        _changeListeners.remove(listener);
    }
}
