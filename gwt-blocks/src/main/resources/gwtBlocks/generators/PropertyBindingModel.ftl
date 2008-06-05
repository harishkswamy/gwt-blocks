  public PropertyBindingModel<${propertyTypeName}> ${propertyModelGetterName}()
  {
      PropertyBindingModel<${propertyTypeName}> propModel = getChild("${propertyPath}");

      if (propModel == null)
      {
          propModel = new PropertyBindingModel<${propertyTypeName}>("${propertyPath}", this, ${textConverter})
          {
              protected void setPropertyValue()
              {
                  ${domainModelTypeName} parent = getParent();
                  parent.getValue().${propertyGetterPath}set${propertyName}(getValue());
              }

              protected ${propertyTypeName} getPropertyValue()
              {
                  ${domainModelTypeName} parent = getParent();
                  return parent.getValue().${propertyGetterPath}get${propertyName}();
              }
          };
      }

      return propModel;
  }
