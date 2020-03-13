import React from 'react';
import SimpleUserView from "../../../utils/components/Event/SimpleUser";
import ElementTabs from "../../../utils/components/Tabs";
import PropTypes from "prop-types";
import {SimpleUser} from "../../../types";

const Users = ({users, statuses}) => {
    return <ElementTabs elements={users}
                        mapElement={user => user.user}
                        Form={SimpleUserView}
                        elementsFieldTab={"userStatus"}
                        tabs={statuses}/>;
};

Users.propTypes = {
    users: PropTypes.arrayOf(SimpleUser),
    statuses: PropTypes.arrayOf(PropTypes.string)
};

export default Users;