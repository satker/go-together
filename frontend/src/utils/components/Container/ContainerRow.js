import React from "react";
import './style.css';

const Container = ({children}) => {
    return <div className='container-main-info-common margin-bottom-20'>{children}</div>
};

Container.propTypes = {};

export default Container;