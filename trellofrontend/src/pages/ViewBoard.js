import React from "react";
import {Component} from "react/cjs/react.production.min.js";

class ViewBoard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            projectId: undefined,
            boardId: undefined,
        };
        if (this.props.location.state) {
            this.state.projectId = this.props.location.state.projectId
            this.state.boardId = this.props.location.state.integrationId
        }
        console.log(this.state)
    }

    render() {
        return (
            <div>
                nyah
            </div>
        );
    }
}

export default ViewBoard