import React, {useState} from "react";
import {Redirect} from 'react-router-dom';
import queryString from 'query-string'
import {useGoogleAuth} from "../components/GoogleAuthProvider.js";
import Loading from "../components/Loading.js";
import TrelloClient, {Trello} from "react-trello-client";

function Home(props) {
    const values = queryString.parse(props.location.search)
    const projectId = values['project-id'];
    const integrationId = values['trello-id'];

    const [trelloToken, setToken] = useState(undefined)

    /* Auth with google */
    const {signIn, googleUser, isInitialized, isSignedIn} = useGoogleAuth()
    let emailAddress = undefined;
    if (!isInitialized) {
        return (<Loading iconColor={"black"}/>)
    } else if (!isSignedIn) {
        signIn()
        return (<Loading iconColor={"black"}/>)
    }
    emailAddress = googleUser.getBasicProfile().getEmail()

    /* Auth with Trello */
    // const isAuthed = Trello.authorized && Trello.authorized()
    /* Authenticate if we need to */
    if (!trelloToken) {
        console.log("Authenticating Trello")
        return (<TrelloClient
            apiKey="38e2c9e0bd5f083ac3e8e19ed8a1a5fa" // Get the API key from https://trello.com/app-key/
            clientVersion={1} // number: {1}, {2}, {3}
            apiEndpoint="https://api.trello.com" // string: "https://api.trello.com"
            authEndpoint="https://trello.com" // string: "https://trello.com"
            intentEndpoint="https://trello.com" // string: "https://trello.com"
            authorizeName="React Trello Client" // string: "React Trello Client"
            authorizeType="popup" // string: popup | redirect
            authorizePersist={true}
            authorizeInteractive={false}
            authorizeScopeRead={true} // boolean: {true} | {false}
            authorizeScopeWrite={true} // boolean: {true} | {false}
            authorizeScopeAccount={true} // boolean: {true} | {false}
            authorizeExpiration="never" // string: "1hour", "1day", "30days" | "never"
            authorizeOnSuccess={() => {
                console.log("Authenticated Trello")
                setToken(Trello.token())
            }}
            authorizeOnError={() => {
                console.log("Failed to auth Trello")
            }}
            autoAuthorize={true} // boolean: {true} | {false}
            authorizeButton={true} // boolean: {true} | {false}
            buttonStyle="flat" // string: "metamorph" | "flat"
            buttonColor="grayish-blue" // string: "green" | "grayish-blue" | "light"
            buttonText="Authenticate" // string: "Login with Trello"
        />)
    }

    /* If there is a project, and no integration
     * Then we want to go and add a new one */
    if (integrationId === undefined && projectId !== undefined) {
        /* Firstly, we need to be authenticated */
        return (
            <Redirect to={{
                pathname: "/pickBoard",
                state: {projectId: projectId, emailAddress: emailAddress, token: trelloToken}
            }}
            />
        )
        /* We have both an integration and a project so we go and display that */
    } else if (integrationId && projectId) {
        return (
            <Redirect to={{
                pathname: "/viewBoard",
                state: {projectId: projectId, integrationId: integrationId, token: trelloToken}
            }}
            />
        );
    } else {
        return (
            <div>
                <p>No project or integration selected</p>
            </div>
        );
    }
}

export default Home;
