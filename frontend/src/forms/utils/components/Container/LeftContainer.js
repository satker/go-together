import React from "react";
import PropTypes from "prop-types";
import {Box} from "@material-ui/core";

import './style.css';

const LeftContainer = ({children, style}) => {
    return <Box display="flex"
                alignContent="flex-start"
                flexDirection="column"
                style={style}>{children}</Box>
};

LeftContainer.propTypes = {
    children: PropTypes.node,
    style: PropTypes.object
};

export default LeftContainer;