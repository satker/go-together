import React, {useContext, useState} from 'react';
import MainInfo from "./MainInfo";
import Button from "reactstrap/es/Button";
import {Context} from "../../Context";
import {EVENT_SERVICE_URL} from "../../utils/constants";
import {Event} from "../../types";
import {onChange} from "../../utils/utils";
import {navigate} from 'hookrouter';
import Container from "@material-ui/core/Container";
import PaidThings from "./PaidThings";
import Route from "./Route";

const ViewEvent = ({event}) => {
    const [createEvent, setCreateEvent] = useState(event);

    const [state] = useContext(Context);
    console.log(createEvent)
    const saveEvent = () => {
        let saveObj = {...createEvent};
        saveObj.id = createEvent.id;
        saveObj.author = {
            id: state.userId
        };
        if (createEvent.id) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events', (response) =>
                response.id && navigate('/events/' + response.id), 'POST', saveObj);
        } else {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events', (response) =>
                response && navigate('/events/' + response.id), 'PUT', saveObj);
        }
    };

    return <Container>
        <MainInfo event={createEvent}
                  onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <PaidThings event={createEvent}
                    onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <Route event={createEvent}
               onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <Button className="btn btn-success" onClick={saveEvent}>Save</Button>
    </Container>
};

ViewEvent.propTypes = {
    event: Event.isRequired
};

export default ViewEvent;