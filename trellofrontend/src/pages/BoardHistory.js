import React from 'react';
import { Component } from 'react/cjs/react.production.min.js';
import { Redirect } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import Card from 'react-bootstrap/Card';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';

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
		fetch(`http://localhost:5002/boardHistory/${this.values['trello-id']}`)
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
				return <Redirect to={this.state.redirect} />;
			}
			return (
				<div>
					<div className="pl-1 pt-1 d-flex flex-row justify-content-start">
						<button
							type="button"
							className="btn btn-primary"
							onClick={this.redirectToViewBoard}
						>
							Back
						</button>
					</div>
					<div className="pt-3 d-flex flex-row align-items-center">
						<span className="h3 col">Board history of</span>
					</div>
					<div className="d-flex flex-row justify-content-center">
						<span className="j-self-center h1 border-bottom border-dark">
							{this.state.board.name}
						</span>
					</div>
					<form onSubmit={this.onFormSubmit}>
						<div className="pt-3 form-group">
							<DatePicker
								selected={this.state.startDate}
								onChange={this.handleChange}
								name="startDate"
								maxDate={new Date()}
								dateFormat="MM/dd/yyyy"
							/>
							<button className="btn btn-primary">Confirm Date</button>
						</div>
					</form>
					<div className="d-flex flex-row justify-content-center">
						<span className="text-secondary">
							Date filtered by: {this.state.finalDate.toISOString()}
						</span>
					</div>
					<div className="container">
						{this.buildBoardHistory(this.state.lists)}
					</div>
				</div>
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

	// The board is built without ordering, and therefore on refreshing the page, elements may move around.
	buildBoardHistory(lists) {
		return (
			<div
				className="d-flex flex-row mt-3"
				style={{ overflowY: window.scroll }}
			>
				{this.buildLists(lists)}
			</div>
		);
	}

	// Build a list that is column separated based on the number of lists on the board. This will need rework when list size is large (probably a scrollbar or something).
	buildLists(lists) {
		let listElements = lists.length;

		if (listElements > 0) {
			return lists.map((list) => (
				<ul key={list.list.id} className="col-md-3">
					<b>List: </b> {list.list.name}
					<br />
					{this.buildCards(list)}
				</ul>
			));
		} else {
			return (
				<span className="h3 col">
					No lists were found!
					<br />
					Try choosing a different date...
				</span>
			);
		}
	}

	// Build a column of cards, which is called per list.
	buildCards(list) {
		return list.cards.map((card) => (
			<Card key={card.card.id} style={{ width: '17rem', height: '13rem' }}>
				<Card.Body>
					<Card.Title>{card.card.name}</Card.Title>
					<Card.Text>
						{this.handleCardDescription(card.card.description)}
					</Card.Text>
				</Card.Body>
				<Card.Footer>
					<Card.Link href="#">View members</Card.Link>
					<Card.Link href="#">View details</Card.Link>
				</Card.Footer>
			</Card>
		));
	}

	handleCardDescription(description) {
		const descriptionLength = description.length;
		const maxLength = 80;

		if (descriptionLength > maxLength) {
			return description.substring(0, maxLength) + '...';
		} else if (description !== '') {
			return description;
		} else {
			return 'No description found...';
		}
	}
}

export default BoardHistory;
