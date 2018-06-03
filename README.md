# feed-management

1. POST /fms/user/add - for user signup
		Note : there shouldn't be any logged in user.
		request - {
			"username":"user1",
			"name": "Name",
			"companyName": "XYZ",
			"password": <md5 encrypted password in bytes>
		}
		response - {
			"username":"user1",
			"name": "Name",
			"companyName": "XYZ",
			"following": [user2,userX...]
		}
		
2. POST /fms/signin - for user signin
		Note : there shouldn't be any logged in user.
		request - {
			"username":"user1",
			"password": <md5 encrypted password in bytes>
		}
		response - {
			"username":"user1",
			"name": "Name",
			"companyName": "XYZ",
			"following": [user2,userX...]
		}
		
3. POST /fms/signout - for logging out
		Note : someone must be logged in.

4. POST /fms/user/follow
		Note : someone must be logged in.
		request - {
			"username":"user2"
		}
		
5. POST /fms/post
		Note : someone must be logged in.
		request - {
			"this is the message being posted. this feed will contain buzzword"
		}
		
6. GET /fms/home
		Note : someone must be logged in.
		response with buzzwords (one,two) - 
			[
			    {
			        "message": "{\"0 twone t\"}",
			        "postedOn": 1528038979759,
			        "postedBy": "u2"
			    },
			    {
			        "message": "{\"3 two one two t\"}",
			        "postedOn": 1528038975628,
			        "postedBy": "u2"
			    },
			    {
			        "message": "{\"3 one one two\"}",
			        "postedOn": 1528038954141,
			        "postedBy": "u3"
			    },
			    {
			        "message": "{\"4 one one two one\"}",
			        "postedOn": 1528038950871,
			        "postedBy": "u3"
			    },
			    {
			        "message": "{\"1 tw one t\"}",
			        "postedOn": 1528038977788,
			        "postedBy": "u2"
			    },
			    {
			        "message": "{\"5 two two two two two\"}",
			        "postedOn": 1528038961452,
			        "postedBy": "u3"
			    },
			    {
			        "message": "{\"1 two\"}",
			        "postedOn": 1528038956525,
			        "postedBy": "u3"
			    },
			    {
			        "message": "{\"0 three\"}",
			        "postedOn": 1528038959260,
			        "postedBy": "u3"
			    }
			]