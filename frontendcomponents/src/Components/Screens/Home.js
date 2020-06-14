import React from "react";
import {useHistory} from 'react-router-dom';
import queryString from 'query-string'

function Home(props) {
    const values = queryString.parse(props.location.search)
    const history = useHistory();
    if (values.integrationId === undefined && values.projectId !== undefined) {
        history.push({
            pathname: "/addBoard",
            state: {projectId:values.projectId}
        })
        return (
            <div>
                Redirecting...
            </div>
        );
    } else {
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Home Page</h1>
                <p>Student ID</p>
            </div>
        );
    }
}

export default Home;
