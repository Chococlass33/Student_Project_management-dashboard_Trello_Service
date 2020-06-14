import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'

const API_KEY = "38e2c9e0bd5f083ac3e8e19ed8a1a5fa"
const URL = "http://15ce3c0f222c.ngrok.io"

// const URL = "localhost:3002"

class AddBoard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            token: this.props.location.hash ? queryString.parse(this.props.location.hash).token : undefined,
            projectId: undefined
        };
        if (this.props.location.state) {
            this.state.projectId  = this.props.location.state.projectId
        } else {
            const values = queryString.parse(this.props.location.search)
            this.state.projectId = values.projectId
        }
    }

    render() {
        /* Check if we have a token */
        if (this.state.token && this.state.projectId) {
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <p>Authorised using token: {this.state.token} for projectId {this.state.projectId}</p>
                </div>
            );
        } else if (this.state.projectId) {
            /* Go get one */
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <a href={this.generateAuthString(this.state.projectId)}>
                        <button>Authorise Trello</button>
                    </a>
                </div>
            );
        } else {
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <p>ERROR</p>
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
