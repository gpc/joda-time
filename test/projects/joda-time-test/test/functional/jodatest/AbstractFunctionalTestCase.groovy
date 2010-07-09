package jodatest

import functionaltestplugin.FunctionalTestCase
import org.w3c.dom.Element

abstract class AbstractFunctionalTestCase extends FunctionalTestCase {

	void setUp() {
		super.setUp()

		def port = System.properties."server.port" ?: 8080
		baseURL = "http://localhost:${port}/"
	}

	void assertTextByXPath(String expected, String xpath) {
		Element node = byXPath(xpath)
		if (!node) fail "$xpath matched no nodes"
		assertEquals(expected, node.textContent)
	}

}