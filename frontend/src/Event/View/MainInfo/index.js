import React, {useContext} from "react";
import EventLikes from "../../../utils/components/Event/EventLikes";
import ParticipationButton from "../ParticipationButton";
import {Context} from "../../../Context";
import * as PropTypes from "prop-types";
import {Event, EventUser} from "../../../types";

const MainInfo = ({event, users, setRefresh}) => {
    const [state] = useContext(Context);

    return <div className='container-main-info-item' style={{width: '600px'}}>
        <div className='margin-right-item'>
            <h4>{event.name}</h4>
        </div>
        <div className='margin-right-item'>
            {state.userId && state.userId !== event.author.id && <EventLikes eventId={event.id}/>}
        </div>
        <div className='margin-right-item'>
            {state.userId && state.userId !== event.author.id &&
            <ParticipationButton users={users}
                                 setRefresh={setRefresh}
                                 eventId={event.id}/>}
        </div>
        <div className='margin-right-item'>
            <h5>About</h5>
        </div>
        <div className='margin-right-item' dangerouslySetInnerHTML={{__html: event.description}}/>
        <div className='margin-right-item'>
            <h5>Event paid things:</h5>
        </div>
        <div className='margin-right-item'>
            {event.paidThings.map((p, key) => {
                return (<p key={key}>• {p.cashCategory} - {p.paidThing.name}</p>
                )
            })}
        </div>
        <div className='margin-right-item'>
            Trip dates: {event.startDate.format('LLL')} -> {event.endDate.format('LLL')}
        </div>
    </div>
};

MainInfo.propTypes = {
    event: Event.isRequired,
    users: PropTypes.arrayOf(EventUser),
    setRefresh: PropTypes.func.isRequired
};

export default MainInfo;