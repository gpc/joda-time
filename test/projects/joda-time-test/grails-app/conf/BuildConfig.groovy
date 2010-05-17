grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {        
        grailsPlugins()
        grailsHome()
        mavenLocal()
        mavenCentral()
    }
    dependencies {
		test "org.hamcrest:hamcrest-all:1.1"
	}
}

grails.plugin.location.'joda-time'="../../.."