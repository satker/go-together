import React, {useEffect} from "react";
import PropTypes from 'prop-types';

import LoadableContent from "forms/utils/components/LoadableContent";
import {connect} from "App/Context";
import {ResponseData} from "forms/utils/types";

import {getEvent} from "./actions";
import ViewForm from "./ViewForm";

const ViewEvent = ({id, getEvent, event}) => {
    useEffect(() => {
        getEvent(id);
    }, [getEvent, id]);

    return <LoadableContent loadableData={event}>
        <ViewForm/>
    </LoadableContent>;
};

ViewEvent.propTypes = {
    id: PropTypes.string.isRequired,
    getEvent: PropTypes.func.isRequired,
    event: ResponseData.isRequired
};

const mapStateToProps = state => ({
    event: state.components.forms.event.eventView.event,
});

export default connect(mapStateToProps, {getEvent})(ViewEvent);