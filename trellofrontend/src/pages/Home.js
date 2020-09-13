import React from "react";
import {Redirect} from 'react-router-dom';
import queryString from 'query-string'

function Home(props) {
    const values = queryString.parse(props.location.search)
    const projectId = values['project-id'];
    const integrationId = values['trello-id'];
    /* If there is a project, and no integration
     * Then we want to go and add a new one */
    if (integrationId === undefined && projectId !== undefined) {
        /* Firstly, we need to be authenticated */
        return (
            <Redirect to={{
                pathname: "/addBoard",
                state: {projectId: projectId}
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
