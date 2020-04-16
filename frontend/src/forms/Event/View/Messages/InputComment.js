import React, {useState} from "react";
import {Input} from "reactstrap";
import {connect} from "../../../../App/Context";
import PropTypes from "prop-types";
import SendIcon from '@material-ui/icons/Send';
import {FORM_ID} from "../constants";
import {putReview} from "./actions";

const InputComment = ({refresh, eventId, userMessageId, eventUserId, readOnly, setRefreshChats, userId, putReview}) => {
    const [message, setMessage] = useState('');
    const send = () => {
        const authorId = userId === eventUserId ? eventId : userId;
        const recipientId = userMessageId === eventUserId ? eventId : userMessageId;
        setMessage('');
        const body = {
            message,
            authorId,
            recipientId
        };
        putReview(eventId, body, setRefreshChats, refresh);
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
    refresh: PropTypes.func.isRequired,
    putReview: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    userId: state.userId
});

export default connect(mapStateToProps, {putReview}, FORM_ID)(InputComment);