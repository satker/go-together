import React, {useEffect} from "react";
import {connect} from "../../../App/Context";
import PropTypes from 'prop-types';
import {getEvent} from "./actions";
import {ResponseData} from "../../utils/types";
import LoadableContent from "../../utils/components/LoadableContent";
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

const mapStateToProps = () => state => ({
    event: state.components.forms.event.eventView.event,
});

export default connect(mapStateToProps, {getEvent})(ViewEvent);