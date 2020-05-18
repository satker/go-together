import React from 'react';

import {getSrcForImg} from "forms/utils/utils";

const MessageItem = ({message, user, userMessageId, setUserMessageId}) => {
    return <div className='message-item custom-border' onClick={() => setUserMessageId(user.id)}
                style={{borderRightStyle: userMessageId === user.id ? 'none' : ''}}>
        <div className='flex' style={{width: '30%'}}>
            <img className='simple_user_img' src={getSrcForImg(user.userPhoto)} alt=''/>
        </div>
        <div className='flex' style={{width: '70%'}}>
            <div className='flex'>{user.login}</div>
            <div className='flex'>{message.message}</div>
        </div>
    </div>;
};

MessageItem.propTypes = {};

export default MessageItem;