class UrlMappings {
	static mappings = {
		"/timestamp"(controller: 'timestamp') {
			action = [GET: 'get', PUT: 'put']
		}
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
