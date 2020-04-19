import React, {useEffect, useState} from 'react';
import {connect} from "../../../App/Context";
import {DEFAULT_CREATE_EVENT} from "../../utils/constants";
import * as PropTypes from "prop-types";
import ViewEvent from "./ViewEvent";
import moment from 'moment';
import {FORM_ID} from "./constants";
import {getEvent} from "./actions";

const CreateEvent = ({id, event, getEvent}) => {
    const [currentEvent, setCurrentEvent] = useState(null);

    useEffect(() => {
        if (id) {
            getEvent(id);
        }
    }, [id, getEvent]);

    useEffect(() => {
        if (event) {
            const newEvent = {...event};
            newEvent.startDate = moment(newEvent.startDate);
            newEvent.endDate = moment(newEvent.endDate);
            setCurrentEvent(newEvent);
        }
    }, [setCurrentEvent, event]);

    return ((id && currentEvent) || !id) && <ViewEvent event={id ? currentEvent : {...DEFAULT_CREATE_EVENT}}/>
};

CreateEvent.propTypes = {
    id: PropTypes.string,
    event: PropTypes.object.isRequired,
    getEvent: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    event: state[FORM_ID].event
});

export default connect(mapStateToProps, {getEvent})(CreateEvent)(FORM_ID);