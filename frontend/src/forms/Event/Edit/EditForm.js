import React, {useEffect, useState} from 'react';
import MainInfo from "./MainInfo";
import {connect} from "../../../App/Context";
import {Event, ResponseData} from "../../utils/types";
import {onChange} from "../../utils/utils";
import {navigate} from 'hookrouter';
import PaidThings from "./PaidThings";
import Route from "./Route";
import Container from "../../utils/components/Container/ContainerRow";
import * as PropTypes from "prop-types";
import {postUpdatedEvent, putNewEvent} from "./actions";
import {FORM_ID} from "./constants";
import moment from "moment";
import Button from "@material-ui/core/Button";

const EditForm = ({event, userId, postUpdatedEvent, putNewEvent, updatedEvent, newEvent}) => {
    const [createEvent, setCreateEvent] = useState({
        ...event,
        startDate: event.startDate ? moment(event.startDate) : null,
        endDate: event.endDate ? moment(event.endDate) : null
    });

    const saveEvent = () => {
        let saveObj = {...createEvent};
        saveObj.id = createEvent.id;
        saveObj.author = {
            id: userId
        };
        createEvent.id ? postUpdatedEvent(saveObj) : putNewEvent(saveObj);
    };

    useEffect(() => {
        const id = updatedEvent.response.id || newEvent.response.id;
        if (id) {
            navigate('/events/' + id)
        }
    }, [updatedEvent, newEvent]);

    return <Container formId={FORM_ID}>
        <MainInfo event={createEvent}
                  onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <PaidThings event={createEvent}
                    onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <Route event={createEvent}
               onChangeEvent={onChange(createEvent, setCreateEvent)}/>
        <Button className="btn btn-success" onClick={saveEvent}>Save</Button>
    </Container>
};

EditForm.propTypes = {
    event: Event.isRequired,
    userId: PropTypes.string,
    postUpdatedEvent: PropTypes.func.isRequired,
    putNewEvent: PropTypes.func.isRequired,
    updatedEvent: ResponseData.isRequired,
    newEvent: ResponseData.isRequired
};

const mapStateToProps = (FORM_ID) => (state) => ({
    userId: state.userId,
    updatedEvent: state[FORM_ID]?.updatedEvent,
    newEvent: state[FORM_ID]?.newEvent
});

export default connect(mapStateToProps, {postUpdatedEvent, putNewEvent})(EditForm)(FORM_ID);