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
import org.joda.time.contrib.hibernate.*
import org.joda.time.*
import org.springframework.orm.hibernate3.support.ClobStringType

@WithGMock
class InstallationTests extends AbstractCliTestCase {

	File tempDir = new File(System.properties."java.io.tmpdir", getClass().name)
	File packagedPlugin = new File(workDir, "grails-joda-time-1.2-SNAPSHOT.zip")
	String tempProjectName = RandomStringUtils.randomAlphanumeric(8)

	static final DEFAULT_MAPPINGS = [
			(DateTime): PersistentDateTime,
			(Duration): PersistentDuration,
			(Instant): PersistentInstant,
			(Interval): PersistentInterval,
			(LocalDate): PersistentLocalDate,
			(LocalTime): PersistentLocalTimeAsString,
			(LocalDateTime): PersistentLocalDateTime,
			(Period): PersistentPeriod,
	].asImmutable()

	@Before
	void setUp() {
		super.setUp()

		runGrailsCommand("package-plugin")

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
		installJodaTimePlugin(appBaseDir)
		def config = parseAppConfig(appBaseDir)

		assertThat "GORM mappings in Config.groovy", config.grails.gorm.default.mapping, instanceOf(Closure)

		config.grails.gorm.default.mapping.delegate = mock()
		DEFAULT_MAPPINGS.each {
			config.grails.gorm.default.mapping.delegate."user-type"(allOf(hasEntry("type", it.value), hasEntry("class", it.key)))
		}
		play {
			config.grails.gorm.default.mapping()
		}
	}

	@Test
	void doesNotOverrideExistingGormMappings() {
		def appBaseDir = createTempApp()
		appendToAppConfig appBaseDir,
				"""
// a pre-existing mapping config block that should not be affected by installing Joda-Time
grails.gorm.default.mapping = {
	"user-type" type: org.springframework.orm.hibernate3.support.ClobStringType, "class": String
}
"""

		installJodaTimePlugin appBaseDir
		def config = parseAppConfig(appBaseDir)

		config.grails.gorm.default.mapping.delegate = mock() {
			"user-type"(allOf(hasEntry("type", ClobStringType), hasEntry("class", String)))
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
		runGrailsCommand "install-plugin", packagedPlugin.absolutePath
	}

	private def appendToAppConfig(File appBaseDir, String text) {
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

}
