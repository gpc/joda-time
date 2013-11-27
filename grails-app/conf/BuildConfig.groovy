grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
		mavenRepo 'http://repo.grails.org/grails/libs-releases'
		mavenRepo 'https://oss.sonatype.org/content/groups/public'
	}

	dependencies {
		compile 'joda-time:joda-time:2.3'

		test('org.spockframework:spock-grails-support:0.7-groovy-2.0') { export = false }
		test('org.hamcrest:hamcrest-all:1.1') { export = false }
		test('org.jodd:jodd-lagarto:3.4.1') { export = false }
	}

	plugins {
		build ':release:2.2.0', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
