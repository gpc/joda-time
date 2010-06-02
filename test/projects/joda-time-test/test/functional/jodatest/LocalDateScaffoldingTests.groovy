package jodatest

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import static javax.servlet.http.HttpServletResponse.SC_OK

class LocalDateScaffoldingTests extends AbstractFunctionalTestCase {

	def rob

	void setUp() {
		super.setUp()

		rob = Person.build(name: "Rob", birthday: new LocalDate(1971, 11, 29))
	}

	void tearDown() {
		super.tearDown()
		Person.list()*.delete(flush: true)
	}

	void testEmptyListView() {
		get "/person"
		assertStatus SC_OK
		assertTitle "Person List"
	}

	void testCreate() {
		get "/person/create"
		assertStatus SC_OK
		assertTitle "Create Person"

		form() {
			name = "Alex"
			birthday_day = "2"
			birthday_month = "10"
			birthday_year = "2008"
			click "Create"
		}

		assertContentContains "Person ${rob.id + 1} created"

		def alex = Person.findByName("Alex")
		assertEquals(new LocalDate(2008, 10, 2), alex.birthday)
	}

	void testShow() {
		[Locale.UK, Locale.US, Locale.FRANCE, Locale.GERMANY, Locale.CANADA, Locale.CANADA_FRENCH].each {locale ->
			get("/person/show/$rob.id?lang=$locale")
			assertStatus SC_OK
			assertTitle "Show Person"

			assertTextByXPath("$rob.id", "//tr[1]/td[@class='value']")
			assertTextByXPath("Rob", "//tr[2]/td[@class='value']")

			String birthday = byXPath("//tr[3]/td[@class='value']")?.textContent
			try {
				def formatter = DateTimeFormat.forStyle("S-").withLocale(locale)
				assertEquals(rob.birthday, formatter.parseDateTime(birthday).toLocalDate())
			} catch (IllegalArgumentException e) {
				fail "Could not parse '$birthday' as time with format ${DateTimeFormat.patternForStyle("S-", locale)} and locale $locale"
			}
		}
	}

	void testEdit() {
		get "/person/edit/$rob.id"
		assertStatus SC_OK
		assertTitle "Edit Person"

		form() {
			assertEquals("Rob", name)
			assertEquals(["29"], birthday_day)
			assertEquals(["11"], birthday_month)
			assertEquals(["1971"], birthday_year)
		}
	}

	void testListViewIsSortable() {
		Person.build(name: "Ilse", birthday: new LocalDate(1972, 7, 6))
		Person.build(name: "Alex", birthday: new LocalDate(2008, 10, 2))

		get "/person/list"
		assertStatus SC_OK
		assertTitle "Person List"

		// sort by local date
		click "Birthday"
		assertEquals("Rob", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Ilse", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Alex", byXPath("//tbody/tr[3]/td[2]").textContent)

		// sort descending
		click "Birthday"
		assertEquals("Alex", byXPath("//tbody/tr[1]/td[2]").textContent)
		assertEquals("Ilse", byXPath("//tbody/tr[2]/td[2]").textContent)
		assertEquals("Rob", byXPath("//tbody/tr[3]/td[2]").textContent)
	}

}