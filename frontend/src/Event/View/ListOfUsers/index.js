import React from 'react';
import Profile from "../Profile";
import PropTypes from "prop-types";

const ListOfUsers = ({users}) => {

    return users.map(user => <Profile user={user}/>)
};

ListOfUsers.propTypes = {
    users: PropTypes.array
};

export default ListOfUsers;