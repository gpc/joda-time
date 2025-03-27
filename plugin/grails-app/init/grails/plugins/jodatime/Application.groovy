package grails.plugins.jodatime

import grails.boot.GrailsApp
import grails.plugins.metadata.PluginSource
import grails.boot.config.GrailsAutoConfiguration

@PluginSource
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application)
    }
}