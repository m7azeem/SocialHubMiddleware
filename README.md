# SocialHubMiddleware
Middleware to connect between twitter and instagram APIs, and our frontend

## Getting started 

**Anypoint Studio**
1. Install [AnyPoint Studio](https://www.mulesoft.com/lp/dl/studio)
2. Open AnyPoint Studio (we are going to follow [this guide](https://docs.mulesoft.com/mule-user-guide/v/3.3/using-git-with-studio))
3. Go to Help > Install New Software...
4. Paste this in **work with** field ``` Egit Update Site - http://download.eclipse.org/egit/updates-2.1 ```
5. After installation, restart mule
6. Go to File > Import...
7. Select Git > Projects from Git > URI
8. Paste the URI ``` https://github.com/m7azeem/SocialHubMiddleware.git ``` and clone the **master** branch
9. Import existsing project, if there is already a project, then please delete those. 
10. You can run the application as a **Mule Application**
11. Upon running you can visit ```http://localhost:8082/socialhub/console``` for console and for api usage ```http://localhost:8082/socialhub/api/```. Check API Documentation for more details.
12. Later, you can update your repository by right clicking the folder in project explorer and selecting ```Team > Pull```

**MongoDB**
1. Install MongoDB
2. Open terminal/cmd and run ```mongod``` or ```sudo mongod``` (for mac) to run a mongoDB server
3. Open another terminal/cmd and run ```mongo``` to connect mongoDB server

**Postman**
1. Download and install [postman](https://www.getpostman.com/), to test API
2. Open postman, and test any API using GET/POST methods
3. For POST methods, make sure in body you select **raw** with **JSON(application/JSON)**, you can now post your JSON data and send it to the server.


## API documentation
- Check out and test the API calls avaliable from http://localhost:8082/socialhub/console/