package jodatest

class Employee {

	String name
	Job job

	static embedded = ['job']

	static constraints = {
		name blank: false
	}
}

