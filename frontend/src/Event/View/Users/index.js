import React, {useContext} from 'react';
import ElementTabs from "../../../utils/components/Tabs";
import PropTypes from "prop-types";
import {EventUser} from "../../../types";
import {Context} from "../../../Context";
import {EVENT_SERVICE_URL} from "../../../utils/constants";

const Users = ({users, statuses, setUsers, eventId}) => {
    const [state] = useContext(Context);

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

    console.log(users)
    return <ElementTabs elements={users}
                        onClick={approveUser}
                        onDelete={removeUserFromEvent}
                        isUsers={true}
                        elementsFieldTab={"userStatus"}
                        tabs={statuses}/>;
};

Users.propTypes = {
    users: PropTypes.arrayOf(EventUser),
    statuses: PropTypes.arrayOf(PropTypes.string),
    setUsers: PropTypes.func.isRequired,
    eventId: PropTypes.string.isRequired
};

export default Users;