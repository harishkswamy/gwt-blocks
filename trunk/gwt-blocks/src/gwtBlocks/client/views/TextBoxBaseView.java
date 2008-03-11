package gwtBlocks.client.views;

import gwtBlocks.client.models.InputModel;
import gwtBlocks.client.models.ValueModel;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author hkrishna
 */
public class TextBoxBaseView extends BaseView implements InputView
{
    private TextBoxBase _textWidget;

    public TextBoxBaseView(TextBoxBase textWidget, InputModel model)
    {
        _textWidget = textWidget;
        _textWidget.addChangeListener(new ChangeListener()
        {
            public void onChange(Widget sender)
            {
                ((InputModel) getModel()).setText(_textWidget.getText());
            }
        });

        initWidget(_textWidget);
        setModel(model);
    }

    protected TextBoxBase getTextWidget()
    {
        return _textWidget;
    }

    public void valueChanged(ValueModel model)
    {
        _textWidget.setText(((InputModel) model).getText());
    }

    public void setEnabled(boolean flag)
    {
        _textWidget.setEnabled(flag);
    }
}
