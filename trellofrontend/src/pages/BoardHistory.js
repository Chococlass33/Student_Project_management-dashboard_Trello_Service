import React from 'react';
import {Component} from 'react/cjs/react.production.min.js';
import {Redirect} from 'react-router-dom';
import DatePicker from 'react-datepicker';
import Card from 'react-bootstrap/Card';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";

import queryString from 'query-string';

import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';

class BoardHistory extends Component {
    constructor(props) {
        super(props);
        this.values = queryString.parse(props.location.search);
        this.state = {
            startDate: new Date(),
            finalDate: new Date(),
            loaded: false,
            ready: false,
            token: this.values.token,
        };
    }

    handleChange(date) {
        this.setState({
            startDate: date,
        });
    }

    onFormSubmit(e) {
        e.preventDefault();
        this.setState({
            finalDate: this.state.startDate,
            loaded: false,
            ready: false,
        });
        const finalDate = this.handleDateFormat();
        fetch(`http://localhost:5002/history/${this.values['trello-id']}?token=${this.state.token}&date=${finalDate}`)
            .then((response) => response.json())
            .then((response) =>
                this.setState({
                    ...this.state,
                    loaded: true,
                    board: response,
                })
            );
    }

    //
    // Handles date format for passing through to the backend.
    handleDateFormat() {
        let month =
            this.state.startDate.getUTCMonth() + 1 < 10
                ? '0' + (this.state.startDate.getUTCMonth() + 1)
                : this.state.startDate.getUTCMonth() + 1;
        let day =
            this.state.startDate.getUTCDate() < 10
                ? '0' + this.state.startDate.getUTCDate()
                : this.state.startDate.getUTCDate();
        return `${this.state.startDate.getUTCFullYear()}-${month}-${day}`;
    }

    componentDidMount() {
        // On mount, load the board and set the state.
        fetch(`http://localhost:5002/history/${this.values['trello-id']}?token=${this.state.token}`)
            .then(response => response.json())
            .then(response =>
                this.setState({
                        ...this.state,
                        loaded: true,
                        board: response
                    }
                )
            );
    }

    componentDidUpdate() {
        // On update, use data and generate the final state of the component we want.
        // The '!this.state.ready' condition will always be false after the first run.
        if (this.state.loaded && !this.state.ready) {
            console.log(this.state);
            this.setState({
                ...this.state,
                ready: true,
            });
        }
    }

    render() {
        if (this.state.loaded) {
            if (this.state.redirect) {
                return <Redirect to={this.state.redirect}/>;
            }
            return (
                <Container fluid>
                    <Row>
                        <Col>
                            <Button variant="primary" onClick={this.redirectToViewBoard}>
                                Back
                            </Button>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <h3>Board history of</h3>
                        </Col>
                    </Row>
                    <Row className="justify-content-md-center">
                        <Col md={"auto"}>
                            <h1 className="border-bottom border-dark">
                                {this.state.board.name}
                            </h1>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <DatePicker
                                selected={this.state.startDate}
                                onChange={this.handleChange.bind(this)}
                                name="startDate"
                                maxDate={new Date()}
                                dateFormat="MM/dd/yyyy"/>

                            <Button onClick={this.onFormSubmit.bind(this)} variant="primary" className="m-3">
                                Confirm Date</Button>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <span className="text-secondary">
                                Date filtered by: {this.state.finalDate.toISOString()}
                            </span>
                        </Col>
                    </Row>
                    <Container fluid>
                        <Row style={{flexWrap: "nowrap"}}>
                            {this.buildBoard(this.state.board)}
                        </Row>
                    </Container>
                </Container>
            );
        } else {
            // While component is loading...
            return (
                <div>
                    <h1>Loading...</h1>
                </div>
            );
        }
    }

    redirectToViewBoard = () => {
        this.setState({
            redirect: `/?project-id=${this.values['project-id']}&trello-id=${this.values['trello-id']}`,
        });
    };

    buildBoard(board) {
        if (board.lists.size === 0) {
            return (
                <span className="h3 col">
					No lists were found!
					<br/>
					Try choosing a different date...
				</span>
            );
        }
        return Object.values(board.lists)
            .sort((a, b) => a.pos - b.pos)
            .map(list => (<Col>
                <b>List: </b> {list.name}
                <br/>
                {this.buildList(list)}
            </Col>))
    }

    buildList(list) {
        return Object.values(list.cards)
            .sort((a, b) => a.pos - b.pos)
            .map(card => (
                <Card key={card.id} className="m-2" style={{width: '17rem', height: '13rem'}}>
                    <Card.Body>
                        <Card.Title>{card.name}</Card.Title>
                        <Card.Text>
                            {this.handleCardDescription(card.desc)}
                        </Card.Text>
                    </Card.Body>
                    <Card.Footer>
                        <Card.Link href="#">View members</Card.Link>
                        <Card.Link href="#">View details</Card.Link>
                    </Card.Footer>
                </Card>
            ))
    }

    /**
     * Truncate the card description if it gets too long
     * @param description The description to truncate
     * @returns {string|*} The truncated description
     */
    handleCardDescription(description) {
        const descriptionLength = description.length;
        const maxLength = 80;

        if (descriptionLength > maxLength) {
            return description.substring(0, maxLength) + '...';
        } else {
            return description;
        }
    }

}

export default BoardHistory;
