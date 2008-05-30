package gwtBlocks.rebind;

import freemarker.template.Template;
import gwtBlocks.client.Converters;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

public class BindingModelGenerator extends AbstractClassGenerator
{
    private Template _propertyModelTemplate;

    protected void addImports(ClassSourceFileComposerFactory composerFactory)
    {
        composerFactory.addImport("gwtBlocks.client.Converters");
        composerFactory.addImport("gwtBlocks.client.ValidationException");
        composerFactory.addImport("gwtBlocks.client.models.BeanBindingModel");
        composerFactory.addImport("gwtBlocks.client.models.MessageModel");
        composerFactory.addImport("gwtBlocks.client.models.PropertyBindingModel");
        composerFactory.addImport("gwtBlocks.client.models.ValueModel");
    }

    protected void generateSource(String packageName, String genClassName) throws Exception
    {
        _propertyModelTemplate = getTemplate("propertyModel.ftl");

        String domainTypeName = getMetaData("gwt-blocks.domain.class-name", 0, 0);

        JClassType domainClass = getType(domainTypeName);
        List propertyGetters = getPropertyGetters(_genClass, domainClass);

        for (Iterator itr = propertyGetters.iterator(); itr.hasNext();)
        {
            Object[] getterProps = (Object[]) itr.next();
            JMethod getter = (JMethod) getterProps[0];
            String propertyPath = (String) getterProps[1];
            String propModelGetterName = (String) getterProps[2];
            generatePropertyBindingSource(getter, propertyPath, propModelGetterName, domainTypeName);
        }
    }

    private void generatePropertyBindingSource(JMethod domainGetter, String propertyPath, String propModelGetterName,
        String domainTypeName) throws Exception
    {
        Map root = new HashMap();

        String propertyName = domainGetter.getName().replaceFirst("get", "");
        String propertyTypeName = domainGetter.getReturnType().getQualifiedSourceName();
        root.put("propertyModelGetterName", propModelGetterName);
        root.put("propertyPath", propertyPath);
        root.put("propertyName", propertyName);
        root.put("propertyTypeName", propertyTypeName);
        root.put("domainTypeName", domainTypeName);
        root.put("converterName", getConverterName(propertyTypeName));

        StringWriter out = new StringWriter();
        _propertyModelTemplate.process(root, out);
        out.flush();

        _sourceWriter.println(out.toString());

        out.close();
    }

    /**
     * @param modelClass
     * @param domainClass
     * @return A list of Object arrays. Each Object array is of size 3 and contains the following information.
     *         <ol>
     *         <li>JMethod of the actual getter method in the domain object
     *         <li>Property path getter string terminated by a period, for ex. "getClient().getRegion()."
     *         <li>Property model getter name as defined in the BeanBindingModel, for ex. getFieldIdModel
     *         </ol>
     * @throws UnableToCompleteException
     */
    private List getPropertyGetters(JClassType modelClass, JClassType domainClass) throws UnableToCompleteException
    {
        List getters = new ArrayList();
        JMethod[] methods = modelClass.getMethods();
        JType[] nullArg = new JType[0];

        for (int i = 0; i < methods.length; i++)
        {
            if (methods[i].isAbstract()
                && methods[i].getReturnType().getSimpleSourceName().equals("PropertyBindingModel"))
            {
                String propertyPath = getPropertyPath(methods[i]);
                JMethod getterMethod = null;

                if (propertyPath == null)
                {
                    String getter = methods[i].getName().substring(0, methods[i].getName().lastIndexOf("Model"));
                    getterMethod = getMethod(domainClass, getter, nullArg);
                    propertyPath = "";
                }
                else
                {
                    String[] props = propertyPath.split("\\.");
                    propertyPath = "";
                    JClassType propClass = domainClass;

                    for (int j = 0; j < props.length; j++)
                    {
                        String getter = "get" + props[j].substring(0, 1).toUpperCase() + props[j].substring(1);

                        if (j < props.length - 1)
                            propertyPath += getter + "().";

                        getterMethod = getMethod(propClass, getter, nullArg);
                        propClass = getterMethod.getReturnType().isClassOrInterface();
                    }
                }

                getters.add(new Object[] { getterMethod, propertyPath, methods[i].getName() });
            }
        }

        return getters;
    }

    private String getPropertyPath(JMethod method)
    {
        String[][] metaData = method.getMetaData("gwt-blocks.domain.property-path");

        if (metaData.length > 0)
            return metaData[0][0];

        return null;
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

    private String getConverterName(String type)
    {
        String converterName = (String) Converters.getConverterNameMap().get(type);
        return converterName == null ? "null" : converterName;
    }
}
