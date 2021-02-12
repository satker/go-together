import React from 'react';
import PropTypes from "prop-types";

import View from "./View";
import Edit from "./Edit";

const Event = ({id, isView}) => {

    return isView ?
        <View id={id}/> :
        <Edit id={id}/>;
}

Event.propTypes = {
    id: PropTypes.string,
    isView: PropTypes.bool.isRequired
};

export default Event;