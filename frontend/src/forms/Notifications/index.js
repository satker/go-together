import React, {useEffect} from 'react';
import PropTypes from 'prop-types';

import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import {NotificationObject} from "forms/utils/types";

import Notification from "./Notification";
import {readNotifications} from "./actions";

const Notifications = ({userNotifications, readNotifications, userId}) => {
    useEffect(() => {
        if (userId) {
            readNotifications(userId);
        }
    }, [readNotifications, userId]);

    return <Container>
        {userNotifications.map(notification =>
            <Notification notification={notification} key={notification.id}/>
        )}
    </Container>
};

Notifications.propTypes = {
    userNotifications: PropTypes.arrayOf(NotificationObject).isRequired,
    readNotifications: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    userNotifications: state.components.forms.notifications.userNotifications.response
});

export default connect(mapStateToProps, {readNotifications})(Notifications);