import React, {useCallback, useEffect, useState} from "react";
import {connect} from "../../../../../App/Context";
import PropTypes from "prop-types";
import {keys} from 'lodash'
import MessageItem from "./MessageItem";
import {getChats, postUsersInfo} from "./actions";
import {ResponseData} from "../../../../utils/types";

const UserChats = ({
                       eventUserId, eventId, userMessageId, setUserMessageId, userId,
                       getChats, messages, postUsersInfo, usersInfo
                   }) => {
    const [timer, setTimer] = useState(null);

    const getUserChats = useCallback(() => {
        getChats(eventId);
    }, [eventId, getChats]);


    useEffect(() => {
        if (!messages.inProcess &&
            !usersInfo.inProcess &&
            messages.response.length !== 0) {
            let notFoundUserIds = [];
            const userIds = keys(messages.response) || [];
            const cachedUserIds = usersInfo.response.map(user => user.id);
            if (cachedUserIds.length === 0) {
                notFoundUserIds = userIds;
            } else {
                cachedUserIds.filter(userId => !userIds.filter(id => id === userId)[0])
                    .forEach(userId => {
                        notFoundUserIds.push(userId);
                    });
            }

            if (notFoundUserIds.length !== 0) {
                postUsersInfo(notFoundUserIds);
            }
        }
    }, [usersInfo, messages, postUsersInfo]);

    useEffect(() => {
        if (userId === eventUserId && !timer) {
            //setTimer(setInterval(getChats, 2000));
        }
    }, [userId, eventUserId, timer, setTimer, getUserChats]);

    useEffect(() => {
        getUserChats();
    }, [getUserChats]);

    return <div className='container-chats' style={{width: '30%'}}>
        {usersInfo.response.length === keys(messages.response).length &&
        keys(messages.response).map(key => {
            const message = messages.response[key];
            const user = usersInfo.response.filter(user => user.id === key)[0];
            return <MessageItem user={user}
                                key={key}
                                message={message}
                                userMessageId={userMessageId}
                                setUserMessageId={setUserMessageId}/>
        })}
    </div>
};

UserChats.propTypes = {
    eventUserId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    getChats: PropTypes.func.isRequired,
    postUsersInfo: PropTypes.func.isRequired,
    messages: ResponseData.isRequired,
    usersInfo: ResponseData.isRequired
};

const mapStateToProps = () => state => ({
    userId: state.userId.value,
    messages: state.components.forms.event.eventView.messages.chats,
    usersInfo: state.components.forms.event.eventView.messages.usersInfo
});

export default connect(mapStateToProps, {getChats, postUsersInfo})(UserChats);