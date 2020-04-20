import React from "react";
import './style.css';
import PropTypes from "prop-types";

const RightContainer = ({children, style, isBordered}) => {
    const styleClass = 'container-main-info-item' + (isBordered ? ' custom-border' : '');
    return <div className={styleClass} style={style}>{children}</div>
};

RightContainer.propTypes = {
    children: PropTypes.node,
    isBordered: PropTypes.bool,
    style: PropTypes.object
};

export default RightContainer;