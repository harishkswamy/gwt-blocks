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

/**
 * @author hkrishna
 */
public class InputModel<T> extends ValidatableModel<T>
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
    public InputModel(String key, CompositeModel<?> parent)
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
    @SuppressWarnings("unchecked")
    public void setText(String text)
    {
        try
        {
            T value = null;

            // Convert
            if (_converter != null)
            {
                getMessageModel().clear(this);
                value = _converter.getValue(text);
            }
            else
                value = (T) text; // Assuming T is String

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
        T value = getValue();

        if (_converter == null)
            return value == null ? "" : value.toString();

        return _converter.getString(value);
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
