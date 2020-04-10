import React, {useCallback, useContext, useEffect, useState} from "react";
import PropTypes from 'prop-types';
import {MESSAGE_SERVICE_URL} from '../../../utils/constants'
import {Context} from "../../../Context";
import InputComment from "./InputComment";
import './style.css'
import moment from "moment";
import MessagesContainer from "./MesagesContainer";
import UserChats from "./Chats";

export const URL_EVENT_REVIEWS = MESSAGE_SERVICE_URL + "/events/_id_/messages/_author_id_";

const Messages = ({eventId, userMessageId, eventUserId, setUserMessageId}) => {
    const [reviewsByEvent, setReviewsByEvent] = useState([]);
    const [lock, setLock] = useState(false);
    const [timer, setTimer] = useState(null);
    const [refreshChats, setRefreshChats] = useState(false);
    const [state] = useContext(Context);

    const getMessages = useCallback(() => {
        const authorId = state.userId === eventUserId ? userMessageId : state.userId;
        state.fetchWithToken(URL_EVENT_REVIEWS.replace("_id_", eventId)
                .replace('_author_id_', authorId),
            reviews => {
                const updateReviews = reviews.map(review => {
                    review.date = moment(review.date);
                    return review;
                }).sort((review1, review2) => review1.date.diff(review2.date, 'seconds'));
                setReviewsByEvent(updateReviews);
            });
    }, [eventUserId, eventId, state, userMessageId]);

    useEffect(() => {
        const authorId = state.userId === eventUserId ? userMessageId : state.userId;
        if (userMessageId && authorId) {
            getMessages();
            setLock(true)
        } else {
            setReviewsByEvent([])
        }
    }, [getMessages, setReviewsByEvent, state, userMessageId, eventUserId]);

    useEffect(() => {
        if (lock && !timer) {
            const timeout = setInterval(getMessages, 2000);
            setTimer(timeout);
        } else if (!lock && timer) {
            clearInterval(timer);
            setTimer(null)
        }
    }, [lock, setTimer, setLock, timer, getMessages]);

    return <div className='custom-border' style={{display: 'flex', flexDirection: 'row'}}>
        <UserChats eventUserId={eventUserId}
                   userMessageId={userMessageId}
                   setUserMessageId={setUserMessageId}
                   eventId={eventId}
                   refreshChats={refreshChats}
                   setRefreshChats={setRefreshChats}/>
        <div className='container-input-messages'>
            <div className='container-messages'>
                <MessagesContainer eventId={eventId}
                                   eventUserId={eventUserId}
                                   reviews={reviewsByEvent}/>
            </div>
            <InputComment readOnly={reviewsByEvent.length === 0}
                          setReviewsByEvent={setReviewsByEvent}
                          userMessageId={userMessageId}
                          eventId={eventId}
                          eventUserId={eventUserId}
                          refresh={getMessages}
                          setRefreshChats={setRefreshChats}
            />
        </div>
    </div>
};

Messages.props = {
    eventUserId: PropTypes.string,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    setUserMessageId: PropTypes.func.isRequired
};

export default Messages;