package gwtBlocks.client.models;

/**
 * @author hkrishna
 */
public interface ComposableModel
{
    void setParent(String key, CompositeModel parent);

    CompositeModel getParent();

    void parentValueChanged();
}
