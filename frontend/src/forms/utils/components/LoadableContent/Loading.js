import React from "react";
import './index.css'
import PropTypes from "prop-types";
import CircularProgress from "@material-ui/core/CircularProgress";

const Loading = ({loadingMessage}) => {
    return <div className='loading'>
        <CircularProgress/>
        <span>{loadingMessage}</span>
    </div>
};

Loading.propTypes = {
    loadingMessage: PropTypes.string
};

Loading.defaultProps = {
    loadingMessage: 'Wait...'
};

export default Loading;