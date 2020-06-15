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

Once ngrok is run, you will need to alter the url specified in the `CALLBACK_URL` field of the`com.spmd.trello.WebhookController` file in the backed. Ensure to keep the `/webhook` ending. Next you will need to update the frontend tunnel url specified in the `@CrossOrigin` annotation on the same file  
Additionally you will need to change the `URL` constant specified in the `src/Components/Screens/AddBoard.js` file of the frontend

# Running The Backend
To run the backend simply run 
```shell script
./gradlew bootRun
```
in the main directory of the project

# Running The Frontend
To run the frontend simply run
```shell script
npm start
```
in the `frontendcomponents` directory
