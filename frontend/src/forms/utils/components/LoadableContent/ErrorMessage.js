import React from "react";
import PropTypes from "prop-types";

import './index.css'

const ErrorMessage = ({error}) => {
    return error ? <div className='error'>
        Error. {error.toString()}
    </div> : null;
};

ErrorMessage.propTypes = {
    error: PropTypes.string.isRequired
};

export default ErrorMessage;