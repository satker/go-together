import React, {useContext, useEffect, useState} from "react";
import PropTypes from 'prop-types';
import {MESSAGE_SERVICE_URL} from '../../../utils/constants'
import {Context} from "../../../Context";
import InputComment from "./InputComment";
import './style.css'
import moment from "moment";
import MessagesContainer from "./MesagesContainer";

export const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages/_author_id_";

const Messages = ({eventId, userId, eventUserId}) => {
    const [reviewsByEvent, setReviewsByEvent] = useState([]);
    const [state] = useContext(Context);

    useEffect(() => {
        const authorId = state.userId === eventUserId ? userId : state.userId;
        if (userId && authorId) {
            state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId)
                    .replace('_author_id_', authorId),
                reviews => {
                    const updateReviews = reviews.map(review => {
                        review.date = moment(review.date);
                        return review;
                    }).sort((review1, review2) => review1.date.diff(review2.date, 'minutes'));
                    setReviewsByEvent(updateReviews);
                });
        } else {
            setReviewsByEvent([])
        }
    }, [eventId, setReviewsByEvent, state, userId, eventUserId]);

    return <div className='flex border'>
        <div className='container-messages flex'>
            <MessagesContainer eventId={eventId}
                               eventUserId={eventUserId}
                               reviews={reviewsByEvent}/>
        </div>
        <InputComment readOnly={reviewsByEvent.length === 0}
                      setReviewsByEvent={setReviewsByEvent}
                      userId={userId}
                      eventId={eventId}
                      eventUserId={eventUserId}
        />
    </div>
};

Messages.props = {
    eventUserId: PropTypes.string,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string
};

export default Messages;