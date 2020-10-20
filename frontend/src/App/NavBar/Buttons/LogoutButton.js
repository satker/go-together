import React from "react";
import IconButton from "@material-ui/core/IconButton";
import Badge from "@material-ui/core/Badge";
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import {cleanAuth} from "../actions";

const LogoutButton = ({menuId, cleanAuth}) => {
    return <IconButton key={menuId + '_logout'}
                       onClick={cleanAuth}
                       color="inherit">
        <Badge color="secondary">
            <ExitToAppIcon/>
        </Badge>
    </IconButton>
};

LogoutButton.propTypes = {
    menuId: PropTypes.string.isRequired
};

const mapStateToProps = (state) => ({
    userId: state.auth.value.userId
});

export default connect(mapStateToProps, {cleanAuth})(LogoutButton);