import React from 'react';
import { Component } from 'react/cjs/react.production.min.js';
import queryString from 'query-string';

class Board extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {
		const values = queryString.parse(this.props.location.search);
		fetch(`http://167.99.7.70:5002/boards/${values.integrationId}`)
			.then((response) => response.json())
			.then((board) => ({ board }))
			.then((data) =>
				fetch(`http://167.99.7.70:5002/members`)
					.then((response) => response.json())
					.then((members) => ({ ...data, members: members }))
			)
			.then((data) =>
				fetch(`http://167.99.7.70:5002/cards`)
					.then((response) => response.json())
					.then((cards) => ({ ...data, cards: cards }))
			)
			.then((data) =>
				fetch(`http://167.99.7.70:5002/lists`)
					.then((response) => response.json())
					.then((lists) => ({ ...data, lists: lists }))
			)
			.then((data) => this.setState({ loaded: true, ...data }));
	}

	render() {
		if (this.state.loaded) {
			console.log(this.state);
			return (
				<div>
					<h1 style={{ marginTop: '64px' }}>Board Info</h1>
					<h2>Board</h2>
					<p>
						<b>Name: </b> "{this.state.board.name}"<br />
						<b>Description: </b> "{this.state.board.description}"<br />
						<b>URL: </b>{' '}
						<a href={this.state.board.shortLink}>
							{this.state.board.shortLink}
						</a>
						<br />
					</p>
					<h2>Members</h2>
					<ul>{this.buildMembers(this.state.members)}</ul>
					<h2>Lists</h2>
					<ul>{this.buildLists(this.state.lists)}</ul>
					<h2>Cards</h2>
					<ul>{this.buildCards(this.state.cards)}</ul>
				</div>
			);
		} else {
			return (
				<div>
					<h1 style={{ marginTop: '64px' }}>Board Info</h1>
					<p>Loading...</p>
				</div>
			);
		}
	}

	buildMembers(members) {
		return members.map((member) => (
			<li>
				<b>Name</b> "{member.fullName}"<br />
				<b>Email</b> "{member.email}"<br />
				<b>Type</b> "{member.type}"<br />
			</li>
		));
	}

	buildLists(lists) {
		return lists.map((list) => (
			<li>
				<b>Name: </b> "{list.name}"<br />
				<b>Id: </b> "{list.id}"<br />
			</li>
		));
	}

	buildCards(cards) {
		return cards.map((card) => (
			<li>
				<b>Name: </b> "{card.name}"<br />
				<b>Description: </b> "{card.description}"<br />
				<b>List Id: </b> "{card.listId}"<br />
				<b>URL: </b> <a href={card.shortLink}>{card.shortLink}</a>
				<br />
			</li>
		));
	}
}

export default Board;
