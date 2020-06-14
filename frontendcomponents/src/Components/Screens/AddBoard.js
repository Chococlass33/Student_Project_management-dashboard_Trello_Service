import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'

const URL = "http://15ce3c0f222c.ngrok.io"

// const URL = "localhost:3002"

class AddBoard extends Component {

    render() {
        /* Check if we have a token */
        if (this.props.location.hash) {
            const hash = queryString.parse(this.props.location.hash)
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <p>Authorised using token: {hash.token}</p>
                </div>
            );
        } else {
            /* Go get one */
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <a href={this.generateAuthString(this.props.location.state.projectId)}>
                        <button>Authorise Trello</button>
                    </a>
                </div>
            );
        }

    }

    generateAuthString(projId) {
        return "https://trello.com/1/authorize?" +
            "expiration=never" +
            "&name=Student%20Project%20Management%20Dashboard" +
            "&scope=read" +
            "&callback_method=fragment" +
            `&return_url=${URL}/addBoard?projectId=${projId}` +
            "&response_type=token" +
            `&key=${API_KEY}`
    }
}

export default AddBoard;
