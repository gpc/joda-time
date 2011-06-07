grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
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
		compile "joda-time:joda-time:1.6.2"
		test("org.hamcrest:hamcrest-all:1.1") {
			excludes "junit", "jmock", "easymock"
			exported = false
		}
		test("org.gmock:gmock:0.8.0") {
			excludes "junit"
			exported = false
		}
    }
	plugins {
		build(":release:1.0.0.M2") {
			export = false
		}
		test(":spock:0.5-groovy-1.7") {
			export = false
		}
	}
}
