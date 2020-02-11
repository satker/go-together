import React, {useContext, useEffect, useState} from "react";
import {Context} from "../../Context";
import {EVENTS_URL} from '../../utils/constants'
import ViewEvent from "./ViewEvent";
import PropTypes from 'prop-types';

const GetAndViewApartment = ({id}) => {
    const [event, setEvent] = useState(null);

    const [state] = useContext(Context);

    useEffect(() => {
        state.fetchWithToken(EVENTS_URL + "/" + id, setEvent)
    }, [id, setEvent, state]);

    return event && <ViewEvent event={event}/>;
};

GetAndViewApartment.propTypes = {
    id: PropTypes.string.isRequired
};

export default GetAndViewApartment;