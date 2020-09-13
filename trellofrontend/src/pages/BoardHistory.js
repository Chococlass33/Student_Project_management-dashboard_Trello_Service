import React from 'react';
import { Component } from 'react/cjs/react.production.min.js';
import DatePicker from 'react-datepicker';
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
		this.setState({ finalDate: this.state.startDate });
		this.setState({ loaded: false });
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
			return (
				<div>
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
								dateFormat="MM/dd/yyyy"
							/>
							<button className="btn btn-primary">Confirm Date</button>
						</div>
					</form>
					<div>
						{this.buildBoardHistory(
							this.state.board,
							this.state.lists,
							this.state.finalDate.toISOString()
						)}
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

	buildBoardHistory(board, lists, date) {
		return (
			<div className="row">
				<span className="col-sm-3">Date filtered by: {date}</span>
			</div>
		);
	}
}

export default BoardHistory;
