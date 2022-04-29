require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/top_level_generate_dir'
require 'buildr/gwt'
require 'buildr/shade'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

# JDK options passed to test environment. Essentially turns assertions on.
GALDR_TEST_OPTIONS =
  {
    'braincheck.environment' => 'development',
    'galdr.environment' => 'development'
  }

desc 'galdr: An ECS implementation'
define 'galdr' do
  project.group = 'org.realityforge.galdr'
  compile.options.source = '17'
  compile.options.target = '17'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/galdr')
  pom.add_developer('realityforge', 'Peter Donald')

  desc 'galdr core library'
  define 'core' do
    pom.include_transitive_dependencies << artifact(:javax_annotation)
    pom.include_transitive_dependencies << artifact(:jsinterop_annotations)
    pom.include_transitive_dependencies << artifact(:braincheck_core)

    compile.with :javax_annotation,
                 :jsinterop_annotations,
                 :grim_annotations,
                 :braincheck_core

    compile.options[:processor_path] << artifacts(:grim_processor, :javax_json)

    gwt_enhance(project)

    package(:jar)
    package(:sources)
    package(:javadoc)

    test.using :testng
    test.options[:java_args] = %w(-ea)
    test.options[:properties] =
      GALDR_TEST_OPTIONS.merge('galdr.diagnostic_messages_file' => _('src/test/java/galdr/diagnostic_messages.json'))

    test.compile.with :braincheck_testng,
                      :javax_json # Required to support validating invariant messages in tests
  end

  desc 'The Annotation processor'
  define 'processor' do
    pom.dependency_filter = Proc.new { |_| false }

    compile.with :proton_core,
                 :javapoet,
                 :javax_annotation

    test.with :proton_qa,
              project('core').package(:jar),
              project('core').compile.dependencies

    package(:jar)
    package(:sources)
    package(:javadoc)

    package(:jar).enhance do |jar|
      jar.merge(artifact(:javapoet))
      jar.merge(artifact(:proton_core))
      jar.enhance do |f|
        Buildr::Shade.shade(f,
                            f,
                            'com.squareup.javapoet' => 'galdr.processor.vendor.javapoet',
                            'org.realityforge.proton' => 'galdr.processor.vendor.proton')
      end
    end

    test.using :testng
    test.options[:properties] = {
      'galdr.subsystem.fixture_dir' => _('src/test/fixtures/subsystem'),
      'galdr.application.fixture_dir' => _('src/test/fixtures/application')
    }
    test.options[:java_args] = ['-ea']

    iml.test_source_directories << _('src/test/fixtures/application/input')
    iml.test_source_directories << _('src/test/fixtures/application/expected')
    iml.test_source_directories << _('src/test/fixtures/application/bad_input')
    iml.test_source_directories << _('src/test/fixtures/subsystem/input')
    iml.test_source_directories << _('src/test/fixtures/subsystem/expected')
    iml.test_source_directories << _('src/test/fixtures/subsystem/bad_input')
  end

  desc 'Galdr Integration Tests'
  define 'integration-tests' do
    test.options[:properties] = GALDR_TEST_OPTIONS.merge('galdr.integration_fixture_dir' => _('src/test/fixtures'))
    test.options[:java_args] = ['-ea']

    test.using :testng
    test.compile.options[:processor] = true
    test.compile.with :javax_json,
                      :jsonassert,
                      :android_json,
                      project('core').package(:jar),
                      project('core').compile.dependencies,
                      project('processor').package(:jar),
                      project('processor').compile.dependencies

    # The generators are configured to generate to here.
    iml.test_source_directories << _('generated/processors/test/java')
  end

  doc.from(projects(%w(core))).
    using(:javadoc,
          :windowtitle => 'Galdr API Documentation',
          :linksource => true,
          :timestamp => false,
          :group => {
            'Core Packages' => 'galdr*',
            'Annotation Packages' => 'galdr.annotations*'
          },
          :link => %w(https://docs.oracle.com/javase/8/docs/api http://www.gwtproject.org/javadoc/latest/)
    )

  cleanup_javadocs(project, 'galdr')

  ipr.add_testng_configuration('core',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Dgaldr.environment=development -Dgaldr.check_diagnostic_messages=false -Dgaldr.output_fixture_data=false -Dgaldr.diagnostic_messages_file=src/test/java/galdr/diagnostic_messages.json')
  ipr.add_testng_configuration('core - update invariant messages',
                               :module => 'core',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Dgaldr.environment=development -Dgaldr.check_diagnostic_messages=true -Dgaldr.output_fixture_data=true -Dgaldr.diagnostic_messages_file=src/test/java/galdr/diagnostic_messages.json')
  ipr.add_testng_configuration('processor',
                               :module => 'processor',
                               :jvm_args => '-ea -Dgaldr.output_fixture_data=false -Dgaldr.subsystem.fixture_dir=src/test/fixtures/subsystem -Dgaldr.subsystem.fixture_dir=src/test/fixtures/application')
  ipr.add_testng_configuration('integration-tests',
                               :module => 'integration-tests',
                               :jvm_args => '-ea -Dbraincheck.environment=development -Dgaldr.environment=development')

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end
