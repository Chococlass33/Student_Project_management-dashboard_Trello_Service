import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import Chart from "react-google-charts";
import Loading from "../components/Loading.js";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Card from "react-bootstrap/Card";
import ListGroup from "react-bootstrap/ListGroup";
import Button from "react-bootstrap/Button";


/**
 * The page that handles showing the visualisations.
 * Makes API calls,
 * then processes them,
 * then displays them using google charts
 */
class ViewBoard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            boardId: undefined,
            loaded: false, // Have we got data
            ready: false, // Have we processed it
            token: undefined,
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.token = this.props.location.state.token
            this.state.boardId = this.props.location.state.integrationId
        }
    }

    /**
     * When the component is mounted, lets make all our api calls
     */
    componentDidMount() {
        /* If we haven't already do all our requests */
        if (!this.state.loaded) {
            /* Get the members for the board */
            this.doRequest(`http://localhost:5002/data/cardMembers/${this.state.boardId}`,
                'cardMembers', {})
                /* Get card data */
                .then(data => this.doRequest(`http://localhost:5002/data/boardDetails/${this.state.boardId}`,
                    'boardData', data))
                /* Get the size of all the lists */
                .then(data => this.doRequest(`http://localhost:5002/data/listSizes/${this.state.boardId}`,
                    'listSizes', data))
                .then(data => this.doRequest(`http://localhost:5002/history/${this.state.boardId}`,
                    'history', data))
                /* Save this in the state */
                .then(data => this.setState({...data, ...this.state, loaded: true}))
        }
    }

    /**
     * Performs a request and then adds the result to the data
     * @param request The url to make the request to
     * @param prop The name of the field to store the result in
     * @param data The running data object
     */
    doRequest(request, prop, data) {
        return fetch(`${request}?token=${this.state.token}`)
            .then(response => response.json())
            .then(response => {
                let newData = {}
                newData[prop] = response
                return {...data, ...newData}
            })
            .catch(response => {
                let newData = {}
                newData[prop] = undefined
                return {...data, ...newData}
            })
    }


    /**
     * Once the component updates,
     * if it's not ready yet, then we process the data we just got
     * and mark the page ready
     */
    componentDidUpdate() {
        if (this.state.loaded && !this.state.ready) {
            console.log("State after API calls", this.state)
            this.setState({
                ...this.state,
                ready: true,
                cardMembers: this.state.cardMembers.map(entry => [entry.label, entry.value]),
                listSizes: this.state.listSizes.map(entry => [entry.label, entry.value])
            })
        }
    }


    render() {
        /* We have no project or trello id. Something has gone wrong */
        if (!this.state.projectId || !this.state.boardId) {
            return <h1>Error. No project id and/or board id.</h1>
        }
        /* We have not loaded the data, or not processed it */
        if (!this.state.loaded || !this.state.ready) {
            return (<Loading iconColor={"black"}/>)
        }
        /* We have no board data */
        if (!this.state.boardData) {
            return (<h1>Invalid Trello Id</h1>)
        }
        /* Show the visualisations */
        return (
            <Container fluid>
                <Row>
                    <Col>
                        <h1>
                            {this.state.boardData.name}
                        </h1>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Card className="rounded shadow-sm">
                            <Card.Body>
                                <Card.Title>Basic Information</Card.Title>
                                <ListGroup variant="flush">
                                    <ListGroup.Item>Date Last Active
                                        - {this.state.boardData.dateLastActivity}</ListGroup.Item>
                                    <ListGroup.Item>Number of Members - {this.state.boardData.memberCount}</ListGroup.Item>
                                    <ListGroup.Item>Number of Lists - {this.state.boardData.listCount}</ListGroup.Item>
                                </ListGroup>
                                <Button target="_blank" className="float-right" href={this.state.boardData.shortUrl}
                                        varient="primary">View
                                    Board</Button>
                                <Button className="float-left"
                                        href={`/viewHistory?project-id=${this.state.projectId}&trello-id=${this.state.boardId}&token=${this.state.token}`}
                                        varient="primary">View History</Button>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col>
                        <Card className="rounded shadow-sm">
                            <Card.Body>
                                <Card.Title>Member Breakdown</Card.Title>
                                <Card.Subtitle className="mb-2 text-muted">Num of cards assigned to
                                    Members</Card.Subtitle>
                                <Chart
                                    height={'300px'}
                                    chartType="PieChart"
                                    loader={<div>Loading Chart</div>}
                                    data={[
                                        ['Members', 'Num Cards'],
                                        ...this.state.cardMembers
                                    ]}
                                    options={{
                                        title: 'Card Allocation'
                                    }}
                                    rootProps={{'data-testid': '1'}}
                                />
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col>
                        <Card className="rounded shadow-sm">
                            <Card.Body>
                                <Card.Title>Card Allocation</Card.Title>
                                <Card.Subtitle className="mb-2 text-muted">Num of cards in each list</Card.Subtitle>
                                <Chart
                                    height={'300px'}
                                    chartType="PieChart"
                                    loader={<div>Loading Chart</div>}
                                    data={[
                                        ['List', 'Num Cards'],
                                        ...this.state.listSizes
                                    ]}
                                    options={{
                                        title: 'List Sizes'
                                    }}
                                    rootProps={{'data-testid': '1'}}
                                />
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default ViewBoard