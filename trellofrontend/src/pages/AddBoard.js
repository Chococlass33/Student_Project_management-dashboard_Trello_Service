import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import TrelloClient, {Trello} from "react-trello-client";
import queryString from 'query-string'
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Container from "react-bootstrap/Container";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const API_KEY = "38e2c9e0bd5f083ac3e8e19ed8a1a5fa"

class AddBoard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            boards: undefined,
            emailAddress: undefined,
            hasWebhook: false,
            webhookError: undefined,
            chosenBoard: undefined,
            showModal: false,
            confirmBoard: undefined
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.emailAddress = this.props.location.state.emailAddress
        } else {
            const values = queryString.parse(this.props.location.search)
            this.state.projectId = values['project-id'];
        }
    }

    isAuthed() {
        return Trello.authorized && Trello.authorized()
    }

    componentDidMount() {
        if (this.isAuthed() && !this.state.boards) {
            console.log("Fetching Boards")
            fetch(`https://api.trello.com/1/members/me/boards?fields=name,url&key=${API_KEY}&token=${Trello.token()}`)
                .then(res => res.json())
                .then((result) => {
                        console.log("Boards Fetched")
                        this.setState((state, props) =>
                            ({...state, boards: result}))
                    },
                    (error) => {
                        console.log("Error Fetching Board")
                        this.setState((state, props) => {
                            return {...state, boards: undefined};
                        })
                    })
        }
    }

    render() {
        /* Authenticate if we need to */
        if (!this.isAuthed()) {
            console.log("Authenticating Trello")
            return (<TrelloClient
                apiKey="38e2c9e0bd5f083ac3e8e19ed8a1a5fa" // Get the API key from https://trello.com/app-key/
                clientVersion={1} // number: {1}, {2}, {3}
                apiEndpoint="https://api.trello.com" // string: "https://api.trello.com"
                authEndpoint="https://trello.com" // string: "https://trello.com"
                intentEndpoint="https://trello.com" // string: "https://trello.com"
                authorizeName="React Trello Client" // string: "React Trello Client"
                authorizeType="popup" // string: popup | redirect
                authorizePersist={true}
                authorizeInteractive={false}
                authorizeScopeRead={true} // boolean: {true} | {false}
                authorizeScopeWrite={true} // boolean: {true} | {false}
                authorizeScopeAccount={true} // boolean: {true} | {false}
                authorizeExpiration="never" // string: "1hour", "1day", "30days" | "never"
                authorizeOnSuccess={() => {
                    console.log("Authenticated Trello")
                }}
                authorizeOnError={() => {
                    console.log("Failed to auth Trello")
                }}
                autoAuthorize={true} // boolean: {true} | {false}
                authorizeButton={true} // boolean: {true} | {false}
                buttonStyle="flat" // string: "metamorph" | "flat"
                buttonColor="grayish-blue" // string: "green" | "grayish-blue" | "light"
                buttonText="Authenticate" // string: "Login with Trello"
            />)
        }

        /* Check if we have a token */
        if (this.state.projectId) {
            if (this.state.boards) {
                const boards = this.state.boards.map((board, i) =>
                    (<ListGroup.Item action onClick={this.showModal.bind(this, board, Trello.token())}>
                        {board.name}
                    </ListGroup.Item>)
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
                                <a href={`/?project-id=${this.state.projectId}&trello-id=${this.state.chosenBoard.id}`}>View
                                    Integration</a>
                                <br/>
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
                        <>
                            <Modal show={this.state.showModal}
                                   onHide={this.closeModal.bind(this)}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Confirm Board</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>You are adding the board {this.state.confirmBoard?.name}</Modal.Body>
                                <Modal.Footer>
                                    <Button target="_blank"
                                            variant="secondary"
                                            href={this.state.confirmBoard?.url}>
                                        View Board
                                    </Button>
                                    <Button variant="primary" onClick={this.closeModal.bind(this)}>
                                        Confirm
                                    </Button>
                                </Modal.Footer>
                            </Modal>
                            <Container className="d-flex justify-content-center">
                                <Card className="w-50">
                                    <Card.Body>
                                        <Card.Title>Add a Board</Card.Title>
                                        <Card.Subtitle className="mb-2 text-muted">Clicking will show more details and
                                            confirmation</Card.Subtitle>
                                        <Card.Text>
                                            <ListGroup>
                                                {boards}
                                            </ListGroup>
                                        </Card.Text>
                                    </Card.Body>
                                </Card>
                            </Container>
                        </>
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
        }
        return (
            <div>
                <h1 style={{marginTop: "64px"}}>Add Board</h1>
                <p>ERROR</p>
            </div>
        );
    }

    closeModal() {
        if (this.state.showModal) {
            this.setState({...this.state, showModal: false, confirmBoard: undefined})
        }
    }

    showModal(board) {
        if (!this.state.showModal) {
            this.setState({...this.state, showModal: true, confirmBoard: board})
        }
    }

    /**
     * Contacts the backend API service to setup tracking for the board
     * @param board The information of the board to track
     * @param token The trello auth token to use
     */
    useBoard(board, token) {
        console.log(`Setting up board ${board.name} (${board.id})`)
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
            .then(response => response.text().then(text => [response, text]))
            .then(([response, text]) => {
                switch (text) {
                    case "Board already tracked":
                        console.log(text)
                        this.requestFailed(text)
                        break;
                    case "Webhook already existed, but board still scraped":
                    case "Webhook created & Board Scraped":
                        console.log(text)
                        return this.returnIntegrationId(board)
                            .then(() => {
                                console.log(`Recorded integration id for board ${board.name} (${board.id})`)
                                this.setState((state, props) => {
                                    return {
                                        ...state,
                                        hasWebhook: true,
                                        webhookError: undefined,
                                        chosenBoard: board
                                    };
                                })
                            });
                    default:
                        console.log(`Unknown response when setting webhook. Got text '${text}'`, response)
                        this.requestFailed(`Unknown response when setting webhook. Got text '${text}'`)
                        break;
                }
            })
            .catch(reason => this.requestFailed(reason))
    }

    returnIntegrationId(board) {
        const request = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                trelloName: board.name,
                emailAddress: this.state.emailAddress,
                trelloId: board.id,
                projectId: this.state.projectId
            })
        }
        return fetch("http://spmdhomepage-env.eba-upzkmcvz.ap-southeast-2.elasticbeanstalk.com/user-project-service/save-trello", request)
            .then(response => {
                    if (response.ok) {
                        console.log("Central service responded OK")
                    } else {
                        console.log("Central service responded ERROR", response)
                    }
                }
            )
    }

    requestFailed(error) {
        console.log("Request failed: " + error)
        this.setState((state, props) =>
            ({
                ...state,
                hasWebhook: false,
                webhookError: error
            }))
    }
}

export default AddBoard;
