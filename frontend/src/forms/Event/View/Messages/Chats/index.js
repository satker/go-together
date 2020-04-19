import React, {useCallback, useEffect, useState} from "react";
import {connect} from "../../../../../App/Context";
import PropTypes from "prop-types";
import {keys} from 'lodash'
import MessageItem from "./MessageItem";
import {FORM_ID} from "../../constants";
import {getMessages, postUsersInfo} from "./actions";

const UserChats = ({
                       eventUserId, eventId, userMessageId, refreshChats, setRefreshChats, setUserMessageId, userId,
                       getMessages, messages, postUsersInfo, usersInfo
                   }) => {
    const [timer, setTimer] = useState(null);

    const getChats = useCallback(() => {
        getMessages(eventId);
        setRefreshChats(false);
    }, [eventId, setRefreshChats, getMessages]);


    useEffect(() => {
        if (messages.length !== 0) {
            let notFoundUserIds = [];
            const userIds = keys(messages) || [];
            const cachedUserIds = usersInfo.map(user => user.id);
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
            setTimer(setInterval(getChats, 2000));
        }
    }, [userId, eventUserId, timer, setTimer, getChats]);

    useEffect(() => {
        if (userId === eventUserId && refreshChats) {
            getChats();
        }
    }, [eventUserId, refreshChats, getChats, userId]);

    return <div className='container-chats' style={{width: '30%'}}>
        {usersInfo.length === keys(messages).length && keys(messages).map(key => {
            const message = messages[key];
            const user = usersInfo.filter(user => user.id === key)[0];
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
    getMessages: PropTypes.func.isRequired,
    postUsersInfo: PropTypes.func.isRequired,
    messages: PropTypes.array,
    usersInfo: PropTypes.array
};

const mapStateToProps = (FORM_ID) => state => ({
    userId: state.userId,
    messages: state[FORM_ID]?.messages || [],
    usersInfo: state[FORM_ID]?.usersInfo || []
});

export default connect(mapStateToProps, {getMessages, postUsersInfo})(UserChats)(FORM_ID);