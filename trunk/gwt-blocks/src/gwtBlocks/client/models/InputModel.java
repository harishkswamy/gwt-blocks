package gwtBlocks.client.models;

import gwtBlocks.client.ConvertionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author hkrishna
 */
public class InputModel extends ValidatableModel
{
    private Converter _converter;

    /**
     * Instatiates and registers itself as a child in the provided parent.
     * 
     * @param key
     *            The key that identifies this model in the parent.
     * @param parent
     *            The parent model.
     */
    public InputModel(String key, CompositeModel parent)
    {
        setParent(key, parent);
    }

    /**
     * Intended to be invoked by the form widget to set the widget's text value. This method will convert the text
     * value, validate it and invoke {@link ValueModel#setValue(Object)}.
     * 
     * @param text
     *            The text value from the widget.
     */
    public void setText(String text)
    {
        try
        {
            Object value = text;

            // Convert
            if (_converter != null)
            {
                getMessageModel().clear(this);
                value = _converter.getValue(text);
            }

            // setValue
            setValue(value);
        }
        catch (ConvertionException e)
        {
            getMessageModel().addMessage(this, e.getMessage());
        }
    }

    /**
     * @return The formatted text to be displayed in the widget.
     */
    public String getText()
    {
        Object value = getValue();

        if (_converter == null)
            return value == null ? "" : value.toString();

        return _converter.getString(value);
    }

    /**
     * Intended to be invoked by the form widget to set the widget's text value. This method will convert the text
     * value, validate it and invoke {@link ValueModel#setValue(Object)}.
     * 
     * @param textList
     *            The text value from the list widget.
     */
    public void setTextList(List textList)
    {
        try
        {
            List value = textList;

            // Convert
            if (_converter != null)
            {
                getMessageModel().clear(this);

                if (textList != null)
                {
                    value = new ArrayList();

                    for (Iterator itr = textList.iterator(); itr.hasNext();)
                        value.add(_converter.getValue((String) itr.next()));
                }
            }

            // setValue
            setValue(value);
        }
        catch (ConvertionException e)
        {
            getMessageModel().addMessage(this, e.getMessage());
        }
    }

    /**
     * @return A list of formatted strings to be displayed in the widget.
     */
    public List getTextList()
    {
        List value = (List) getValue();

        Converter converter = getConverter();

        if (converter == null || value == null)
            return value;

        List list = new ArrayList();

        for (Iterator itr = value.iterator(); itr.hasNext();)
            list.add(converter.getString(itr.next()));

        return list;
    }

    /**
     * @param converter
     *            The {@link Converter} that performs type conversions going either directions, from the widget to the
     *            model and the other way around.
     */
    public void setConverter(Converter converter)
    {
        _converter = converter;
    }

    /**
     * @return Returns this model's {@link Converter}.
     */
    public Converter getConverter()
    {
        return _converter;
    }
}
