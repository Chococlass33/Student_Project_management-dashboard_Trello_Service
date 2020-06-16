import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'

class Actions extends Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    componentDidMount() {
        const values = queryString.parse(this.props.location.search)
        fetch(`http://167.99.7.70:5002/actions?boardId=${values.integrationId}`)
            .then(response => response.json())
            .then(actions => actions.sort((one, two) => two.dateCreated.localeCompare(one.dateCreated)))
            .then(actions => {
                console.log(actions)
                return actions
            })
            .then(actions => actions.map(action => {
                action.data = JSON.parse(action.data) // We need to use this because our json isn't "Strict"
                return action
            }))
            .then(actions => actions.map(action => (
                <li>
                    <b>Action Type:</b> {action.type}<br/>
                    <b>Member:</b> {action.memberId}<br/>
                    <b>Card Name:</b> {action.data.card.name}<br/>
                    <b>Card URL:</b> <a
                    href={`https://trello.com/c/${action.data.card.shortLink}`}>https://trello.com/c/{action.data.card.shortLink}</a><br/>
                    <div>
                        <i>
                            {
                                action.type === "updateCard" ? ((action.data.listAfter && action.data.listBefore)
                                    ? `Card moved lists from ${action.data.listBefore.name} to ${action.data.listAfter.name}`
                                    : this.getActionFields(action)) : ""
                            }</i>
                    </div>

                    <br/>
                </li>
            )))
            .then(actions =>
                this.setState({
                    actions: actions
                })
            )
    }

    render() {
        if (this.state.actions) {
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Student Activity</h1>
                    <p>Action Log</p>
                    <ul>{this.state.actions}</ul>
                </div>
            );
        } else {
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Student Activity</h1>
                    <p>Action Log</p>
                    <p>Loading...</p>
                </div>
            );
        }
    }

    getActionFields(action) {
        console.log(action)
        const field = Object.keys(action.data.old).pop()
        console.log(action.data.old[field])
        console.log(action.data.card[field])
        return `Field '${field}' was updated from '${action.data.old[field]}' into '${action.data.card[field]}' `
    }
}


export default Actions;
