import React from 'react';
import PropTypes from "prop-types";

import ViewEvent from "./View";
import CreateEvent from "./Edit";

const Event = ({id, isView}) => isView ?
    <ViewEvent id={id}/> :
    <CreateEvent id={id}/>;

Event.propTypes = {
    id: PropTypes.string,
    isView: PropTypes.bool.isRequired
};

export default Event;