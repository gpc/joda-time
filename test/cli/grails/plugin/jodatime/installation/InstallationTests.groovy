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
import org.junit.*
import static org.hamcrest.Matchers.*
import static org.junit.Assert.assertThat
import org.gmock.*
import org.joda.time.*

@WithGMock
class InstallationTests extends AbstractCliTestCase {

	private getPluginVersion() {
		getClass().classLoader.loadClass("JodaTimeGrailsPlugin").newInstance().version
	}

	File tempDir = new File(System.properties."java.io.tmpdir", getClass().name)
	File packagedPlugin = new File(workDir, "grails-joda-time-${pluginVersion}.zip")
	String tempProjectName = RandomStringUtils.randomAlphanumeric(8)

	static final DEFAULT_MAPPINGS = [
			"org.joda.time.DateTime": "org.joda.time.contrib.hibernate.PersistentDateTime",
			"org.joda.time.Duration": "org.joda.time.contrib.hibernate.PersistentDuration",
			"org.joda.time.Instant": "org.joda.time.contrib.hibernate.PersistentInstant",
			"org.joda.time.Interval": "org.joda.time.contrib.hibernate.PersistentInterval",
			"org.joda.time.LocalDate": "org.joda.time.contrib.hibernate.PersistentLocalDate",
			"org.joda.time.LocalTime": "org.joda.time.contrib.hibernate.PersistentLocalTimeAsString",
			"org.joda.time.LocalDateTime": "org.joda.time.contrib.hibernate.PersistentLocalDateTime",
			"org.joda.time.Period": "org.joda.time.contrib.hibernate.PersistentPeriod",
	].asImmutable()
	
	@Before
	void setUp() {
		super.setUp()
		
		runGrailsCommand "maven-install"

		tempDir.mkdirs()
	}

	@After
	void tearDown() {
		super.tearDown()
		tempDir.deleteDir()
	}

	@Test
	void updatesConfigWithGormMappings() {
		def appBaseDir = createTempApp()
		generateBuildConfig appBaseDir, "joda-time:joda-time-hibernate:1.3"
		runGrailsCommand "install-joda-time-gorm-mappings"
		def config = parseAppConfig(appBaseDir)

		assertThat "GORM mappings in Config.groovy", config.grails.gorm.default.mapping, instanceOf(Closure)

		config.grails.gorm.default.mapping.delegate = mock()
		DEFAULT_MAPPINGS.each {
			def userType = Class.forName(it.value)
			def persistenceClass = Class.forName(it.key)
			config.grails.gorm.default.mapping.delegate."user-type"(allOf(hasEntry("type", userType), hasEntry("class", persistenceClass)))
		}
		play {
			config.grails.gorm.default.mapping()
		}
	}

	@Test
	void doesNotOverrideExistingGormMappings() {
		def appBaseDir = createTempApp()
		generateBuildConfig appBaseDir, "joda-time:joda-time-hibernate:1.3"
		appendToAppConfig appBaseDir,
				"""
// a pre-existing mapping config block that should not be affected by installing Joda-Time
grails.gorm.default.mapping = {
	"user-type" type: org.springframework.orm.hibernate3.support.ClobStringType, "class": String
}
"""

		runGrailsCommand "install-joda-time-gorm-mappings"
		def config = parseAppConfig(appBaseDir)

		config.grails.gorm.default.mapping.delegate = mock() {
			"user-type"(allOf(hasEntry("type", Class.forName("org.springframework.orm.hibernate3.support.ClobStringType")), hasEntry("class", String)))
		}
		play {
			config.grails.gorm.default.mapping()
		}
	}

	private File createTempApp() {
		workDir = tempDir
		runGrailsCommand "create-app", tempProjectName
		return new File(tempDir, tempProjectName)
	}

	private ConfigObject parseAppConfig(File appBaseDir) {
		def configFile = new File(appBaseDir, "grails-app/conf/Config.groovy")
		new ConfigSlurper().parse(configFile.toURI().toURL())
	}

	private void installJodaTimePlugin(File appBaseDir) {
		workDir = appBaseDir
		generateBuildConfig appBaseDir, "joda-time:joda-time-hibernate:1.2"
	}

	private void appendToAppConfig(File appBaseDir, String text) {
		def configFile = new File(appBaseDir, "grails-app/conf/Config.groovy")
		configFile.withWriterAppend {
			it.write text
		}
	}

	private void runGrailsCommand(String... args) {
		execute(args as List)
		waitForProcess()
		verifyHeader()
	}

	private void generateBuildConfig(File appBaseDir, String persistenceLib) {
		new File(appBaseDir, "grails-app/conf/BuildConfig.groovy").text = """\
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
		runtime "${persistenceLib}"
    }
	plugins {
		compile ":joda-time:${pluginVersion}"
	}
}
"""
	}

}
