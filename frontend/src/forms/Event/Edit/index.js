import React, {useEffect} from 'react';
import {connect} from "../../../App/Context";
import {DEFAULT_CREATE_EVENT} from "../../utils/constants";
import * as PropTypes from "prop-types";
import EditForm from "./EditForm";
import {FORM_ID} from "./constants";
import {getEvent} from "./actions";
import LoadableContent from "../../utils/components/LoadableContent";

const CreateEvent = ({id, event, getEvent}) => {
    useEffect(() => {
        if (id) {
            getEvent(id);
        }
    }, [id, getEvent]);

    return id ? <LoadableContent loadableData={event}>
        <EditForm event={event.response}/>
    </LoadableContent> : <EditForm event={{...DEFAULT_CREATE_EVENT}}/>
};

CreateEvent.propTypes = {
    id: PropTypes.string,
    event: PropTypes.object.isRequired,
    getEvent: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    event: state.components.forms.event.eventEdit.event
});

export default connect(mapStateToProps, {getEvent})(CreateEvent)(FORM_ID);