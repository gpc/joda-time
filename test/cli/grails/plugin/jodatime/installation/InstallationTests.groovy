/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.jodatime.installation

import grails.test.AbstractCliTestCase
import org.apache.commons.lang.RandomStringUtils
import static org.apache.commons.io.FileUtils.checksumCRC32
import org.joda.time.*
import org.junit.*

class InstallationTests extends AbstractCliTestCase {

	File tempDir = new File(System.properties."java.io.tmpdir", getClass().simpleName)
	String tempProjectName = RandomStringUtils.randomAlphanumeric(8)

	/*
	 * only want to install plugin once as its slow but can't do statically from @BeforeClass as runGrailsCommand is an
	 * instance method (wish this was Spock where setupSpec would do the job perfectly).
	 */
	private static boolean mavenInstalled = false

	@Before
	void setUp() {
		super.setUp()
		if (!mavenInstalled) {
			runGrailsCommand "maven-install"
			mavenInstalled = true
		}
		tempDir.mkdirs()
		createTempApp()
	}

	@After
	void tearDown() {
		super.tearDown()
		tempDir.deleteDir()
	}

	@Grab("joda-time:joda-time-hibernate:1.3")
	@Test
	void installJodaTimeGormMappingsUpdatesConfigWithGormMappingsForOldPersistenceSupport() {
		generateBuildConfig 'runtime("joda-time:joda-time-hibernate:1.3") { excludes "joda-time", "hibernate" }'
		runGrailsCommand "package"
		runGrailsCommand "install-joda-time-gorm-mappings"

		def config = parseAppConfig()
		def mappings = getGormMappings(config)

		assert mappings[DateTime] == org.joda.time.contrib.hibernate.PersistentDateTime
		assert mappings[LocalDate] == org.joda.time.contrib.hibernate.PersistentLocalDate
	}

	@Grab("org.jadira.usertype:usertype.jodatime:1.9")
	@Grab("org.hibernate:hibernate-core:3.6.7.Final")
	@Test
	void installJodaTimeGormMappingsUpdatesConfigWithGormMappingsForNewPersistenceSupport() {
		generateBuildConfig 'runtime "org.jadira.usertype:usertype.jodatime:1.9"'
		runGrailsCommand "package"
		runGrailsCommand "install-joda-time-gorm-mappings"

		def config = parseAppConfig()
		def mappings = getGormMappings(config)

		assert mappings[DateTime] == org.jadira.usertype.dateandtime.joda.PersistentDateTime
		assert mappings[LocalDate] == org.jadira.usertype.dateandtime.joda.PersistentLocalDate
	}

	@Test
	void installJodaTimeGormMappingsDoesNotOverrideExistingGormMappings() {
		generateBuildConfig 'runtime "org.jadira.usertype:usertype.jodatime:1.9"'
		runGrailsCommand "package"

		appendToAppConfig """
// a pre-existing mapping config block that should not be affected by installing Joda-Time
grails.gorm.default.mapping = {
	"user-type" type: org.springframework.orm.hibernate3.support.ClobStringType, "class": String
}
"""

		runGrailsCommand "install-joda-time-gorm-mappings"

		def config = parseAppConfig()
		def mappings = getGormMappings(config)

		assert !mappings.containsKey(DateTime)
	}

	@Test
	void installJodaTimeGormMappingsDoesNothingWhenNoPersistenceSupportIsInstalled() {
		generateBuildConfig()
		runGrailsCommand "package"
		runGrailsCommand "install-joda-time-gorm-mappings"

		def config = parseAppConfig()

		assert !config.grails.gorm.default.mapping
	}

	@Test
	void installJodaTimeTemplatesCopiesTemplatesIntoProject() {
		generateBuildConfig()
		runGrailsCommand "package"
		runGrailsCommand "install-joda-time-templates"

		def srcFile = new File("src/templates/scaffolding/renderEditor.template")
		def destFile = new File(workDir, "src/templates/scaffolding/renderEditor.template")

		assert destFile.isFile()
		assert checksumCRC32(srcFile) == checksumCRC32(destFile)
	}

	@Test
	void installJodaTimeTemplatesCanOptionallyInstallHtml5Version() {
		generateBuildConfig()
		runGrailsCommand "package"
		runGrailsCommand "install-joda-time-templates", "-html5"

		def srcFile = new File("src/templates/scaffolding/renderEditor.template.html5")
		def destFile = new File(workDir, "src/templates/scaffolding/renderEditor.template")

		assert destFile.isFile()
		assert checksumCRC32(srcFile) == checksumCRC32(destFile)
	}

	@Test
	void installJodaTimeTemplatesOverwritesExistingTemplates() {
		generateBuildConfig()
		runGrailsCommand "package"
		runGrailsCommand "install-templates"

		def srcFile = new File("src/templates/scaffolding/renderEditor.template")
		def destFile = new File(workDir, "src/templates/scaffolding/renderEditor.template")

		assert destFile.isFile()
		assert checksumCRC32(srcFile) != checksumCRC32(destFile)

		runGrailsCommand "install-joda-time-templates"

		assert destFile.isFile()
		assert checksumCRC32(srcFile) == checksumCRC32(destFile)
	}

	private Map<Class, String> getGormMappings(ConfigObject config) {
		def mappingClosure = config.grails.gorm.default.mapping
		assert mappingClosure instanceof Closure
		// set the delegate of the config mapping closure to something that will collect the mappings
		def mappings = [:]
		mappingClosure.delegate = new Expando()
		mappingClosure.delegate."user-type" = { Map mapping ->
			mappings[mapping.class] = mapping.type
		}
		// execute the config closure
		mappingClosure()
		// return the map of joda-time type to persistent type
		return mappings
	}

	private void createTempApp() {
		workDir = tempDir
		runGrailsCommand "create-app", tempProjectName
		def projectDir = new File(tempDir, tempProjectName)
		workDir = projectDir
	}

	private ConfigObject parseAppConfig() {
		def configFile = new File(workDir, "grails-app/conf/Config.groovy")
		new ConfigSlurper().parse(configFile.toURI().toURL())
	}

	private void appendToAppConfig(String text) {
		def configFile = new File(workDir, "grails-app/conf/Config.groovy")
		configFile.withWriterAppend {
			it.write text
		}
	}

	private void runGrailsCommand(String... args) {
		println "$workDir.absolutePath > `grails ${args.join(' ')}`..."
		execute(args as List)
		waitForProcess()
		verifyHeader()
	}

	private void generateBuildConfig(String persistenceLib = "") {
		new File(workDir, "grails-app/conf/BuildConfig.groovy").text = """\
grails.servlet.version = "2.5"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.source.level = 1.6
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
    inherits "global"
    log "error"
    checksums true
    repositories {
        inherits true
		mavenLocal() // vital that mavenLocal comes first so we get the build's version of the joda-time plugin
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
		${persistenceLib}
    }
    plugins {
        compile ":hibernate:\$grailsVersion"
		compile ":joda-time:${pluginVersion}"
        build ":tomcat:\$grailsVersion"
    }
}
"""
	}

	private getPluginVersion() {
		getClass().classLoader.loadClass("JodaTimeGrailsPlugin").newInstance().version
	}

}
