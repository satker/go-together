import React from "react";
import PropTypes from "prop-types";

import './index.css'

const ErrorMessage = ({error}) => {
    return <div className='error'>
        Error. {error}
    </div>
};

ErrorMessage.propTypes = {
    error: PropTypes.string.isRequired
};

export default ErrorMessage;