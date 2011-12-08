eventTestPhaseStart = { phase ->
	if (phase == "unit") {
		event "StatusUpdate", ["configuring joda-time support for simple datastore"]
		def marshallerClass = classLoader.loadClass("grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller")
		marshallerClass.initialize()
	}
}