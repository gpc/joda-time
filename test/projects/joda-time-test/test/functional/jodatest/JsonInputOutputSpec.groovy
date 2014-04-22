package jodatest

import spock.lang.*
import org.joda.time.*
import com.mashape.unirest.http.Unirest

@Issue("http://jira.grails.org/browse/GPJODATIME-45")
@Unroll
class JsonInputOutputSpec extends Specification {

	static final BASE_URL = 'http://localhost:8080'

	def "rendering LocalDateTime as JSON"() {
		given:
		DateTimeUtils.setCurrentMillisFixed System.currentTimeMillis()
		def now = new LocalDateTime()

		when:
		def response = Unirest.get("$BASE_URL/timestamp")
			.header("Accept", "application/json")
			.asJson()

		then:
		println response.body
		new LocalDateTime(response.body.object.timestamp) == now

		cleanup:
		DateTimeUtils.setCurrentMillisSystem()
	}

	def "parsing LocalDateTime from JSON"() {
		when:
		def response = Unirest.put("$BASE_URL/timestamp")
			.header("Content-Type", "application/json")
			.body("{timestamp:\"$value\"}")
			.asString()

		then:
		response.body == expected

		where:
		value                     | expected
		"2014-04-21T08:04:18.123" | "2014-04-21T08:04:18.123"
		"2014-04-21T08:04:18"     | "2014-04-21T08:04:18.000"
		"2014-04-21T08:04"        | "2014-04-21T08:04:00.000"
	}

}
