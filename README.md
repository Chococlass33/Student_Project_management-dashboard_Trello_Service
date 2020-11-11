# Student Project Management Dashboard

# Prerequisites
As this project involves callback urls and webhooks, a way to expose localhost is needed.  
To avoid having to fiddle with ports and network configuration an nGrok config file is provided.

As such, before running either the backend and/or the frontend it is recommended to run ngrok with this config.  
```shell script
./ngrok start --all --config ngrok.yml
```
Please note, that the ngrok binary and the given command are intended for use on a Linux system.
If you are using a different OS you may need to obtain a different binary and tweak the command.

Once ngrok is running, take note of which urls are being tunneled to which localhost ports.  
The url tunnelling to `localhost:3002` is the _frontend_ URL  
The URL tunnelling to `localhost:5002` is the _backend_ URL
# Running The Backend
To run the backend from source simply run 
```shell script
./gradlew bootRun -Pargs=--frontend="<frontend url>",--backend="<backend url>"
```
in the main directory of the project

To run a jar use,
```shell script
java -jar build/libs/trello-0.0.1-SNAPSHOT.jar --frontend="<frontend url>" --backend="<backend url>"
```


# Running The Frontend
To run the frontend simply run
```shell script
REACT_APP_DOMAIN=<frontend url> npm start 
```
in the `frontendcomponents` directory

At present, one cannot run a production build on a local server due to requiring the ngrok tunnel

# Note
As stated above, these all assume that you are running this on a linux box. The specifics may be different on another OS