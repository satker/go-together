import React, {useEffect, useState} from "react";
import PropTypes from "prop-types";

import {DEFAULT_EVENT_USER} from "forms/utils/constants";
import {connect} from "App/Context";
import {EventUser} from "forms/utils/types";
import CustomButton from "forms/utils/components/CustomButton";

import {addMeToList, deleteMeFromList} from "./actions";

const ParticipationButton = ({
                                 eventId, users, getUsers, userId, addMeToList,
                                 deleteMeFromList, meToList, meFromList
                             }) => {
    const [flag, setFlag] = useState(false);

    const addMeToWaitApproveList = () => {
        const meObject = {...DEFAULT_EVENT_USER};
        meObject.user.id = userId;
        meObject.eventId = eventId;
        addMeToList(meObject);
        setFlag(true);
    };

    const removeMeFromEvent = () => {
        const meObject = {...DEFAULT_EVENT_USER};
        meObject.user.id = userId;
        meObject.eventId = eventId;
        deleteMeFromList(meObject);
        setFlag(true);
    };

    useEffect(() => {
        if (flag && !(meFromList.inProcess || meToList.inProcess)) {
            getUsers(eventId);
            setFlag(false);
        }
    }, [flag, meFromList, meToList, setFlag, getUsers, eventId]);

    const ifIPartOfEvent = !!users.find(user => user.user.id === userId);
    const actionButton = ifIPartOfEvent ? removeMeFromEvent : addMeToWaitApproveList;
    const buttonTitle = ifIPartOfEvent ? "Remove me from event" : "Add me to event";

    return <CustomButton color='primary'
                         text={buttonTitle}
                         onClick={actionButton}/>;
};

ParticipationButton.propTypes = {
    eventId: PropTypes.string.isRequired,
    users: PropTypes.arrayOf(EventUser),
    setRefresh: PropTypes.func.isRequired,
    userId: PropTypes.string,
    addMeToList: PropTypes.func.isRequired,
    deleteMeFromList: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    meToList: state.components.forms.event.eventView.participationButton.meToList,
    meFromList: state.components.forms.event.eventView.participationButton.meFromList
});

export default connect(mapStateToProps, {addMeToList, deleteMeFromList})(ParticipationButton);