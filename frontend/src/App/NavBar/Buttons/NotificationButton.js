import React from "react";
import PropTypes from "prop-types";
import Badge from "@material-ui/core/Badge";
import IconButton from "@material-ui/core/IconButton";
import NotificationsIcon from "@material-ui/icons/Notifications";

const NotificationButton = ({menuId}) => {
    return <IconButton href='/#'
                       key={menuId + '_notification'}
                       aria-label="show 17 new notifications"
                       color="inherit">
        <Badge badgeContent={17}
               color="secondary">
            <NotificationsIcon/>
        </Badge>
    </IconButton>;
};

NotificationButton.propTypes = {
    menuId: PropTypes.string.isRequired
};

export default NotificationButton;