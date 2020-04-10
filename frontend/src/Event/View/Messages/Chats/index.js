import React, {useCallback, useContext, useEffect, useState} from "react";
import {Context} from "../../../../Context";
import {MESSAGE_SERVICE_URL, USER_SERVICE_URL} from "../../../../utils/constants";
import PropTypes from "prop-types";
import {keys} from 'lodash'
import MessageItem from "./MessageItem";

const UserChats = ({eventUserId, eventId, userMessageId, refreshChats, setRefreshChats, setUserMessageId}) => {
    const [creatorMessages, setCreatorMessages] = useState([]);
    const [messageUsers, setMessageUsers] = useState([]);
    const [timer, setTimer] = useState(null);
    const [state] = useContext(Context);

    const getChats = useCallback(() => {
        state.fetchWithToken(MESSAGE_SERVICE_URL + '/events/' + eventId + '/messages/', messages => {
            let notFoundUserIds = [];
            const userIds = keys(messages) || [];
            const cachedUserIds = messageUsers.map(user => user.id);
            if (cachedUserIds.length === 0) {
                notFoundUserIds = userIds;
            } else {
                cachedUserIds.filter(userId => !userIds.filter(id => id === userId)[0])
                    .forEach(userId => {
                        notFoundUserIds.push(userId);
                    });
            }

            if (notFoundUserIds.length !== 0) {
                state.fetchWithToken(USER_SERVICE_URL + '/users/simple', users => {
                    setMessageUsers([...messageUsers, ...users])
                }, 'POST', notFoundUserIds);
            }

            setCreatorMessages(messages);
        });
        setRefreshChats(false);
    }, [messageUsers, eventId, setRefreshChats, state]);

    useEffect(() => {
        if (state.userId === eventUserId && !timer) {
            setTimer(setInterval(getChats, 2000));
        }
    }, [state, eventUserId, timer, setTimer, getChats]);

    useEffect(() => {
        if (state.userId === eventUserId && refreshChats) {
            getChats();
        }
    }, [eventUserId, refreshChats, getChats, state]);

    return <div className='container-chats' style={{width: '30%'}}>
        {messageUsers.length === keys(creatorMessages).length && keys(creatorMessages).map(key => {
            const message = creatorMessages[key];
            const user = messageUsers.filter(user => user.id === key)[0];
            return <MessageItem user={user}
                                key={key}
                                message={message}
                                userMessageId={userMessageId}
                                setUserMessageId={setUserMessageId}/>
        })}
    </div>
};

UserChats.propTypes = {
    eventUserId: PropTypes.string.isRequired
};

export default UserChats;