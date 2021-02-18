import React, {useState} from "react";
import PropTypes from "prop-types";
import moment from "moment";

import {connect} from "App/Context";
import LabeledInput from "forms/utils/components/LabeledInput";
import {Event, Review} from "forms/utils/types";

import {putNewMessage} from "../actions";
import SendButtonIcon from "forms/utils/components/Icon/Sent";

const InputMessage = ({
                          userId, event, setMessages, messages, putNewMessage, readOnly, userMessageId
                      }) => {
    const [message, setMessage] = useState('');

    const sendMessage = () => {
        const date = moment();
        setMessage('');
        const newMessage = {
            message,
            authorId: userId === event.author.id ? event.id : userId,
            recipientId: userId === event.author.id ? userMessageId : event.id,
            date
        };
        setMessages([...messages, newMessage]);
        setMessage('');
        putNewMessage(event.id, newMessage);
    };

    return <div className='container-input'>
        <LabeledInput
            id="message"
            label="Enter message"
            multiline
            rowsMax={20}
            readOnly={readOnly}
            value={message}
            onChange={setMessage}
        />
        <SendButtonIcon onAction={sendMessage}/>
    </div>
};

InputMessage.propTypes = {
    messages: PropTypes.arrayOf(Review),
    event: Event.isRequired,
    setMessages: PropTypes.func.isRequired,
    userId: PropTypes.string,
    userMessageId: PropTypes.string,
    putNewMessage: PropTypes.func.isRequired,
    readOnly: PropTypes.bool
};

const mapStateToProps = state => ({
    userId: state.auth.response.userId,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {putNewMessage})(InputMessage);