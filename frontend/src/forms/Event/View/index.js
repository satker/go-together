import React, {useEffect, useState} from "react";
import {connect} from "../../../App/Context";
import ViewEvent from "./ViewEvent";
import PropTypes from 'prop-types';
import moment from "moment";
import {FORM_ID} from "./constants";
import {getEvent} from "./actions";
import {Event} from "../../utils/types";
import {isEmpty} from "lodash";

const GetAndViewEvent = ({id, getEvent, event}) => {
    const [currentEvent, setCurrentEvent] = useState(null);

    useEffect(() => {
        getEvent(id);
    }, [getEvent, id]);

    useEffect(() => {
        if (!isEmpty(event.response) && !event.inProcess) {
            const result = {...event.response};
            result.startDate = moment(result.startDate);
            result.endDate = moment(result.endDate);
            setCurrentEvent(result)
        }
    }, [setCurrentEvent, event]);

    return currentEvent && <ViewEvent event={currentEvent}/>;
};

GetAndViewEvent.propTypes = {
    id: PropTypes.string.isRequired,
    getEvent: PropTypes.func.isRequired,
    event: Event
};

const mapStateToProps = (FORM_ID) => state => ({
    event: state[FORM_ID]?.event,
});

export default connect(mapStateToProps, {getEvent})(GetAndViewEvent)(FORM_ID);