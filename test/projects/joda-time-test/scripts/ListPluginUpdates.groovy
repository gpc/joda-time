import groovy.xml.dom.DOMCategory

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsPlugins")

def getAvailablePluginVersions = {
	def plugins = [:]
	eachRepository {repo, url ->
		use(DOMCategory) {
			pluginsList.'plugin'.each {plugin ->
				def name = plugin.'@name'
				def version
				if (plugin.'@latest-release') {
					version = plugin.'@latest-release'
				}
				else if (plugin.'release'.size() > 0) {
					// determine latest release by comparing version names in lexicografic order
					version = plugin.'release'[0].'@version'
					plugin.'release'.each {
						if (!"${it.'@version'}".endsWith("SNAPSHOT") && "${it.'@version'}" > version) version = "${it.'@version'}"
					}
				}
				plugins."$name" = version
			}
		}
	}
	return plugins
}

def getInstalledPluginVersions = {
	def plugins = [:]
	def pluginXmls = readAllPluginXmlMetadata()
	for (p in pluginXmls) {
		def name = p.@name.text()
		def version = p.@version.text()
		plugins."$name" = version
	}
	return plugins
}

target('default': "Checks installed plugin versions against latest releases on repositories") {
	depends(updatePluginsList)

	def availablePluginVersions = getAvailablePluginVersions()
	def installedPluginVersions = getInstalledPluginVersions()

	boolean headerDisplayed = false
	if (installedPluginVersions) {
		installedPluginVersions.each {name, version ->
			def availableVersion = availablePluginVersions."$name"
			if (availableVersion != version) {
				if (!headerDisplayed) {
					println """
Plugins with available updates are listed below:
-------------------------------------------------------------
<Plugin>            <Current>         <Available>"""
					headerDisplayed = true
				}
				println "${name.padRight(20, " ")}${version.padRight(16, " ")}  ${availableVersion}"
			}
		}
		if (!headerDisplayed) {
			println "\nAll plugins are up to date."
		}
	} else {
		println "\nYou do not have any plugins installed."
	}
}
