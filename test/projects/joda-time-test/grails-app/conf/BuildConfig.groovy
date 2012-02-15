grails.project.work.dir = "target"
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
		mavenRepo "http://m2repo.spockframework.org/ext/"
	}

	def gebVersion = '0.6.2'
	def seleniumVersion = '2.17.0'

	dependencies {
		test "org.hamcrest:hamcrest-all:1.2"
		test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
			excludes "xml-apis"
		}
		test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
		test "org.codehaus.geb:geb-spock:$gebVersion"
		compile "org.jadira.usertype:usertype.jodatime:1.9"
	}
	plugins {
		build ":tomcat:$grailsVersion"
		runtime ":fields:1.0.4"
		runtime ":hibernate:$grailsVersion"
		test ":build-test-data:1.1.2"
		test ":fixtures:1.0.7"
		test ":geb:$gebVersion"
		test ":spock:0.6-SNAPSHOT"
	}
}

grails.plugin.location.'joda-time' = "../../.."