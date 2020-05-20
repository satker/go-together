import React from "react";
import PropTypes from 'prop-types';
import {Box} from "@material-ui/core";

import './style.css';

const ContainerColumn = ({children, isBordered, style}) => {
    const styleClass = 'margin-bottom-20 margin-right-item' + (isBordered ? ' custom-border' : '');
    return <Box display="flex"
                flexWrap="wrap"
                flexDirection="row"
                justifyContent="space-between" className={styleClass} style={style}>{children}</Box>
};

ContainerColumn.propTypes = {
    children: PropTypes.node,
    isBordered: PropTypes.bool,
    style: PropTypes.object
};

export default ContainerColumn;