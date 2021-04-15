# AtomSuperDivision-backend

## Environment Variables

|Name                | Value                        |
|--------------------|------------------------------|
|DATASOURCE_URL	     |{your database name}          |
|DATASOURCE_USERNAME |{your local mysql username}   |
|DATASOURCE_PASSWORD |{your local mysql password}   |
|HIBERNATE_DIALECT   |{necessary hibernate dialect} |
|TEST_DATASOURCE_URL |{your test database name}     |
|LOGGING_LEVEL       |{INFO}                           |
|CLIENT_ID           |{2fd7d5d7a947e005e3c1}                           |
|REDIRECT_URI        |{url to redirect}             |        
|CLIENT SECRET       |{2377b2ae601e5840835a3cad54e7233f05fe2a66}                           |
|JWT_SECRET_KEY      |{add a 64bit secret key}     |

##GET /memes
> - Header: X-meme-token 
> - Body: {}
####  Responses
* if the meme repository contains memes it returns 
> HTTP 200 status and sends back a JSON with a list of meme objects }
* if the meme repository does not contain any memes it returns 
> HTTP 200 status and sends back an empty list }

##GET /meme/{id}
> - Header: X-meme-token 
> - Body: {}
> - Pathvariable: id
####  Responses
* if a meme exists with the searched id it returns
> HTTP 200 status and sends back a JSON with the meme object }
* if a meme does not exist with the searched id it returns 
> HTTP 400 status and sends back a message:{"No meme found with given ID"}

##DELETE /meme/delete/{id}
> - Header: X-meme-token 
> - Body: {}
> - Pathvariable: id
####  Responses
* if a meme exists with the searched id it returns
> HTTP 200 status and sends back a message: "Meme with id: "+id+ " successfully deleted." }
* if a meme does not exist with the searched id it returns 
> HTTP 400 status and sends back a message:{"No meme found with given ID"}


##POST /meme
> - Header: X-meme-token 
> - Body: {   "url":"example",
              "caption":"hello"}
####  Responses
* if meme creation was successful it returns
> HTTP 200 status and sends back a JSON with meme's caption and time of creation }


##POST /meme/{id}
> - Header: X-meme-token
> - Pathvariable: id 
> - Body: {   "url":"example",
              "caption":"hello"}
####  Responses
* if comment creation was successful it returns
> HTTP 200 status and sends back a JSON with the meme and the belonging comments }
* if a meme does not exist with the searched id it returns 
> HTTP 400 status and sends back a message:{"No meme found with given ID"}


##DELETE /meme/comment/{id}
> - Header: X-meme-token
> - Pathvariable: id 
> - Body: {}
####  Responses
* if comment deletion was successful it returns
> HTTP 200 status }
* if a comment does not exist with the searched id it returns 
> HTTP 400 status and sends back a message:{"No comment found with this ID"}


##GET /memes/trending
> - Header: X-meme-token
> - Body: {}
####  Responses
* if the meme repository contains memes it returns 
> HTTP 200 status and sends back a JSON with a list of meme objects listed by most viewed }
* if the meme repository does not contain any memes it returns 
> HTTP 200 status and sends back an empty list }


##GET /genre
> - Header: X-meme-token
> - RequestParam: genre
> - Body: {}
####  Responses
* if the meme repository contains memes it returns 
> HTTP 200 status and sends back a JSON with a list of meme objects listed by searched genre }
* if the meme repository does not contain any memes of searched genre it returns 
> HTTP 200 status and sends back an empty list }