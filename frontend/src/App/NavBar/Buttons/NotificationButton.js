import React, {useEffect} from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import Badge from "@material-ui/core/Badge";
import IconButton from "@material-ui/core/IconButton";
import NotificationsIcon from "@material-ui/icons/Notifications";

import {connect} from "App/Context";
import {getUserNotifications} from "forms/Notifications/actions";

const NotificationButton = ({menuId, userNotificationsSize, getUserNotifications, userId}) => {
    useEffect(() => {
        if (userId) {
            getUserNotifications(userId);
        }
    }, [getUserNotifications, userId]);

    return <IconButton onClick={() => navigate('/notifications', true)}
                       key={menuId + '_notification'}
                       aria-label={"show " + userNotificationsSize + " new notifications"}
                       color="inherit">
        <Badge badgeContent={userNotificationsSize}
               color="secondary">
            <NotificationsIcon/>
        </Badge>
    </IconButton>;
};

NotificationButton.propTypes = {
    menuId: PropTypes.string.isRequired,
    userNotificationsSize: PropTypes.number.isRequired,
    getUserNotifications: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    userNotificationsSize: state.components.forms.notifications.userNotifications.response.length,
    userId: state.userId.value
});

export default connect(mapStateToProps, {getUserNotifications})(NotificationButton);