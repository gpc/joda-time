package jodatest

import org.joda.time.Period
import spock.lang.Unroll

@Unroll
class PeriodScaffoldingSpec extends GebSpec {

	def song1

	def setup() {
		Song.withNewSession {
			song1 = Song.build(artist: "La Roux", title: "Bulletproof", duration: new Period(0, 3, 25, 0))
		}
	}

	def cleanup() {
		Song.withNewSession {
			Song.list()*.delete(flush: true)
		}
	}

	def "list"() {
		when:
		go "/song"

		then:
		$("tbody tr", 0).find("td", 0).text() == song1.artist
		$("tbody tr", 0).find("td", 2).text() == "3 minutes and 25 seconds"
	}

	def "create"() {
		when:
		go "/song/create"
		$("form").artist = "Handsome Furs"
		$("form").title = "I'm Confused"
		$("form").duration_hours = "00"
		$("form").duration_minutes = "3"
		$("form").duration_seconds = "35"
		$("input.save").click()

		then:
		$(".message").text() ==~ /Song \d+ created/

		and:
		def song2 = Song.findByArtistAndTitle("Handsome Furs", "I'm Confused")
		song2.duration == new Period(0, 3, 35, 0)
	}

	def "show"() {
		when:
		go "/song/show/$song1.id"

		then:
		$("li.fieldcontain", 2).find(".property-value").text() == "3 minutes and 25 seconds"
	}

	def "edit"() {
		when:
		go "/song/edit/$song1.id"

		then:
		$("form").duration_hours == "0"
		$("form").duration_minutes == "3"
		$("form").duration_seconds == "25"
	}

	def "list view is sorted after clicking the column header #x times"() {
		given:
		Song.build(artist: "Handsome Furs", title: "I'm Confused", duration: new Period(0, 3, 35, 0))
		Song.build(artist: "Motorhead", title: "Ace of Spades", duration: new Period(0, 2, 47, 0))

		when:
		go "/song"
		x.times {
			$("th a", text: "Duration").click()
		}

		then:
		$("tbody tr")*.find("td", 1)*.text() == expected

		where:
		x | expected
		1 | ["Ace of Spades", "Bulletproof", "I'm Confused"]
		2 | ["I'm Confused", "Bulletproof", "Ace of Spades"]
	}
}