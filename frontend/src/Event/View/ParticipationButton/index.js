import React, {useContext} from "react";
import {DEFAULT_EVENT_USER, EVENT_SERVICE_URL} from "../../../utils/constants";
import {Context} from "../../../Context";
import Button from "reactstrap/es/Button";
import {EventUser} from "../../../types";
import PropTypes from "prop-types";

const ParticipationButton = ({eventId, users, setRefresh}) => {
    const [state] = useContext(Context);

    const addMeToWaitApproveList = () => {
        const meObject = {...DEFAULT_EVENT_USER};
        meObject.user.id = state.userId;
        meObject.eventId = eventId;
        state.fetchWithToken(EVENT_SERVICE_URL + '/events/users',
            () => setRefresh(true), 'POST', meObject);
    };

    const removeMeFromEvent = () => {
        const meObject = {...DEFAULT_EVENT_USER};
        meObject.user.id = state.userId;
        meObject.eventId = eventId;
        state.fetchWithToken(EVENT_SERVICE_URL + '/events/users',
            () => setRefresh(true), 'DELETE', meObject);
    };

    const ifIPartOfEvent = !!users.find(user => user.user.id === state.userId);
    const actionButton = ifIPartOfEvent ? removeMeFromEvent : addMeToWaitApproveList;
    const buttonTitle = ifIPartOfEvent ? "Remove me from event" : "Add me to event";

    return <Button className="btn btn-success" onClick={actionButton}>{buttonTitle}</Button>;
};

ParticipationButton.propTypes = {
    eventId: PropTypes.string.isRequired,
    users: PropTypes.arrayOf(EventUser),
    setRefresh: PropTypes.func.isRequired
};

export default ParticipationButton;