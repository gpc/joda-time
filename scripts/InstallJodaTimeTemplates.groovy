import org.apache.commons.io.FileUtils

includeTargets << grailsScript("_GrailsInit")

target("default": "Installs scaffolding templates to enable dynamic scaffolding with Joda Time properties.") {
    depends(parseArguments)

    // workaround for 1.0.4 where 'isInteractive' is not present in binding
    boolean interactive = binding.variables.containsKey('isInteractive') ? isInteractive : true
    boolean force = argsMap.force || !interactive ?: false

    File dir = new File("$jodaTimePluginDir/src/templates/scaffolding")
    if (!dir?.isDirectory()) {
        event("StatusError", ["Unable to install templates as plugin template files are missing"])
    } else {
        event("StatusUpdate", ["Copying templates from ${jodaTimePluginDir}"])

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
}