import React, {useEffect} from "react";
import PropTypes from "prop-types";

import CreateButton from "App/NavBar/Buttons/CreateButton";
import NotificationButton from "App/NavBar/Buttons/NotificationButton";
import AccountButton from "App/NavBar/Buttons/AccountButton";
import LogoutButton from "App/NavBar/Buttons/LogoutButton";
import EditButton from "App/NavBar/Buttons/EditButton";
import {connect} from "App/Context";
import Login from "forms/Login";
import {CSRF_TOKEN, USER_ID} from "App/Context/constants";

const ToolbarButtons = ({userId, menuId, handleMenuClose, token}) => {
    useEffect(() => {
        if (token && userId) {
            localStorage.setItem(CSRF_TOKEN, token);
            localStorage.setItem(USER_ID, userId);
        }
    }, [token, userId])

    return token && userId ? <>
            <EditButton menuId={menuId}/>
            <CreateButton menuId={menuId}/>
            <NotificationButton menuId={menuId}/>
            <AccountButton menuId={menuId}/>
            <LogoutButton menuId={menuId}/>
        </> :
        <Login formId={menuId} handleMenuClose={handleMenuClose}/>
};

ToolbarButtons.propTypes = {
    userId: PropTypes.string,
    menuId: PropTypes.string.isRequired,
    handleMenuClose: PropTypes.func.isRequired,
    token: PropTypes.string
};

const mapStateToProps = (state) => ({
    userId: state.auth.response.userId,
    token: state.auth.response.token
});

export default connect(mapStateToProps, null)(ToolbarButtons);