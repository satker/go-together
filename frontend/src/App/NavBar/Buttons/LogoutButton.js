import IconButton from "@material-ui/core/IconButton";
import Badge from "@material-ui/core/Badge";
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import PropTypes from "prop-types";
import React, {useEffect} from "react";
import {set as setCookie} from "js-cookie";
import {USER_ID} from "../../../forms/utils/constants";
import {connect} from "../../Context";
import {cleanToken, cleanUserId} from "../actions";
import {CSRF_TOKEN} from "../../Context/constants";

const LogoutButton = ({menuId, cleanUserId, cleanToken, userId}) => {
    const logout = () => {
        setCookie(USER_ID, null);
        cleanUserId();
    };

    useEffect(() => {
        if (!userId) {
            setCookie(CSRF_TOKEN, null);
            cleanToken();
        }
    }, [userId, cleanToken]);


    return <IconButton key={menuId + '_logout'}
                       onClick={logout}
                       color="inherit">
        <Badge color="secondary">
            <ExitToAppIcon/>
        </Badge>
    </IconButton>
};

LogoutButton.propTypes = {
    menuId: PropTypes.string.isRequired
};

const mapStateToProps = () => (state) => ({
    userId: state.userId.value
});

export default connect(mapStateToProps, {cleanUserId, cleanToken})(LogoutButton);