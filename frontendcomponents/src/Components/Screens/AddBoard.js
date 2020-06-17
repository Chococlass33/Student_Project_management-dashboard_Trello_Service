import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import queryString from 'query-string'

const API_KEY = "38e2c9e0bd5f083ac3e8e19ed8a1a5fa"

// const URL = "167.99.7.70:3002"

class AddBoard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            token: this.props.location.hash ? queryString.parse(this.props.location.hash).token : undefined,
            projectId: undefined,
            boards: undefined,
            hasWebhook: false,
            webhookError: undefined,
            chosenBoard: undefined
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
        } else {
            const values = queryString.parse(this.props.location.search)
            this.state.projectId = values.projectId
        }
    }

    componentDidMount() {
        if (this.state.token && !this.state.boards) {
            console.log("Fetching Boards")
            fetch(`https://api.trello.com/1/members/me/boards?fields=name,url&key=${API_KEY}&token=${this.state.token}`)
                .then(res => res.json())
                .then((result) => {
                        console.log("done")
                        this.setState((state, props) => {
                            return {
                                ...state,
                                boards: result
                            };
                        })
                    },
                    (error) => {
                        console.log("error:" + error)
                        this.setState((state, props) => {
                            return state + {boards: undefined};
                        })
                    })
        }
    }

    render() {
        /* Check if we have a token */
        if (this.state.token && this.state.projectId) {
            console.log("boards: " + this.state.boards)
            if (this.state.boards) {
                const boards = this.state.boards.map((board, i) =>
                    (<div>
                        Board: {board.name}<br/>
                        <a target="_blank" href={board.url}>Board URL</a>
                        <button onClick={this.useBoard.bind(this, board, this.state.token)}>Use Board</button>
                    </div>)
                );
                if (this.state.hasWebhook) {
                    return (
                        <div>
                            <h1 style={{marginTop: "64px"}}>Add Board</h1>
                            <p>
                                Board webhook made for board "{this.state.chosenBoard.name}"
                                ({this.state.chosenBoard.id})<br/>
                                <a target="_blank" href={this.state.chosenBoard.url}>Board URL</a>
                            </p>
                            <p>
                                <a href={`http://167.99.7.70:3002/?projectId=${this.state.projectId}&integrationId=${this.state.chosenBoard.id}`}>View
                                    Integration</a>
                                <br/>
                                <a href={`http://localhost:3000/project?project_id=${this.state.projectId}`}> Return to
                                    add integration</a>
                            </p>
                        </div>
                    )
                } else if (this.state.webhookError) {
                    return (
                        <div>
                            <h1 style={{marginTop: "64px"}}>Add Board</h1>
                            <p>
                                Unable to create webhook for selected board.<br/>
                                Please try again, choose a different board, or return to the choose integration page.

                                Error: "{this.state.webhookError.toString()}"
                            </p>
                            <tbody>{boards}</tbody>
                        </div>
                    )
                } else {
                    return (
                        <div>
                            <h1 style={{marginTop: "64px"}}>Add Board</h1>
                            <tbody>{boards}</tbody>
                        </div>
                    )
                }

            } else {
                return (
                    <div>
                        <h1 style={{marginTop: "64px"}}>Add Board</h1>
                        <p>Loading trello boards</p>
                    </div>
                );
            }
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


    /**
     * Generates the url to obtain authentication from trello
     * @param projId The id of the project
     * @returns {string} The URL to redirect to
     */
    generateAuthString(projId) {
        console.log(process.env)
        console.log(process.env.DOMAIN)
        return "https://trello.com/1/authorize?" +
            "expiration=never" +
            "&name=Student%20Project%20Management%20Dashboard" +
            "&scope=read" +
            "&callback_method=fragment" +
            `&return_url=http://167.99.7.70:3002/addBoard?projectId=${projId}` +
            "&response_type=token" +
            `&key=${API_KEY}`
    }

    /**
     * Contacts the backend API service to setup tracking for the board
     * @param board The information of the board to track
     * @param token The trello auth token to use
     */
    useBoard(board, token) {
        console.log(board)
        const webhookBody = {
            idModel: board.id,
            token: token
        }
        const request = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(webhookBody)
        }
        fetch("http://167.99.7.70:5002/webhook/new", request)
            .then((result) => {
                if (result.ok) {
                    console.log("Request Succeeded: " + result)
                    // this.returnIntegrationId(board.id, this.state.projectId)
                    //     .then(() => {
                            this.setState((state, props) => {
                                return {
                                    ...state,
                                    hasWebhook: true,
                                    webhookError: undefined,
                                    chosenBoard: board
                                };
                            })
                        // });

                } else {
                    result.text()
                        .then(JSON.parse)
                        .then(result => {
                            console.log(result.message)
                            this.requestFailed(result.message)
                        })
                }
            }, this.requestFailed.bind(this))
    }

    returnIntegrationId(boardId, projectId) {
        const request = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                trelloId: boardId,
                projectId: projectId
            })
        }
        return fetch("http://localhost:5000/user-project-service/save-trello", request).then(
            response => {
                if (response.ok) {
                    console.log(`Returned integration id successfully: '${response.text()}'`)
                } else {
                    response.text()
                        .then(JSON.parse)
                        .then(result => console.log(result.message))
                }
            }
        )
    }

    requestFailed(error) {
        console.log("Request failed: " + error)
        this.setState((state, props) => {
            return {
                ...state,
                hasWebhook: false,
                webhookError: error
            };
        })
    }
}

export default AddBoard;
