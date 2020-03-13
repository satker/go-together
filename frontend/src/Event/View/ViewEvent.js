import React, {useContext, useEffect, useState} from "react";
import Profile from "./Profile";
import Reviews from "./Reviews";
import {Context} from "../../Context";
import {Event} from "../../types";
import FormReference from "../../utils/components/FormReference";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import Container from "@material-ui/core/Container";
import EventLikes from "../../utils/components/Event/EventLikes";
import Users from "./Users";
import {EVENT_SERVICE_URL} from "../../utils/constants";
import ParticipationButton from "./ParticipationButton";

const ViewEvent = ({event}) => {
    const [state] = useContext(Context);
    const [refresh, setRefresh] = useState(true);
    const [users, setUsers] = useState([]);
    const [statuses, setStatuses] = useState([]);

    useEffect(() => {
        if (refresh) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + event.id + '/users/statuses', setStatuses);
            state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + event.id + '/users', setUsers);
            setRefresh(!refresh);
        }
    }, [state, setUsers, setStatuses, event, refresh, setRefresh]);

    return <Container>
        {state.userId === event.author.id &&
        <FormReference formRef={'/events/' + event.id + '/edit'} description='Edit event'/>}

        <div className='container-main-info'>
            <div className='container-main-info-item fixed-width-content '>
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
                <div className='margin-right-item' dangerouslySetInnerHTML={{__html: event.description}}/>
                <div className='margin-right-item'>
                    <h5>Event paid things:</h5><br/>
                    {event.paidThings.map((p, key) => {
                        return (<p key={key}>• {p.cashCategory} - {p.paidThing.name}</p>
                        )
                    })}
                </div>
            </div>
            <div className='container-main-info-item center-items fixed-width-profile'>
                <Profile user={event.author}/>
            </div>
        </div>
        Max count of users: {event.peopleCount}
        <Users users={users}
               statuses={statuses}/>
        <b>Route: </b> {event.route.map(location => location.location.name + ", " +
        location.location.country.name).join(" -> ")}
        <ObjectGeoLocation
            editable={false}
            setCurrentCenter={() => null}
            routes={event.route}
            height={400}
        />
        <Reviews eventId={event.id}/>
    </Container>;
};

ViewEvent.propTypes = {
    event: Event.isRequired
};

export default ViewEvent;