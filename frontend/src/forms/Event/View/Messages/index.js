import React from "react";
import PropTypes from 'prop-types';
import './style.css'
import UserChats from "./Chats";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import MessagesForm from "./MessagesForm";
import {connect} from "../../../../App/Context";

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

const mapStateToProps = () => state => ({
    userId: state.userId.value,
    eventAuthorId: state.components.forms.event.eventView.event.response.author.id

});

export default connect(mapStateToProps, null)(Messages);