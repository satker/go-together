import React, {useContext, useState} from 'react';
import MainInfo from "./MainInfo";
import Button from "reactstrap/es/Button";
import {Context} from "../../Context";
import {EVENT_SERVICE_URL} from "../../utils/constants";
import {Event} from "../../types";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import {get, set} from 'lodash';
import {navigate} from 'hookrouter';
import Container from "@material-ui/core/Container";
import PaidThings from "./PaidThings";

const ViewEvent = ({event}) => {
    const [createEvent, setCreateEvent] = useState(event);

    const [state] = useContext(Context);

    const saveEvent = () => {
        let saveObj = {...createEvent};
        saveObj.id = createEvent.id;
        saveObj.userId = state.userId;
        if (createEvent.id) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events', (response) =>
                response.id && navigate('/events/' + response.id), 'POST', saveObj);
        } else {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events', (response) =>
                response && navigate('/events/' + response.id), 'PUT', saveObj);
        }
    };

    const onChangeField = (obj, field) => {
        const updatedEvent = {...createEvent};
        const currentValue = get(updatedEvent, field);
        if (currentValue !== obj) {
            set(updatedEvent, field, obj);
            setCreateEvent(updatedEvent);
        }
    };

    return <Container>
        <MainInfo event={createEvent}
                  onChange={onChangeField}/>
        <PaidThings event={createEvent}
                    onChange={onChangeField}/>
        <ObjectGeoLocation
            onChange={onChangeField}
            draggable={true}
            longitude={createEvent.route.length !== 0 ? createEvent.route[0].longitude : 73.8567}
            latitude={createEvent.route.length !== 0 ? createEvent.route[0].latitude : 18.5204}
            header={'V'}
            height={400}
        />
        <Button className="btn btn-success" onClick={saveEvent}>Save</Button>
    </Container>
};

ViewEvent.propTypes = {
    event: Event.isRequired
};

export default ViewEvent;