import React from 'react';
import GetAndViewEvent from "./View";
import CreateEvent from "./Edit";
import * as PropTypes from "prop-types";

const Event = ({id, isView}) => isView ?
    <GetAndViewEvent id={id}/> :
    <CreateEvent id={id}/>;

Event.propTypes = {
    id: PropTypes.string,
    isView: PropTypes.bool.isRequired
};

export default Event;