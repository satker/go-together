import React from "react";
import './style.css';

const LeftContainer = ({children, style}) => {
    return <div className='container-main-info-item' style={style}>{children}</div>
};

LeftContainer.propTypes = {};

export default LeftContainer;