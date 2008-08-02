package gwtBlocks.client.views;

import gwtBlocks.client.models.InputModel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author hkrishna
 */
public class RadioButtonView<M extends InputModel<V>, V> extends BaseView<RadioButton, M>
{
    private V _value;

    protected RadioButtonView(M model, String label, V value, String group)
    {
        super(model, label, value, group);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RadioButton buildView(M model, Object... args)
    {
        String label = (String) args[0];
        _value = (V) args[1];
        String group = (String) args[2];

        final RadioButton rb = new RadioButton(group, label);
        rb.addClickListener(new ClickListener()
        {
            public void onClick(Widget sender)
            {
                if (rb.isChecked())
                    getModel().setValue(_value);
            }
        });

        return rb;
    }

    public void valueChanged(M model)
    {
        if (_value != null && model.getValue() != null && _value.equals(model.getValue()))
            getWidget().setChecked(true);
        else
            getWidget().setChecked(false);
    }
}
