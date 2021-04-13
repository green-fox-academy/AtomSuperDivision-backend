# AtomSuperDivision-backend

## Environment Variables

|Name                | Value                        |
|--------------------|------------------------------|
|DATASOURCE_URL	     |{your database name}          |
|DATASOURCE_USERNAME |{your local mysql username}   |
|DATASOURCE_PASSWORD |{your local mysql password}   |
|HIBERNATE_DIALECT   |{necessary hibernate dialect} |
|TEST_DATASOURCE_URL |{your test database name}     |
|LOGGING_LEVEL       |{?}                           |
|CLIENT_ID           |{?}                           |
|REDIRECT_URI        |{url to redirect}             |        
|CLIENT SECRET       |{?}                           |
|JWT_SECRET_KEY      |{add a 64bit seccret key}     |

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
