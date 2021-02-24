import React from "react";
import IconButton from "@material-ui/core/IconButton";
import Badge from "@material-ui/core/Badge";
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import {cleanAuth} from "../actions";
import {clearTimers} from "App/TemporaryTimer/actions";

const LogoutButton = ({menuId, cleanAuth, clearTimers}) => {
    const logout = () => {
        clearTimers();
        cleanAuth();
    }
    return <IconButton key={menuId + '_logout'}
                       onClick={logout}
                       color="inherit">
        <Badge color="secondary">
            <ExitToAppIcon/>
        </Badge>
    </IconButton>
};

LogoutButton.propTypes = {
    menuId: PropTypes.string.isRequired,
    cleanAuth: PropTypes.func.isRequired,
    clearTimers: PropTypes.func.isRequired
};

const mapStateToProps = (state) => ({
    userId: state.auth.response.userId
});

export default connect(mapStateToProps, {cleanAuth, clearTimers})(LogoutButton);