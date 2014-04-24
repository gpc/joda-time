grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()
		mavenCentral()
	}

	def gebVersion = '0.9.2'
	def seleniumVersion = '2.41.0'

	dependencies {
		test "org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion",
				"org.seleniumhq.selenium:selenium-support:$seleniumVersion",
				"org.gebish:geb-spock:$gebVersion"
		compile 'org.jadira.usertype:usertype.jodatime:1.9'
	}
	plugins {
		build ":tomcat:7.0.52.1"
		runtime ":fields:1.3",
				":hibernate:3.6.10.12"
		test ':build-test-data:2.1.2',
				':fixtures:1.2',
				":geb:$gebVersion"
        compile ":scaffolding:2.0.2"
    }
}

grails.plugin.location.'joda-time' = '../../..'
