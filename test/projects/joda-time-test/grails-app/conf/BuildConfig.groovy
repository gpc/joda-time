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
	}

	def gebVersion = '0.7.0'
	def seleniumVersion = '2.20.0'

	dependencies {
		compile "org.hamcrest:hamcrest-core:1.2"
		test("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
		test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
		test "org.codehaus.geb:geb-spock:$gebVersion"
		compile "org.jadira.usertype:usertype.jodatime:1.9"
	}
	plugins {
		build ":tomcat:$grailsVersion"
		runtime ":fields:1.2"
		runtime ":hibernate:$grailsVersion"
		test ":build-test-data:1.1.2"
		test ":fixtures:1.0.7"
		test ":geb:$gebVersion"
		test ":spock:0.6"
	}
}

grails.plugin.location.'joda-time' = "../../.."