package jodatest

import org.joda.time.*
import static javax.servlet.http.HttpServletResponse.*

class DurationScaffoldingTests extends AbstractFunctionalTestCase {

	def marathon1

	void setUp() {
		super.setUp()

		marathon1 = Marathon.build(runner: "Haile Gebrselassie", time: new Period(2, 3, 59, 0).toStandardDuration())
	}

	void tearDown() {
		super.tearDown()
		Marathon.list()*.delete(flush: true)
	}

	void testListView() {
		get "/marathon"
		assertStatus SC_OK
		assertTitle "Marathon List"
		assertEquals("$marathon1.id", byXPath("//tbody/tr[1]/td[1]").textContent)
		assertEquals("Haile Gebrselassie", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("2 hours, 3 minutes and 59 seconds", byXPath("//tbody/tr[1]/td[3]").textContent)
	}

	void testCreate() {
		get "/marathon/create"
		assertStatus SC_OK
		assertTitle "Create Marathon"

		form() {
			runner = "Glenn Saqui"
			time_hours = "2"
			time_minutes = "36"
			time_seconds = "51"
			click "Create"
		}

		assertContentContains "Marathon ${marathon1.id + 1} created"

		def marathon2 = Marathon.findByRunner("Glenn Saqui")
		assertEquals(new Period(2, 36, 51, 0).toStandardDuration(), marathon2.time)
	}

	void testShow() {
		get("/marathon/show/$marathon1.id")
		assertStatus SC_OK
		assertTitle "Show Marathon"

		assertTextByXPath("$marathon1.id", "//tr[1]/td[@class='value']")
		assertTextByXPath("Haile Gebrselassie", "//tr[2]/td[@class='value']")
		assertTextByXPath("2 hours, 3 minutes and 59 seconds", "//tr[3]/td[@class='value']")
	}

	void testEdit() {
		get "/marathon/edit/$marathon1.id"
		assertStatus SC_OK
		assertTitle "Edit Marathon"

		form() {
			assertEquals "Haile Gebrselassie", runner
			assertEquals "2", time_hours
			assertEquals "3", time_minutes
			assertEquals "59", time_seconds
		}
	}

	void testListViewIsSortable() {
		Marathon.build(runner: "Glenn Saqui", time: new Period(2, 36, 51, 0).toStandardDuration())
		Marathon.build(runner: "Samuel Wanjiru", time: new Period(2, 5, 10, 0).toStandardDuration())

		get "/marathon/list"
		assertStatus SC_OK
		assertTitle "Marathon List"

		// sort by duration
		click "Time"
		assertEquals("Haile Gebrselassie", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Samuel Wanjiru", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Glenn Saqui", byXPath("//tbody/tr[3]/td[2]").textContent)

		// sort descending
		click "Time"
		assertEquals("Glenn Saqui", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Samuel Wanjiru", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Haile Gebrselassie", byXPath("//tbody/tr[3]/td[2]").textContent)
	}

}