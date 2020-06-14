import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'

class AddBoard extends Component {
    componentDidMount() {
        const values = queryString.parse(this.props.location.search)

        console.log(values.projectId)

    }

    render() {
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Home Page</h1>
                <p>Student ID</p>
            </div>
        );
    }

    generateAuthString() {
        return "https://trello.com/1/authorize?expiration=never&name=Student%20Project%20Management%20Dashboard&scope=read&response_type=token&key=" + API_KEY
    }
}

export default AddBoard;
