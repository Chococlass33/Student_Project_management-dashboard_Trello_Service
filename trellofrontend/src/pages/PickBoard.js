import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import TrelloClient, {Trello} from "react-trello-client";
import queryString from 'query-string'
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Container from "react-bootstrap/Container";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import {Redirect} from "react-router-dom";

const API_KEY = "38e2c9e0bd5f083ac3e8e19ed8a1a5fa"

class PickBoard extends Component {

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
            confirmBoard: undefined,
            redirect: false
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
        if (this.state.boards) {
            const boards = this.state.boards.map((board, i) =>
                (<ListGroup.Item action onClick={this.showModal.bind(this, board, Trello.token())}>
                    {board.name}
                </ListGroup.Item>)
            );
            if (this.state.redirect) {
                return (<Redirect to={{
                    pathname: "/addBoard",
                    state: {
                        projectId: this.state.projectId,
                        emailAddress: this.state.emailAddress,
                        chosenBoard: this.state.confirmBoard,
                        token: Trello.token()
                    }
                }}
                />)
            }
            return (<>
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
                        <Button variant="primary" onClick={this.confirmBoard.bind(this)}>
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
            </>)

        } else {
            return (
                <div>
                    <h1 style={{marginTop: "64px"}}>Add Board</h1>
                    <p>Loading trello boards</p>
                </div>
            );
        }
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

    confirmBoard() {
        this.closeModal()
        this.setState({...this.state, redirect: true})
    }

}

export default PickBoard;
