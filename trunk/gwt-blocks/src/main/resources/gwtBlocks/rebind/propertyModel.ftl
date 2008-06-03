  public PropertyBindingModel<${propertyTypeName}> ${propertyModelGetterName}()
  {
      PropertyBindingModel<${propertyTypeName}> propModel = getChild("${propertyName}");

      if (propModel == null)
      {
          propModel = new PropertyBindingModel<${propertyTypeName}>("${propertyName}", this, ${converterName})
          {
              protected void setPropertyValue()
              {
                  ${domainModelTypeName} parent = getParent();
                  parent.getValue().${propertyPath}set${propertyName}(getValue());
              }

              protected ${propertyTypeName} getPropertyValue()
              {
                  ${domainModelTypeName} parent = getParent();
                  return parent.getValue().${propertyPath}get${propertyName}();
              }
          };
      }

      return propModel;
  }
