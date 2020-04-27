import React from "react";
import './style.css';
import PropTypes from "prop-types";

const LeftContainer = ({children, style}) => {
    return <div className='container-main-info-item' style={style}>{children}</div>
};

LeftContainer.propTypes = {
    children: PropTypes.node,
    style: PropTypes.object
};

export default LeftContainer;