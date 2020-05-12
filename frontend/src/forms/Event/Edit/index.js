import React, {useEffect} from 'react';
import {connect} from "../../../App/Context";
import * as PropTypes from "prop-types";
import EditForm from "./EditForm";
import {getEvent, updateEvent} from "./actions";
import LoadableContent from "../../utils/components/LoadableContent";

const CreateEvent = ({id, event, getEvent}) => {
    useEffect(() => {
        if (id) {
            getEvent(id);
        }
    }, [id, getEvent]);

    return <LoadableContent loadableData={event}>
        <EditForm/>
    </LoadableContent>
};

CreateEvent.propTypes = {
    id: PropTypes.string,
    event: PropTypes.object.isRequired,
    getEvent: PropTypes.func.isRequired,
    updateEvent: PropTypes.func.isRequired
};

const mapStateToProps = () => (state) => ({
    event: state.components.forms.event.eventEdit.event
});

export default connect(mapStateToProps, {getEvent, updateEvent})(CreateEvent);