/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.energizedwork.grails.plugins.jodatime.*

class JodaTimeGrailsPlugin {
    def version = "1.1"
	def grailsVersion = "1.1 > *"
    def dependsOn = [core: "1.1 > *", converters: "1.1 > *"]

    def author = "Rob Fletcher"
    def authorEmail = "rob@energizedwork.com"
    def title = "Joda-Time Plugin"
    def description = '''\\
The Joda-Time Plugin integrates the <a href="http://joda-time.sourceforge.net/">Joda Time</a> date/time library into
Grails. The plugin provides:
<ul>
<li>Libraries necessary to use Joda Time types as persistent fields on domain classes.</li>
<li>The ability to bind from form inputs to Joda Time fields on domain or command objects.</li>
<li>JSON and XML rendering of Joda Time types.</li>
<li>Tag-libs for input and output of Joda Time data.</li>
<li>Enhancements to Grails scaffolding to support domain classes with Joda Time fields.</li>
<li>Compatibility and consistency methods added to Joda Time types.</li>
</ul>
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
        jodaTimePropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)
    }

    def doWithDynamicMethods = { ctx ->
        JodaDynamicMethods.registerDynamicMethods()
		JodaConverters.registerJsonAndXmlMarshallers()
    }
}
