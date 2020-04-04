import React from 'react';
import {getSrcForImg} from "../../../../utils/utils";
import {Image} from "react-bootstrap";

const MessageItem = ({message, user, userMessageId, setUserMessageId}) => {
    return <div className='message-item' onClick={() => setUserMessageId(user.id)}
                style={{borderRightStyle: userMessageId === user.id ? 'none' : ''}}>
        <div className='flex' style={{width: '30%'}}>
            <Image className='simple_user_img' src={getSrcForImg(user.userPhoto)}/>
        </div>
        <div className='flex' style={{width: '70%'}}>
            <div className='flex'>{user.login}</div>
            <div className='flex'>{message.message}</div>
        </div>
    </div>;
};

MessageItem.propTypes = {};

export default MessageItem;