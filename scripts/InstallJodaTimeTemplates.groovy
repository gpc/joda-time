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

includeTargets << grailsScript("_GrailsInit")

target(installJodaTimeTemplates: "Installs scaffolding templates to enable dynamic scaffolding with Joda Time properties.") {

	def srcdir = new File("$jodaTimePluginDir/src/templates/scaffolding")
	def destdir = new File("$basedir/src/templates/scaffolding/")

	if (srcdir?.isDirectory()) {
		event "StatusUpdate", ["Copying templates from $jodaTimePluginDir"]

		boolean html5 = argsMap["html5"]

		def copyTemplates = ["renderEditor.template"]

		for (name in copyTemplates) {
			def srcfile = new File(srcdir, html5 ? "${name}.html5" : name)
			def destfile = new File(destdir, name)
			ant.copy file: srcfile.absolutePath, tofile: destfile.absolutePath, overwrite: true, failonerror: false
		}

		if (html5) {
			println """\
*******************************************************************************
* To support HTML5 inputs for Joda-time properties ensure that
* `jodatime.format.html5 = true` is set in your Config.groovy
*******************************************************************************
"""
		}
		event "StatusFinal", ["Template installation complete"]
	} else {
		event "StatusError", ["Unable to install templates as plugin template files are missing"]
	}
}

setDefaultTarget(installJodaTimeTemplates)