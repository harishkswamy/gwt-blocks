package gwtBlocks.client.views;

import gwtBlocks.client.models.ValueChangeListener;
import gwtBlocks.client.models.ValueModel;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author hkrishna
 */
public abstract class BaseView extends Composite implements ValueChangeListener, View
{
    private ValueModel _model;
    
    public void setModel(ValueModel model)
    {
        if (_model != null)
            _model.removeChangeListener(this);

        _model = model;

        if (_model != null)
        {
            _model.registerChangeListener(this);
            valueChanged(_model);
        }
    }

    public ValueModel getModel()
    {
        return _model;
    }

    public void valueChanged(ValueModel model)
    {
    }
}
