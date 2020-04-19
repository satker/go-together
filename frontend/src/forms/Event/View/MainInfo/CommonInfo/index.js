import React from "react";
import EventLikes from "../../../../utils/components/Event/EventLikes";
import ParticipationButton from "../../ParticipationButton";
import {connect} from "../../../../../App/Context";
import * as PropTypes from "prop-types";
import {Event, EventUser} from "../../../../utils/types";
import LeftContainer from "../../../../utils/components/Container/LeftContainer";
import ItemContainer from "../../../../utils/components/Container/ItemContainer";
import {FORM_ID} from "../../constants";

const CommonInfo = ({event, users, setRefresh, userId}) => {
    return <LeftContainer style={{width: '600px'}}>
        <ItemContainer>
            <h4>{event.name}</h4>
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id && <EventLikes eventId={event.id}/>}
        </ItemContainer>
        <ItemContainer>
            {userId && userId !== event.author.id &&
            <ParticipationButton users={users}
                                 setRefresh={setRefresh}
                                 eventId={event.id}/>}
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
            Trip dates: {event.startDate.format('LLL')} -> {event.endDate.format('LLL')}
        </ItemContainer>
    </LeftContainer>
};

CommonInfo.propTypes = {
    event: Event.isRequired,
    users: PropTypes.arrayOf(EventUser),
    setRefresh: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = () => state => ({
    userId: state.userId
});

export default connect(mapStateToProps, null)(CommonInfo)(FORM_ID);