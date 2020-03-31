import React from 'react'
import PropTypes from 'prop-types'
import ChatIcon from '@material-ui/icons/Chat';

const MessageIcon = ({onAction}) =>
    <ChatIcon onClick={onAction} className='chat-icon'/>;

MessageIcon.propTypes = {
    onAction: PropTypes.func.isRequired
};

export default MessageIcon;