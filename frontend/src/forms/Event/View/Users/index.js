import React, {useEffect, useState} from 'react';
import PropTypes from "prop-types";

import ElementTabs from "forms/utils/components/Tabs";
import {ResponseData} from "forms/utils/types";
import LoadableContent from "forms/utils/components/LoadableContent";
import {connect} from "App/Context";

import Messages from "../Messages";
import {postUserStatus} from "./actions";
import {getStatuses, getUsers} from "../actions";

const Users = ({event, users, statuses, userId, postUserStatus, userStatus, getUsers, getStatuses}) => {
    const [userMessageId, setUserMessageId] = useState(null);
    const [flag, setFlag] = useState(false);

    useEffect(() => {
        getStatuses(event.id);
    }, [getStatuses, event]);

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
        const approvedUser = [...users.response].filter(user => user.user.id === userId).map(user => {
            user.userStatus = status;
            return user;
        })[0];
        postUserStatus(approvedUser);
        setFlag(true);
    };

    return <>
        <LoadableContent loadableData={statuses}>
            <LoadableContent loadableData={users}>
                {userId === event.author.id && <ElementTabs elements={users.response}
                                                            onClick={updateUserStatus('APPROVED')}
                                                            onDelete={updateUserStatus('REJECTED')}
                                                            onAction={setUserMessageId}
                                                            isUsers={true}
                                                            elementsFieldTab={"userStatus"}
                                                            tabs={statuses.response}/>}
            </LoadableContent>
        </LoadableContent>
        <Messages userMessageId={userMessageId}
                  setUserMessageId={setUserMessageId}/>
    </>;
};

Users.propTypes = {
    users: ResponseData.isRequired,
    statuses: ResponseData.isRequired,
    eventId: PropTypes.string.isRequired,
    eventUserId: PropTypes.string.isRequired,
    userId: PropTypes.string,
    postUserStatus: PropTypes.func.isRequired,
    getUsers: PropTypes.func.isRequired,
    getStatuses: PropTypes.func.isRequired
};

const mapStateToProps = () => state => ({
    userId: state.userId.value,
    userStatus: state.components.forms.event.eventView.usersParticipation.userStatus,
    statuses: state.components.forms.event.eventView.statuses,
    users: state.components.forms.event.eventView.users,
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {postUserStatus, getUsers, getStatuses})(Users);