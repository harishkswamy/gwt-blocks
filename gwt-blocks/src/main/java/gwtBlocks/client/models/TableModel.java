package gwtBlocks.client.models;

import java.util.List;

/**
 * @author hkrishna
 */
public abstract class TableModel<V> extends CompositeModel<List<V>>
{
    public TableModel()
    {
    }

    public TableModel(String name, CompositeModel<?> parent)
    {
        setParent(name, parent);
    }
}
