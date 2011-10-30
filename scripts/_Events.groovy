eventTestPhaseStart = { phase ->
	if (phase == "unit") {
		grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller.initialize()
	}
}