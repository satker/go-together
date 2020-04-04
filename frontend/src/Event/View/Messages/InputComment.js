import React, {useContext, useState} from "react";
import {Input} from "reactstrap";
import {Context} from "../../../Context";
import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import PropTypes from "prop-types";
import SendIcon from '@material-ui/icons/Send';

const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages";

const InputComment = ({refresh, eventId, userMessageId, eventUserId, readOnly, setRefreshChats}) => {
    const [message, setMessage] = useState('');

    const [state] = useContext(Context);

    const send = () => {
        const authorId = state.userId === eventUserId ? eventId : state.userId;
        const recipientId = userMessageId === eventUserId ? eventId : userMessageId;
        setMessage(null);
        const body = {
            message,
            authorId,
            recipientId
        };
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId), () => {
            setRefreshChats(true);
            refresh()
        }, 'PUT', body);
    };

    return <div className='container-input'>
        <Input type="text"
               id="name"
               value={message}
               readOnly={readOnly}
               onChange={(evt) => setMessage(evt.target.value)}/>
        <SendIcon className='send-message-icon'
                  onClick={readOnly ? () => null : send}/>
    </div>
};

InputComment.propTypes = {
    setRefreshChats: PropTypes.func.isRequired,
    setReviewsByEvent: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    eventUserId: PropTypes.string,
    userId: PropTypes.string,
    refresh: PropTypes.func.isRequired
};

export default InputComment;