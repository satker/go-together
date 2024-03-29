import React from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import IconButton from "@material-ui/core/IconButton";
import AccountCircle from "@material-ui/icons/AccountCircle";

const AccountButton = ({menuId}) => {
    return <IconButton
        key={menuId + '_account'}
        onClick={() => navigate('/home', true)}
        color="inherit"
    >
        <AccountCircle/>
    </IconButton>;
};

AccountButton.propTypes = {
    menuId: PropTypes.string.isRequired
};

export default AccountButton;