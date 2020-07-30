import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";
import moment from "moment";
import SendIcon from "@material-ui/icons/Send";

import {connect} from "App/Context";
import LabeledInput from "forms/utils/components/LabeledInput";
import {Review} from "forms/utils/types";
import CustomButton from "forms/utils/components/CustomButton";

import {putNewMessage} from "../actions";

const InputMessage = ({
                          userId, eventId, setMessages, messages,
                          userRecipientId, putNewMessage, readOnly, createOffer,
                          sentRtcMessage
                      }) => {
    const [message, setMessage] = useState('');

    useEffect(() => {
        createOffer();
    }, [createOffer]);

    const sendMessage = () => {
        const date = moment();
        setMessage('');
        const newMessage = {
            message,
            authorId: userId,
            recipientId: userRecipientId,
            date
        };
        setMessages([...messages, newMessage]);
        setMessage('');
        sentRtcMessage(newMessage);
        putNewMessage(eventId, newMessage);
    };

    return <div className='container-input'>
        <CustomButton onClick={createOffer} text='create offer'/>
        <LabeledInput
            id="message"
            label="Enter message"
            multiline
            rowsMax={20}
            readOnly={readOnly}
            value={message}
            onChange={setMessage}
        />
        <SendIcon className='send-message-icon'
                  onClick={sendMessage}/>
    </div>
};

InputMessage.propTypes = {
    messages: PropTypes.arrayOf(Review),
    setMessages: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    eventUserId: PropTypes.string,
    userMessageId: PropTypes.string,
    putNewMessage: PropTypes.func.isRequired,
    readOnly: PropTypes.bool
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    eventId: state.components.forms.event.eventView.event.response.id
});

export default connect(mapStateToProps, {putNewMessage})(InputMessage);