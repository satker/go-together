import React, {useContext, useEffect, useState} from 'react';
import {Context} from "../../../Context";
import {EVENT_SERVICE_URL} from "../../../utils/constants";
import SimpleUserView from "../../../utils/components/Event/SimpleUser";
import ElementTabs from "../../../utils/components/Tabs";

const Users = ({eventId}) => {
    const [users, setUsers] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [state] = useContext(Context);


    useEffect(() => {
        state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + eventId + '/users/statuses', setStatuses);
        state.fetchWithToken(EVENT_SERVICE_URL + '/events/' + eventId + '/users', setUsers);
    }, [state, setUsers, setStatuses, eventId]);

    return <ElementTabs elements={users}
                        Form={SimpleUserView}
                        elementsFieldTab={"userStatus"}
                        tabs={statuses}/>;
};

Users.propTypes = {};

export default Users;