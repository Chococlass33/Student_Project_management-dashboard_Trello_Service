import React from "react";
import {Component} from "react/cjs/react.production.min.js";
import 'bootstrap/dist/css/bootstrap.min.css';

class ViewBoard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            boardId: undefined,
            loaded:false
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.boardId = this.props.location.state.integrationId
        }
        console.log(this.state)
    }

    render() {
        return (
            <div className="container h-100 mh-100">
                <div className="row">
                    <h1 className="col">
                        Board Name
                    </h1>
                </div>
                <div className="row">
                    <div className="col-sm">
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Basic Information</h5>
                                <p className="card-text">This will contain basic information about the board</p>
                                <br/>
                                <a href="#" className="btn btn-primary float-right">View Board</a>
                            </div>
                        </div>
                    </div>
                    <div className={"col-sm"}>
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Basic Information</h5>
                                <p className="card-text">This will contain basic information about the board</p>
                                <br/>
                                <a href="#" className="btn btn-primary float-right">View Board</a>
                            </div>
                        </div>
                    </div>
                    <div className={"col-sm"}>
                        <div className="card rounded shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">Basic Information</h5>
                                <p className="card-text">This will contain basic information about the board</p>
                                <br/>
                                <a href="#" className="btn btn-primary float-right">View Board</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ViewBoard