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
                /* Get card data */
                .then(data => this.doRequest(`http://localhost:5002/boards/${this.state.boardId}`,
                    'boardData', data))
                /* Get the size of all the lists */
                .then(data => this.doRequest(`http://localhost:5002/data/listSizes/${this.state.boardId}`,
                    'listSizes', data))
                /* Get the information on the lists */
                .then(data => this.doRequest(`http://localhost:5002/data/boardLists/${this.state.boardId}`,
                    'lists', data))
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
                return {...data, ...newData}
            })
            .catch(response => {
                let newData = {}
                newData[prop] = undefined
                return {...data, ...newData}
            })
    }

    processMemberChart() {
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
        return chartData
    }

    processListChart() {
        let nameMap = {};
        for (let list of this.state.lists) {
            nameMap[list.id] = list.name
        }
        let chartData = []
        for (let id in this.state.listSizes) {
            if (this.state.listSizes.hasOwnProperty(id)) {
                chartData.push([nameMap[id], this.state.listSizes[id]])
            }
        }
        return chartData
    }

    componentDidUpdate() {
        if (this.state.loaded && !this.state.ready) {
            console.log("State after API calls", this.state)
            this.setState({
                ...this.state,
                ready: true,
                memberAllocations: this.processMemberChart(),
                listSizes: this.processListChart()
            })
        }
    }


    render() {
        if (!this.state.projectId || !this.state.boardId) {
            return <h1>Error. No project id and/or board id.</h1>
        }
        if (!this.state.loaded || !this.state.ready) {
            return (<h1>Loading...</h1>)
        }
        if (!this.state.boardData) {
            return (<h1>Invalid Trello Id</h1>)
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
                                    <li className="list-group-item">Number of Members - {this.state.members.length}</li>
                                    <li className="list-group-item">Number of Lists - {this.state.lists.length}</li>
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
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ViewBoard