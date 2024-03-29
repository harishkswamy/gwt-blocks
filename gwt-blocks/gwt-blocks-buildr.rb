# Copyright 2008 Harish Krishnaswamy
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
module GwtBlocksProject
  include Extension

  GROUP   = 'com.google.code'
  NAME    = 'gwt-blocks'
  VERSION = '2.0.0'
  
  GWT_BLOCKS_CLIENT_LIB = "#{GROUP}:#{NAME}:jar:client:#{VERSION}"
  GWT_BLOCKS_SERVER_LIB = "#{GROUP}:#{NAME}:jar:server:#{VERSION}"

  GWT_USER    = 'com.google.gwt:gwt-user:jar:1.5.0'
  GWT_WINDOWS = 'com.google.gwt:gwt-dev:jar:windows:1.5.0'
  GWT_SERVLET = 'com.google.gwt:gwt-servlet:jar:1.5.0'

  first_time do
    Buildr.repositories.remote << 'http://repo1.maven.org/maven2'
    Buildr.repositories.remote << 'http://mirrors.ibiblio.org/pub/mirrors/maven2'
  end
  
  def source_deps
    return @source_deps if defined? @source_deps
    @source_deps = ['org.slf4j:slf4j-api:jar:1.5.0']
    @source_deps << Buildr.group( %w{ logback-core  logback-classic }, :under => 'ch.qos.logback', :version => '0.9.9' )
  end
  
  def test_deps
    return @test_deps if defined? @test_deps
    @test_deps = %w{ cglib:cglib-nodep:jar:2.1_3  org.objenesis:objenesis:jar:1.0 }
    @test_deps << Buildr.group( %w{ jmock  jmock-legacy }, :under => 'org.jmock', :version => '2.4.0' )
    @test_deps << Buildr.group( %w{ hamcrest-core  hamcrest-library }, :under => 'org.hamcrest', :version => '1.1' )
    @test_deps << runtime_deps
  end

  def runtime_deps
    return @runtime_deps if defined? @runtime_deps
    @runtime_deps = [source_deps]
  end
  
  def client_deps
    return @client_deps if defined? @client_deps
    @client_deps = [GWT_USER, GWT_WINDOWS]
    @client_deps << GWT_BLOCKS_CLIENT_LIB if project.name != NAME
    @client_deps
  end

  def init_java_project(options={})
	  project.group = options[:group] || GROUP
	  project.version = options[:version] || VERSION

    yield if block_given?
  end
  
	def init_gwt_project(options={})
	  init_java_project options

    artifacts artifact(GWT_USER).from(Buildr.settings.user['gwt_user'])
    artifacts artifact(GWT_WINDOWS).from(Buildr.settings.user['gwt_windows'])
    artifacts artifact(GWT_SERVLET).from(Buildr.settings.user['gwt_servlet'])

    source_deps << client_deps
    source_deps << [GWT_SERVLET]
    source_deps << GWT_BLOCKS_SERVER_LIB if project.name != NAME

    yield if block_given?
	end

	def build_java_project(options={})
	  build_java options

    package :jar

    yield if block_given?
	end
  
  def build_gwt_module(options={})
    build_java options
    
    client_jar = package(:jar, :classifier => 'client').clean
    client_jar.include('target/classes', :as => '.').exclude('target/classes/**/server/**/*')
    client_jar.include('target/resources', :as => '.').exclude('target/resources/**/server/**/*')
    client_jar.include('src/main/java/*').exclude('src/main/java/**/server/**/*')
    
    server_jar = package(:jar, :classifier => 'server').clean
    server_jar.include('target/classes', :as => '.').exclude(*client_classes)
    server_jar.include('target/resources', :as => '.').exclude(*client_resources)
    
    yield if block_given?
  end
  
  def build_gwt_project(module_name, options={})
    build_java options

    build do
      Java::Commands.java 'com.google.gwt.dev.GWTCompiler', '-out', 'target/gwt/out', '-gen', 'target/gwt/gen', module_name,
        :java_args => ['-Xmx512M'],
        :classpath => [FileList['src/main/java', 'src/main/resources', 'target/classes'], source_deps], :verbose => true
    end

    war = package(:war).clean
    war.include("target/gwt/out/#{module_name}", :as => '.')
    war.include('src/main/webapp/*')
    war.path('WEB-INF/classes').tap do |path|
      path.include('target/classes', :as => '.').exclude(*client_classes)
      path.include('target/resources', :as => '.').exclude(*client_resources)
    end
    war.path('WEB-INF/lib').include(artifacts(source_deps)).exclude(artifacts(client_deps))

    task :deploy => ['package'] do
      FileUtils.rm_rf('target/temp')
      unzip('target/temp' => war).extract
      FileUtils.mkdir_p('target/webapp')
      FileUtils.rm_rf Dir.glob('target/webapp/*')
      FileUtils.cp_r('target/temp/.', 'target/webapp')
    end

    yield if block_given?
  end

private
  def build_java(options)
	  compile.options.source = options[:java_version] || '1.5'
	  compile.options.target = options[:java_version] || '1.5'

    compile.with(source_deps)
	  test.with(test_deps)
  end

  def client_classes
    ['**/client/**/*', '**/generators/**/*']
  end

  def client_resources
    ['**/public/**/*', '**/*.gwt.xml']
  end
end

class Buildr::Project
  include GwtBlocksProject
end
