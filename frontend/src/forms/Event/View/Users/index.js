import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

import ElementTabs from "forms/utils/components/Tabs";
import {Event, ResponseData} from "forms/utils/types";
import LoadableContent from "forms/utils/components/LoadableContent";
import {connect} from "App/Context";

import Messages from "../Messages";
import {postUserStatus} from "./actions";
import {getStatuses, getUsers} from "../actions";

const Users = ({event, users, statuses, userId, postUserStatus, userStatus, getUsers, getStatuses}) => {
    const [userMessageId, setUserMessageId] = useState(null);
    const [flag, setFlag] = useState(false);

    useEffect(() => {
        getStatuses();
    }, [getStatuses]);

    useEffect(() => {
        if (event.author.id !== userId) {
            setUserMessageId(event.author.id);
        }
    }, [setUserMessageId, event, userId]);

    useEffect(() => {
        if (flag && userStatus) {
            getUsers(event.id);
            setFlag(false);
        }
    }, [getUsers, userStatus, flag, event]);

    const updateUserStatus = (status) => (userId) => {
        const approvedUser = [...users.response.result].filter(user => user.user.id === userId).map(user => {
            user.userStatus = status;
            return user;
        })[0];
        postUserStatus(approvedUser);
        setFlag(true);
    };

    return <>
        <LoadableContent loadableData={statuses}>
            {userId === event.author.id && <ElementTabs elements={users.response.result}
                                                        onClick={updateUserStatus('APPROVED')}
                                                        onDelete={updateUserStatus('REJECTED')}
                                                        onAction={setUserMessageId}
                                                        isUsers={true}
                                                        elementsFieldTab={"userStatus"}
                                                        tabs={statuses.response}/>}
        </LoadableContent>
        {userId && <Messages userMessageId={userMessageId}
                             setUserMessageId={setUserMessageId}/>}
    </>;
};

Users.propTypes = {
    event: Event,
    userStatus: ResponseData,
    users: ResponseData.isRequired,
    statuses: ResponseData.isRequired,
    userId: PropTypes.string,
    postUserStatus: PropTypes.func.isRequired,
    getUsers: PropTypes.func.isRequired,
    getStatuses: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    userId: state.auth.response.userId,
    userStatus: state.components.forms.event.eventView.usersParticipation.userStatus,
    statuses: state.components.forms.event.eventView.statuses,
    users: state.components.forms.event.eventView.users,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {postUserStatus, getUsers, getStatuses})(Users);