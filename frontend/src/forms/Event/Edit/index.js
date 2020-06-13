import React, {useEffect} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import LoadableContent from "forms/utils/components/LoadableContent";

import EditForm from "./EditForm";
import {getEvent, updateEvent} from "./actions";

const CreateEvent = ({id, event, getEvent}) => {
    useEffect(() => {
        if (id) {
            getEvent(id);
        }
    }, [id, getEvent]);

    return <LoadableContent loadableData={event} additionalCheck={response => id && response.id !== id}>
        <EditForm/>
    </LoadableContent>
};

CreateEvent.propTypes = {
    id: PropTypes.string,
    event: PropTypes.object.isRequired,
    getEvent: PropTypes.func.isRequired,
    updateEvent: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    event: state.components.forms.event.eventEdit.event
});

export default connect(mapStateToProps, {getEvent, updateEvent})(CreateEvent);