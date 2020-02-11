import React, {useContext, useEffect, useState} from 'react';
import {Context} from "../../Context";
import {DEFAULT_CREATE_EVENT, EVENT_SERVICE_URL} from "../../utils/constants";
import * as PropTypes from "prop-types";
import ViewApartment from "./ViewApartment";

const CreateApartment = ({id}) => {
    const [event, setEvents] = useState(null);

    const [state] = useContext(Context);

    useEffect(() => {
        if (id) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + id, setEvents)
        }
    }, [id, state]);

    return ((id && event) || !id) && <ViewApartment apartment={id ? event : {...DEFAULT_CREATE_EVENT}}/>
};

CreateApartment.propTypes = {
    id: PropTypes.string
};

export default CreateApartment;