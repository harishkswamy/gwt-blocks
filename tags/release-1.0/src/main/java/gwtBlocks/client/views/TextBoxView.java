package gwtBlocks.client.views;

import gwtBlocks.client.models.InputModel;

import com.google.gwt.user.client.ui.TextBox;

/**
 * @author hkrishna
 */
public class TextBoxView extends TextBoxBaseView
{
    private static TextBox buildTextBox()
    {
        TextBox textBox = new TextBox();
        textBox.addStyleName("cvg-text-box");

        return textBox;
    }

    TextBoxView(InputModel model)
    {
        super(buildTextBox(), model);
    }

    public TextBox getTextBox()
    {
        return (TextBox) getTextWidget();
    }
}
