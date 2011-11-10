eventTestPhaseStart = { phase ->
	if (phase == "unit") {
		event "StatusUpdate", ["configuring joda-time support for simple datastore"]
		grails.plugin.jodatime.simpledatastore.SimpleMapJodaTimeMarshaller.initialize()
	}
}