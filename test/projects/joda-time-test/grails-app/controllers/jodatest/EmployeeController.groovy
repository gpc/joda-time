package jodatest

class EmployeeController {
	static scaffold = true
	
	def beforeInterceptor = {
		if (request.method == 'POST') {
			params.each { println it }
		}
	}
}
