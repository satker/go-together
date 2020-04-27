import React from "react";
import './style.css';
import PropTypes from "prop-types";

const Container = ({children}) => {
    return <div className='container-main-info-common margin-bottom-20'>{children}</div>
};

Container.propTypes = {
    children: PropTypes.node
};

export default Container;