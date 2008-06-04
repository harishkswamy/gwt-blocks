// Copyright 2008 Harish Krishnaswamy
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package gwtBlocks.client.models;

import gwtBlocks.client.ValueChangeListener;

import java.util.HashSet;
import java.util.Set;

/**
 * This is the base implementation of {@link ComposableModel} of the MVC framework.
 * <p>
 * The specific features of this model are its ability to let sub classes hook behavior before and after notifying
 * listeners and its ability to batch multiple changes to its value and only notify the listeners when the batch is
 * committed.
 * 
 * @author hkrishna
 */
public class BaseModel<V> implements ComposableModel<V>
{
    private V                                                      _value;
    private Set<ValueChangeListener<? extends ComposableModel<V>>> _changeListeners;
    private boolean                                                _inBatchMode;
    private CompositeModel<?>                                      _parent;

    public void setParent(String key, CompositeModel<?> parent)
    {
        if (parent == _parent)
            return;

        if (_parent != null)
            _parent.removeChild(key);

        _parent = parent;

        if (_parent != null)
            _parent.addChild(key, this);
    }

    @SuppressWarnings("unchecked")
    public <P extends CompositeModel<?>> P getParent()
    {
        return (P) _parent;
    }

    public void parentValueChanged()
    {
    }

    /**
     * Sets this model's value and notifies registered change listeners. Sub classes can implement
     * {@link #beforeNotifyChangeListeners()} and {@link #afterNotifyChangeListeners()} methods to be invoked before and
     * after notifying change listeners.
     * <p>
     * If in batch mode this method will not notify listeners until {@link #commitBatch()} is called, neither will it
     * invoke the {@link #beforeNotifyChangeListeners()} or {@link #afterNotifyChangeListeners()} hooks.
     * 
     * @param value
     *            The new value of the model.
     * @see #commitBatch()
     */
    public void setValue(V value)
    {
        _value = value;

        if (_parent != null)
            _parent.childValueChanged(this);

        if (!_inBatchMode)
            fireValueChanged();
    }

    private void fireValueChanged()
    {
        beforeNotifyChangeListeners();

        if (_changeListeners != null)
            notifyChangeListeners();

        afterNotifyChangeListeners();
    }

    @SuppressWarnings("unchecked")
    private void notifyChangeListeners()
    {
        // Notify model change listeners
        for (ValueChangeListener listener : _changeListeners)
            listener.valueChanged(this);
    }

    /**
     * Hook method that will be called immediately after this model's value is set but before notifying any registered
     * change listeners. This method will not be invoked when in batch mode until {@link #commitBatch()} is called.
     */
    protected void beforeNotifyChangeListeners()
    {
    }

    /**
     * Hook method that will be called immediately after notifying all registered change listeners. This method will not
     * be invoked when in batch mode until {@link #commitBatch()} is called.
     */
    protected void afterNotifyChangeListeners()
    {
    }

    public V getValue()
    {
        return _value;
    }

    /**
     * Starting a batch stops the changes to this model value from being propagated to listeners.
     * 
     * @see #commitBatch()
     */
    public void startBatch()
    {
        _inBatchMode = true;
    }

    /**
     * If in batch mode, this method notifies all listeners and ends the batch.
     */
    public void commitBatch()
    {
        if (!_inBatchMode)
            return;

        try
        {
            fireValueChanged();
        }
        finally
        {
            _inBatchMode = false;
        }
    }

    public void registerChangeListener(ValueChangeListener<? extends ComposableModel<V>> listener)
    {
        if (_changeListeners == null)
            _changeListeners = new HashSet<ValueChangeListener<? extends ComposableModel<V>>>();

        _changeListeners.add(listener);
    }

    public void removeChangeListener(ValueChangeListener<? extends ComposableModel<V>> listener)
    {
        if (_changeListeners == null)
            return;

        _changeListeners.remove(listener);
    }
}
