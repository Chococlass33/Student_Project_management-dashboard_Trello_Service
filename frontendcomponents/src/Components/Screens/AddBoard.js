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
            projectId: undefined,
            boards: undefined
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
        } else {
            const values = queryString.parse(this.props.location.search)
            this.state.projectId = values.projectId
        }
    }

    componentDidMount() {
        if (this.state.token) {
            console.log("Fetching Boards")
            fetch(`https://api.trello.com/1/members/me/boards?fields=name,url&key=${API_KEY}&token=${this.state.token}`)
                .then(res => res.json())
                .then((result) => {
                        console.log("done")
                        this.setState((state, props) => {
                            return  {
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
                        <a target="_blank"  href={board.url}>Board URL</a>
                        <button onClick={this.useBoard.bind(this, board)}>Use Board</button>
                    </div>)
                );
                return (
                    <div>
                        <h1 style={{marginTop: "64px"}}>Add Board</h1>
                        <tbody>{boards}</tbody>
                    </div>
                )
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

    useBoard(board) {
        console.log(board)
    }
}

export default AddBoard;
