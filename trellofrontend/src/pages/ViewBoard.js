import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import 'bootstrap/dist/css/bootstrap.min.css';
import Chart from "react-google-charts";

class ViewBoard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            boardId: undefined,
            loaded: false, // We have no data
            ready: false // We have processed the
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.boardId = this.props.location.state.integrationId
        }
    }

    componentDidMount() {
        /* If we haven't already do all our requests */
        if (!this.state.loaded) {
            /* Get the members for the board */
            this.doRequest(`http://localhost:5002/data/boardMembers/${this.state.boardId}`,
                'members', {})
                /* Get how many cards each member is in */
                .then(data => this.doRequest(`http://localhost:5002/data/cardMembers/${this.state.boardId}`,
                    'cardMembers', data))
                .then(data => this.doRequest(`http://localhost:5002/boards/${this.state.boardId}`,
                    'boardData', data))
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
        return fetch(request)
            .then(response => response.json())
            .then(response => {
                let newData = {}
                newData[prop] = response
                console.log("data", data)
                return {...data, ...newData}
            })
    }

    componentDidUpdate() {
        if (this.state.loaded && !this.state.ready) {
            console.log(this.state)
            let nameMap = {};
            for (let member of this.state.members) {
                nameMap[member.id] = member.fullName
            }
            let chartData = []
            for (let id in this.state.cardMembers) {
                if (this.state.cardMembers.hasOwnProperty(id)) {
                    chartData.push([nameMap[id], this.state.cardMembers[id]])
                }
            }
            this.setState({
                ...this.state,
                ready: true,
                memberAllocations: chartData
            })
        }
    }


    render() {
        if (!this.state.loaded || !this.state.ready) {
            return (<div>Loading...</div>)
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <h1 className="col">
                        {this.state.boardData.name}
                    </h1>
                </div>
                <div className="row">
                    <div className="col">
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Basic Information</h5>
                                <ul className="list-group list-group-flush">
                                    <li className="list-group-item">Date Created
                                        - {this.state.boardData.dateCreated}</li>
                                    <li className="list-group-item">Dapibus ac facilisis in</li>
                                    <li className="list-group-item">Morbi leo risus</li>
                                    <li className="list-group-item">Porta ac consectetur ac</li>
                                    <li className="list-group-item">Vestibulum at eros</li>
                                </ul>
                                <a href={this.state.boardData.shortLink} target="_blank"
                                   className="btn btn-primary float-right">View Board</a>
                                <a href={`/viewHistory?project-id=${this.state.projectId}&trello-id=${this.state.boardId}`}
                                    className="btn btn-primary float-left">View
                                    History</a>
                            </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Member Breakdown</h5>
                                <h6 className="card-subtitle mb-2 text-muted">Num of cards assigned to Members</h6>
                                <Chart
                                    height={'300px'}
                                    chartType="PieChart"
                                    loader={<div>Loading Chart</div>}
                                    data={[
                                        ['Members', 'Num Cards'],
                                        ...this.state.memberAllocations
                                    ]}
                                    options={{
                                        title: 'Card Allocation'
                                    }}
                                    rootProps={{'data-testid': '1'}}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="col">
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Card Allocation</h5>
                                <h6 className="card-subtitle mb-2 text-muted">Num of cards in each list</h6>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ViewBoard