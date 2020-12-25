import React, {useEffect} from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import Badge from "@material-ui/core/Badge";
import IconButton from "@material-ui/core/IconButton";
import NotificationsIcon from "@material-ui/icons/Notifications";

import {connect} from "App/Context";
import {getUnreadUserNotifications} from "forms/Notifications/actions";

const NotificationButton = ({menuId, userNotificationsSize, getUnreadUserNotifications, userId}) => {
    useEffect(() => {
        userId && getUnreadUserNotifications(userId);
    }, [getUnreadUserNotifications, userId]);

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

const mapStateToProps = state => {
    const notifications = state.components.forms.notifications.unreadNotifications;
    return ({
        userNotificationsSize: (notifications.response?.result || [])
            .filter(notificationMessage => !notificationMessage.isRead)
            .length,
        userId: state.auth.response.userId
    });
}

export default connect(mapStateToProps, {getUnreadUserNotifications})(NotificationButton);