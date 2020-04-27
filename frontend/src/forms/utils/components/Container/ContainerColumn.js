import React from "react";
import PropTypes from 'prop-types';
import './style.css';

const ContainerColumn = ({children, isBordered, style}) => {
    const styleClass = 'container-main-info margin-bottom-20' + (isBordered ? ' custom-border' : '');
    return <div className={styleClass} style={style}>{children}</div>
};

ContainerColumn.propTypes = {
    children: PropTypes.node,
    isBordered: PropTypes.bool,
    style: PropTypes.object
};

export default ContainerColumn;