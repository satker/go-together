import React from "react";
import './style.css';

const ContainerColumn = ({children, isBordered, style}) => {
    const styleClass = 'container-main-info margin-bottom-20' + (isBordered ? ' custom-border' : '');
    return <div className={styleClass} style={style}>{children}</div>
};

ContainerColumn.propTypes = {};

export default ContainerColumn;