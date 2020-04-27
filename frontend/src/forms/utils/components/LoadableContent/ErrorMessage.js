import React from "react";
import './index.css'
import PropTypes from "prop-types";

const ErrorMessage = ({error}) => {
    return <div className='error'>
        Error. {error}
    </div>
};

ErrorMessage.propTypes = {
    error: PropTypes.string.isRequired
};

export default ErrorMessage;