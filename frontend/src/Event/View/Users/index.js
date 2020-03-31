import React, {useContext, useEffect, useState} from 'react';
import ElementTabs from "../../../utils/components/Tabs";
import PropTypes from "prop-types";
import {EventUser} from "../../../types";
import {Context} from "../../../Context";
import {EVENT_SERVICE_URL} from "../../../utils/constants";
import Messages from "../Messages";

const Users = ({users, statuses, setUsers, eventId, eventUserId}) => {
    const [userMessageId, setUserMessageId] = useState(null);

    const [state] = useContext(Context);

    useEffect(() => {
        if (eventUserId !== state.userId) {
            setUserMessageId(eventUserId);
        }
    }, [setUserMessageId, eventUserId, state]);

    const approveUser = (userId) => {
        const approvedUser = [...users].filter(user => user.user.id === userId).map(user => {
            user.userStatus = 'APPROVED';
            return user;
        })[0];

        state.fetchWithToken(EVENT_SERVICE_URL + '/events/users', () => {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + eventId + '/users', setUsers);
        }, 'POST', approvedUser);
    };

    const removeUserFromEvent = (userId) => {
        const removedUser = [...users].filter(user => user.user.id === userId)[0];

        state.fetchWithToken(EVENT_SERVICE_URL + '/events/users', () => {
            state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + eventId + '/users', setUsers);
        }, 'DELETE', removedUser);
    };

    return <div className='flex margin-top-10'>
        {state.userId === eventUserId && <ElementTabs elements={users}
                                                      onClick={approveUser}
                                                      onDelete={removeUserFromEvent}
                                                      onAction={setUserMessageId}
                                                      isUsers={true}
                                                      elementsFieldTab={"userStatus"}
                                                      tabs={statuses}/>}
        <Messages eventId={eventId}
                  userId={userMessageId}
                  eventUserId={eventUserId}/>
    </div>;
};

Users.propTypes = {
    users: PropTypes.arrayOf(EventUser),
    statuses: PropTypes.arrayOf(PropTypes.string),
    setUsers: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired,
    eventUserId: PropTypes.string.isRequired
};

export default Users;