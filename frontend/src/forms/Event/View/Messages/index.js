import React from "react";
import PropTypes from 'prop-types';

import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import {connect} from "App/Context";

import UserChats from "./Chats";
import MessagesForm from "./MessagesForm";

import './style.css'

const Messages = ({userMessageId, eventAuthorId, setUserMessageId, userId}) => {
    return <ContainerColumn isBordered>
        {eventAuthorId === userId && <UserChats userMessageId={userMessageId}
                                                setUserMessageId={setUserMessageId}/>}
        <MessagesForm userMessageId={userMessageId}/>
    </ContainerColumn>
};

Messages.props = {
    userMessageId: PropTypes.string,
    eventUserId: PropTypes.string,
    eventId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    setUserMessageId: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    eventAuthorId: state.components.forms.event.eventView.event.response.author.id

});

export default connect(mapStateToProps, null)(Messages);