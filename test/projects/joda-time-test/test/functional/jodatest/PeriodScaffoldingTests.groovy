package jodatest

import org.joda.time.*
import static javax.servlet.http.HttpServletResponse.*

class PeriodScaffoldingTests extends AbstractFunctionalTestCase {

	def song1

	void setUp() {
		super.setUp()

		song1 = Song.build(artist: "La Roux", title: "Bulletproof", duration: new Period(0, 3, 25, 0))
	}

	void tearDown() {
		super.tearDown()
		Song.list()*.delete(flush: true)
	}

	void testListView() {
		get "/song"
		assertStatus SC_OK
		assertTitle "Song List"
		assertEquals("$song1.id", byXPath("//tbody/tr[1]/td[1]").textContent)
		assertEquals("La Roux", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Bulletproof", byXPath("//tbody/tr[1]/td[3]").textContent)
		assertEquals("3 minutes and 25 seconds", byXPath("//tbody/tr[1]/td[4]").textContent)
	}

	void testCreate() {
		get "/song/create"
		assertStatus SC_OK
		assertTitle "Create Song"

		form() {
			artist = "Handsome Furs"
			title = "I'm Confused"
			duration_hours = "00"
			duration_minutes = "3"
			duration_seconds = "35"
			click "Create"
		}

		assertContentContains "Song ${song1.id + 1} created"

		def song2 = Song.findByArtistAndTitle("Handsome Furs", "I'm Confused")
		assertEquals(new Period(0, 3, 35, 0), song2.duration)
	}

	void testShow() {
		get("/song/show/$song1.id")
		assertStatus SC_OK
		assertTitle "Show Song"

		assertTextByXPath("$song1.id", "//tr[1]/td[@class='value']")
		assertTextByXPath("La Roux", "//tr[2]/td[@class='value']")
		assertTextByXPath("Bulletproof", "//tr[3]/td[@class='value']")
		assertTextByXPath("3 minutes and 25 seconds", "//tr[4]/td[@class='value']")
	}

	void testEdit() {
		get "/song/edit/$song1.id"
		assertStatus SC_OK
		assertTitle "Edit Song"

		form() {
			assertEquals "La Roux", artist
			assertEquals "Bulletproof", title
			assertEquals "0", duration_hours
			assertEquals "3", duration_minutes
			assertEquals "25", duration_seconds
		}
	}

	void testListViewIsSortable() {
		Song.build(artist: "Handsome Furs", title: "I'm Confused", duration: new Period(0, 3, 35, 0))
		Song.build(artist: "Television", title: "Marquee Moon", duration: new Period(0, 10, 38, 0))

		get "/song/list"
		assertStatus SC_OK
		assertTitle "Song List"

		// sort by period
		click "Duration"
		assertEquals("Bulletproof", byXPath("//tbody/tr[1]/td[3]").textContent)
		assertEquals("I'm Confused", byXPath("//tbody/tr[2]/td[3]").textContent)
		assertEquals("Marquee Moon", byXPath("//tbody/tr[3]/td[3]").textContent)

		// sort descending
		click "Duration"
		assertEquals("Marquee Moon", byXPath("//tbody/tr[1]/td[3]").textContent)
		assertEquals("I'm Confused", byXPath("//tbody/tr[2]/td[3]").textContent)
		assertEquals("Bulletproof", byXPath("//tbody/tr[3]/td[3]").textContent)
	}

}