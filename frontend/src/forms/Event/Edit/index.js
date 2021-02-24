import React, {useEffect} from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import LoadableContent from "forms/utils/components/LoadableContent";

import Content from "./Content";
import {cleanEvent, getEvent} from "./actions";

const CreateEvent = ({id, event, getEvent, cleanEvent}) => {
    useEffect(() => {
        cleanEvent();
    }, [cleanEvent]);

    useEffect(() => {
        if (id) {
            getEvent(id);
        }
    }, [id, getEvent]);

    return <LoadableContent loadableData={event} additionalCheck={response => id && response.id !== id}>
        <Content isUpdate={!!id}/>
    </LoadableContent>
};

CreateEvent.propTypes = {
    id: PropTypes.string,
    event: PropTypes.object.isRequired,
    getEvent: PropTypes.func.isRequired,
    cleanEvent: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    event: state.components.forms.event.eventEdit.event
});

export default connect(mapStateToProps, {getEvent, cleanEvent})(CreateEvent);