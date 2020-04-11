import React, {useContext, useEffect, useState} from "react";
import Profile from "./Profile";
import {Context} from "../../Context";
import {Event} from "../../types";
import FormReference from "../../utils/components/FormReference";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import Container from "@material-ui/core/Container";
import Users from "./Users";
import {EVENT_SERVICE_URL} from "../../utils/constants";
import MainInfo from "./MainInfo";

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

        <div className='container-main-info margin-bottom-20'>
            <MainInfo event={event}
                      setRefresh={setRefresh}
                      users={users}/>
            <Profile user={event.author}/>
        </div>
        <ObjectGeoLocation
            editable={false}
            setCurrentCenter={() => null}
            routes={event.route}
            height={400}
        />
        <Users setUsers={setUsers}
               eventUserId={event.author.id}
               eventId={event.id}
               users={users}
               statuses={statuses}/>
    </Container>;
};

ViewEvent.propTypes = {
    event: Event.isRequired
};

export default ViewEvent;