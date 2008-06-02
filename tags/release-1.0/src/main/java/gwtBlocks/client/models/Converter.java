package gwtBlocks.client.models;

import gwtBlocks.client.ConvertionException;

public interface Converter
{
    Object getValue(String value) throws ConvertionException;

    String getString(Object value);
}
