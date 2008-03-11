package gwtBlocks.client.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Composes {@link ComposableModel}s into a single {@link ValueModel}.
 * 
 * @author hkrishna
 */
public class CompositeModel extends ValidatableModel
{
    /**
     * An immutable {@link Iterator} that iterates over the children.
     */
    public static class ChildIterator implements Iterator
    {
        private Iterator _itr;

        private ChildIterator(Map children)
        {
            _itr = children.values().iterator();
        }

        public boolean hasNext()
        {
            return _itr.hasNext();
        }

        public Object next()
        {
            return _itr.next();
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove child from here.");
        }
    }

    private Map _children;

    /**
     * Adds the provided child to this model if it is not already present and sets this model as the parent in the
     * provided child. If this model already contains the provided child, this method does nothing.
     * 
     * @param key
     *            The key to identify the provided child in the children map.
     * @param child
     *            Child to be added.
     */
    public void addChild(String key, ComposableModel child)
    {
        if (child == null)
            return;

        Map children = getOrCreateChildren();

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

        ComposableModel child = (ComposableModel) _children.remove(key);

        if (child != null)
            child.setParent(key, null);
    }

    /**
     * @param key
     *            The key that identifies the requested child in the children map.
     * @return The child identified by the provided key or null if the key is not present in the children map.
     */
    public ComposableModel getChild(String key)
    {
        if (_children == null)
            return null;

        return (ComposableModel) _children.get(key);
    }

    private Map getOrCreateChildren()
    {
        if (_children == null)
            _children = new HashMap();

        return _children;
    }

    /**
     * @return An {@link Iterator} that iterates over this model's children. The returned iterator disallows mutations.
     */
    public Iterator getChildIterator()
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
    public void childValueChanged(ComposableModel child)
    {
    }

    protected void beforeNotifyChangeListeners()
    {
        if (_children == null)
            return;

        for (Iterator itr = _children.values().iterator(); itr.hasNext();)
            ((ComposableModel) itr.next()).parentValueChanged();
    }
}
