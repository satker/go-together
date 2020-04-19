import React, {useEffect, useState} from 'react';
import ElementTabs from "../../../utils/components/Tabs";
import PropTypes from "prop-types";
import {EventUser} from "../../../utils/types";
import {connect} from "../../../../App/Context";
import Messages from "../Messages";
import {postUserStatus} from "./actions";
import {FORM_ID} from "../constants";
import {getUsers} from "../actions";

const Users = ({users, statuses, eventId, eventUserId, userId, postUserStatus, userStatus, getUsers}) => {
    const [userMessageId, setUserMessageId] = useState(null);
    const [flag, setFlag] = useState(false);

    useEffect(() => {
        if (eventUserId !== userId) {
            setUserMessageId(eventUserId);
        }
    }, [setUserMessageId, eventUserId, userId]);

    useEffect(() => {
        if (flag && userStatus) {
            getUsers(eventId);
            setFlag(false);
        }
    }, [getUsers, userStatus, flag, eventId]);

    const updateUserStatus = (status) => (userId) => {
        const approvedUser = [...users].filter(user => user.user.id === userId).map(user => {
            user.userStatus = status;
            return user;
        })[0];
        postUserStatus(approvedUser);
        setFlag(true);
    };

    return <>
        {userId === eventUserId && <ElementTabs elements={users}
                                                onClick={updateUserStatus('APPROVED')}
                                                onDelete={updateUserStatus('REJECTED')}
                                                onAction={setUserMessageId}
                                                isUsers={true}
                                                elementsFieldTab={"userStatus"}
                                                tabs={statuses}/>}
        <Messages eventId={eventId}
                  userMessageId={userMessageId}
                  setUserMessageId={setUserMessageId}
                  eventUserId={eventUserId}/>
    </>;
};

Users.propTypes = {
    users: PropTypes.arrayOf(EventUser),
    statuses: PropTypes.arrayOf(PropTypes.string),
    eventId: PropTypes.string.isRequired,
    eventUserId: PropTypes.string.isRequired,
    userId: PropTypes.string.isRequired,
    postUserStatus: PropTypes.func.isRequired,
    getUsers: PropTypes.func.isRequired
};

const mapStateToProps = () => state => ({
    userId: state.userId,
    userStatus: state[FORM_ID]?.userStatus
});

export default connect(mapStateToProps, {postUserStatus, getUsers})(Users)(FORM_ID);