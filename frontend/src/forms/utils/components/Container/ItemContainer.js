import React from "react";
import './style.css';
import PropTypes from "prop-types";

const ItemContainer = ({children, style}) => {
    return <div className='flex margin-right-item' style={style}>{children}</div>
};

ItemContainer.propTypes = {
    children: PropTypes.node,
    style: PropTypes.string
};

export default ItemContainer;