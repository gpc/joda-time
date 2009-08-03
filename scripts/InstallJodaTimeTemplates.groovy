import org.apache.commons.io.FileUtils
import grails.util.GrailsUtil

includeTargets << grailsScript("_GrailsInit")

target("default": "Installs scaffolding templates to enable dynamic scaffolding with Joda Time properties.") {
    def srcdir = new File("$jodaTimePluginDir/src/templates/scaffolding")
    if (!srcdir?.isDirectory()) {
        event("StatusError", ["Unable to install templates as plugin template files are missing"])
    } else {
        event("StatusUpdate", ["Copying templates from $jodaTimePluginDir"])

        def destdir = new File("$basedir/src/templates/scaffolding/")

		def copyTemplates = ["renderEditor.template"]
		def deleteTemplates = []
		if (GrailsUtil.grailsVersion[0..2].toFloat() < 1.2) {
			copyTemplates << "list.gsp"
			copyTemplates << "show.gsp"
		} else {
			deleteTemplates << "list.gsp"
			deleteTemplates << "show.gsp"
		}

        copyTemplates.each {name ->
			def srcfile = new File(srcdir, name)
            def destfile = new File(destdir, name)
			ant.copy(file: srcfile.absolutePath, tofile: destfile.absolutePath, overwrite: true, failonerror: false)
        }

		deleteTemplates.each {name ->
			use(FileUtils) {
				def srcfile = new File(srcdir, name)
				def destfile = new File(destdir, name)
				if (destfile.file && srcfile.checksumCRC32() == destfile.checksumCRC32()) {
					event("StatusUpdate", ["Removing old template file $name (no longer needed in Grails 1.2+)"])
					ant.delete(file: destfile.absolutePath, quiet: true)
				}
			}
		}

        event("StatusFinal", ["Template installation complete"])
    }
}