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
		mavenRepo "http://repo.grails.org/grails/libs-releases"
		mavenRepo "http://m2repo.spockframework.org/ext/"
	}
	dependencies {
		compile "joda-time:joda-time:2.0"
//		compile "org.grails:grails-datastore-simple:1.0.0.RC1"
		test("org.hamcrest:hamcrest-all:1.1") {
			exported = false
		}
//		test("org.grails:grails-datastore-gorm-test:1.0.0.RC1") {
//			transitive = false
//		}
		def datastoreVersion = "1.0.0.RC1"
		compile("org.grails:grails-datastore-gorm-mongo:$datastoreVersion",
				"org.grails:grails-datastore-gorm-plugin-support:$datastoreVersion",
				"org.grails:grails-datastore-gorm:$datastoreVersion",
				"org.grails:grails-datastore-core:$datastoreVersion",
				"org.grails:grails-datastore-web:$datastoreVersion") {
			transitive = false
		}

		test("org.grails:grails-datastore-gorm-test:$datastoreVersion",
				"org.grails:grails-datastore-simple:$datastoreVersion") {
			transitive = false
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
