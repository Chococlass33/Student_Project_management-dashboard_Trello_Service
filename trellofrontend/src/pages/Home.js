import React from "react";
import {Redirect} from 'react-router-dom';
import queryString from 'query-string'
import {useGoogleAuth} from "../components/GoogleAuthProvider.js";
import Loading from "../components/Loading.js";

function Home(props) {
    const values = queryString.parse(props.location.search)
    const projectId = values['project-id'];
    const integrationId = values['trello-id'];

    console.log(useGoogleAuth())
    const {signIn, googleUser, isInitialized, isSignedIn} = useGoogleAuth()
    let emailAddress = undefined;
    if (!isInitialized) {
        return (<Loading iconColor={"black"}/>)
    } else if (!isSignedIn) {
        // refreshUser()
        signIn()
        return (<Loading iconColor={"black"}/>)
    }
    emailAddress = googleUser.getBasicProfile().getEmail()


    /* If there is a project, and no integration
     * Then we want to go and add a new one */
    if (integrationId === undefined && projectId !== undefined) {
        /* Firstly, we need to be authenticated */
        return (
            <Redirect to={{
                pathname: "/pickBoard",
                state: {projectId: projectId, emailAddress: emailAddress}
            }}
            />
        )
        /* We have both an integration and a project so we go and display that */
    } else if (integrationId && projectId) {
        return (
            <Redirect to={{
                pathname: "/viewBoard",
                state: {projectId: projectId, integrationId: integrationId}
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
