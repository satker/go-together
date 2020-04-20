import React, {useEffect} from "react";
import {connect} from "../../../App/Context";
import PropTypes from 'prop-types';
import {FORM_ID} from "./constants";
import {getEvent} from "./actions";
import {ResponseData} from "../../utils/types";
import LoadableContent from "../../utils/components/LoadableContent";
import ViewForm from "./ViewForm";

const GetAndViewEvent = ({id, getEvent, event}) => {
    useEffect(() => {
        getEvent(id);
    }, [getEvent, id]);

    return <LoadableContent loadableData={event}>
        <ViewForm event={event.response}/>
    </LoadableContent>;
};

GetAndViewEvent.propTypes = {
    id: PropTypes.string.isRequired,
    getEvent: PropTypes.func.isRequired,
    event: ResponseData.isRequired
};

const mapStateToProps = (FORM_ID) => state => ({
    event: state[FORM_ID]?.event,
});

export default connect(mapStateToProps, {getEvent})(GetAndViewEvent)(FORM_ID);