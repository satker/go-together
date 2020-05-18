import React from 'react';
import PropTypes from "prop-types";

import './style.css';

const Marker = ({color, name}) => {
    return (
        <div>
            <div
                className="pin bounce"
                style={{backgroundColor: color, cursor: 'pointer'}}
                title={name}
            />
            <div className="pulse"/>
        </div>
    );
};

Marker.propTypes = {
    color: PropTypes.string.isRequired,
    name: PropTypes.string
};

export default Marker;