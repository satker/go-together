import React from "react";
import PropTypes from "prop-types";
import SendIcon from "@material-ui/icons/Send";

import './style.css';

const SendButtonIcon = ({onAction}) => <SendIcon className='send-message-icon' onClick={onAction}/>

SendButtonIcon.propTypes = {
    onAction: PropTypes.func.isRequired
};

export default SendButtonIcon;