import React from "react";
import './style.css';

const ItemContainer = ({children, style}) => {
    return <div className='flex margin-right-item' style={style}>{children}</div>
};

ItemContainer.propTypes = {};

export default ItemContainer;