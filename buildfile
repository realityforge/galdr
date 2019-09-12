require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/top_level_generate_dir'
require 'buildr/gwt'

desc 'galdr: An ECS implementation'
define 'galdr' do
  project.group = 'org.realityforge.galdr'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/galdr')
  pom.add_developer('realityforge', 'Peter Donald')

  desc 'galdr core library'
  define 'core' do
    compile.with :javax_annotation,
                 :braincheck

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.options[:java_args] = %w(-ea)
  end

  ipr.add_testng_configuration('core',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Dgaldr.environment=development -Dgaldr.check_diagnostic_messages=true -Dgaldr.output_fixture_data=true -Dgaldr.diagnostic_messages_file=core/src/test/java/galdr/diagnostic_messages.json')

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_component('JavaProjectCodeInsightSettings') do |xml|
    xml.tag!('excluded-names') do
      xml << '<name>com.sun.istack.internal.NotNull</name>'
      xml << '<name>com.sun.istack.internal.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.Nullable</name>'
      xml << '<name>org.jetbrains.annotations.NotNull</name>'
      xml << '<name>org.testng.AssertJUnit</name>'
    end
  end
  ipr.add_component('NullableNotNullManager') do |component|
    component.option :name => 'myDefaultNullable', :value => 'javax.annotation.Nullable'
    component.option :name => 'myDefaultNotNull', :value => 'javax.annotation.Nonnull'
    component.option :name => 'myNullables' do |option|
      option.value do |value|
        value.list :size => '2' do |list|
          list.item :index => '0', :class => 'java.lang.String', :itemvalue => 'org.jetbrains.annotations.Nullable'
          list.item :index => '1', :class => 'java.lang.String', :itemvalue => 'javax.annotation.Nullable'
        end
      end
    end
    component.option :name => 'myNotNulls' do |option|
      option.value do |value|
        value.list :size => '2' do |list|
          list.item :index => '0', :class => 'java.lang.String', :itemvalue => 'org.jetbrains.annotations.NotNull'
          list.item :index => '1', :class => 'java.lang.String', :itemvalue => 'javax.annotation.Nonnull'
        end
      end
    end
  end
end
