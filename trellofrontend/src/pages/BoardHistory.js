import React from 'react';
import {Component} from 'react/cjs/react.production.min.js';
import {Redirect} from 'react-router-dom';
import DatePicker from 'react-datepicker';
import Card from 'react-bootstrap/Card';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

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
        };
        this.handleChange = this.handleChange.bind(this);
        this.onFormSubmit = this.onFormSubmit.bind(this);
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
        fetch(
            `http://localhost:5002/boardHistory/${this.values['trello-id']}?date=${finalDate}`
        )
            .then((response) => response.json())
            .then((response) =>
                this.setState({
                    loaded: true,
                    board: response.board,
                    lists: response.lists,
                })
            );
    }

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
        const finalDate = `${month}${day}${this.state.startDate.getUTCFullYear()}`;
        return finalDate;
    }

    componentDidMount() {
        // On mount, load the board and set the state.
        fetch(`http://localhost:5002/history/${this.values['trello-id']}?`)
            .then((response) => response.json())
            .then((response) =>
                this.setState({
                    loaded: true,
                    board: response.board,
                    lists: response.lists,
                })
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
                            <Form onSubmit={this.onFormSubmit}>
                                <DatePicker
                                    selected={this.state.startDate}
                                    onChange={this.handleChange}
                                    name="startDate"
                                    maxDate={new Date()}
                                    dateFormat="MM/dd/yyyy"/>

                                <Button variant="primary" className="m-3">Confirm Date </Button>
                            </Form>
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
                            {this.buildLists(this.state.lists)}
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

    // Build a list that is column separated based on the number of lists on the board. This will need rework when list size is large (probably a scrollbar or something).
    /**
     * Convert multiple lists into visual representations.
     * Relies on the buildCards method to convert all the cards in a list into visual cards
     * @param lists The lists to convert
     */
    buildLists(lists) {
        if (lists.length > 0) {
            return lists.map(list => (
                <Col>
                    <b>List: </b> {list.list.name}
                    <br/>
                    {this.buildCards(list)}
                </Col>
            ));
        } else {
            return (
                <span className="h3 col">
					No lists were found!
					<br/>
					Try choosing a different date...
				</span>
            );
        }
    }

    // Build a column of cards, which is called per list.
    /**
     * Produce a visual representation of all the cards in a list
     * @param list The list to convert
     */
    buildCards(list) {
        return list.cards.map((card) => (
            <Card key={card.id} className="m-2" style={{width: '17rem', height: '13rem'}}>
                <Card.Body>
                    <Card.Title>{card.name}</Card.Title>
                    <Card.Text>
                        {this.handleCardDescription(card.description)}
                    </Card.Text>
                </Card.Body>
                <Card.Footer>
                    <Card.Link href="#">View members</Card.Link>
                    <Card.Link href="#">View details</Card.Link>
                </Card.Footer>
            </Card>
        ));
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
