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
		mavenLocal()
		mavenCentral()
	}

	def gebVersion = '0.9.0-RC-1'
	def seleniumVersion = '2.28.0'

	dependencies {
		test 'org.spockframework:spock-grails-support:0.7-groovy-2.0',
				"org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion",
				"org.seleniumhq.selenium:selenium-support:$seleniumVersion",
				"org.gebish:geb-spock:$gebVersion"
		compile 'org.jadira.usertype:usertype.jodatime:1.9'
	}
	plugins {
		build ":tomcat:$grailsVersion"
		runtime ":fields:1.3",
				":hibernate:$grailsVersion"
		test ':build-test-data:2.0.3',
				':fixtures:1.2',
				":geb:$gebVersion"
		test(':spock:0.7') {
			exclude 'spock-grails-support'
		}
	}
}

grails.plugin.location.'joda-time' = '../../..'