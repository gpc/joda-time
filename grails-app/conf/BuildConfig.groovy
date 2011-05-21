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
		compile("joda-time:joda-time-hibernate:1.2") {
			excludes "hibernate", "joda-time"
		}
//		compile "org.jadira.usertype:usertype.jodatime:1.2"
		test("org.hamcrest:hamcrest-all:1.1") {
			excludes "junit", "jmock", "easymock"
			exported = false
		}
		test("org.gmock:gmock:0.8.0") {
			excludes "junit"
			exported = false
		}
    }
}
