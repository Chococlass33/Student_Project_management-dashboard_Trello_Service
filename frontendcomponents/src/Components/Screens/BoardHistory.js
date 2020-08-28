import React from 'react';
import { Component } from 'react/cjs/react.production.min.js';
import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';

class BoardHistory extends Component {
	constructor(props) {
		super(props);
		this.state = {
			startDate: new Date(),
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
		console.log(this.state.startDate.toISOString());
		// Here, we want to perform the API call, and send information such as:
		// - IntegrationID
		// - BoardID
		// - Date (in ISO form)
	}

	componentDidMount() {
		// Either obtain board information with current date OR show nothing
		// under the datepicker, and only show the state of the board when
		// a date is selected.
		this.setState({ loaded: true });
	}

	render() {
		if (this.state.loaded) {
			console.log(this.state.startDate.toISOString());
			return (
				<div>
					<div>
						<h1 style={{ marginTop: '64px' }}>Board History</h1>
					</div>
					<form onSubmit={this.onFormSubmit}>
						<div className="form-group">
							<DatePicker
								selected={this.state.startDate}
								onChange={this.handleChange}
								name="startDate"
								dateFormat="MM/dd/yyyy"
							/>
							<button className="btn btn-primary">Confirm Date</button>
						</div>
					</form>
				</div>
			);
		} else {
			return (
				<div>
					<h1>No board history found.</h1>
				</div>
			);
		}
	}
}

export default BoardHistory;
