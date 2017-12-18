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

package grails.plugins.jodatime

import grails.plugins.Plugin
import grails.plugins.jodatime.binding.*
import grails.plugins.jodatime.converters.JodaConverters

class JodaTimeGrailsPlugin extends Plugin {

    def version = "2.1.0-SNAPSHOT"
    def grailsVersion = "3.3.0 > *"
    def title = "Joda-Time Plugin"
    def author = "Rob Fletcher"
    def authorEmail = "rob@freeside.co"
    def description = 'Joda Time integration for Grails'
    def profiles = ['web']
    def documentation = "http://grails.org/plugin/joda-time"
    def license = "APACHE"
    def organization = [name: "TO THE NEW Digital", url: "http://www.tothenew.com/"]
    def developers = [
        [name: 'Rob Fletcher', email: 'rob@freeside.co'],
        [name: "Puneet Behl", email: "puneet.behl007@gmail.com"],
        [name: "Søren Berg Glasius", email: "soeren@glasius.dk"]
    ]
    def issueManagement = [url: 'https://github.com/gpc/joda-time/issues']
    def scm = [url: 'https://gpc.github.io/joda-time/latest/']

    Closure doWithSpring() {{ ->
        jodaTimePropertyEditorRegistrar(JodaTimePropertyEditorRegistrar)

        DateTimeConverter.SUPPORTED_TYPES.each{ jodaType ->
            "joda${jodaType.simpleName}Converter"(DateTimeConverter) {
                configuration = config
                type = jodaType
            }
        }
        PeriodConverter.SUPPORTED_TYPES.each{ jodaType ->
            "joda${jodaType.simpleName}Converter"(PeriodConverter) {
                type = jodaType
            }
        }
        "jodaDateTimeZoneConverter"(DateTimeZoneConverter)
    }}

    void doWithDynamicMethods() {
        JodaDynamicMethods.registerDynamicMethods()
        JodaConverters.registerJsonAndXmlMarshallers()

        def grailsWebDataBinder = grailsApplication.mainContext.grailsWebDataBinder
        DateTimeStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsWebDataBinder.registerStructuredEditor type, new DateTimeStructuredBindingEditor(type)
        }
        PeriodStructuredBindingEditor.SUPPORTED_TYPES.each{ type ->
            grailsWebDataBinder.registerStructuredEditor type, new PeriodStructuredBindingEditor(type)
        }
    }
}
