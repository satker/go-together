import React, {useEffect} from "react";
import PropTypes from "prop-types";

import EventLikes from "forms/utils/components/Event/EventLikes";
import {getEventsLikes} from "forms/utils/components/Event/EventLikes/actions";
import {Event, ResponseData} from "forms/utils/types";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {connect} from "App/Context";

import ParticipationButton from "../../ParticipationButton";
import {getUsers} from "../../actions";
import {getCorrectDateFromString} from "forms/utils/utils";

const CommonInfo = ({event, users, getUsers, userId, getEventsLikes}) => {
    useEffect(() => {
        getEventsLikes([event.id]);
    }, [event, getEventsLikes]);

    return <LeftContainer style={{width: '600px'}}>
        <ItemContainer>
            <h4>{event.name}</h4>
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id && <EventLikes eventId={event.id} eventIds={[event.id]}/>}
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id &&
                <ParticipationButton users={users.response.result || users.response}
                                     getUsers={getUsers}
                                     eventId={event.id}/>
            }
        </ItemContainer>
        <ItemContainer>
            <h5>About</h5>
        </ItemContainer>
        <ItemContainer>
            <div dangerouslySetInnerHTML={{__html: event.description}}/>
        </ItemContainer>
        <ItemContainer>
            Trip dates: {getCorrectDateFromString(event.startDate)} -> {getCorrectDateFromString(event.endDate)}
        </ItemContainer>
    </LeftContainer>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    users: ResponseData.isRequired,
    setRefresh: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    event: state.components.forms.event.eventView.event.response,
    users: state.components.forms.event.eventView.users,
    userId: state.auth.response.userId
});

export default connect(mapStateToProps, {getEventsLikes, getUsers})(CommonInfo);