includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_PluginDependencies")

target('default': "Prepares development environment") {
	depends(installPlugins, installDependencies)
	event("StatusFinal", ["Done"])
}

target(installPlugins: "Installs all plugins") {
	depends(resolveDependencies)
}

target(installDependencies: "Installs library dependencies via Ivy") {
	if (metadata['plugins.ivy']) {
		includeTargets << new File ("${ivyPluginDir}/scripts/GetDependencies.groovy")
		depends(dependencies)
	} else {
		event("StatusFinal", ["The Ivy plugin is not installed"])
	}
}
