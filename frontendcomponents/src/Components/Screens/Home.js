import React from "react";
import {useHistory} from 'react-router-dom';
import queryString from 'query-string'

function Home(props) {
    const values = queryString.parse(props.location.search)
    const history = useHistory();
    if (values.integrationId === undefined && values.projectId !== undefined) {
        history.push({
            pathname: "/addBoard",
            state: {projectId: values.projectId}
        })
        return (
            <div>
                Redirecting...
            </div>
        );
    } else if (values.integrationId && values.projectId) {
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Home Page</h1>
                <p>
                    Project ID: {values.projectId}<br/>
                    Integration ID: {values.integrationId}
                </p>
                <p>
                    <a href={`http://localhost:3002/Actions?integrationId=${values.integrationId}`}>View Actions</a><br/>
                    <a href={`http://localhost:3002/Board?integrationId=${values.integrationId}`}>View Board</a>
                </p>

            </div>
        );
    } else {
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Home Page</h1>
                <p>No project or integration selected</p>
            </div>
        );
    }
}

export default Home;
