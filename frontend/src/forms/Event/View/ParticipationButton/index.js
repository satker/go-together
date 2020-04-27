import React, {useEffect, useState} from "react";
import {DEFAULT_EVENT_USER} from "../../../utils/constants";
import {connect} from "../../../../App/Context";
import {EventUser} from "../../../utils/types";
import PropTypes from "prop-types";
import {deleteMeFromList, postMeToList} from "./actions";
import CustomButton from "../../../utils/components/CustomButton";

const ParticipationButton = ({
                                 eventId, users, setRefresh, userId, postMeToList,
                                 deleteMeFromList, meToList, meFromList
                             }) => {
    const [flag, setFlag] = useState(false);

    const addMeToWaitApproveList = () => {
        const meObject = {...DEFAULT_EVENT_USER};
        meObject.user.id = userId;
        meObject.eventId = eventId;
        postMeToList(meObject);
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
            setRefresh(true);
            setFlag(false);
        }
    }, [flag, meFromList, meToList, setFlag, setRefresh]);

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
    postMeToList: PropTypes.func.isRequired,
    deleteMeFromList: PropTypes.func.isRequired
};

const mapStateToProps = () => state => ({
    userId: state.userId,
    meToList: state.components.forms.event.eventView.participationButton.meToList,
    meFromList: state.components.forms.event.eventView.participationButton.meFromList
});

export default connect(mapStateToProps, {postMeToList, deleteMeFromList})(ParticipationButton);