package jodatest

import javax.servlet.http.HttpServletResponse
import org.joda.time.*
import org.joda.time.format.*
import static javax.servlet.http.HttpServletResponse.SC_OK

class LocalTimeScaffoldingTests extends AbstractFunctionalTestCase {

	def alarm1

	void setUp() {
		super.setUp()

		alarm1 = Alarm.build(description: "Morning", time: new LocalTime(7, 0))
	}

	void tearDown() {
		super.tearDown()
		Alarm.list()*.delete(flush: true)
	}

	void testEmptyListView() {
		get "/alarm"
		assertStatus SC_OK
		assertTitle "Alarm List"
	}

	void testCreate() {
		get "/alarm/create"
		assertStatus SC_OK
		assertTitle "Create Alarm"

		form() {
			description = "Gym"
			time_hour = "06"
			time_minute = "15"
			click "Create"
		}

		assertContentContains "Alarm ${alarm1.id + 1} created"

		def alarm2 = Alarm.findByDescription("Gym")
		assertEquals(new LocalTime(6, 15), alarm2.time)
	}

	void testShow() {
		[Locale.UK, Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.CANADA, Locale.CANADA_FRENCH].each {locale ->
			get("/alarm/show/$alarm1.id?lang=$locale")
			assertStatus SC_OK
			assertTitle "Show Alarm"

			assertTextByXPath("$alarm1.id", "//tr[1]/td[@class='value']")
			assertTextByXPath("Morning", "//tr[2]/td[@class='value']")

			String time = byXPath("//tr[3]/td[@class='value']")?.textContent
			try {
				def formatter = DateTimeFormat.forStyle("-S").withLocale(locale)
				assertEquals(alarm1.time, formatter.parseDateTime(time).toLocalTime())
			} catch (IllegalArgumentException e) {
				fail "Could not parse '$time' as time with format ${DateTimeFormat.patternForStyle("-S", locale)} and locale $locale"
			}
		}
	}

	void testEdit() {
		get "/alarm/edit/$alarm1.id"
		assertStatus SC_OK
		assertTitle "Edit Alarm"

		form() {
			assertEquals("Morning", description)
			assertEquals(["07"], time_hour)
			assertEquals(["00"], time_minute)
		}
	}

	void testListViewIsSortable() {
		Alarm.build(description: "Gym", time: new LocalTime(6, 15))
		Alarm.build(description: "Lie In", time: new LocalTime(10, 30))

		get "/alarm/list"
		assertStatus SC_OK
		assertTitle "Alarm List"

		// sort by local date
		click "Time"
		assertEquals("Gym", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Morning", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Lie In", byXPath("//tbody/tr[3]/td[2]").textContent)

		// sort descending
		click "Time"
		assertEquals("Lie In", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Morning", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Gym", byXPath("//tbody/tr[3]/td[2]").textContent)
	}

}