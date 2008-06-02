package gwtBlocks.client.views;

import gwtBlocks.client.models.InputModel;

/**
 * @author hkrishna
 */
public class ViewFactory
{
    public TextBoxView newStringBox(InputModel model, String name, String styleName)
    {
        model.setName(name);

        TextBoxView stringBox = new TextBoxView(model);

        return stringBox;
    }
}
