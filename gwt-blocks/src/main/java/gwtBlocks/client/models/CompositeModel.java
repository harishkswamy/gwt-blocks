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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Composes {@link BaseModel}s into a single {@link BaseModel}.
 * 
 * @author hkrishna
 */
public class CompositeModel<V> extends ValidatableModel<V>
{
    /**
     * An immutable {@link Iterator} that iterates over the children.
     */
    private static class ChildIterator implements Iterator<BaseModel<?>>
    {
        private Iterator<BaseModel<?>> _itr;

        private ChildIterator(Map<String, BaseModel<?>> children)
        {
            _itr = children.values().iterator();
        }

        public boolean hasNext()
        {
            return _itr.hasNext();
        }

        public BaseModel<?> next()
        {
            return _itr.next();
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove child from here.");
        }
    }

    private Map<String, BaseModel<?>> _children;

    /**
     * Adds the provided child to this model if it is not already present and sets this model as the parent in the
     * provided child. If this model already contains the provided child, this method does nothing.
     * 
     * @param key
     *            The key to identify the provided child in the children map.
     * @param child
     *            Child to be added.
     */
    public void addChild(String key, BaseModel<?> child)
    {
        if (child == null)
            return;

        Map<String, BaseModel<?>> children = getOrCreateChildren();

        if (children.put(key, child) == null)
            child.setParent(key, this);
    }

    /**
     * Removes the provided child from this model if it is present and clears the parent of the provided child. If this
     * model does not contain the provided child, this method does nothing.
     * 
     * @param key
     *            The key that identifies the child to be removed in the children map.
     */
    public void removeChild(String key)
    {
        if (_children == null)
            return;

        BaseModel<?> child = _children.remove(key);

        if (child != null)
            child.setParent(key, null);
    }

    /**
     * @param key
     *            The key that identifies the requested child in the children map.
     * @return The child identified by the provided key or null if the key is not present in the children map.
     */
    @SuppressWarnings("unchecked")
    public <C extends BaseModel<?>> C getChild(String key)
    {
        if (_children == null)
            return null;

        return (C) _children.get(key);
    }

    /**
     * @return An {@link Iterator} that iterates over this model's children. The returned iterator disallows mutations.
     */
    public Iterator<BaseModel<?>> getChildIterator()
    {
        if (_children == null)
            return null;

        return new ChildIterator(_children);
    }

    /**
     * Listener to the child value change event.
     * 
     * @param child
     *            The child that is notifying of its value change.
     */
    public void childValueChanged(BaseModel<?> child)
    {
    }

    @Override
    protected void beforeNotifyChangeListeners()
    {
        super.beforeNotifyChangeListeners();

        if (_children == null)
            return;

        for (BaseModel<?> child : _children.values())
            child.parentValueChanged();
    }

    private Map<String, BaseModel<?>> getOrCreateChildren()
    {
        if (_children == null)
            _children = new HashMap<String, BaseModel<?>>();

        return _children;
    }
}
