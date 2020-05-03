import React, {useCallback, useEffect, useState} from "react";
import PropTypes from 'prop-types';
import {connect} from "../../../../App/Context";
import './style.css'
import moment from "moment";
import MessagesContainer from "./MesagesContainer";
import UserChats from "./Chats";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import {getReviewsByEvent} from "./actions";
import {Review} from "../../../utils/types";
import MessageRTC from "./MessageRTC";

const Messages = ({eventId, userMessageId, eventUserId, setUserMessageId, userId, getReviewsByEvent, reviewsByEvent}) => {
    const [lock, setLock] = useState(false);
    const [parsedReviewsByEvent, setParsedReviewsByEvent] = useState([]);
    const [timer, setTimer] = useState(null);
    const [refreshChats, setRefreshChats] = useState(false);

    const getMessages = useCallback(() => {
        const authorId = userId === eventUserId ? userMessageId : userId;
        getReviewsByEvent(eventId, authorId);
    }, [eventUserId, eventId, userId, userMessageId, getReviewsByEvent]);

    useEffect(() => {
        const updateReviews = reviewsByEvent.response.map(review => {
            review.date = moment(review.date);
            return review;
        }).sort((review1, review2) => review1.date.diff(review2.date, 'seconds'));
        setParsedReviewsByEvent(updateReviews);
    }, [reviewsByEvent]);

    useEffect(() => {
        const authorId = userId === eventUserId ? userMessageId : userId;
        if (userMessageId && authorId) {
            getMessages();
            setLock(true)
        } else {
            setParsedReviewsByEvent([])
        }
    }, [getMessages, setParsedReviewsByEvent, userId, userMessageId, eventUserId]);

    useEffect(() => {
        if (lock && !timer) {
            //const timeout = setInterval(getMessages, 2000);
            //setTimer(timeout);
        } else if (!lock && timer) {
            //clearInterval(timer);
            //setTimer(null)
        }
    }, [lock, setTimer, setLock, timer, getMessages]);

    return <ContainerColumn isBordered>
        {eventUserId === userId && <UserChats eventUserId={eventUserId}
                                              userMessageId={userMessageId}
                                              setUserMessageId={setUserMessageId}
                                              eventId={eventId}
                                              refreshChats={refreshChats}
                                              setRefreshChats={setRefreshChats}/>}
        <div className='container-input-messages' style={{width: eventUserId === userId ? '70%' : '100%'}}>
            <div className='container-messages'>
                <MessagesContainer eventId={eventId}
                                   eventUserId={eventUserId}
                                   reviews={parsedReviewsByEvent}/>
            </div>
            <MessageRTC eventId={eventId}
                        messages={parsedReviewsByEvent}
                        setMessages={setParsedReviewsByEvent}
                        eventUserId={eventUserId}
                        userMessageId={userMessageId}
                        readOnly={parsedReviewsByEvent.length === 0}/>
        </div>
    </ContainerColumn>
};

Messages.props = {
    userMessageId: PropTypes.string,
    eventUserId: PropTypes.string,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    setUserMessageId: PropTypes.func.isRequired,
    getReviewsByEvent: PropTypes.func.isRequired,
    reviewsByEvent: PropTypes.arrayOf(Review)
};

const mapStateToProps = () => state => ({
    userId: state.userId,
    reviewsByEvent: state.components.forms.event.eventView.messages.reviewsByEvent
});

export default connect(mapStateToProps, {getReviewsByEvent})(Messages);