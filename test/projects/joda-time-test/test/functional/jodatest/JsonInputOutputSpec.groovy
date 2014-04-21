package jodatest

import spock.lang.*
import com.mashape.unirest.http.Unirest

@Issue("http://jira.grails.org/browse/GPJODATIME-45")
@Unroll
class JsonInputOutputSpec extends Specification {

	def "parsing LocalDateTime from JSON"() {
		when:
		def parsed = Unirest.put("http://localhost:8080/timestamp")
		       .header("Content-Type", "application/json")
		       .body("{timestamp:\"$value\"}")
		       .asString()

		then:
		parsed.body == expected

		where:
		value                     | expected
		"2014-04-21T08:04:18.123" | "2014-04-21T08:04:18.123"
		"2014-04-21T08:04:18"     | "2014-04-21T08:04:18.000"
	}

}
