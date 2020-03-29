import React, {useContext} from "react";
import EventLikes from "../../../utils/components/Event/EventLikes";
import ParticipationButton from "../ParticipationButton";
import {Context} from "../../../Context";

const MainInfo = ({event, users, setRefresh}) => {
    const [state] = useContext(Context);

    return <>
        <div className='margin-right-item'>
            <h4>{event.name}</h4>
        </div>
        <div className='margin-right-item'>
            {state.userId && <EventLikes eventId={event.id}/>}
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
            Max count of users: {event.peopleCount}
        </div>
    </>
};

MainInfo.propTypes = {};

export default MainInfo;