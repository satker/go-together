import React from "react";
import './style.css';
import PropTypes from "prop-types";
import {Box} from "@material-ui/core";

const ItemContainer = ({children, style}) => {
    return <Box display="flex"
                className='margin-right-item'
                style={style}>{children}</Box>
};

ItemContainer.propTypes = {
    children: PropTypes.node,
    style: PropTypes.string
};

export default ItemContainer;