module GwtBlocksProject
  include Extension

  GROUP   = 'com.google.code'
  NAME    = 'gwt-blocks'
  VERSION = '1.0.0'
  
  ORACLE      = 'com.oracle:ojdbc14:jar:10.2.0.4'
  GWT_USER    = 'com.google.gwt:gwt-user:jar:1.4.10'
  GWT_WINDOWS = 'com.google.gwt:gwt-dev:jar:windows:1.4.10'
  GWT_SERVLET = 'com.google.gwt:gwt-servlet:jar:1.4.10'
  FREEMARKER  = 'org.freemarker:freemarker:jar:2.3.10'

  first_time do
    Buildr.repositories.remote << 'http://repo1.maven.org/maven2'
    Buildr.repositories.remote << 'http://mirrors.ibiblio.org/pub/mirrors/maven2'
  end
  
  def local_dev?
    Buildr.environment == 'local-dev'
  end
  
  def source_deps
    return @source_deps if defined? @source_deps
    @source_deps = ['org.slf4j:slf4j-api:jar:1.5.0']
    @source_deps << Buildr.group( %w{ logback-core  logback-classic }, :under => 'ch.qos.logback', :version => '0.9.9' )
  end
  
  def test_deps
    return @test_deps if defined? @test_deps
    @test_deps = %w{ org.objenesis:objenesis:jar:1.0 }
    @test_deps << Buildr.group( %w{ jmock  jmock-legacy }, :under => 'org.jmock', :version => '2.4.0' )
    @test_deps << Buildr.group( %w{ hamcrest-core  hamcrest-library }, :under => 'org.hamcrest', :version => '1.1' )
    @test_deps << runtime_deps
  end

  def runtime_deps
    return @runtime_deps if defined? @runtime_deps
    @runtime_deps = [source_deps, local_dev? ? 'hsqldb:hsqldb:jar:1.8.0.7' : ORACLE]
  end
  
  def client_deps
    return @client_deps if defined? @client_deps
    @client_deps = [GWT_USER, GWT_WINDOWS]
    @client_deps
  end

	#url = "https://profile.oracle.com/jsp/reg/loginHandler.jsp?username=harishkswamy&password=XXX&redirectUrl=http://download.oracle.com/otn/utilities_drivers/jdbc/10204/ojdbc14.jar"
	#install download(artifact(ORACLE)=>url)
	
	def init_project(options={})
	  project.group = options[:group] || GROUP
	  project.version = options[:version] || VERSION
    
    artifacts artifact(ORACLE).from(Buildr.settings.user['oracle_jar']) if !local_dev?
    artifacts artifact(GWT_USER).from(Buildr.settings.user['gwt_user'])
    artifacts artifact(GWT_WINDOWS).from(Buildr.settings.user['gwt_windows'])
    artifacts artifact(GWT_SERVLET).from(Buildr.settings.user['gwt_servlet'])

    source_deps << client_deps
    source_deps << [GWT_SERVLET, FREEMARKER]

    yield if block_given?
	end

	def build_project(options={})
	  compile.options.target = options[:java_version] || '1.5'

    compile.with(source_deps)
	  test.with(test_deps)

    yield if block_given?
	end
  
  def build_gwt_project(module_name, options={})

    build_project options

    build do
      Java::Commands.java 'com.google.gwt.dev.GWTCompiler', '-out', 'target/gwt/out', '-gen', 'target/gwt/gen', module_name,
        :java_args => ['-Xmx512M'],
        :classpath => [FileList['src/main/java', 'src/main/resources', 'target/classes'], source_deps], :verbose => true
    end

    war = package(:war).clean
    war.include("target/gwt/out/#{module_name}", :as => '.')
    war.include('src/main/webapp/*')
    war.path('WEB-INF/classes').tap do |path|
      path.include('target/classes', :as => '.').exclude('**/client/*')
      path.include('target/resources', :as => '.').exclude('**/public/**/*')
    end
    war.path('WEB-INF/lib').include(artifacts(source_deps)).exclude(artifacts(client_deps))

    task :deploy => ['package'] do
      unzip('target/webapp' => war).extract
    end

  end

end

class Buildr::Project
  include GwtBlocksProject
end