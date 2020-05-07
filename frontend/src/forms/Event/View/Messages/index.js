import React from "react";
import PropTypes from 'prop-types';
import './style.css'
import UserChats from "./Chats";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";
import MessagesForm from "./MessagesForm";
import {connect} from "../../../../App/Context";

const Messages = ({eventId, userMessageId, eventUserId, setUserMessageId, userId}) => {
    return <ContainerColumn isBordered>
        {eventUserId === userId && <UserChats eventUserId={eventUserId}
                                              userMessageId={userMessageId}
                                              setUserMessageId={setUserMessageId}
                                              eventId={eventId}/>}
        <MessagesForm userMessageId={userMessageId}
                      eventUserId={eventUserId}
                      eventId={eventId}/>
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
    userId: state.userId,
});

export default connect(mapStateToProps, null)(Messages);