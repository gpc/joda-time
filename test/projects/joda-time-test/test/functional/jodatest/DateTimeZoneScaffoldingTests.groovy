package jodatest

import javax.servlet.http.HttpServletResponse
import org.joda.time.*
import org.joda.time.format.*
import static javax.servlet.http.HttpServletResponse.SC_OK

class DateTimeZoneScaffoldingTests extends AbstractFunctionalTestCase {

	def london

	void setUp() {
		super.setUp()

		london = City.build(name: "London", timeZone: DateTimeZone.forID("Europe/London"))
	}

	void tearDown() {
		super.tearDown()
		City.list()*.delete(flush: true)
	}

	void testEmptyListView() {
		get "/city"
		assertStatus SC_OK
		assertTitle "City List"
	}

	void testCreate() {
		get "/city/create"
		assertStatus SC_OK
		assertTitle "Create City"

		form() {
			name = "Vancouver"
			timeZone = "America/Vancouver"
			click "Create"
		}

		assertContentContains "City ${london.id + 1} created"

		def jerome = City.findByName("Vancouver")
		assertEquals DateTimeZone.forID("America/Vancouver"), jerome.timeZone
	}

	void testShow() {
		get("/city/show/$london.id")
		assertStatus SC_OK
		assertTitle "Show City"

		assertTextByXPath("$london.id", "//tr[1]/td[@class='value']")
		assertTextByXPath("London", "//tr[2]/td[@class='value']")
		assertTextByXPath("Europe/London", "//tr[3]/td[@class='value']")
	}

	void testEdit() {
		get "/city/edit/$london.id"
		assertStatus SC_OK
		assertTitle "Edit City"

		form() {
			assertEquals "London", name
			assertEquals(["Europe/London"], timeZone)
		}
	}

	void testListViewIsSortable() {
		City.build(name: "Vancouver", timeZone: DateTimeZone.forID("America/Vancouver"))
		City.build(name: "Auckland", timeZone: DateTimeZone.forID("Pacific/Auckland"))

		get "/city/list"
		assertStatus SC_OK
		assertTitle "City List"

		// sort by zone
		click "Time Zone"
		assertEquals("Vancouver", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("London", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Auckland", byXPath("//tbody/tr[3]/td[2]").textContent)

		// sort descending
		click "Time Zone"
		assertEquals("Auckland", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("London", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Vancouver", byXPath("//tbody/tr[3]/td[2]").textContent)
	}
}