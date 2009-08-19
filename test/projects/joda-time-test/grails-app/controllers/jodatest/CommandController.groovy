package jodatest

import org.joda.time.*
import org.joda.time.format.*

class CommandController {

	def index = {
	}

	def submitLocalDate = { LocalDateCommand command ->
		if (command.hasErrors()) {
			render view: "index", model: [localDateCommand: command]
		} else {
			def formattedValue = DateTimeFormat.forPattern("d MMMM yyyy").print(command.localDate)
			flash.message = "You entered: $formattedValue"
			redirect action: index
		}
	}

}

class LocalDateCommand {
	LocalDate localDate
	static constraints = {
		localDate nullable: false
	}
}