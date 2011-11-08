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
	dependencies {
		compile "joda-time:joda-time:2.0"
		test("org.hamcrest:hamcrest-all:1.1") {
			exported = false
		}
	}
	plugins {
		build(":release:1.0.0.RC3") {
			export = false
		}
		build(":svn:1.0.0.M1") {
			export = false
		}
		test(":spock:0.6-SNAPSHOT") {
			export = false
		}
	}
}
