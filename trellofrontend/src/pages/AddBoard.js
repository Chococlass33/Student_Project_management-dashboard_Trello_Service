import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import Loading from "../components/Loading.js";
import Card from "react-bootstrap/Card";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";

class AddBoard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            chosenBoard: undefined,
            emailAddress: undefined,
            token: undefined,

            added: false,
            errored: false,
            error: undefined
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.chosenBoard = this.props.location.state.chosenBoard
            this.state.emailAddress = this.props.location.state.emailAddress
            this.state.token = this.props.location.state.token
        }
    }

    componentDidMount() {
        console.log(`Setting up board ${this.state.chosenBoard.name} (${this.state.chosenBoard.id})`)
        const webhookBody = {
            idModel: this.state.chosenBoard.id,
            token: this.state.token
        }
        const request = {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(webhookBody)
        }
        // return this.returnIntegrationId(this.state.chosenBoard)
        //     .then(result => {
        //         if (result) {
        //             console.log(`Recorded integration id for board ${this.state.chosenBoard.name} (${this.state.chosenBoard.id})`)
        //             this.setState((state, props) => {
        //                 return {
        //                     ...state,
        //                     added: true
        //                 };
        //             })
        //         } else {
        //             this.requestFailed("Unable to add integration ID")
        //         }
        //     });

        fetch("http://localhost:5002/webhook/new", request)
            .then(response => response.text().then(text => [response, text]))
            .then(([response, text]) => {
                switch (text) {
                    case "Board already tracked":
                        console.log(text)
                        this.requestFailed(text)
                        break;
                    case "Webhook created":
                        console.log(text)

                        this.setState({...this.state, added: true})
                        break;
                        return this.returnIntegrationId(this.state.chosenBoard)
                            .then(result => {
                                if (result) {
                                    console.log(`Recorded integration id for board ${this.state.chosenBoard.name} (${this.state.chosenBoard.id})`)
                                    this.setState((state, props) => {
                                        return {
                                            ...state,
                                            added: true
                                        };
                                    })
                                } else {
                                    this.requestFailed("Unable to add integration ID")
                                }
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
                        console.log("Added integration to the central service")
                        return true
                    } else {
                        console.log("Failed to add integration to the central service", response)
                        response.text().then(console.log)
                        return false
                    }
                }
            )
    }

    requestFailed(error) {
        console.log("Something went wrong", error)
        this.setState((state, props) =>
            ({
                ...state,
                error: error.toString(),
                errored: true
            }))
    }

    render() {
        if (this.state.errored) {
            return (<Container className="d-flex justify-content-center">
                <Card className="w-50">
                    <Card.Body>
                        <Card.Title>Something went wrong</Card.Title>
                        <Card.Text>
                            There was an error trying to setup the integration.
                            <br/>
                            {this.state.error}
                        </Card.Text>
                        <Button variant="primary" href={`/?project-id=${this.state.projectId}`}>Return to add
                            integration</Button>
                    </Card.Body>
                </Card>
            </Container>)
        }
        if (!this.state.added) {
            return (<Loading iconColor={"black"}/>)
        }
        return (<Container className="d-flex justify-content-center">
            <Card className="w-50">
                <Card.Body>
                    <Card.Title>Board Added</Card.Title>
                    <Card.Subtitle className="mb-2 text-muted">{this.state.chosenBoard.name}</Card.Subtitle>
                    <Card.Text>
                        The board was added successfully.
                    </Card.Text>
                    <Button variant="primary" href={`/?project-id=${this.state.projectId}&trello-id=${this.state.chosenBoard.id}`}>View Integration</Button>
                    <Button variant="secondary" href={`/?project-id=${this.state.projectId}`}>Return to add
                        integration</Button>
                </Card.Body>
            </Card>
        </Container>)
    }
}

export default AddBoard;
