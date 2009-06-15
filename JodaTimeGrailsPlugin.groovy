import com.energizedwork.grails.plugins.jodatime.*

class JodaTimeGrailsPlugin {
    def version = '0.4.2'
    def dependsOn = [core: '1.1 > *']

    // TODO Fill in these fields
    def author = "Rob Fletcher"
    def authorEmail = "rob@energizedwork.com"
    def title = "Joda-Time Plugin"
    def description = '''\\
The Joda-Time Plugin integrates the <a href="http://joda-time.sourceforge.net/">Joda Time</a> date/time library into
Grails. The plugin includes the Joda Time library as well as classes for Hibernate persistence. Binding support is
included so that you can bind from form inputs to Joda Time fields on domain or command objects.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/JodaTime+Plugin"

    def pluginExcludes = [
		'grails-app/controllers/**',
		'grails-app/domain/**',
		'grails-app/views/**',
		'grails-app/i18n/**',
		'web-app/**'
	]

    def doWithSpring = {
        customPropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)
    }

    def doWithDynamicMethods = { ctx ->
        JodaDynamicMethods.registerDynamicMethods()
    }
}
