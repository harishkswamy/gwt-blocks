package gwtBlocks.client.models;

import gwtBlocks.client.ValidationException;

public interface Validator
{
    void validate(ValidatableModel value) throws ValidationException;
}
