import React, {useContext, useEffect, useState} from "react";
import {Context} from "../../Context";
import {EVENTS_URL} from '../../utils/constants'
import ViewEvent from "./ViewEvent";
import PropTypes from 'prop-types';
import moment from "moment";

const GetAndViewEvent = ({id}) => {
    const [event, setEvent] = useState(null);

    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(EVENTS_URL + "/" + id, event => {
            event.startDate = moment(event.startDate);
            event.endDate = moment(event.endDate);
            setEvent(event);
        })
    }, [id, setEvent, state]);

    return event && <ViewEvent event={event}/>;
};

GetAndViewEvent.propTypes = {
    id: PropTypes.string.isRequired
};

export default GetAndViewEvent;