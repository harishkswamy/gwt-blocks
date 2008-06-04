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
package gwtBlocks.generators;

import gwtBlocks.client.Converters;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class BeanBindingModelGenerator extends AbstractClassGenerator
{
    private static class PropertyBindingModelGenerator
    {
        private static final StringBuilder _propertyModelTemplate = new StringBuilder();

        private JClassType                 _genClass;
        private SourceWriter               _sourceWriter;

        PropertyBindingModelGenerator(JClassType genClass, SourceWriter sourceWriter) throws Exception
        {
            _genClass = genClass;
            _sourceWriter = sourceWriter;

            if (_propertyModelTemplate.length() == 0)
                loadTemplate();
        }

        private void loadTemplate() throws Exception
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "PropertyBindingModel.ftl")));

            try
            {
                String line = null;

                while ((line = reader.readLine()) != null)
                    _propertyModelTemplate.append(line).append('\n');
            }
            finally
            {
                reader.close();
            }
        }

        void generate(JMethod domainGetter, String propModelGetterName, String propPath, String propGetterPath)
        {
            String propName = domainGetter.getName().replaceFirst("get", "");
            String propTypeName = domainGetter.getReturnType().getQualifiedSourceName();

            String template = _propertyModelTemplate.toString();
            template = template.replaceAll("\\$\\{propertyTypeName\\}", propTypeName);
            template = template.replaceAll("\\$\\{propertyModelGetterName\\}", propModelGetterName);
            template = template.replaceAll("\\$\\{propertyPath\\}", propPath == null ? propName : propPath);
            template = template.replaceAll("\\$\\{propertyName\\}", propName);
            template = template.replaceAll("\\$\\{converterName\\}", getConverterName(propTypeName));
            template = template.replaceAll("\\$\\{domainModelTypeName\\}", _genClass.getName());
            template = template.replaceAll("\\$\\{propertyGetterPath\\}", propGetterPath);

            _sourceWriter.println(template);
        }

        private String getConverterName(String type)
        {
            String converterName = (String) Converters.getConverterNameMap().get(type);
            return converterName == null ? "null" : converterName;
        }
    }

    protected void addImports(ClassSourceFileComposerFactory composerFactory)
    {
        composerFactory.addImport("gwtBlocks.client.models.PropertyBindingModel");
    }

    protected void generateSource(String packageName, String genClassName) throws Exception
    {
        String domainTypeName = _genClass.getAnnotation(BindingClass.class).value().getName();

        JClassType domainClass = getType(domainTypeName);

        generatePropertyModels(_genClass, domainClass, new PropertyBindingModelGenerator(_genClass, _sourceWriter));
    }

    private void generatePropertyModels(JClassType modelClass, JClassType domainClass,
        PropertyBindingModelGenerator propGenerator) throws UnableToCompleteException
    {
        JMethod[] methods = modelClass.getMethods();
        JType[] nullArg = new JType[0];

        for (int i = 0; i < methods.length; i++)
        {
            if (methods[i].isAbstract()
                && methods[i].getReturnType().getSimpleSourceName().equals("PropertyBindingModel"))
            {
                String propertyPath = getPropertyPath(methods[i]), propGetterPath = "";
                JMethod getterMethod = null;

                if (propertyPath == null)
                {
                    String getter = methods[i].getName().substring(0, methods[i].getName().lastIndexOf("Model"));
                    getterMethod = getMethod(domainClass, getter, nullArg);
                }
                else
                {
                    String[] props = propertyPath.split("\\.");
                    JClassType propClass = domainClass;

                    for (int j = 0; j < props.length; j++)
                    {
                        String getter = "get" + props[j].substring(0, 1).toUpperCase() + props[j].substring(1);

                        if (j < props.length - 1)
                            propGetterPath += getter + "().";

                        getterMethod = getMethod(propClass, getter, nullArg);
                        propClass = getterMethod.getReturnType().isClassOrInterface();
                    }
                }

                propGenerator.generate(getterMethod, methods[i].getName(), propertyPath, propGetterPath);
            }
        }
    }

    private String getPropertyPath(JMethod method)
    {
        BindingProperty annotation = method.getAnnotation(BindingProperty.class);

        return annotation == null ? null : annotation.value();
    }

    private JMethod getMethod(JClassType classType, String methodName, JType[] args) throws UnableToCompleteException
    {
        try
        {
            return classType.getMethod(methodName, args);
        }
        catch (NotFoundException e)
        {
            if (classType.getSuperclass() != null)
                return getMethod(classType.getSuperclass(), methodName, args);
            else
            {
                _logger.log(TreeLogger.ERROR, "Method not found. Unable to find method " + methodName + " in "
                    + classType + ". Check if property models conform to the naming conventions.", e);
                throw new UnableToCompleteException();
            }
        }
    }
}
