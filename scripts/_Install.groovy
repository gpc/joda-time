import org.apache.commons.io.FileUtils

// workaround for 1.0.4 where 'isInteractive' is not present in binding
boolean interactive = binding.variables.containsKey('isInteractive') ? isInteractive : true
boolean force = argsMap.force || !interactive ?: false
if (!force) {
	println """
	The Joda-Time plugin will now install templates to support Grails scaffolding for domain classes with Joda-Time
	properties. You will be prompted if this will overwrite any custom templates you already have in src/templates/scaffolding.
	If the existing templates are only from a previous version of the Joda-Time plugin they can be safely overwritten.
	Otherwise you may want to look at the patch files in:
		$pluginBasedir/src/templates/scaffolding
	so you can apply changes manually.
	"""
}

File dir = new File("$pluginBasedir/src/templates/scaffolding")
if (!dir?.isDirectory()) {
	event("StatusError", ["Unable to install templates as plugin template files are missing"])
} else {
	ant.pathconvert(property: "jodatime.template.dir") {
		map(from: pluginBasedir, to: "<plugin>")
		path(location: dir.absolutePath)
	}
	event("StatusUpdate", ["Copying templates from ${ant.antProject.properties.'jodatime.template.dir'} for Grails $grailsVersion"])

	def destdir = new File("$basedir/src/templates/scaffolding/")

	dir.eachFileMatch(~/.*\.(gsp|template)/) {File file ->
		def destfile = new File(destdir, file.name)
		boolean doCopy = true

		if (destfile.isFile()) {
			if (FileUtils.checksumCRC32(file) == FileUtils.checksumCRC32(destfile)) {
				println "$file.name identical to version already installed..."
				doCopy = false
			} else if (!force) {
				ant.input(message: "Overwrite $file.name?", validargs: "y,n,a", addproperty: "jodatime.overwrite.$file.name")
				def answer = ant.antProject.properties."jodatime.overwrite.$file.name"
				if (answer == "n") doCopy = false
				else if (answer == "a") force = true
			}
		}

		if (doCopy) {
			ant.copy(file: file.absolutePath, tofile: destfile.absolutePath, overwrite: true, failonerror: false)
		} else {
			println "Skipping $file.name..."
		}
	}
	event("StatusUpdate", ["Template installation complete"])
}
