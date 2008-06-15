  public PropertyBindingModel<${propertyTypeName}> ${propertyModelGetterName}()
  {
      PropertyBindingModel<${propertyTypeName}> propModel = getChild("${propertyPath}");

      if (propModel == null)
      {
          propModel = new PropertyBindingModel<${propertyTypeName}>("${propertyPath}", this)
          {
              @Override
              protected void setPropertyValue()
              {
                  this.<${domainModelTypeName}>getParent().getValue().${propertyGetterPath}set${propertyName}(getValue());
              }

              @Override
              protected ${propertyTypeName} getPropertyValue()
              {
                  return this.<${domainModelTypeName}>getParent().getValue().${propertyGetterPath}get${propertyName}();
              }
          };
      }

      return propModel;
  }
