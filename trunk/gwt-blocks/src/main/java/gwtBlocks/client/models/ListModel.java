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

import gwtBlocks.client.ConvertionException;
import gwtBlocks.client.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hkrishna
 */
public class ListModel<T> extends ValidatableModel<List<T>>
{
    private StringConverter<T> _converter;

    /**
     * Instatiates and registers itself as a child in the provided parent.
     * 
     * @param key
     *            The key that identifies this model in the parent.
     * @param parent
     *            The parent model.
     */
    public ListModel(String key, CompositeModel<?> parent)
    {
        setParent(key, parent);
    }

    /**
     * Intended to be invoked by the form widget to set the widget's text value. This method will convert the text
     * value, validate it and invoke {@link BaseModel#setValue(Object)}.
     * 
     * @param textList
     *            The text value from the list widget.
     */
    @SuppressWarnings("unchecked")
    public void setTextList(List<String> textList)
    {
        try
        {
            List<T> value = null;

            // Convert
            if (_converter != null)
            {
                clearMessages();

                if (textList != null)
                {
                    value = new ArrayList<T>();

                    for (String str : textList)
                        value.add(_converter.getValue(str));
                }
            }
            else
                value = (List<T>) textList; // Assuming T is String

            // setValue
            setValue(value);
        }
        catch (ConvertionException e)
        {
            addMessage(e.getMessage(), null);
        }
    }

    /**
     * @return A list of formatted strings to be displayed in the widget.
     */
    public List<String> getTextList()
    {
        List<T> value = getValue();

        if (value == null)
            return null;

        List<String> list = new ArrayList<String>();
        StringConverter<T> converter = getConverter();

        for (T val : value)
        {
            if (converter == null)
                list.add(val == null ? "" : val.toString());
            else
                list.add(converter.getString(val));
        }

        return list;
    }

    /**
     * @param converter
     *            The {@link StringConverter} that performs type conversions going either directions, from the widget to
     *            the model and the other way around.
     */
    public void setConverter(StringConverter<T> converter)
    {
        _converter = converter;
    }

    /**
     * @return Returns this model's {@link StringConverter}.
     */
    public StringConverter<T> getConverter()
    {
        return _converter;
    }
}
