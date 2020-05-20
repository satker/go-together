import React from "react";
import PropTypes from "prop-types";
import {Box} from "@material-ui/core";

import './style.css';

const Container = ({children}) => {
    return <Box display="flex"
                flexWrap="wrap"
                flexDirection="column"
                className='margin-bottom-20'>{children}</Box>
};

Container.propTypes = {
    children: PropTypes.node
};

export default Container;