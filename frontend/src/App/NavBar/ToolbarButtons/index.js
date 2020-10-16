import React from "react";
import PropTypes from "prop-types";

import CreateButton from "App/NavBar/Buttons/CreateButton";
import NotificationButton from "App/NavBar/Buttons/NotificationButton";
import AccountButton from "App/NavBar/Buttons/AccountButton";
import LogoutButton from "App/NavBar/Buttons/LogoutButton";
import EditButton from "App/NavBar/Buttons/EditButton";
import {connect} from "App/Context";
import Login from "forms/Login";

const ToolbarButtons = ({userId, menuId, handleMenuClose}) => {
    return userId !== null ? <>
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
    handleMenuClose: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    userId: state.userId.value
});

export default connect(mapStateToProps, null)(ToolbarButtons);