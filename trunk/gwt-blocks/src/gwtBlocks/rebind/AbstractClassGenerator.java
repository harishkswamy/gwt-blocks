package gwtBlocks.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author hkrishna
 */
public abstract class AbstractClassGenerator extends Generator
{
    protected TreeLogger     _logger;
    protected SourceWriter   _sourceWriter;
    protected JClassType     _genClass;

    private GeneratorContext _context;
    private Configuration    _freeMarkerCfg;

    public String generate(TreeLogger logger, GeneratorContext context, String genClassName)
        throws UnableToCompleteException
    {
        _logger = logger;
        _context = context;

        try
        {
            _genClass = getType(genClassName);

            String packageName = _genClass.getPackage().getName();
            String proxyClassName = _genClass.getSimpleSourceName() + "_Proxy";
            String proxyClassFullName = packageName + "." + proxyClassName;

            _sourceWriter = getSourceWriter(packageName, genClassName, proxyClassName);

            if (_sourceWriter != null)
            {
                initFreeMarker();
                generateSource(packageName, proxyClassName);
                _sourceWriter.commit(logger);
            }

            return proxyClassFullName;
        }
        catch (UnableToCompleteException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.log(TreeLogger.ERROR, "Unable to generate source due to unexpected error.", e);
            throw new UnableToCompleteException();
        }
    }

    protected JClassType getType(String typeName) throws UnableToCompleteException
    {
        try
        {
            return _context.getTypeOracle().getType(typeName);
        }
        catch (NotFoundException e)
        {
            _logger.log(TreeLogger.ERROR, "Class not found. Check if " + typeName + " is available in the class path.",
                e);
            throw new UnableToCompleteException();
        }
    }

    private SourceWriter getSourceWriter(String packageName, String genClassName, String proxyClassName)
    {
        PrintWriter printWriter = _context.tryCreate(_logger, packageName, proxyClassName);

        if (printWriter == null)
            return null;

        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, proxyClassName);
        
        if (_genClass.isInterface() != null)
            composerFactory.addImplementedInterface(genClassName);
        else
            composerFactory.setSuperclass(genClassName);

        addImports(composerFactory);

        return composerFactory.createSourceWriter(_context, printWriter);
    }

    protected void initFreeMarker() throws Exception
    {
        _freeMarkerCfg = new Configuration();
        _freeMarkerCfg.setClassForTemplateLoading(getClass(), "");
        _freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    protected Template getTemplate(String name) throws Exception
    {
        return _freeMarkerCfg.getTemplate(name);
    }

    protected String getMetaData(String tagName, int occurance, int field) throws UnableToCompleteException
    {
        try
        {
            return _genClass.getMetaData(tagName)[occurance][field];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            _logger.log(TreeLogger.ERROR, "Missing meta tag. Check if you specified the javadoc tag " + tagName, e);
            throw new UnableToCompleteException();
        }
    }

    protected abstract void addImports(ClassSourceFileComposerFactory composerFactory);

    protected abstract void generateSource(String packageName, String genClassName) throws Exception;
}
