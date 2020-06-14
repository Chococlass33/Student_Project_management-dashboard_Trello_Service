import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'
class Home extends Component {
    componentDidMount() {
        const values = queryString.parse(this.props.location.search)

        /* Check if we want to make a new integration, and if so redirect */
        if (values.integrationId === undefined) {
            //TODO: Redirect to the new integration page
        } else {
            //TODO: Just start displaying shit
        }
        console.log(values.projectId)
        console.log(values.integrationId)
    }

    render() {
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Home Page</h1>
                <p>Student ID</p>
            </div>
        );
    }
}

export default Home;
