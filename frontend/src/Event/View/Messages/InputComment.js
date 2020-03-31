import React, {useContext, useState} from "react";
import {Input} from "reactstrap";
import {Context} from "../../../Context";
import {MESSAGE_SERVICE_URL} from "../../../utils/constants";
import PropTypes from "prop-types";
import SendIcon from '@material-ui/icons/Send';
import moment from "moment";

const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages";

const InputComment = ({setReviewsByEvent, eventId, userId, eventUserId, readOnly}) => {
    const [message, setMessage] = useState('');

    const [state] = useContext(Context);

    const refresh = () => {
        const authorId = state.userId === eventUserId ? userId : state.userId;
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId) + '/' + authorId,
            reviews => {
                const updateReviews = reviews.map(review => {
                    review.date = moment(review.date);
                    return review;
                }).sort((review1, review2) => review1.date.diff(review2.date, 'minutes'));
                setReviewsByEvent(updateReviews)
            }
        );
    };

    const send = () => {
        const authorId = state.userId === eventUserId ? eventId : state.userId;
        const recipientId = userId === eventUserId ? eventId : userId;
        setMessage(null);
        const body = {
            message,
            authorId,
            recipientId
        };
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId), () => refresh(), 'PUT', body);
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
    setReviewsByEvent: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    eventUserId: PropTypes.string,
    userId: PropTypes.string
};

export default InputComment;