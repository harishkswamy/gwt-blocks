    public PropertyBindingModel ${propertyModelGetterName}()
    {
        PropertyBindingModel propModel = (PropertyBindingModel) getChild("${propertyName}");

        if (propModel == null)
        {
            propModel = new PropertyBindingModel("${propertyName}", this, ${converterName})
                {
                    protected void setPropertyValue()
                    {
                        ((${domainTypeName}) getParent().getValue()).${propertyPath}set${propertyName}((${propertyTypeName}) getValue());
                    }

                    protected Object getPropertyValue()
                    {
                    	return ((${domainTypeName}) getParent().getValue()).${propertyPath}get${propertyName}();
                    }
                };
        }

        return propModel;
    }
