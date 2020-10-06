import React, {useEffect} from 'react';
import PropTypes from 'prop-types';

import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import {NotificationObject} from "forms/utils/types";

import Notification from "./Notification";
import {getUserNotifications, readNotifications} from "./actions";
import LoadableContent from "../utils/components/LoadableContent";

const Notifications = ({userNotifications, readNotifications, userId, getUserNotifications}) => {
    useEffect(() => {
        if (userId) {
            getUserNotifications(userId);
        }
    }, [getUserNotifications, userId]);

    useEffect(() => {
        if (userId) {
            readNotifications(userId);
        }
    }, [readNotifications, userId]);
    return <Container>
        <LoadableContent loadableData={userNotifications}>
            {(userNotifications.response?.result || []).map(notification =>
                <Notification notification={notification.notificationMessage} key={notification.id}/>
            )}
        </LoadableContent>
    </Container>
};

Notifications.propTypes = {
    userNotifications: PropTypes.arrayOf(NotificationObject).isRequired,
    readNotifications: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    userId: state.userId.value,
    userNotifications: state.components.forms.notifications.userNotifications
});

export default connect(mapStateToProps, {readNotifications, getUserNotifications})(Notifications);