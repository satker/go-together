import React from "react";
import PropTypes from "prop-types";
import {Box} from "@material-ui/core";

import './style.css';

const RightContainer = ({children, style, isBordered}) => {
    const styleClass = isBordered ? ' custom-border' : '';
    return <Box display="flex"
                alignContent="flex-start"
                flexDirection="column"
                className={styleClass} style={style}>{children}</Box>
};

RightContainer.propTypes = {
    children: PropTypes.node,
    isBordered: PropTypes.bool,
    style: PropTypes.object
};

export default RightContainer;