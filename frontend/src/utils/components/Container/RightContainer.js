import React from "react";
import './style.css';

const RightContainer = ({children, style, isBordered}) => {
    const styleClass = 'container-main-info-item' + (isBordered ? ' custom-border' : '');
    return <div className={styleClass} style={style}>{children}</div>
};

RightContainer.propTypes = {};

export default RightContainer;