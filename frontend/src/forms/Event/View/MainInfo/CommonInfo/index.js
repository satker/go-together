import React, {useEffect} from "react";
import PropTypes from "prop-types";
import moment from "moment";

import EventLikes from "forms/utils/components/Event/EventLikes";
import LoadableContent from "forms/utils/components/LoadableContent";
import {postLikes} from "forms/utils/components/Event/EventLikes/actions";
import {Event, ResponseData} from "forms/utils/types";
import LeftContainer from "forms/utils/components/Container/LeftContainer";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {connect} from "App/Context";

import ParticipationButton from "../../ParticipationButton";
import {getUsers} from "../../actions";

const CommonInfo = ({event, users, getUsers, userId, postLikes}) => {
    useEffect(() => {
        postLikes([event.id]);
    }, [event, postLikes]);

    return <LeftContainer style={{width: '600px'}}>
        <ItemContainer>
            <h4>{event.name}</h4>
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id && <EventLikes eventId={event.id} eventIds={[event.id]}/>}
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id &&
            <LoadableContent loadableData={users}>
                <ParticipationButton users={users.response}
                                     getUsers={getUsers}
                                     eventId={event.id}/>
            </LoadableContent>
            }
        </ItemContainer>
        <ItemContainer>
            <h5>About</h5>
        </ItemContainer>
        <ItemContainer>
            <div dangerouslySetInnerHTML={{__html: event.description}}/>
        </ItemContainer>
        <ItemContainer>
            <h5>Event paid things:</h5>
        </ItemContainer>
        <ItemContainer>
            {event.paidThings.map((p, key) => {
                return (<p key={key}>• {p.cashCategory} - {p.paidThing.name}</p>
                )
            })}
        </ItemContainer>
        <ItemContainer>
            Trip dates: {moment(event.startDate).format('LLL')} -> {moment(event.endDate).format('LLL')}
        </ItemContainer>
    </LeftContainer>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    users: ResponseData.isRequired,
    setRefresh: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = () => state => ({
    event: state.components.forms.event.eventView.event.response,
    users: state.components.forms.event.eventView.users,
    userId: state.userId.value
});

export default connect(mapStateToProps, {postLikes, getUsers})(CommonInfo);