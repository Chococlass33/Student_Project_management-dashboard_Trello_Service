import React from "react";
import {Trello} from 'react-trello-client'
import {Redirect} from 'react-router-dom';
import queryString from 'query-string'

function Home(props) {
    const values = queryString.parse(props.location.search)
    const projectId = values['project-id'];
    const integrationId = values['trello-id'];
    console.log("obj")
    console.log(Trello)
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
            <div>
                <p>
                    Project ID: {projectId}<br/>
                    Integration ID: {integrationId}
                </p>
                <p>
                    <a href={`/Actions?integrationId=${integrationId}`}>View Actions</a><br/>
                    <a href={`/Board?integrationId=${integrationId}`}>View Board</a>
                </p>

            </div>
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
